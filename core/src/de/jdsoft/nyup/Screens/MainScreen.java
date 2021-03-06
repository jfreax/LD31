package de.jdsoft.nyup.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import de.jdsoft.nyup.Level.LevelMapping;
import de.jdsoft.nyup.Nuyp;
import de.jdsoft.nyup.NuypInput;
import de.jdsoft.nyup.TextFlashEffect;
import de.jdsoft.nyup.World;

public class MainScreen implements Screen {
    final Nuyp game;

    public static OrthographicCamera uiCam;
    private World world;

    SpriteBatch batch;

    private final Stage uiStage;
    private final BitmapFont font;
    private final BitmapFont fontBig;
    private final TextFlashEffect textEffect;

    private final TextureRegion coinTexture;
    private final TextureRegion heartTexture;
    private final TextureRegion clockTexture;
    private final TextureRegion footTexture;
    private final Color coinColor;
    private final Color heartColor;

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    int currentLevelNumber = 0;

    InputMultiplexer input;
    private long startTime;

    private final CharSequence enterText;
    private BitmapFont.TextBounds enterTextBounds;

    public MainScreen (final Nuyp game) {
        this.game = game;

        // setup camera
        float w = Nuyp.WORLD_WIDTH = Gdx.graphics.getWidth();
        float h = Nuyp.WORLD_HEIGHT = Gdx.graphics.getHeight();

        uiCam = new OrthographicCamera(w, h);
        uiCam.position.set(uiCam.viewportWidth / 2f, uiCam.viewportHeight / 2f, 0);

        // game world
        world = new World(LevelMapping.map.get(currentLevelNumber));
        world.init();

        world.addInitListener(new Runnable() {
            @Override
            public void run() {
                lostTextSet = false;
                textEffect.hide();

                begin();
            }
        });

        world.addEndListener(new Runnable() {
            @Override
            public void run() {
                CharSequence text = "YOU WON!";

                if (world.isLost()) {
                    text = "YOU LOST!";
                }
                textEffect.setText(text);
                textEffect.setPosition(Gdx.graphics.getWidth() / 2.f, Gdx.graphics.getHeight() / 2.f);

                if (world.isLost()) {
                    textEffect.start(null);
                } else {
                    textEffect.start(new Runnable() {
                        @Override
                        public void run() {
                            nextLevel();
                        }
                    });
                }
            }
        });

        // input handling
        input = new InputMultiplexer();
        input.addProcessor(new NuypInput(world));
        input.addProcessor(world);

        // ui
        uiStage = new Stage();
        font = new BitmapFont();
        fontBig = new BitmapFont(Gdx.files.internal("fonts/chango.fnt"));
        batch = new SpriteBatch();

        textEffect = new TextFlashEffect(new BitmapFont(Gdx.files.internal("fonts/chango2.fnt")), "Test");
        textEffect.hide();
        uiStage.addActor(textEffect);

        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            enterText = "Click to continue";
        } else {
            enterText = "Press Enter to continue";
        }

        enterTextBounds = fontBig.getBounds(enterText);

        coinTexture = new TextureRegion(world.getMapTexture(), 96, 64, 32, 32);
        coinColor = Color.valueOf("ffdf7d");
        heartTexture = new TextureRegion(world.getMapTexture(), 160, 64, 32, 32);
        heartColor = Color.valueOf("d95763");
        clockTexture = new TextureRegion(world.getMapTexture(), 160, 96, 32, 32);
        footTexture = new TextureRegion(world.getMapTexture(), 192, 64, 32, 32);

        begin();
    }

    private void nextLevel() {
        if (currentLevelNumber+1 < LevelMapping.map.size()) {
            currentLevelNumber++;
            world.setLevel(LevelMapping.map.get(currentLevelNumber));
            world.init();
            begin();
        }
    }

    private void begin() {
        startTime = System.currentTimeMillis();

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

        float levelHeightInPixel = world.getLevel().getMapHeight() * World.T2W_Y;
        shapeRenderer.setProjectionMatrix(uiCam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.valueOf("1d273b"));
        shapeRenderer.rect(0, levelHeightInPixel, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - levelHeightInPixel);
        shapeRenderer.end();

        uiStage.draw();

        batch.setProjectionMatrix(uiCam.combined);
        batch.begin();

        // fps
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 8, 20);

        float yText = Gdx.graphics.getHeight() - 16;

        // coins
        batch.draw(coinTexture, 6, Gdx.graphics.getHeight() - 40);
        font.setColor(coinColor);
        CharSequence coinText = world.getLevel().getPlayer().getPoints() + " (" + world.getOverallPoints() + ")";
        BitmapFont.TextBounds coinBounds = font.getBounds(coinText);
        font.draw(batch, coinText, 42, yText);

        // lives
        batch.draw(heartTexture, 42 + coinBounds.width + 10, Gdx.graphics.getHeight() - 40);
        font.setColor(heartColor);
        font.draw(batch, String.valueOf(world.getLevel().getPlayer().getLifes()), 42 + coinBounds.width + 10 + 34, yText);

        // level test
        font.setColor(Color.WHITE);
        CharSequence lvlText = LevelMapping.map.get(currentLevelNumber).getLevelHelp();
        BitmapFont.TextBounds lvlBounds = font.getBounds(lvlText);
        font.draw(batch, LevelMapping.map.get(currentLevelNumber).getLevelHelp(), (Gdx.graphics.getWidth() - lvlBounds.width) / 2.0f, yText);

        // steps
        font.setColor(Color.WHITE);
        batch.draw(footTexture, Gdx.graphics.getWidth() - 188, Gdx.graphics.getHeight() - 38, 28, 28);
        font.draw(batch, String.valueOf(world.getLevel().getPlayer().getSteps()), Gdx.graphics.getWidth() - 152, yText);

        // timer
        font.setColor(Color.WHITE);
        batch.draw(clockTexture, Gdx.graphics.getWidth() - 108, Gdx.graphics.getHeight() - 40);
        font.draw(batch, ((System.currentTimeMillis() - startTime) / 1000) + "s", Gdx.graphics.getWidth() - 60, yText);


        // press enter to continue - text
        if (world.isLost()) {
            fontBig.draw(batch, enterText, (Gdx.graphics.getWidth() - enterTextBounds.width) / 2.0f, Gdx.graphics.getHeight() * 0.1f);
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
//        Nuyp.WORLD_WIDTH = width;
//        Nuyp.WORLD_HEIGHT = height;
//
//        uiCam.viewportWidth = width;
//        uiCam.viewportHeight = height;
//        uiCam.update();
//
//        world.updateCamera();
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
