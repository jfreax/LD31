package de.jdsoft.nyup.Entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import de.jdsoft.nyup.AI.EntityAI;
import de.jdsoft.nyup.AI.GhostAI;
import de.jdsoft.nyup.Level.LevelRule;
import de.jdsoft.nyup.Screens.MainScreen;
import de.jdsoft.nyup.World;

public class Ghost extends Entity {
    final static boolean debug = false;

    private static final float EPSILON = 3.f;
    EntityAI ai = new GhostAI();
    Vector2 target = null;
    private Color color;

    ShapeRenderer shape;
    private boolean canGetEaten = false;


    public Ghost(int x, int y, Color color, TiledMap map, LevelRule level) {
        super(x, y, map, new Texture("gfx/ghost.png"), 1, level);

        this.color = color;

        maxSpeed = 50;
        rotationSpeed = 0.08f;

        ai.init(getX(), getY(), map, this, level);
        target = ai.nextPosition();

        shape  = new ShapeRenderer();
        shape.setAutoShapeType(true);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (Math.abs(target.x - getX()) < EPSILON && Math.abs(target.y - getY()) < EPSILON) {
            ai.setPosition(getX(), getY());
            target = ai.nextPosition();
        }

        if (target.x < getX()) {
            moveBy(-delta * maxSpeed, 0);
            //setDirection(Direction.WEST);
        }
        if (target.x > getX()) {
            moveBy(delta * maxSpeed, 0);
            //setDirection(Direction.EAST);
        }
        if (target.y > getY()) {
            moveBy(0, delta * maxSpeed);
            //setDirection(Direction.NORTH);
        }
        if (target.y < getY()) {
            moveBy(0, -delta * maxSpeed);
            //setDirection(Direction.SOUTH);
        }
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {

        Color oldColor = batch.getColor();

        if (canGetEaten) {
            batch.setColor(0.4f, 0.4f, 1.0f, 1.0f);
        } else {
            batch.setColor(color);
        }
        super.draw(batch, parentAlpha);

        batch.setColor(oldColor);

        if (debug) {
            batch.end();

            MainScreen.uiCam.update();
            shape.setProjectionMatrix(MainScreen.uiCam.combined);

            shape.begin();
            shape.set(ShapeRenderer.ShapeType.Filled);

            GhostAI gai = (GhostAI) ai;
            float max = 0.0f;
            for (int i = 0; i < gai.grid.length; i++) {
                for (int j = 0; j < gai.grid[0].length; j++) {
                    if (gai.grid[i][j] > max) {
                        max = gai.grid[i][j];
                    }
                }
            }
            Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);

            float f = 1.0f / max;
            // System.out.println(f);
            for (int i = 0; i < gai.grid.length; i++) {
                for (int j = 0; j < gai.grid[0].length; j++) {
                    shape.setColor(1, 0, 0, gai.grid[i][j] * f);
                    shape.rect(i * 32, j * 32, 32, 32);
                }
            }

            float x = getX();
            float y = getY();

            float buffer = World.T2W_X / 5.0f;
            float TS = World.TILE_SIZE;

            TiledMapTileLayer.Cell tmp;


            shape.setColor(Color.PINK);
            for (int i = -10; i <= 10; i++) {
                for (int j = -10; j <= 10; j++) {
                    int xC = (int) ((x + buffer + i * (TS - 2 * buffer)) / World.T2W_X);
                    int yC = (int) ((y + buffer + j * (TS - 2 * buffer)) / World.T2W_Y);

                    tmp = wallLayer.getCell(xC, yC);
                    if (tmp != null) {
                        shape.circle(xC * World.T2W_X + (World.T2W_X/2.0f), yC * World.T2W_Y + (World.T2W_Y/2.0f), 10);
                    }
                }
            }

            shape.end();
            batch.begin();
        }
    }

    public void canGetEaten(boolean b) {
        this.canGetEaten = b;
    }

    public boolean isCanGetEaten() {
        return canGetEaten;
    }

    public GhostAI getAI() {
        return (GhostAI) ai;
    }

}
