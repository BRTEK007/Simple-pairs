package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;

public class GameScreen implements Screen {

    private MyGdxGame parent;

    final Color[] CARD_COLORS = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA};
	final int GRID_WIDTH = 1;
	final int GRID_HEIGHT = 4;
	final float SPACING = 0.88f;
	final float UNCOVER_DELAY = 0.5f;

	SpriteBatch batch;
	OrthographicCamera camera;
	Texture texture_off;
	private Array<Card> cards;
	private Card card1, card2;
	private boolean canClick;

    public GameScreen(MyGdxGame _p){
        parent = _p;


		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		Vector2 dimensions = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(false, dimensions.x, dimensions.y);

		float sy = (dimensions.y*SPACING)/GRID_HEIGHT;
		float sx = (dimensions.x*SPACING)/GRID_WIDTH;
		int size = (int)Math.floor(sy < sx ? sy : sx);

		int marginX = (int)Math.floor((dimensions.x-size*GRID_WIDTH)/(GRID_WIDTH+2));
		int marginY = (int)Math.floor((dimensions.y-size*GRID_HEIGHT)/(GRID_HEIGHT+2));

		int offsetX = (int)Math.floor( (dimensions.x - (size*GRID_WIDTH+marginX*(GRID_WIDTH-1)))/2 );
		int offsetY = (int)Math.floor( (dimensions.y - (size*GRID_HEIGHT+marginY*(GRID_HEIGHT-1)))/2 );

		texture_off = getTexture(size);

		cards = new Array<>();

		Array<CardTemplate> templatesAll = new Array<>();

		int template_id = 0;
		for(int i = 0; i < CARD_COLORS.length; i++){
			templatesAll.add(
					new CardTemplate(SHAPE.CIRCLE, CARD_COLORS[i], true, template_id++),
					new CardTemplate(SHAPE.TRIANGLE, CARD_COLORS[i], true, template_id++),
					new CardTemplate(SHAPE.SQUARE, CARD_COLORS[i], true, template_id++)
			);
			templatesAll.add(
					new CardTemplate(SHAPE.CIRCLE, CARD_COLORS[i], false, template_id++),
					new CardTemplate(SHAPE.TRIANGLE, CARD_COLORS[i], false, template_id++),
					new CardTemplate(SHAPE.SQUARE, CARD_COLORS[i], false, template_id++)
			);
		}

		Array<CardTemplate> templatesPairs = new Array<>();

		for(int i = 0; i < (GRID_WIDTH*GRID_HEIGHT)/2; i++){
			CardTemplate template = templatesAll.removeIndex(MathUtils.random(0,templatesAll.size-1));
			templatesPairs.add(template, template);
		}

		for(int y = 0; y < GRID_HEIGHT; y++)
		for(int x = 0; x < GRID_WIDTH; x++){
			CardTemplate template = templatesPairs.removeIndex(MathUtils.random(0,templatesPairs.size-1));

			Texture texture_on = getCardTexture(size, template);

			Card c = new Card(
					offsetX + x*(size + marginX),
					offsetY + y*(size + marginY),
					template.id,
					texture_on,
					texture_off
					);
			cards.add(c);
		}

		card1 = null;
		card2 = null;
		canClick = true;

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if(canClick && Gdx.input.justTouched()) {
			Vector3 touchPoint = new Vector3();
			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			for (Card c : cards){
				if (c.clicked(new Vector2(touchPoint.x, touchPoint.y))) {
					if(card1 == null){
						card1 = c;
					}else{
						card2 = c;
						canClick = false;
						Timer.schedule(new Timer.Task(){
							@Override
							public void run() {
								if(card1.id == card2.id){//collecting cards
									cards.removeValue(card1, true);
									cards.removeValue(card2, true);
									card1.dispose();
									card2.dispose();
									if(cards.size == 0){//WIN GAME
										parent.showResults();
									}
								}else{
									card1.hide();
									card2.hide();
								}
								card1 = null;
								card2 = null;
								canClick = true;
							}
						}, UNCOVER_DELAY);
					}
					break;
				}
			}
		}

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		ScreenUtils.clear(0, 0, 0, 1);
		batch.begin();
		for(Card c: cards)
			c.draw(batch);
		batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
		texture_off.dispose();
		for(Card c: cards)
			c.dispose();
    }

    private Texture getTexture(int s){
		Pixmap pixmap = new Pixmap(s, s, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fillRectangle(0,0,s,s);
		Texture t = new Texture(pixmap);
		pixmap.dispose();
		return t;
	}

	private Texture getCardTexture(int s, CardTemplate _t){
		Pixmap pixmap = new Pixmap(s, s, Pixmap.Format.RGBA8888);
		drawPuzzle(pixmap, s, _t.shape, _t.color, _t.filled);
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

			case SQUARE:
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
