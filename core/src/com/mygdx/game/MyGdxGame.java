package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;

public class MyGdxGame extends Game {
	private GameScreen gameScreen;
	private MenuScreen menuScreen;
	private ResultsScreen resultsScreen;

	@Override
	public void create () {
		menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
	}

	@Override
	public void render () {
		getScreen().render(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose () {
		getScreen().dispose();
	}


	public void startGame(int _gamemode, int _gridSize){//parameters size, etc...
		getScreen().dispose();
		gameScreen = new GameScreen(this, _gamemode, _gridSize);
		setScreen(gameScreen);
	}

	public void showResults(int _time, int _mistakes){
		getScreen().dispose();
		resultsScreen = new ResultsScreen(this, _time, _mistakes);
		setScreen(resultsScreen);
	}

	public void returnToMenu(){
		getScreen().dispose();
		menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
	}



}
