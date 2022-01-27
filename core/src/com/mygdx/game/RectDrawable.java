package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;

public class RectDrawable extends BaseDrawable {

    private Color savedBatchColor = new Color();
    private Texture texture;

    public RectDrawable(int width, int height, int radius) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        myUtils.drawRoundedRectangle(pixmap, 0,0, width, height, radius);
        texture = new Texture(pixmap);
        pixmap.dispose();
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        savedBatchColor.set(batch.getColor());
        batch.setColor(Color.WHITE);
        batch.draw(texture, x, y, width, height);
        batch.setColor(savedBatchColor);
    }

    public void dispose(){
        texture.dispose();
    }

}
