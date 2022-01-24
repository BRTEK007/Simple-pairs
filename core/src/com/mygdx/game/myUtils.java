package com.mygdx.game;

import com.badlogic.gdx.graphics.Pixmap;

public class myUtils {

    public static void drawRoundedRectangle(Pixmap pixmap, int x, int y, int width, int height, int radius) {
        pixmap.fillRectangle(x, y + radius, width, height-2*radius);
        pixmap.fillRectangle(x + radius, y, width - 2*radius, height);
        pixmap.fillCircle(x+radius, y+radius, radius);
        pixmap.fillCircle(x+radius, y+height-radius, radius);
        pixmap.fillCircle(x+width-radius, y+radius, radius);
        pixmap.fillCircle(x+width-radius, y+height-radius, radius);
    }
}
