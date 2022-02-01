package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScreen implements Screen {

    private MyGdxGame parent;

    final Color[] CARD_COLORS = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA};
    final float UI_SCALE = 0.066f;
	final float SPACING = 0.88f;
	final float UNCOVER_DELAY = 1f;

	SpriteBatch batch;
	OrthographicCamera camera;
	Texture texture_off;

	private Texture shapesTextures[];

	private Array<Card> cards;
	private Card card1, card2;
	private boolean canClick;

	private GameObserver gameObserver;
	private Bot bot;
	private boolean playerTurn;

	private int screenWidth;
	private int screenHeight;
	private int gridWidth;
	private int gridHeight;

	private Stage stage;
	private Table table, tablePopup;
	private BitmapFont font, buttonFont;
	private Texture pauseTexture, textureButton1;
	private Label labelState;
	private PopupDrawable popupDrawable1;

	private boolean popupOpened;

    public GameScreen(MyGdxGame _p, int _gamemode, int _gridSize){
        parent = _p;

		batch = new SpriteBatch();

		camera = new OrthographicCamera();

		screenHeight = Gdx.graphics.getHeight();
		screenWidth = Gdx.graphics.getWidth();

		camera.setToOrtho(false, screenWidth, screenHeight);

		switch (_gridSize){
			case 0: gridHeight = 6; gridWidth = 3; break;
			case 1: gridHeight = 7; gridWidth = 4; break;//7 4
			case 2: gridHeight = 8; gridWidth = 5; break;
			case 3: gridHeight = 9; gridWidth = 6; break;
		}

		cards = new Array<>();
//		shapesTextures = new Texture[6];
		generateCards();

		card1 = null;
		card2 = null;
		canClick = true;
		gameObserver = new GameObserver();
		switch (_gamemode){
			case 0: bot = null; break;
			case 1: bot = new Bot(cards.size, 3); break;
			case 2: bot = new Bot(cards.size, 5); break;
			case 3: bot = new Bot(cards.size, cards.size); break;
		}
		playerTurn = true;


		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Raleway-Medium.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

		generateUI(generator, parameter);
		initPopup(generator, parameter);

		generator.dispose();
//		textureBlankWhite = myUtils.getBlankWhite();
    }

    private void initPopup(FreeTypeFontGenerator generator, FreeTypeFontGenerator.FreeTypeFontParameter parameter){
		popupOpened = false;
		popupDrawable1 = new PopupDrawable();

		int buttonWidth = Math.round(0.84f*Gdx.graphics.getWidth() / 2);
		int buttonHeight = Math.round(buttonWidth/1.68f);
		int popupHeight = buttonHeight*2;

		tablePopup = new Table();
		tablePopup.setBackground(popupDrawable1);
		tablePopup.setFillParent(false);
		tablePopup.setSize(Gdx.graphics.getWidth(), popupHeight);
		tablePopup.setY(Gdx.graphics.getHeight()/2 - popupHeight/2);
		tablePopup.setVisible(false);

		parameter.color = Color.BLACK;
		parameter.size = Math.round(buttonHeight*0.42f); // font size
		buttonFont = generator.generateFont(parameter);

		textureButton1 = myUtils.getButtonTexture(buttonWidth, buttonHeight);
		TextureRegion regionButton = new TextureRegion(textureButton1);
		TextButton.TextButtonStyle styleButton = new TextButton.TextButtonStyle();
		styleButton.font = buttonFont;
		styleButton.up = new TextureRegionDrawable(regionButton);
		styleButton.down = new TextureRegionDrawable(regionButton);


		TextButton button1  = new TextButton("MENU", styleButton);
		TextButton button2  = new TextButton("PLAY", styleButton);

		HorizontalGroup group = new HorizontalGroup();
		group.space((Gdx.graphics.getWidth()-buttonWidth*2)/3);
		group.addActor(button1);
		group.addActor(button2);

		tablePopup.add(group);

		stage.addActor(tablePopup);

		button2.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
//				if(popupOpened) return;
				closePopup();
			}
		});

		button1.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Timer.instance().clear();
				Timer.instance().start();
				parent.returnToMenu();
			}
		});
	}

    private void generateUI(FreeTypeFontGenerator generator, FreeTypeFontGenerator.FreeTypeFontParameter parameter){
		table = new Table();
		table.setFillParent(true);
		stage.addActor(table);

		final int widgetSize = Math.round(screenHeight*UI_SCALE*0.84f);
		final int widgetMargin = Math.round((screenHeight*UI_SCALE - widgetSize)/2);

		parameter.size = Math.round(widgetSize*0.84f); // font size
		parameter.color = Color.WHITE;
		font = generator.generateFont(parameter);

		Label.LabelStyle labelStateStyle = new Label.LabelStyle(font, Color.WHITE);
		labelState = new Label("YOUR TURN", labelStateStyle);//YOUR TURN, BOT PLAYING
		labelState.setColor(Color.GREEN);

		pauseTexture = new Texture(Gdx.files.internal("pause.png"));
		TextureRegion pauseRegion = new TextureRegion(pauseTexture);
		Button.ButtonStyle pauseButtonStyle = new Button.ButtonStyle();

		pauseButtonStyle.up = new TextureRegionDrawable(pauseRegion);
		pauseButtonStyle.down = new TextureRegionDrawable(pauseRegion);

		Button buttonPause = new Button(pauseButtonStyle);

		buttonPause.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
                if(popupOpened) return;
				openPopup();
			}
		});

		table.top();
		table.left();
		int padRight = Math.round(screenWidth - widgetSize - widgetMargin - widgetSize*6 - widgetMargin);
		table.add(labelState).size(widgetSize*6, widgetSize).padLeft(widgetMargin).padTop(widgetMargin).padRight(padRight);
		table.add(buttonPause).size(widgetSize, widgetSize).padTop(widgetMargin);
	}

	private void closePopup(){
		popupOpened = false;
		tablePopup.setVisible(false);
		Timer.instance().start();
	}

	private void openPopup(){
		popupOpened = true;
		tablePopup.setVisible(true);
		Timer.instance().stop();
	}

	private void generateCards(){
    	float uiSize = screenHeight*UI_SCALE;
		float sy = ((screenHeight-uiSize)*SPACING)/ gridHeight;
		float sx = (screenWidth*SPACING)/ gridWidth;
		int size = (int)Math.floor(sy < sx ? sy : sx);

		int marginX = (int)Math.floor((screenWidth-size* gridWidth)/(gridWidth +2));
		int marginY = (int)Math.floor(((screenHeight-uiSize)-size* gridHeight)/(gridHeight +2));

		int offsetX = (int)Math.floor( (screenWidth - (size* gridWidth +marginX*(gridWidth -1)))/2 );
		int offsetY = (int)Math.floor( (screenHeight-uiSize - (size* gridHeight +marginY*(gridHeight -1)))/2 );

//		generateShapesTextures(size);
		shapesTextures = myUtils.getShapesTextures(size);

		texture_off = getTexture(size);

		Array<CardTemplate> templatesAll = new Array<>();

		int template_id = 0;
		for(int i = 0; i < CARD_COLORS.length; i++){
			for(int j = 0; j < 6; j++){//shapes
				templatesAll.add(
						new CardTemplate(j, CARD_COLORS[i], template_id++)
				);
			}
		}

		Array<CardTemplate> templatesPairs = new Array<>();

		for(int i = 0; i < (gridWidth * gridHeight)/2; i++){
			CardTemplate template = templatesAll.removeIndex(MathUtils.random(0,templatesAll.size-1));
			templatesPairs.add(template, template);
		}

		for(int y = 0; y < gridHeight; y++)
			for(int x = 0; x < gridWidth; x++){
				CardTemplate template = templatesPairs.removeIndex(MathUtils.random(0,templatesPairs.size-1));
//				CardTemplate template = new CardTemplate(SHAPE.CIRCLE, CARD_COLORS[i], false, template_id++);

//				Texture texture_on = getCardTexture(size, template);

				Card c = new Card(
						offsetX + x*(size + marginX),
						offsetY + y*(size + marginY),
						x + y* gridWidth,
						template.id,
						template.color,
						shapesTextures[template.shape],
						texture_off
				);
				cards.add(c);
			}
	}

    @Override
    public void show() {

    }

    @Override
    public void render(float _delta) {

    	gameObserver.update(_delta);

        if(canClick && !popupOpened && Gdx.input.justTouched()) {
			Vector3 touchPoint = new Vector3();
			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			for (Card c : cards){
				if (c.hidden && c.clicked(new Vector2(touchPoint.x, touchPoint.y))) {
					c.reveal();
					if(bot != null)
						bot.registerCard(c.id, c.key);
					if(card1 == null){
						card1 = c;
					}else{
						card2 = c;
						canClick = false;

						Timer.schedule(new Timer.Task(){
							@Override
							public void run() {
								evaluateMove();
							}
						}, UNCOVER_DELAY);

					}
					break;
				}
			}
		}

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		ScreenUtils.clear(0, 0, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

		batch.begin();
		for(Card c: cards)
			c.draw(batch, _delta);
		batch.end();

		stage.act(Math.min(_delta, 1 / 30f));
		stage.draw();

		if(popupOpened) {//popup opened
			batch.begin();
			batch.setColor(0, 0, 0, 0.66f);
			batch.draw(myUtils.blankWhite, 0, 0, Gdx.graphics.getWidth(), tablePopup.getY());
			batch.draw(myUtils.blankWhite, 0, tablePopup.getY()+tablePopup.getHeight(), Gdx.graphics.getWidth(), tablePopup.getY());
			batch.end();
		}
    }

    private void evaluateMove(){
		if(card1.key == card2.key){//MATCH, collecting cards
			if(bot != null) {
				bot.forgetCard(card1.id);
				bot.forgetCard(card2.id);
			}
			cards.removeValue(card1, true);
			cards.removeValue(card2, true);
//			card1.dispose();
//			card2.dispose();
			if(cards.size == 0){//END GAME
//				System.out.println("<adad>");
				parent.showResults(gameObserver.getTime(), gameObserver.getMistakes());
//				Timer.instance().clear();
//				parent.showResults(1,1);
				return;
			}
			if(playerTurn){
				canClick = true;
			}else{
				Timer.schedule(new Timer.Task(){
					@Override
					public void run() {
						botMove();
					}
				}, UNCOVER_DELAY);
			}
		}else{//not match
			card1.hide();
			card2.hide();
			if(bot != null) {
				if (playerTurn) {//switch to bot
					playerTurn = false;
					labelState.setColor(Color.RED);
					labelState.setText("BOT PLAYING");
					Timer.schedule(new Timer.Task() {
						@Override
						public void run() {
							botMove();
						}
					}, UNCOVER_DELAY);
				} else {//switch to player
					playerTurn = true;
					canClick = true;
					labelState.setColor(Color.GREEN);
					labelState.setText("YOUR TURN");
				}
			}else{
				canClick = true;
			}
		}

		card1 = null;
		card2 = null;
	}

	private void botMove(){
    	if(card1 == null) {//bot pick first card
			card1 = findCardById(bot.getMove());
			bot.registerCard(card1.id, card1.key);
			card1.reveal();

			Timer.schedule(new Timer.Task(){
				@Override
				public void run() {
					botMove();
				}
			}, UNCOVER_DELAY);

		}else if(card2 == null){//pick second card
			card2 = findCardById(bot.getMove());
			bot.registerCard(card2.id, card2.key);
			card2.reveal();

			Timer.schedule(new Timer.Task(){
				@Override
				public void run() {
					evaluateMove();
				}
			}, UNCOVER_DELAY);

		}
	}

	private Card findCardById(int _id){
    	for(Card c : cards)
    		if(c.id == _id) return c;
    	return null;
	}

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
		texture_off.dispose();
//		for(Card c: cards)
//			c.dispose();
		for(int i = 0; i < 6; i++)
			shapesTextures[i].dispose();
		font.dispose();
		buttonFont.dispose();
		pauseTexture.dispose();
//		textureBlankWhite.dispose();
		textureButton1.dispose();
		popupDrawable1.dispose();
    }

    private Texture getTexture(int s){
		Pixmap pixmap = new Pixmap(s, s, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		myUtils.drawRoundedRectangle(pixmap, 0,0,s,s,s/10);
		Texture t = new Texture(pixmap);
		t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		pixmap.dispose();
		return t;
	}

}
