package de.jdsoft.nyup.Entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import de.jdsoft.nyup.Level.LevelRule;
import de.jdsoft.nyup.Utils.Collision;

public class Player extends Entity {
    public Player(int x, int y, TiledMap map, LevelRule level) {
        super(x, y, map, new Texture("gfx/pacman_anim.png"), 10, level);
    }
}
