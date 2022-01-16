package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
//	Sprite sprite;
	OrthographicCamera camera;
	Texture texture;
	Texture textureB;
	private Array<Sprite> sprites;
//	TextureRegion regionB;
	enum SHAPE{
		TRIANGLE, SQUARE, CIRCLE
	};

	
	@Override
	public void create () {
		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		Vector2 dimensions = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(false, dimensions.x, dimensions.y);

		int gridY = 7;
		int gridX = 4;

		float sy = (dimensions.y*0.88f)/gridY;
		float sx = (dimensions.x*0.88f)/gridX;
		int size = (int)Math.floor(sy < sx ? sy : sx);

		int marginX = (int)Math.floor((dimensions.x-size*gridX)/(gridX+2));
		int marginY = (int)Math.floor((dimensions.y-size*gridY)/(gridY+2));

		int offsetX = (int)Math.floor( (dimensions.x - (size*gridX+marginX*(gridX-1)))/2 );
		int offsetY = (int)Math.floor( (dimensions.y - (size*gridY+marginY*(gridY-1)))/2 );

		texture = getTexture(size);
//		TextureRegion region = new TextureRegion(texture,0,0,100,100);
//		texture.dispose();
//		sprite = new Sprite(region);
//		sprite = new Sprite(texture);

		textureB = getTextureB(size);
//		regionB = new TextureRegion(textureB,0,0,100,100);

//		sprite.setPosition(10,10);

		sprites = new Array<>();

		for(int y = 0; y < gridY; y++)
		for(int x = 0; x < gridX; x++){
			Sprite sprite = new Sprite(texture);
			sprite.setPosition(offsetX + x*(size + marginX), offsetY + y*(size + marginY));
			sprites.add(sprite);
		}
	}

	@Override
	public void render () {
		if(Gdx.input.justTouched()) {
			Vector3 touchPoint = new Vector3();
			camera.unproject(touchPoint.set(Gdx.input.getX(),Gdx.input.getY(),0));
			for(Sprite s: sprites) {
				if (s.getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
					s.setRegion(textureB);
				}
			}
		}

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		ScreenUtils.clear(0, 0, 0, 1);
		batch.begin();
		for(Sprite s: sprites)
			batch.draw(s,s.getX(), s.getY());
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		texture.dispose();
		textureB.dispose();
	}

	private Texture getTexture(int s){
		Pixmap pixmap = new Pixmap(s, s, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fillRectangle(0,0,s,s);
		Texture t = new Texture(pixmap);
		pixmap.dispose();
		return t;
	}

	private Texture getTextureB(int s){
		Pixmap pixmap = new Pixmap(s, s, Pixmap.Format.RGBA8888);
//		pixmap.setColor(Color.WHITE);
//		pixmap.drawRectangle(0,0,s,s);
//		pixmap.setColor(Color.RED);
//		pixmap.fillTriangle(s/10,s/10, s - s/10,s/10,s/2,s-s/10);
		drawPuzzle(pixmap, s, SHAPE.TRIANGLE, Color.CYAN, false);
		Texture t = new Texture(pixmap);
		pixmap.dispose();
		return t;
	}

	private void drawPuzzle(Pixmap _pixmap, int _size, SHAPE _shape, Color _color, boolean _filled){
		_pixmap.setColor(_color);
		final float marginRate = 8.8f;
		final float strokeRate = 0.1f;
		switch (_shape){
			case TRIANGLE://TODO fix with vectors
				Vector2 v1 = new Vector2(0, -_size/2 + _size/marginRate);
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
					m.scl((_size/2 - _size/marginRate - _size*strokeRate)/(_size/2 + _size/marginRate));

					v1.mul(m);
					v2.mul(m);
					v3.mul(m);

					v1.add(new Vector2(_size/2, _size/2));
					v2.add(new Vector2(_size/2, _size/2));
					v3.add(new Vector2(_size/2, _size/2));
					_pixmap.setColor(Color.BLACK);
					_pixmap.fillTriangle(Math.round(v1.x), Math.round(v1.y), Math.round(v2.x), Math.round(v2.y), Math.round(v3.x), Math.round(v3.y));
				}

				break;
			case CIRCLE:
				_pixmap.fillCircle(Math.round(_size/2), Math.round(_size/2), Math.round(_size/2 - _size/marginRate));
				if(!_filled){
					_pixmap.setColor(Color.BLACK);
					_pixmap.fillCircle(Math.round(_size/2), Math.round(_size/2), Math.round(_size/2 - _size/marginRate - _size*strokeRate));
				}
				break;

			case SQUARE://TODO finish
				_pixmap.fillRectangle(
						Math.round(_size/marginRate),
						Math.round(_size/marginRate),
						Math.round(_size - 2*_size/marginRate),
						Math.round(_size - 2*_size/marginRate)
				);
				if(!_filled){
					_pixmap.setColor(Color.BLACK);
					_pixmap.fillRectangle(
							Math.round(_size/marginRate + _size*strokeRate),
							Math.round(_size/marginRate + _size*strokeRate),
							Math.round(_size - 2*_size/marginRate - 2*_size*strokeRate),
							Math.round(_size - 2*_size/marginRate - 2*_size*strokeRate)
					);
				}
				break;
		}
	}


}
