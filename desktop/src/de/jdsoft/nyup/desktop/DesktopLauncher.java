package de.jdsoft.nyup.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.jdsoft.nyup.Nuyp;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = Nuyp.WORLD_WIDTH;
		config.height = Nuyp.WORLD_HEIGHT;

		new LwjglApplication(new Nuyp(), config);
	}
}
