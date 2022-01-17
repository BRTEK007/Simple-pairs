package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;

public class CardTemplate {

    public SHAPE shape;
    public Color color;
    public boolean filled;
    public int id;

    public CardTemplate(SHAPE _shape, Color _color, boolean _filled, int _id){
        this.shape = _shape;
        this.color = _color;
        this.filled = _filled;
        this.id = _id;
    }

}
