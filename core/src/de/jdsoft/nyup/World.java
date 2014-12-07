package de.jdsoft.nyup;


import com.badlogic.gdx.Gdx;
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

public class World extends Stage {
    public final static float TILE_SIZE = 32.0f;

    private OrthographicCamera cam;

    private TiledMap map;
    private final Texture mapTexture;
    private TiledMapRenderer renderer;
    private LevelRule level;

    public World(LevelRule level) {
        super();

        this.level = level;

        cam = new OrthographicCamera();
        cam.setToOrtho(false, 40, 22.5f);
        cam.update();


        this.map = new TmxMapLoader().load("maps/level.tmx");
        this.mapTexture = new Texture("gfx/spritemap.png");
        this.renderer = new OrthogonalTiledMapRenderer(map, 1f / TILE_SIZE);

        this.level.init(this);
    }

    @Override
    public void draw() {
        cam.update();
        renderer.setView(cam);
        renderer.render();

        // check collisions
        for (Actor actor1 : getActors().toArray()) {
            for (Actor actor2 : getActors()) {
                Entity entity1 = (Entity) actor1;
                Entity entity2 = (Entity) actor2;
                if (entity1 != entity2 && entity1.getBounds().overlaps(entity2.getBounds())) {
                    level.onEntityCollision(entity1, entity2);
                }
            }
        }

        this.act(Gdx.graphics.getDeltaTime());

        super.draw();
    }

    public TiledMap getMap() {
        return map;
    }

    public Texture getMapTexture() {
        return mapTexture;
    }
}
