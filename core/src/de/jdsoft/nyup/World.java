package de.jdsoft.nyup;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class World extends Stage {
    public final static float TILE_SIZE = 32.0f;

    private OrthographicCamera cam;

    private TiledMap map;
    private TiledMapRenderer renderer;

    public World() {
        super();

        cam = new OrthographicCamera();
        cam.setToOrtho(false, 40, 22.5f);
        cam.update();


        this.map = new TmxMapLoader().load("maps/level.tmx");
        this.renderer = new OrthogonalTiledMapRenderer(map, 1f / TILE_SIZE);
    }

    @Override
    public void draw() {
        cam.update();
        renderer.setView(cam);
        renderer.render();

        this.act(Gdx.graphics.getDeltaTime());

        super.draw();
    }

    public TiledMap getMap() {
        return map;
    }
}
