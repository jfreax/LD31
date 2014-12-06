package de.jdsoft.nyup;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class World {
    private OrthographicCamera cam;

    private TiledMap map;
    private TiledMapRenderer renderer;

    public World() {
        cam = new OrthographicCamera();
        cam.setToOrtho(false, 80, 45);
        cam.update();

        this.map = new TmxMapLoader().load("maps/level.tmx");
        this.renderer = new OrthogonalTiledMapRenderer(map, 1f / 16f);
    }

    public void render(float delta) {

        cam.update();
        renderer.setView(cam);
        renderer.render();
    }
}
