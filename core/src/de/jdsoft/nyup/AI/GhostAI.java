package de.jdsoft.nyup.AI;


import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import de.jdsoft.nyup.Utils.Collision;
import de.jdsoft.nyup.Utils.Vector2i;
import de.jdsoft.nyup.World;

import java.util.Random;

public class GhostAI implements EntityAI {
    private static final float EPSILON = 0.3f;
    private int posX = 0;
    private int posY = 0;
    private TiledMapTileLayer wallLayer;

    float[][] grid;

    @Override
    public void init(int x, int y, TiledMap map) {
        this.wallLayer = (TiledMapTileLayer) map.getLayers().get("wall");
        grid = new float[wallLayer.getWidth()][wallLayer.getHeight()];

        this.setPosition(x, y);
    }

    @Override
    public Vector2i nextPosition() {
        float minValue = Float.MAX_VALUE;
        Vector2i min = new Vector2i(posX, posY);

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (posX + i >= 0 && posX + i < wallLayer.getWidth() && posY + j >= 0 && posY + j < wallLayer.getHeight()) {
                    if (Collision.getCollisionCell(wallLayer, (posX+i) * World.TILE_SIZE, (posY+j) * World.TILE_SIZE) == null ) {
                        float value = heuristic(posX + i, posY + j);

                        if (value < minValue || (value - minValue > EPSILON && Math.random() > 0.6)) {
                            minValue = value;
                            min = new Vector2i(posX + i, posY + j);
                        }
                    }
                }
            }
        }
        return min;
    }

    private float heuristic(int x, int y) {
        return grid[x][y];
    }

    private void markVisited(int x, int y) {
        grid[x][y] += 1.0f;
    }

    @Override
    public void setPosition(int x, int y) {
        this.posX = x;
        this.posY = y;

        markVisited(x, y);
    }


}
