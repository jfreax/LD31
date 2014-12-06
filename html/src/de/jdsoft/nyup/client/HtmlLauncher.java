package de.jdsoft.nyup.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import de.jdsoft.nyup.Nuyp;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(Nuyp.WORLD_WIDTH, Nuyp.WORLD_HEIGHT);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new Nuyp();
        }
}