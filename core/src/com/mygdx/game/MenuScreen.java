package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuScreen implements Screen {

    private MyGdxGame parent;
    private Stage stage;
    private Table table;
    private Texture playTexture, choiceTexture;
    private BitmapFont buttonFont;

    public MenuScreen(MyGdxGame _p) {//TODO clickable buttons + styles
        parent = _p;

        Vector2 dimensions = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        /////////////////////////////////////////////

        int playRadius = Math.round(dimensions.x/4);
        playTexture = getPlayTexture(playRadius);

        TextureRegion playRegion = new TextureRegion(playTexture);
        Button.ButtonStyle playButtonStyle = new Button.ButtonStyle();

        playButtonStyle.up = new TextureRegionDrawable(playRegion);
        playButtonStyle.down = new TextureRegionDrawable(playRegion);

        Button buttonPlay = new Button(playButtonStyle);

        /////////////////////////////////////////////

        int buttonWidth = Math.round(3*dimensions.x/10);
        int buttonHeight = Math.round(buttonWidth/1.68f);
        choiceTexture = getChoiceTexture(buttonWidth, buttonHeight);
        TextureRegion choiceRegion = new TextureRegion(choiceTexture);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Raleway-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = Math.round(buttonHeight*0.5f); // font size
        parameter.color = Color.BLACK;
//        parameter.borderWidth = 2;
//        parameter.spaceX = 2;
        buttonFont = generator.generateFont(parameter);
        generator.dispose();

        TextButton.TextButtonStyle choiceButtonStyle = new TextButton.TextButtonStyle();
        choiceButtonStyle.up = new TextureRegionDrawable(choiceRegion);
        choiceButtonStyle.font = buttonFont;

        String[] options1 = {"BOT II", "BOT III","SOLO", "BOT I",};
        String[] options2 = {"7x4", "8x5", "6x3"};

        Color[] colors1 = {Color.YELLOW, Color.RED, Color.WHITE, Color.GREEN};
        Color[] colors2 = {Color.YELLOW, Color.RED, Color.GREEN};

        final MyButton buttonMode = new MyButton(options1, colors1, choiceButtonStyle);
        final MyButton buttonSize = new MyButton(options2, colors2, choiceButtonStyle);

//      buttonSolo.setChecked(true);
//      button74.setChecked(true);

        /////////////////////////////////////////////

        buttonPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GAMEMODE gamemodes[] = {GAMEMODE.BOT2, GAMEMODE.BOT3, GAMEMODE.SOLO, GAMEMODE.BOT1};
                GRIDSIZE sizes[] = {GRIDSIZE.MEDIUM, GRIDSIZE.BIG, GRIDSIZE.SMALL};

                parent.startGame(gamemodes[buttonMode.state], sizes[buttonSize.state]);
            }
        });

        HorizontalGroup group1 = new HorizontalGroup();
        group1.space(2*dimensions.x/10);
        group1.addActor(buttonMode);
        group1.addActor(buttonSize);

        table.bottom();
        table.add(buttonPlay);
        table.row();
        int padToCenter = Math.round((dimensions.y/2-playRadius) - (buttonHeight+dimensions.x/10));
        table.add(group1).padTop(padToCenter).padBottom(dimensions.x/10);
    }

    @Override
    public void show() {

    }

    private Texture getPlayTexture(int _radius){
        int triOffset = _radius/4;
        Pixmap pixmap = new Pixmap(_radius*2, _radius*2, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fillCircle(_radius,_radius,_radius);
        pixmap.setColor(Color.BLACK);
        pixmap.fillTriangle(triOffset + _radius/2,_radius/2,
                triOffset + _radius + _radius/4,_radius,
                triOffset + _radius/2, _radius*2-_radius/2);
        Texture t = new Texture(pixmap);
        pixmap.dispose();
        return t;
    }

    private Texture getChoiceTexture(int _sx, int _sy){
        Pixmap pixmap2 = new Pixmap(_sx, _sy, Pixmap.Format.RGBA8888);
        pixmap2.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
        pixmap2.fillRectangle(0, 0, _sx, _sy);
//        pixmap2.setColor(Color.BLACK);
//        pixmap2.fillRectangle(_sx/20, _sx/20, _sx - 2*_sx/20, _sy-2*_sx/20);
        Texture t = new Texture(pixmap2);
        pixmap2.dispose();
        return t;
    }

    @Override
    public void render(float _delta) {
        ScreenUtils.clear(0, 0, 0, 1);

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
        playTexture.dispose();
        choiceTexture.dispose();
        buttonFont.dispose();
        stage.dispose();
    }
}
