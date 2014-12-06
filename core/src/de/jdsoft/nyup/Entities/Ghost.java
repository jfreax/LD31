package de.jdsoft.nyup.Entities;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class Ghost extends Entity {
    public Ghost(int x, int y, TiledMap map) {
        super(x, y, map, new Texture("gfx/ghost.png"), 1);
    }
}
