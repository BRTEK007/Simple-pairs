package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MyButton extends TextButton {//TODO change values on click, tint on down

    private boolean hover;
    public int state;
    private String[] options;
    private Color[] colors;

    public MyButton(String[] _options, Color[] _colors, TextButtonStyle skin) {
        super(_options[0], skin);

        state = 0;
        options = _options;
        colors = _colors;

        addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                state = state+1 >= options.length ? 0 : state+1;
                setText(options[state]);
                return true;
            }

        });
    }

    @Override
    public Color getColor() {
//        if (!hover)
//            return super.getColor();
//        else
//            return Color.RED;
        return colors[state];
    }

}
