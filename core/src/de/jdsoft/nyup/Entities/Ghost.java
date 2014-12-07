package de.jdsoft.nyup.Entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import de.jdsoft.nyup.AI.EntityAI;
import de.jdsoft.nyup.AI.GhostAI;
import de.jdsoft.nyup.Level.LevelRule;
import de.jdsoft.nyup.Screens.MainScreen;
import de.jdsoft.nyup.Utils.Vector2i;
import de.jdsoft.nyup.World;

public class Ghost extends Entity {
    final static boolean debug = false;

    private static final float EPSILON = 3.f;
    EntityAI ai = new GhostAI();
    Vector2 target = null;
    private Color color;

    ShapeRenderer shape;


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
        batch.setColor(color);
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
            shape.end();
            batch.begin();
        }
    }

}
