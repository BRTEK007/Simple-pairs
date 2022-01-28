package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;

public class PopupDrawable extends BaseDrawable {

    private Color savedBatchColor = new Color();
    private Texture blankWhite;

    public PopupDrawable() {
        blankWhite = myUtils.getBlankWhite();
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        savedBatchColor.set(batch.getColor());
        batch.setColor(Color.BLACK);
        batch.draw(blankWhite, x, y, width, height);
        batch.setColor(Color.WHITE);
        batch.draw(blankWhite, x, y, width, 1);
        batch.draw(blankWhite, x, y + height-1, width, 1);
        batch.setColor(savedBatchColor);
    }

    public void dispose(){
        blankWhite.dispose();
    }

}
