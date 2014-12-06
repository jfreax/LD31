package de.jdsoft.nyup;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
