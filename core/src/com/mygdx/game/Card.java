package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Card {

    private Sprite sprite;
    private Texture texture_on;
    private Texture texture_off;
    public int id;
    private boolean hidden;

    public Card(int _x, int _y, int _id, Texture _on, Texture _off){
        texture_on = _on;
        texture_off = _off;
        sprite = new Sprite(this.texture_off);
//        sprite.setColor(Color.RED);
//        sprite.rotate(45);
//        sprite.setScale(0.5f);
        sprite.setPosition(_x, _y);
        id = _id;
        hidden = true;
    }

    public boolean clicked(Vector2 _tp){
        if(!hidden) return false;
        if (sprite.getBoundingRectangle().contains(_tp.x, _tp.y)) {
            hidden = false;
            sprite.setRegion(texture_on);
//            sprite.setPosition(-100, -100);
            return true;
        }
        return false;
    }


    public void draw(SpriteBatch _b){
//        _b.enableBlending();
        _b.draw(sprite, sprite.getX(), sprite.getY());
    }

    public void hide(){
        hidden = true;
        sprite.setRegion(texture_off);
    }

    public void dispose(){
        texture_on.dispose();
    }

}