package de.jdsoft.nyup.Utils;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import de.jdsoft.nyup.World;


public class Collision {
    public static TiledMapTileLayer.Cell getCollisionCell(TiledMapTileLayer layer, float x, float y) {
        return getCollisionCell(layer, x, y, World.T2W_X / 5.0f);
    }

    public static TiledMapTileLayer.Cell getCollisionCell(TiledMapTileLayer layer, float x, float y, float buffer) {
        return getCollisionCell(layer, x, y, false, buffer);
    }

    public static TiledMapTileLayer.Cell getCollisionCell(TiledMapTileLayer layer, float x, float y, boolean remove) {
        return getCollisionCell(layer, x, y, remove, World.T2W_X / 5.0f);
    }

    public static TiledMapTileLayer.Cell getCollisionCell(TiledMapTileLayer layer, float x, float y, boolean remove, float buffer) {
        TiledMapTileLayer.Cell tmp;
        float TS = World.TILE_SIZE-2;

        // quick and dirty, really dirty
        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= 1; j++) {
                int xC = (int) ((x + buffer + i * (TS - 2 * buffer)) / World.T2W_X);
                int yC = (int) ((y + buffer + j * (TS - 2 * buffer)) / World.T2W_Y);

                tmp = layer.getCell(xC, yC);
                if (tmp != null && (tmp.getTile() == null || !tmp.getTile().getProperties().containsKey("walkable"))) {
                    if (remove) {
                        layer.setCell(xC, yC, null);
                    }
                    return tmp;
                }
            }
        }

        return null;
    }
}
