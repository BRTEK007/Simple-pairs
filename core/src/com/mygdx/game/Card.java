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
    public int key;
    public int id;
    public boolean hidden;

    public Card(int _x, int _y, int _id, int _key, Texture _on, Texture _off){
        texture_on = _on;
        texture_off = _off;//TODO fix to _off
        sprite = new Sprite(this.texture_off);
//        sprite.setColor(Color.RED);
//        sprite.rotate(45);
//        sprite.setScale(0.5f);
        sprite.setPosition(_x, _y);
//        sprite.setColor(0.5f,0.5f,0.5f,1f);//TODO remove
        key = _key;
        id = _id;
        hidden = true;
    }

    public boolean clicked(Vector2 _tp){
//        if(!hidden) return false;
        if (sprite.getBoundingRectangle().contains(_tp.x, _tp.y))
            return true;
        return false;
    }


    public void draw(SpriteBatch _b){
//        _b.enableBlending();
//        _b.draw(sprite, sprite.getX(), sprite.getY());
        sprite.draw(_b);
    }

    public void hide(){
        hidden = true;
        sprite.setRegion(texture_off);
//        sprite.setColor(0.5f,0.5f,0.5f,1f);
    }

    public void reveal(){
        hidden = false;
        sprite.setRegion(texture_on);
//        sprite.setColor(1,1,1,1f);
    }

    public void dispose(){
        texture_on.dispose();
    }

}
