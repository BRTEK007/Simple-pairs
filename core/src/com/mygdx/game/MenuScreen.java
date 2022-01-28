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

    final String CARDS_STRINGS[] = {"18", "28", "40", "54"};
    final String GAMEMODES_STRINGS[] = {"SOLO", "BOT I", "BOT II", "BOT III"};

    private MyGdxGame parent;

    private int gamemode;
    private int gridsize;

    private Stage stage;
    private Table table, tablePopup;
    private Texture textureButton1,textureButton2, textureButton3, textureBlankWhite;
    private BitmapFont fontButton1, fontButton2, fontButton3, fontLabel;
    private Label labelCards, labelGamemode;
    private PopupDrawable popupDrawable1;
    private TextButton popupButtons[];
    private TextButton buttonCards, buttonGamemode;

    private SpriteBatch batch;

    private int popupMode;//-1 off, 0 - cards, 1 - gamemode

    public MenuScreen(MyGdxGame _p) {//TODO clickable buttons + styles
        parent = _p;

        gamemode = 1;
        gridsize = 1;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Raleway-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        batch = new SpriteBatch();

        initFront(generator, parameter);
        initPopup(generator, parameter);

        generator.dispose();

        textureBlankWhite = myUtils.getBlankWhite();
    }

    @Override
    public void show() {

    }

    private void initFront(FreeTypeFontGenerator generator, FreeTypeFontGenerator.FreeTypeFontParameter parameter){
        int buttonWidth = Math.round(3*Gdx.graphics.getWidth()/10);
        int buttonHeight = Math.round(buttonWidth/1.68f);
        int buttonMargin = Math.round(buttonWidth*0.1f);
        textureButton1 = myUtils.getButtonTexture(buttonWidth, buttonHeight);
        TextureRegion choiceRegion = new TextureRegion(textureButton1);

        parameter.size = Math.round(buttonHeight*0.42f); // font size
        parameter.color = Color.BLACK;
        fontButton1 = generator.generateFont(parameter);

        TextButton.TextButtonStyle choiceButtonStyle = new TextButton.TextButtonStyle();
        choiceButtonStyle.up = new TextureRegionDrawable(choiceRegion);
        choiceButtonStyle.font = fontButton1;

        buttonGamemode = new TextButton(GAMEMODES_STRINGS[1], choiceButtonStyle);
        buttonCards = new TextButton(CARDS_STRINGS[1], choiceButtonStyle);
        /////////////////////////////////////////////

        textureButton2 = myUtils.getButtonTexture(buttonWidth*2 + buttonMargin, Math.round((buttonWidth*2 + buttonMargin)/1.68f));

        parameter.size = Math.round(buttonHeight*0.84f); // font size
        fontButton2 = generator.generateFont(parameter);

        TextureRegion playRegion = new TextureRegion(textureButton2);
        TextButton.TextButtonStyle playButtonStyle = new TextButton.TextButtonStyle();
        playButtonStyle.font = fontButton2;
        playButtonStyle.up = new TextureRegionDrawable(playRegion);
        playButtonStyle.down = new TextureRegionDrawable(playRegion);

        TextButton buttonPlay = new TextButton("PLAY", playButtonStyle);

        parameter.size = Math.round(buttonHeight*0.26f);
        parameter.color = Color.WHITE;
        fontLabel = generator.generateFont(parameter);

        Label.LabelStyle labelStateStyle = new Label.LabelStyle(fontLabel, Color.WHITE);
        labelCards = new Label("cards:", labelStateStyle);
        labelGamemode = new Label("gamemode:", labelStateStyle);

        VerticalGroup group1 = new VerticalGroup();
        group1.space(buttonMargin/2);
        group1.addActor(labelCards);
        group1.addActor(buttonCards);

        VerticalGroup group2 = new VerticalGroup();
        group2.space(buttonMargin/2);
        group2.addActor(labelGamemode);
        group2.addActor(buttonGamemode);

        HorizontalGroup group3 = new HorizontalGroup();
        group3.space(buttonMargin);
        group3.addActor(group1);
        group3.addActor(group2);

        table.add(group3);
        table.row();
        table.add(buttonPlay).padTop(buttonMargin);
        table.row();

        buttonPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(popupMode != -1) return;
                parent.startGame(gamemode, gridsize);
            }
        });

        buttonGamemode.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(popupMode != -1) return;
                openPopup(1);
            }
        });

        buttonCards.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(popupMode != -1) return;
                openPopup(0);
            }
        });
    }

    private void initPopup(FreeTypeFontGenerator generator, FreeTypeFontGenerator.FreeTypeFontParameter parameter){
        popupMode = -1;
        popupDrawable1 = new PopupDrawable();

        int buttonWidth = Math.round(0.84f*Gdx.graphics.getWidth() / 4);
        int buttonHeight = Math.round(buttonWidth/1.68f);
        int popupHeight = buttonHeight*2;

        tablePopup = new Table();
        tablePopup.setBackground(popupDrawable1);
        tablePopup.setFillParent(false);
        tablePopup.setSize(Gdx.graphics.getWidth(), popupHeight);
        tablePopup.setY(Gdx.graphics.getHeight()/2 - popupHeight/2);
        tablePopup.setVisible(false);


        parameter.size = Math.round(buttonHeight*0.42f); // font size
        parameter.color = Color.BLACK;
        fontButton3 = generator.generateFont(parameter);

        textureButton3 = myUtils.getButtonTexture(buttonWidth, buttonHeight);

        parameter.size = Math.round(buttonHeight*0.84f); // font size
        fontButton2 = generator.generateFont(parameter);

        TextureRegion regionButton = new TextureRegion(textureButton3);
        TextButton.TextButtonStyle styleButton = new TextButton.TextButtonStyle();
        styleButton.font = fontButton3;
        styleButton.up = new TextureRegionDrawable(regionButton);
        styleButton.down = new TextureRegionDrawable(regionButton);


        popupButtons = new TextButton[4];
        popupButtons[0] = new TextButton("18", styleButton);
        popupButtons[1] = new TextButton("28", styleButton);
        popupButtons[2] = new TextButton("40", styleButton);
        popupButtons[3] = new TextButton("54", styleButton);

        HorizontalGroup group = new HorizontalGroup();
        group.space((Gdx.graphics.getWidth()-buttonWidth*4)/5);
        group.addActor(popupButtons[0]);
        group.addActor(popupButtons[1]);
        group.addActor(popupButtons[2]);
        group.addActor(popupButtons[3]);

        tablePopup.add(group);

        stage.addActor(tablePopup);

        popupButtons[0].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                if(popupMode != -1) return;
                closePopup(0);
            }
        });

        popupButtons[1].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                if(!popupOpened) return;
                closePopup(1);
            }
        });

        popupButtons[2].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                if(!popupOpened) return;
                closePopup(2);
            }
        });

        popupButtons[3].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                if(!popupOpened) return;
                closePopup(3);
            }
        });

    }

    private void openPopup(int mode){
        popupMode = mode;
        tablePopup.setVisible(true);
        if(popupMode == 0){
            for(int i = 0; i < CARDS_STRINGS.length; i++)
                popupButtons[i].setText(CARDS_STRINGS[i]);
        }else{
            for(int i = 0; i < GAMEMODES_STRINGS.length; i++)
                popupButtons[i].setText(GAMEMODES_STRINGS[i]);
        }

//        popupOpened = true;
    }

    private void closePopup(int choice){
        tablePopup.setVisible(false);
        if(popupMode == 0){//cards
            gridsize = choice;
            buttonCards.setText(CARDS_STRINGS[choice]);
        }else{//gamemode
            gamemode = choice;
            buttonGamemode.setText(GAMEMODES_STRINGS[choice]);
        }
        popupMode = -1;
    }

    @Override
    public void render(float _delta) {
        ScreenUtils.clear(0, 0, 0, 1);

//        batch.begin();
//        batch.draw(textureBlankWhite, 0, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
//        batch.end();

        stage.act(Math.min(_delta, 1 / 30f));
        stage.draw();

        if(popupMode != -1) {//popup opened
            batch.begin();
            batch.setColor(0, 0, 0, 0.66f);
            batch.draw(textureBlankWhite, 0, 0, Gdx.graphics.getWidth(), tablePopup.getY());
            batch.draw(textureBlankWhite, 0, tablePopup.getY()+tablePopup.getHeight(), Gdx.graphics.getWidth(), tablePopup.getY());
            batch.end();
        }
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
        textureButton2.dispose();
        textureButton1.dispose();
        textureButton3.dispose();
        textureBlankWhite.dispose();
        popupDrawable1.dispose();
        fontButton1.dispose();
        fontButton2.dispose();
        fontButton3.dispose();
        fontLabel.dispose();
        stage.dispose();
    }
}
