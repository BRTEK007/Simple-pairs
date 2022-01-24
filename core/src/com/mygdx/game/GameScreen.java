package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
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
	private Table table;
	private BitmapFont font;
	private Texture pauseTexture;
	private Label labelState;

    public GameScreen(MyGdxGame _p, GAMEMODE _gamemode, GRIDSIZE _gridSize){
        parent = _p;

		batch = new SpriteBatch();

		camera = new OrthographicCamera();

		screenHeight = Gdx.graphics.getHeight();
		screenWidth = Gdx.graphics.getWidth();

		camera.setToOrtho(false, screenWidth, screenHeight);

		switch (_gridSize){
			case SMALL: gridHeight = 6; gridWidth = 3; break;
			case MEDIUM: gridHeight = 7; gridWidth = 4; break;
			case BIG: gridHeight = 8; gridWidth = 5; break;
		}

		cards = new Array<>();
		generateCards();

		card1 = null;
		card2 = null;
		canClick = true;
		gameObserver = new GameObserver();
		switch (_gamemode){
			case SOLO: bot = null; break;
			case BOT1: bot = new Bot(cards.size, 3); break;
			case BOT2: bot = new Bot(cards.size, 5); break;
			case BOT3: bot = new Bot(cards.size, cards.size); break;
		}
		playerTurn = true;


		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		table = new Table();
		table.setFillParent(true);
		stage.addActor(table);

		final int widgetSize = Math.round(screenHeight*UI_SCALE*0.84f);
		final int widgetMargin = Math.round((screenHeight*UI_SCALE - widgetSize)/2);

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Raleway-Regular.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = Math.round(widgetSize*0.84f); // font size
		parameter.color = Color.WHITE;
		font = generator.generateFont(parameter);
        generator.dispose();

		Label.LabelStyle labelStateStyle = new Label.LabelStyle(font, Color.WHITE);
		labelState = new Label("YOUR TURN", labelStateStyle);//YOUR TURN, BOT PLAYING
		labelState.setColor(Color.GREEN);

		pauseTexture = new Texture(Gdx.files.internal("pause.png"));
		TextureRegion pauseRegion = new TextureRegion(pauseTexture);
		Button.ButtonStyle pauseButtonStyle = new Button.ButtonStyle();

		pauseButtonStyle.up = new TextureRegionDrawable(pauseRegion);
		pauseButtonStyle.down = new TextureRegionDrawable(pauseRegion);

		Button buttonPause = new Button(pauseButtonStyle);

		table.top();
		table.left();
		int padRight = Math.round(screenWidth - widgetSize - widgetMargin - widgetSize*6 - widgetMargin);
		table.add(labelState).size(widgetSize*6, widgetSize).padLeft(widgetMargin).padTop(widgetMargin).padRight(padRight);
		table.add(buttonPause).size(widgetSize, widgetSize).padTop(widgetMargin);
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

		texture_off = getTexture(size);

		Array<CardTemplate> templatesAll = new Array<>();

		int template_id = 0;
		for(int i = 0; i < CARD_COLORS.length; i++){
			templatesAll.add(
					new CardTemplate(SHAPE.CIRCLE, CARD_COLORS[i], true, template_id++),
					new CardTemplate(SHAPE.TRIANGLE, CARD_COLORS[i], true, template_id++),
					new CardTemplate(SHAPE.SQUARE, CARD_COLORS[i], true, template_id++)
			);
			templatesAll.add(
					new CardTemplate(SHAPE.CIRCLE, CARD_COLORS[i], false, template_id++),
					new CardTemplate(SHAPE.TRIANGLE, CARD_COLORS[i], false, template_id++),
					new CardTemplate(SHAPE.SQUARE, CARD_COLORS[i], false, template_id++)
			);
		}

		Array<CardTemplate> templatesPairs = new Array<>();

		for(int i = 0; i < (gridWidth * gridHeight)/2; i++){
			CardTemplate template = templatesAll.removeIndex(MathUtils.random(0,templatesAll.size-1));
			templatesPairs.add(template, template);
		}

		for(int y = 0; y < gridHeight; y++)
			for(int x = 0; x < gridWidth; x++){
				CardTemplate template = templatesPairs.removeIndex(MathUtils.random(0,templatesPairs.size-1));

				Texture texture_on = getCardTexture(size, template);

				Card c = new Card(
						offsetX + x*(size + marginX),
						offsetY + y*(size + marginY),
						x + y* gridWidth,
						template.id,
						texture_on,
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

        if(canClick && Gdx.input.justTouched()) {
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
		batch.begin();
		for(Card c: cards)
			c.draw(batch, _delta);
		batch.end();

		stage.act(Math.min(_delta, 1 / 30f));
		stage.draw();
    }

    private void evaluateMove(){
		if(card1.key == card2.key){//MATCH, collecting cards
			if(bot != null) {
				bot.forgetCard(card1.id);
				bot.forgetCard(card2.id);
			}
			cards.removeValue(card1, true);
			cards.removeValue(card2, true);
			card1.dispose();
			card2.dispose();
			if(cards.size == 0){//END GAME
				parent.showResults(gameObserver.getTime(), gameObserver.getMistakes());
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
		for(Card c: cards)
			c.dispose();
		font.dispose();
		pauseTexture.dispose();
    }

    private Texture getTexture(int s){
		Pixmap pixmap = new Pixmap(s, s, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
//		pixmap.fillRectangle(0,0,s,s);
		myUtils.drawRoundedRectangle(pixmap, 0,0,s,s,s/10);
//		pixmap.setColor(Color.BLACK);
//		pixmap.fillRectangle(0,0,s,s);
//		drawRoundedRectangle(pixmap, s/10,s/10,s - s/5,s - s/5,s/10, Color.BLACK);
		Texture t = new Texture(pixmap);
		pixmap.dispose();
		return t;
	}

	private Texture getCardTexture(int s, CardTemplate _t){
		Pixmap pixmap = new Pixmap(s, s, Pixmap.Format.RGBA8888);
//		pixmap.setColor(0.2f,0.2f,0.2f,1);
//		pixmap.setColor(Color.WHITE);
//		pixmap.drawRectangle(0,0,s,s);
//		pixmap.setColor(0.2f,0.2f,0.2f,1);
//		pixmap.fillRectangle(0,0,s,s);
		drawPuzzle(pixmap, s, _t.shape, _t.color, _t.filled);
		Texture t = new Texture(pixmap);
		pixmap.dispose();
		return t;
	}

	private void drawPuzzle(Pixmap _pixmap, int _size, SHAPE _shape, Color _color, boolean _filled){
		_pixmap.setColor(_color);
		final float marginRate = 8.8f;
		final float strokeRate = 0.1f;
		switch (_shape){
			case TRIANGLE://TODO fix with vectors
				Vector2 v1 = new Vector2(0, -_size/2 + _size/marginRate);
				Vector2 v2 = v1.cpy();
				v2.rotateDeg(120);
				Vector2 v3 = v2.cpy();
				v3.rotateDeg(120);

				v1.add(new Vector2(_size/2, _size/2));
				v2.add(new Vector2(_size/2, _size/2));
				v3.add(new Vector2(_size/2, _size/2));
				_pixmap.fillTriangle(Math.round(v1.x), Math.round(v1.y), Math.round(v2.x), Math.round(v2.y), Math.round(v3.x), Math.round(v3.y));

				if(!_filled){
					v1.sub(new Vector2(_size/2, _size/2));
					v2.sub(new Vector2(_size/2, _size/2));
					v3.sub(new Vector2(_size/2, _size/2));

					Matrix3 m = new Matrix3();
					m.idt();
					m.scl((_size/2 - _size/marginRate - _size*strokeRate)/(_size/2 + _size/marginRate));

					v1.mul(m);
					v2.mul(m);
					v3.mul(m);

					v1.add(new Vector2(_size/2, _size/2));
					v2.add(new Vector2(_size/2, _size/2));
					v3.add(new Vector2(_size/2, _size/2));
					_pixmap.setColor(Color.BLACK);
//					_pixmap.setColor(0.2f,0.2f,0.2f,1);
					_pixmap.fillTriangle(Math.round(v1.x), Math.round(v1.y), Math.round(v2.x), Math.round(v2.y), Math.round(v3.x), Math.round(v3.y));
				}

				break;
			case CIRCLE:
				_pixmap.fillCircle(Math.round(_size/2), Math.round(_size/2), Math.round(_size/2 - _size/marginRate));
				if(!_filled){
					_pixmap.setColor(Color.BLACK);
//					_pixmap.setColor(0.2f,0.2f,0.2f,1);
					_pixmap.fillCircle(Math.round(_size/2), Math.round(_size/2), Math.round(_size/2 - _size/marginRate - _size*strokeRate));
				}
				break;

			case SQUARE:
				_pixmap.fillRectangle(
						Math.round(_size/marginRate),
						Math.round(_size/marginRate),
						Math.round(_size - 2*_size/marginRate),
						Math.round(_size - 2*_size/marginRate)
				);
				if(!_filled){
					_pixmap.setColor(Color.BLACK);
//					_pixmap.setColor(0.2f,0.2f,0.2f,1);
					_pixmap.fillRectangle(
							Math.round(_size/marginRate + _size*strokeRate),
							Math.round(_size/marginRate + _size*strokeRate),
							Math.round(_size - 2*_size/marginRate - 2*_size*strokeRate),
							Math.round(_size - 2*_size/marginRate - 2*_size*strokeRate)
					);
				}
				break;
		}
	}


}
