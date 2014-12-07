package de.jdsoft.nyup.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import de.jdsoft.nyup.Level.LevelMapping;
import de.jdsoft.nyup.Nuyp;
import de.jdsoft.nyup.NuypInput;
import de.jdsoft.nyup.TextFlashEffect;
import de.jdsoft.nyup.World;

public class MainScreen implements Screen {
    final Nuyp game;

    private OrthographicCamera uiCam;
    private World world;

    SpriteBatch batch;

    private final Stage uiStage;
    private final  BitmapFont font;
    private final BitmapFont fontBig;
    private final TextFlashEffect textEffect;

    int currentLevelNumber = 0;

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
        world.init();

        world.addInitListener(new Runnable() {
            @Override
            public void run() {
                lostTextSet = false;
                textEffect.hide();

                begin();
            }
        });

        // input handling
        input = new InputMultiplexer();
        input.addProcessor(new NuypInput(world));
        input.addProcessor(world);

        // ui
        uiStage = new Stage();
        font = new BitmapFont();
        fontBig = new BitmapFont(Gdx.files.internal("fonts/chango2.fnt"));
        batch = new SpriteBatch();

        textEffect = new TextFlashEffect(new BitmapFont(Gdx.files.internal("fonts/chango2.fnt")), "Test");
        textEffect.hide();
        uiStage.addActor(textEffect);

        begin();
    }

    private void begin() {

        textEffect.setText(LevelMapping.map.get(currentLevelNumber).getLevelHelp());
        textEffect.setPosition(Gdx.graphics.getWidth() / 2.f, Gdx.graphics.getHeight() * 0.8f);
        textEffect.start(new Runnable() {
            @Override
            public void run() {
                textEffect.fadeOut();
                world.start();
            }
        });
    }

    boolean lostTextSet = false;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render game level
        world.draw();

        // render ui
        uiCam.update();
        batch.setProjectionMatrix(uiCam.combined);

        uiStage.draw();

        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
        font.draw(batch, "Points: " + world.getPlayer().getPoints(), 10, Gdx.graphics.getHeight() - 10);

        if(world.isEnd()) {
            if(world.isLost()) {
                if (!textEffect.running() && !lostTextSet) {
                    lostTextSet = true;

                    CharSequence text = "YOU LOST";
                    textEffect.setText(text);
                    textEffect.setPosition(Gdx.graphics.getWidth() / 2.f, Gdx.graphics.getHeight() / 2.f);
                    textEffect.start(null);
                }
            }
        }

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
