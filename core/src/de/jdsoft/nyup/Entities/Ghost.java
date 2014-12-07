package de.jdsoft.nyup.Entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import de.jdsoft.nyup.AI.EntityAI;
import de.jdsoft.nyup.AI.GhostAI;
import de.jdsoft.nyup.Utils.Vector2i;

public class Ghost extends Entity {
    private static final float EPSILON = 1.f;
    EntityAI ai = new GhostAI();
    Vector2 target = null;


    public Ghost(int x, int y, TiledMap map) {
        super(x, y, map, new Texture("gfx/ghost.png"), 1);

        maxSpeed = 50;
        rotationSpeed = 0.08f;

        ai.init(getX(), getY(), map);
        target = ai.nextPosition();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float delta = Gdx.graphics.getDeltaTime();

        Vector2i cellPos = getCurrentCellPos();
        if (Math.abs(target.x - getX()) < EPSILON && Math.abs(target.y - getY()) < EPSILON) {
            ai.setPosition(getX(), getY());
            target = ai.nextPosition();
        }

        System.out.println("Target: " + target.toString() + " | Pos: " + cellPos.toString() + " | Pos2: " + getX() + "x" + getY());

        boolean moved = false;
        if (target.x < getX()) {
           // if (canGoTo(Direction.WEST, delta)) {
                moveBy(-delta * maxSpeed, 0);
                moved = true;
          //  }
            setDirection(Direction.WEST);
        }
        if (target.x > getX()) {
         //   if (canGoTo(Direction.EAST, delta)) {
                moveBy(delta * maxSpeed, 0);
                moved = true;
           // }
            setDirection(Direction.EAST);
        }
        if (target.y > getY()) {
           // if (canGoTo(Direction.NORTH, delta)) {
                moveBy(0, delta * maxSpeed);
                moved = true;
          //  }
            setDirection(Direction.NORTH);
        }
        if ( target.y < getY()) {
          //  if (canGoTo(Direction.SOUTH, delta)) {
                moveBy(0, -delta * maxSpeed);
                moved = true;
          //  }
            setDirection(Direction.SOUTH);
        }



        super.draw(batch, parentAlpha);
    }

}
