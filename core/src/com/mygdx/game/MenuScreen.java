package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
    private Texture texture1, texture2;
    private BitmapFont buttonFont;

    public MenuScreen(MyGdxGame _p) {//TODO clickable buttons + styles
        parent = _p;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Pixmap pixmap = new Pixmap(100,100, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fillRectangle(10, 10, 80, 80);
        texture1 = new Texture(pixmap);
        pixmap.dispose();

        Pixmap pixmap2 = new Pixmap(100,100, Pixmap.Format.RGBA8888);
        pixmap2.setColor(Color.GREEN);
        pixmap2.fillRectangle(10, 10, 80, 80);
        texture2 = new Texture(pixmap2);
        pixmap2.dispose();

        TextureRegion upRegion = new TextureRegion(texture1);
        TextureRegion downRegion = new TextureRegion(texture2);
        buttonFont = new BitmapFont();
        buttonFont.getData().setScale(2);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = new TextureRegionDrawable(upRegion);
        textButtonStyle.down = new TextureRegionDrawable(downRegion);
        textButtonStyle.font = buttonFont;

        TextButton button1 = new TextButton("Button 1", textButtonStyle);
        TextButton button2 = new TextButton("Button 1", textButtonStyle);

        button1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen();
            }
        });

        table.add(button1);
        table.row();
        table.add(button2);
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
        texture2.dispose();
        buttonFont.dispose();
        stage.dispose();
    }
}
