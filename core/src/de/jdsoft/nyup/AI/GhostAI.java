package de.jdsoft.nyup.AI;


import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import de.jdsoft.nyup.Utils.Collision;
import de.jdsoft.nyup.Utils.Utils;
import de.jdsoft.nyup.World;


public class GhostAI implements EntityAI {
    private static final float EPSILON = 0.3f;
    public static final float DECAY = 0.99f;
    public static final float DIFFUSE = 0.4f;

    private float posX = 0;
    private float posY = 0;
    private TiledMapTileLayer wallLayer;

    float[][] grid;

    @Override
    public void init(float x, float y, TiledMap map) {
        this.wallLayer = (TiledMapTileLayer) map.getLayers().get("wall");
        grid = new float[wallLayer.getWidth()][wallLayer.getHeight()];

        this.setPosition(x, y);
    }

    @Override
    public Vector2 nextPosition() {
        float minValue = Float.MAX_VALUE;
        Vector2 min = new Vector2(posX, posY);

        int step = (int) World.TILE_SIZE;
        for (int i = -step; i <= step; i += step) {
            for (int j = -step; j <= step; j += step) {
                if (posX + i >= 0 && posX + i < (wallLayer.getWidth() - 1) * World.TILE_SIZE && posY + j >= 0 &&
                        posY + j < (wallLayer.getHeight() - 1) * World.TILE_SIZE) {
                    if (Collision.getCollisionCell(wallLayer, (posX + i), (posY + j)) == null) {
                        float value = heuristic((int) ((posX + i) / World.TILE_SIZE), (int) ((posY + j) / World.TILE_SIZE));

                        if (value < minValue) {
                            minValue = value;
                            min = new Vector2(posX + i, posY + j);
                        } else if ((Math.random() > 0.7)) {
                            min = new Vector2(posX + i, posY + j);
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
        float[][] buffered = Utils.copy2DArray(grid);

        buffered[x][y] = 1.0f;

        diffuse(grid, buffered);
        Utils.copyInto2DArray(buffered, grid);
        diffuse(grid, buffered);
    }

    public void diffuse(float[][] dst, float[][] src) {
        for (int i = 0; i < wallLayer.getWidth(); i++) {
            for (int j = 0; j < wallLayer.getHeight(); j++) {
                int n = 0;
                float d = 0;
                if (i > 0) {
                    n++;
                    d += src[i - 1][j];
                }
                if (i < wallLayer.getWidth() - 1) {
                    n++;
                    d += src[i + 1][j];
                }
                if (j > 0) {
                    n++;
                    d += src[i][j - 1];
                }
                if (j < wallLayer.getHeight() - 1) {
                    n++;
                    d += src[i][j + 1];
                }
                if (n == 0) {
                    n = 1;
                    d = 0;
                }
                dst[i][j] = DIFFUSE * (d / n)
                        + (1 - DIFFUSE) * DECAY * src[i][j];

            }
        }
    }

    @Override
    public void setPosition(float x, float y) {
        this.posX = x;
        this.posY = y;


        markVisited((int) (x / World.TILE_SIZE), (int) (y / World.TILE_SIZE));
    }


}
