package com.mygdx.game;

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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import sun.font.TextLabel;

public class ResultsScreen implements Screen {

    private MyGdxGame parent;

    private Stage stage;
    private Table table;
    private Texture texture1;
    private BitmapFont buttonFont;

    public ResultsScreen(MyGdxGame _p, int _time, int _mistakes){
        parent = _p;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Vector2 dimensions = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        int sizeX = Math.round(dimensions.x * 0.88f);
        int sizeY = Math.round(0.88f * dimensions.y / 6);
        int padding = Math.round((dimensions.y - sizeY*5)/12);

        Pixmap pixmap = new Pixmap(sizeX, sizeY, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
        pixmap.fillRectangle(0, 0, sizeX, sizeY);
        texture1 = new Texture(pixmap);
        pixmap.dispose();

        TextureRegion region = new TextureRegion(texture1);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Raleway-Medium.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = Math.round(sizeY*0.66f); // font size
        parameter.color = Color.BLACK;
        buttonFont = generator.generateFont(parameter);
//        buttonFont.setColor(Color.BLACK);
//        generator.dispose();

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = new TextureRegionDrawable(region);
        textButtonStyle.down = new TextureRegionDrawable(region);
        textButtonStyle.font = buttonFont;

        TextButton button = new TextButton("OK", textButtonStyle);

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.returnToMenu();
            }
        });

        parameter.color = Color.WHITE;
        parameter.size = Math.round(sizeY*0.33f);
        buttonFont = generator.generateFont(parameter);

        Label.LabelStyle labelStyle = new Label.LabelStyle(buttonFont, Color.WHITE);
        Label label = new Label("time: " + _time + "s", labelStyle);

        table.bottom();
        table.add(label).pad(padding);
        table.row();
        table.add(button).pad(padding);

        generator.dispose();
    }

    @Override
    public void show() {

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
        texture1.dispose();
        buttonFont.dispose();
        stage.dispose();
    }
}
