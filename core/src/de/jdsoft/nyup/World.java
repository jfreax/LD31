package de.jdsoft.nyup;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import de.jdsoft.nyup.Entities.Entity;
import de.jdsoft.nyup.Level.LevelRule;

import java.util.HashMap;

public class World extends Stage {

    public enum SoundID {
        LEVEL_UP,
        LOST,
        PICKUP_COIN,
        PICKUP_MUSHROOM,
        PICKUP_2,
        LASER_START,
        LASER_DOWN,
        KILL_GHOST
    }

    public final static float ACTIONBAR_HEIGHT = 50;

    public final static float TILE_X = 40;
    public final static float TILE_Y = 21;
    public final static float TILE_SIZE = 32.0f;
    public static float T2W_X;
    public static float T2W_Y;

    private OrthographicCamera cam;

    private TiledMap map;
    private final Texture mapTexture;
    private final Texture laserMapTexture;
    private TiledMapRenderer renderer;
    private LevelRule level;

    private HashMap<SoundID, Sound> soundMap;

    private boolean end = false;
    private boolean lost = false;
    private boolean pause = true;

    private int overallPoints = 0;


    Runnable initListener = null;
    private Runnable endListener = null;

    public World(LevelRule level) {
        super();

        T2W_X = Gdx.graphics.getWidth() / TILE_X;
        T2W_Y = Gdx.graphics.getHeight() / (TILE_Y);

        float off = ACTIONBAR_HEIGHT / T2W_Y;
        T2W_Y = Gdx.graphics.getHeight() / (TILE_Y+off);

        System.out.println(Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight());
        Gdx.app.debug("Nuyp", Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight());

        cam = new OrthographicCamera();
        cam.setToOrtho(false, TILE_X, (TILE_Y+off));
        cam.update();

        this.map = new TmxMapLoader().load("maps/level.tmx");
        this.mapTexture = new Texture("gfx/spritemap.png");
        this.laserMapTexture = new Texture("gfx/spritemap_laser.png");
        this.renderer = new OrthogonalTiledMapRenderer(map, 1f / TILE_SIZE);

        soundMap = new HashMap<SoundID, Sound>();
        soundMap.put(SoundID.LEVEL_UP, Gdx.audio.newSound(Gdx.files.internal("sound/level_up.wav")));
        soundMap.put(SoundID.LOST, Gdx.audio.newSound(Gdx.files.internal("sound/lost.wav")));
        soundMap.put(SoundID.PICKUP_COIN, Gdx.audio.newSound(Gdx.files.internal("sound/pickup_coin.wav")));
        soundMap.put(SoundID.PICKUP_MUSHROOM, Gdx.audio.newSound(Gdx.files.internal("sound/pickup_mushroom.wav")));
        soundMap.put(SoundID.PICKUP_2, Gdx.audio.newSound(Gdx.files.internal("sound/pickup2.wav")));
        soundMap.put(SoundID.LASER_START, Gdx.audio.newSound(Gdx.files.internal("sound/laser_start.wav")));
        soundMap.put(SoundID.LASER_DOWN, Gdx.audio.newSound(Gdx.files.internal("sound/laser_down.wav")));
        soundMap.put(SoundID.KILL_GHOST, Gdx.audio.newSound(Gdx.files.internal("sound/kill_ghost.wav")));

        this.level = level;
    }

    public void init() {
        end = false;
        lost = false;

        this.getActors().clear();

        this.level.init(this);

        if (initListener != null) {
            initListener.run();
        }
    }

    @Override
    public void draw() {
        cam.update();
        renderer.setView(cam);
        renderer.render();

        if (!end && !pause) {

            // check collisions
            for (Actor actor1 : getActors().toArray()) {
                Entity entity1 = (Entity) actor1;
                for (Actor actor2 : getActors()) {
                    Entity entity2 = (Entity) actor2;
                    if (entity1 != entity2 && entity1.getBounds().overlaps(entity2.getBounds())) {
                        level.onEntityCollision(entity1, entity2);
                    }
                }
            }

            this.act(Gdx.graphics.getDeltaTime());
        }

        super.draw();
    }

    public void addInitListener(Runnable listener) {
        initListener = listener;
    }

    public void addEndListener(Runnable listener) {
        endListener = listener;
    }

    public void lost() {
        playSound(SoundID.LOST);
        level.levelShutdown();

        lost = true;
        end = true;
        pause = true;

        if (endListener != null) {
            endListener.run();
        }
    }

    public void won() {
        playSound(SoundID.LEVEL_UP);
        level.levelShutdown();

        end = true;
        pause = true;

        overallPoints += level.getPlayer().points;

        if (endListener != null) {
            endListener.run();
        }

    }

    public void setLevel(LevelRule level) {
        this.level = level;
    }

    public TiledMap getMap() {
        return map;
    }

    public Texture getMapTexture() {
        return mapTexture;
    }

    public Texture getLaserMapTexture() {
        return laserMapTexture;
    }

    public boolean isEnd() {
        return end;
    }

    public boolean isLost() {
        return lost;
    }

    public void pause() {
        pause = true;
    }

    public void start() {
        pause = false;
    }

    public LevelRule getLevel() {
        return level;
    }

    public void playSound(SoundID id) {
        playSound(id, 1.0f);
    }

    public void playSound(SoundID id, float volume) {
        soundMap.get(id).play(volume);
    }

    public int getOverallPoints() {
        return overallPoints;
    }
}
