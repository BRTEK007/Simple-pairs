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
    private Color color;
    public int key;
    public int id;
    public boolean hidden;

    final float rotationSpeed = (float) Math.PI*3;
    private float angle;
    private boolean flipped;
    private boolean finished;

    public Card(int _x, int _y, int _id, int _key, Color _color, Texture _on, Texture _off){
        texture_on = _on;
        texture_off = _off;//TODO fix to _off
        color = _color;
        sprite = new Sprite(this.texture_off);
        sprite.setColor(Color.WHITE);
//        sprite.setColor(Color.RED);
//        sprite.rotate(45);
//        sprite.setScale(0.5f);
        sprite.setPosition(_x, _y);
//        sprite.setColor(0.5f,0.5f,0.5f,1f);//TODO remove
        key = _key;
        id = _id;
        hidden = true;

        angle = 0;
        flipped = false;
        finished = true;
    }

    public boolean clicked(Vector2 _tp){
//        if(!hidden) return false;
        if (sprite.getBoundingRectangle().contains(_tp.x, _tp.y))
            return true;
        return false;
    }


    public void draw(SpriteBatch _b, float _delta){
//        _b.enableBlending();
//        _b.draw(sprite, sprite.getX(), sprite.getY());
        if(!finished) {
            if(!hidden) {//revealing
                angle += rotationSpeed * _delta;
                if (!flipped && angle >= Math.PI / 2) {
                    flipped = true;
                    sprite.setRegion(texture_on);
                    sprite.setColor(color);
                } else if (angle >= Math.PI) {
                    finished = true;
                    angle = (float) Math.PI;
                }
            }else{
                angle -= rotationSpeed * _delta;
                if (!flipped && angle <= Math.PI / 2) {
                    flipped = true;
                    sprite.setRegion(texture_off);
                    sprite.setColor(Color.WHITE);
                } else if (angle <= 0) {
                    finished = true;
                    angle = 0;
                }
            }
            sprite.setScale((float)Math.cos(angle),1);
        }

        sprite.draw(_b);
    }

    public void hide(){
        hidden = true;
//        sprite.setRegion(texture_off);
//        sprite.setColor(0.5f,0.5f,0.5f,1f);
        finished = false;
        flipped = false;
    }

    public void reveal(){
        hidden = false;
//        sprite.setRegion(texture_on);
        finished = false;
        flipped = false;
//        sprite.setColor(1,1,1,1f);
    }

}
