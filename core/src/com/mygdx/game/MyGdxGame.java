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
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		Vector2 dimensions = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(false, dimensions.x, dimensions.y);

		float sy = (dimensions.y*0.8f)/8;
		float sx = (dimensions.x*0.8f)/5;
		int size = (int)Math.floor(sy < sx ? sy : sx);

		int marginX = (int)Math.floor((dimensions.x-size*5)/7);
		int marginY = (int)Math.floor((dimensions.y-size*8)/10);

		int offsetX = (int)Math.floor( (dimensions.x - (size*5+marginX*4))/2 );
		int offsetY = (int)Math.floor( (dimensions.y - (size*8+marginY*7))/2 );

		texture = getTexture(size);
//		TextureRegion region = new TextureRegion(texture,0,0,100,100);
//		sprite = new Sprite(region);
//		sprite = new Sprite(texture);

		textureB = getTextureB(size);
//		regionB = new TextureRegion(textureB,0,0,100,100);

//		sprite.setPosition(10,10);

		sprites = new Array<>();

		for(int y = 0; y < 8; y++)
		for(int x = 0; x < 5; x++){
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
		pixmap.setColor(Color.WHITE);
		pixmap.drawRectangle(0,0,s,s);
		Texture t = new Texture(pixmap);
		pixmap.dispose();
		return t;
	}

}
