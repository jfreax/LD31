package de.jdsoft.nyup.Utils;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import de.jdsoft.nyup.World;


public class Collision {
    public static TiledMapTileLayer.Cell getCollisionCell(TiledMapTileLayer layer, float x, float y) {
        return getCollisionCell(layer, x, y, false);
    }

    public static TiledMapTileLayer.Cell getCollisionCell(TiledMapTileLayer layer, float x, float y, boolean remove) {
        TiledMapTileLayer.Cell tmp;

        // x -= World.TILE_SIZE;
        // y -= World.TILE_SIZE;

        float TS = World.TILE_SIZE;
        float M = TS / 5.f;

        // quick and dirty, really dirty
        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= 1; j++) {
                int xC = (int) ((x + M + i * (TS - 2 * M)) / World.TILE_SIZE);
                int yC = (int) ((y + M + j * (TS - 2 * M)) / World.TILE_SIZE);

                tmp = layer.getCell(xC, yC);
                if (tmp != null) {
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
