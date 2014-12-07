package de.jdsoft.nyup.Entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import de.jdsoft.nyup.AI.EntityAI;
import de.jdsoft.nyup.AI.GhostAI;
import de.jdsoft.nyup.Level.LevelRule;
import de.jdsoft.nyup.Utils.Vector2i;

public class Ghost extends Entity {
    private static final float EPSILON = 1.f;
    EntityAI ai = new GhostAI();
    Vector2 target = null;


    public Ghost(int x, int y, TiledMap map, LevelRule level) {
        super(x, y, map, new Texture("gfx/ghost.png"), 1, level);

        maxSpeed = 50;
        rotationSpeed = 0.08f;

        ai.init(getX(), getY(), map, this, level);
        target = ai.nextPosition();
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
            setDirection(Direction.WEST);
        }
        if (target.x > getX()) {
            moveBy(delta * maxSpeed, 0);
            setDirection(Direction.EAST);
        }
        if (target.y > getY()) {
            moveBy(0, delta * maxSpeed);
            setDirection(Direction.NORTH);
        }
        if (target.y < getY()) {
            moveBy(0, -delta * maxSpeed);
            setDirection(Direction.SOUTH);
        }
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {


        super.draw(batch, parentAlpha);
    }

}
