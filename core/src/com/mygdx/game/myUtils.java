package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;

public class myUtils {

    public static final float SHAPE_MARGIN_RATE = 8.8f;
    public static final float SHAPE_STROKE_RATE = 0.1f;

    public static Texture blankWhite;


    public static void init(){
        blankWhite = getBlankWhite();
    }

    public static void dispose(){
        blankWhite.dispose();
    }


    public static void drawRoundedRectangle(Pixmap pixmap, int x, int y, int width, int height, int radius) {
        pixmap.fillRectangle(x, y + radius, width, height-2*radius);
        pixmap.fillRectangle(x + radius, y, width - 2*radius, height);
        pixmap.fillCircle(x+radius, y+radius, radius);
        pixmap.fillCircle(x+radius, y+height-radius, radius);
        pixmap.fillCircle(x+width-radius, y+radius, radius);
        pixmap.fillCircle(x+width-radius, y+height-radius, radius);
    }

    private static Texture getBlankWhite(){
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture t = new Texture(pixmap);
        pixmap.dispose();
        return t;
    }


    public static Texture[] getShapesTextures(int _size){
        Texture t[] = new Texture[6];

        for(int i = 0; i < 6; i++){
            Pixmap pixmap = new Pixmap(_size, _size, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            switch (i){
                case 0: myUtils.drawShapeCircle(pixmap, _size, true); break;
                case 1: myUtils.drawShapeCircle(pixmap, _size, false); break;
                case 2: myUtils.drawShapeRectangle(pixmap, _size, true); break;
                case 3: myUtils.drawShapeRectangle(pixmap, _size, false); break;
                case 4: myUtils.drawShapeTriangle(pixmap, _size, true); break;
                case 5: myUtils.drawShapeTriangle(pixmap, _size, false); break;
            }
            t[i] = new Texture(pixmap);
//			shapesTextures[i].setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.MipMap);
            pixmap.dispose();
        }

        return t;
    }


    public static Texture getButtonTexture(int _sx, int _sy){
        Pixmap pixmap = new Pixmap(_sx, _sy, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
        myUtils.drawRoundedRectangle(pixmap, 0,0,_sx,_sy,_sx/10);
        Texture t = new Texture(pixmap);
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pixmap.dispose();
        return t;
    }

    public static void drawShapeTriangle(Pixmap _pixmap, int _size, boolean _filled){
        Vector2 v1 = new Vector2(0, -_size/2 + _size/ SHAPE_MARGIN_RATE);
        Vector2 v2 = v1.cpy();
        v2.rotateDeg(120);
        Vector2 v3 = v2.cpy();
        v3.rotateDeg(120);

        v1.add(new Vector2(_size/2, _size/2));
        v2.add(new Vector2(_size/2, _size/2));
        v3.add(new Vector2(_size/2, _size/2));
        _pixmap.fillTriangle(Math.round(v1.x), Math.round(v1.y), Math.round(v2.x), Math.round(v2.y), Math.round(v3.x), Math.round(v3.y));

        if(!_filled){
            v1.sub(new Vector2(_size/2, _size/2));
            v2.sub(new Vector2(_size/2, _size/2));
            v3.sub(new Vector2(_size/2, _size/2));

            Matrix3 m = new Matrix3();
            m.idt();
            m.scl((_size/2 - _size/ SHAPE_MARGIN_RATE - _size* SHAPE_STROKE_RATE)/(_size/2 + _size/ SHAPE_MARGIN_RATE));

            v1.mul(m);
            v2.mul(m);
            v3.mul(m);

            v1.add(new Vector2(_size/2, _size/2));
            v2.add(new Vector2(_size/2, _size/2));
            v3.add(new Vector2(_size/2, _size/2));
            _pixmap.setColor(Color.BLACK);
            _pixmap.fillTriangle(Math.round(v1.x), Math.round(v1.y), Math.round(v2.x), Math.round(v2.y), Math.round(v3.x), Math.round(v3.y));
        }
    }

    public static void drawShapeCircle(Pixmap _pixmap, int _size, boolean _filled){
        _pixmap.fillCircle(Math.round(_size/2), Math.round(_size/2), Math.round(_size/2 - _size/ SHAPE_MARGIN_RATE));

//		_pixmap.setColor(new Color(0.5f, 0.5f, 0.5f, 1));
//		_pixmap.drawCircle(Math.round(_size/2), Math.round(_size/2), Math.round(_size/2 - _size/ SHAPE_MARGIN_RATE));

        if(!_filled){
            _pixmap.setColor(Color.BLACK);
            _pixmap.fillCircle(Math.round(_size/2), Math.round(_size/2), Math.round(_size/2 - _size/ SHAPE_MARGIN_RATE - _size* SHAPE_STROKE_RATE));

//			_pixmap.setColor(new Color(0.5f, 0.5f, 0.5f, 1));
//			_pixmap.drawCircle(Math.round(_size/2), Math.round(_size/2), Math.round(_size/2 - _size/ SHAPE_MARGIN_RATE - _size* SHAPE_STROKE_RATE));
        }
    }

    public static void drawShapeRectangle(Pixmap _pixmap, int _size, boolean _filled){
        _pixmap.fillRectangle(
                Math.round(_size/ SHAPE_MARGIN_RATE),
                Math.round(_size/ SHAPE_MARGIN_RATE),
                Math.round(_size - 2*_size/ SHAPE_MARGIN_RATE),
                Math.round(_size - 2*_size/ SHAPE_MARGIN_RATE)
        );
        if(!_filled){
            _pixmap.setColor(Color.BLACK);
//					_pixmap.setColor(0.2f,0.2f,0.2f,1);
            _pixmap.fillRectangle(
                    Math.round(_size/ SHAPE_MARGIN_RATE + _size* SHAPE_STROKE_RATE),
                    Math.round(_size/ SHAPE_MARGIN_RATE + _size* SHAPE_STROKE_RATE),
                    Math.round(_size - 2*_size/ SHAPE_MARGIN_RATE - 2*_size* SHAPE_STROKE_RATE),
                    Math.round(_size - 2*_size/ SHAPE_MARGIN_RATE - 2*_size* SHAPE_STROKE_RATE)
            );
        }
    }

}
