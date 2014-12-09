package de.jdsoft.nyup;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import de.jdsoft.nyup.Screens.MainScreen;

public class Nuyp extends Game {
	public static int WORLD_WIDTH = 1280;
	public static int WORLD_HEIGHT = 720;

	@Override
	public void create() {
		setScreen(new MainScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
		Gdx.app.exit();
	}

	public static float getScaleFactorX() {
		return WORLD_WIDTH / 1280.f;
	}

	public static float getScaleFactorY() {
		return WORLD_HEIGHT / 720.f;
	}
}
