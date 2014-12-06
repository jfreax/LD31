package de.jdsoft.nyup.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.jdsoft.nyup.Nuyp;
import de.jdsoft.nyup.NuypInput;
import de.jdsoft.nyup.Player;
import de.jdsoft.nyup.World;

public class MainScreen implements Screen {
    final Nuyp game;

    private OrthographicCamera uiCam;
    private World world;
    private Player player;

    SpriteBatch batch;
    private BitmapFont font;

    InputMultiplexer input;

    public MainScreen (final Nuyp game) {
        this.game = game;

        // setup camera
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        uiCam = new OrthographicCamera(w, h);
        uiCam.position.set(uiCam.viewportWidth / 2f, uiCam.viewportHeight / 2f, 0);

        // game world
        world = new World();
        player = new Player(world.getMap());
        world.addActor(player);
        world.setKeyboardFocus(player);

        // input handling
        input = new InputMultiplexer();
        input.addProcessor(new NuypInput());
        input.addProcessor(world);

        // ui
        font = new BitmapFont();
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render game level
        world.draw();

        // render ui
        uiCam.update();
        batch.setProjectionMatrix(uiCam.combined);

        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
        font.draw(batch, "Points: " + player.getPoints(), 10, Gdx.graphics.getHeight() - 10);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
//        cam.viewportWidth = width;
//        cam.viewportHeight = height;
//        cam.update();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(input);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        world.dispose();
    }
}
