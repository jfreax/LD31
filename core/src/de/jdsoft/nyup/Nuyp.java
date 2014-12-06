package de.jdsoft.nyup;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.jdsoft.nyup.Screens.MainScreen;

public class Nuyp extends Game {
	public static final int WORLD_WIDTH = 1280;
	public static final int WORLD_HEIGHT = 720;

	@Override
	public void create() {
		setScreen(new MainScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
		Gdx.app.exit();
	}
}
