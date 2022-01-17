package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Card {

    private Sprite sprite;
    private Texture texture_on;
    private Texture texture_off;

    public Card(int _x, int _y, Texture _on, Texture _off){
        this.texture_on = _on;
        this.texture_off = _off;
        this.sprite = new Sprite(this.texture_off);
        this.sprite.setPosition(_x, _y);
    }

    public boolean clicked(Vector2 _tp){
        if (sprite.getBoundingRectangle().contains(_tp.x, _tp.y)) {
            sprite.setRegion(texture_on);
            return true;
        }
        return false;
    }


    public void draw(SpriteBatch _b){
        _b.draw(sprite, sprite.getX(), sprite.getY());
    }

    public void dispose(){
        texture_on.dispose();
    }

}
