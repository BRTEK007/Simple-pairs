package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuScreen implements Screen {

    private MyGdxGame parent;

    private Stage stage;
    private Table table, tablePopup;
    private Texture textureButton1,textureButton2, lineTexture;
    private BitmapFont fontButton1, fontButton2, fontLabel;
    private Label labelCards, labelGamemode;
    private PopupDrawable popupDrawable1;
//    private RectDrawable drawableButton1;

    private SpriteBatch batch;

    boolean popupOpened;

    public MenuScreen(MyGdxGame _p) {//TODO clickable buttons + styles
        parent = _p;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
//        table.setSize(400,400);
        stage.addActor(table);

        /////////////////////////////////////////////

        int buttonWidth = Math.round(3*Gdx.graphics.getWidth()/10);
        int buttonHeight = Math.round(buttonWidth/1.68f);
        int buttonMargin = Math.round(buttonWidth*0.1f);
        textureButton1 = getTextureButton(buttonWidth, buttonHeight);
        TextureRegion choiceRegion = new TextureRegion(textureButton1);
//        drawableButton1 = new RectDrawable(buttonWidth, buttonHeight, buttonWidth/10);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Raleway-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = Math.round(buttonHeight*0.42f); // font size
        parameter.color = Color.BLACK;
        fontButton1 = generator.generateFont(parameter);

        TextButton.TextButtonStyle choiceButtonStyle = new TextButton.TextButtonStyle();
        choiceButtonStyle.up = new TextureRegionDrawable(choiceRegion);
        choiceButtonStyle.font = fontButton1;

        String[] options1 = {"BOT II", "BOT III","SOLO", "BOT I",};
        String[] options2 = {"7x4", "8x5", "6x3"};

        Color[] colors1 = {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE};
        Color[] colors2 = {Color.WHITE, Color.WHITE, Color.WHITE};

        final MyButton buttonMode = new MyButton(options1, colors1, choiceButtonStyle);
        final MyButton buttonSize = new MyButton(options2, colors2, choiceButtonStyle);
        /////////////////////////////////////////////

        textureButton2 = getTextureButton(buttonWidth*2 + buttonMargin, Math.round((buttonWidth*2 + buttonMargin)/1.68f));

        parameter.size = Math.round(buttonHeight*0.84f); // font size
        fontButton2 = generator.generateFont(parameter);

        TextureRegion playRegion = new TextureRegion(textureButton2);
        TextButton.TextButtonStyle playButtonStyle = new TextButton.TextButtonStyle();
        playButtonStyle.font = fontButton2;
        playButtonStyle.up = new TextureRegionDrawable(playRegion);
        playButtonStyle.down = new TextureRegionDrawable(playRegion);

        TextButton buttonPlay = new TextButton("PLAY", playButtonStyle);

        buttonPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GAMEMODE gamemodes[] = {GAMEMODE.BOT2, GAMEMODE.BOT3, GAMEMODE.SOLO, GAMEMODE.BOT1};
                GRIDSIZE sizes[] = {GRIDSIZE.MEDIUM, GRIDSIZE.BIG, GRIDSIZE.SMALL};

                parent.startGame(gamemodes[buttonMode.state], sizes[buttonSize.state]);
            }
        });

        parameter.size = Math.round(buttonHeight*0.26f);
        parameter.color = Color.WHITE;
        fontLabel = generator.generateFont(parameter);

        Label.LabelStyle labelStateStyle = new Label.LabelStyle(fontLabel, Color.WHITE);
        labelCards = new Label("cards:", labelStateStyle);
        labelGamemode = new Label("gamemode:", labelStateStyle);

        VerticalGroup group1 = new VerticalGroup();
        group1.space(buttonMargin/2);
        group1.addActor(labelCards);
        group1.addActor(buttonSize);

        VerticalGroup group2 = new VerticalGroup();
        group2.space(buttonMargin/2);
        group2.addActor(labelGamemode);
        group2.addActor(buttonMode);

        HorizontalGroup group3 = new HorizontalGroup();
        group3.space(buttonMargin);
        group3.addActor(group1);
        group3.addActor(group2);

        table.add(group3);
        table.row();
        table.add(buttonPlay).padTop(buttonMargin);
        table.row();

        ///////////////////////////////

        lineTexture = getLineTexture();
        batch = new SpriteBatch();

        popupOpened = false;

        popupDrawable1 = new PopupDrawable();

        tablePopup = new Table();
        tablePopup.setBackground(popupDrawable1);
        tablePopup.setFillParent(false);
        tablePopup.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()/4);
        tablePopup.setY(Gdx.graphics.getHeight()/4);
//        tablePopup.setVisible(false);


        parameter.size = Math.round(buttonHeight*0.42f); // font size
        parameter.color = Color.BLACK;
        fontButton1 = generator.generateFont(parameter);


        TextButton button1 = new TextButton("18", choiceButtonStyle);
        TextButton button2 = new TextButton("28", choiceButtonStyle);
        TextButton button3 = new TextButton("40", choiceButtonStyle);
        TextButton button4 = new TextButton("54", choiceButtonStyle);

        tablePopup.add(button1);
        tablePopup.add(button2);
        tablePopup.add(button3);
        tablePopup.add(button4);

        stage.addActor(tablePopup);

        generator.dispose();
    }

    @Override
    public void show() {

    }

    private Texture getLineTexture(){
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture t = new Texture(pixmap);
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pixmap.dispose();
        return t;
    }

    private Texture getTextureButton(int _sx, int _sy){
        Pixmap pixmap = new Pixmap(_sx, _sy, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
        myUtils.drawRoundedRectangle(pixmap, 0,0,_sx,_sy,_sx/10);
        Texture t = new Texture(pixmap);
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pixmap.dispose();
        return t;
    }

    @Override
    public void render(float _delta) {
        ScreenUtils.clear(0, 0, 0, 1);

//        batch.begin();
//        batch.draw(lineTexture, menuOffset, 0, 1, Gdx.graphics.getHeight());
//        batch.draw(lineTexture, Gdx.graphics.getWidth() - menuOffset, 0, 1, Gdx.graphics.getHeight());
//        batch.end();

        stage.act(Math.min(_delta, 1 / 30f));
        stage.draw();
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
        lineTexture.dispose();
        textureButton2.dispose();
        textureButton1.dispose();
//        drawableButton1.dispose();
        popupDrawable1.dispose();
        fontButton1.dispose();
        fontButton2.dispose();
        fontLabel.dispose();
        stage.dispose();
    }
}
