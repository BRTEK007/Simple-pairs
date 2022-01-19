package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MyButton extends TextButton {//TODO change values on click, tint on down

    private boolean hover;

    public MyButton(String text, TextButtonStyle skin) {
        super(text, skin);
        addListener(new InputListener() {
//            @Override
//            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
//                hover = true;
//            }
//
//            @Override
//            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
//                hover = false;
//            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                hover = true;
                return true;
            }

        });
    }

    @Override
    public Color getColor() {
        if (!hover)
            return super.getColor();
        else
            return Color.RED;
    }

}
