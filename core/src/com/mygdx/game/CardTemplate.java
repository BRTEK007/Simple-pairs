package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;

public class CardTemplate {

    public SHAPE shape;
    public Color color;
    public boolean filled;

    public CardTemplate(SHAPE _shape, Color _color, boolean _filled){
        this.shape = _shape;
        this.color = _color;
        this.filled = _filled;
    }

}
