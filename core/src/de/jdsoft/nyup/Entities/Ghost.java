package de.jdsoft.nyup.Entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import de.jdsoft.nyup.AI.EntityAI;
import de.jdsoft.nyup.AI.GhostAI;
import de.jdsoft.nyup.Utils.Vector2i;

public class Ghost extends Entity {
    EntityAI ai = new GhostAI();
    Vector2i target = null;

    public Ghost(int x, int y, TiledMap map) {
        super(x, y, map, new Texture("gfx/ghost.png"), 1);

        ai.init(x, y, map);
        target = ai.nextPosition();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float delta = Gdx.graphics.getDeltaTime();

        Vector2i cellPos = getCurrentCellPos();
        if (target.x == cellPos.x && target.y == cellPos.y) {
            ai.setPosition(target.x, target.y);
            target = ai.nextPosition();
        }

        if (target.x < cellPos.x) {
            if (canGoTo(Direction.WEST, delta)) {
                moveBy(-delta * maxSpeed, 0);
            }
            setDirection(Direction.WEST);
        }
        if (target.x > cellPos.x) {
            if (canGoTo(Direction.EAST, delta)) {
                moveBy(delta * maxSpeed, 0);
            }
            setDirection(Direction.EAST);
        }
        if (target.y > cellPos.y) {
            if (canGoTo(Direction.NORTH, delta)) {
                moveBy(0, delta * maxSpeed);
            }
            setDirection(Direction.NORTH);
        }
        if ( target.y < cellPos.y) {
            if (canGoTo(Direction.SOUTH, delta)) {
                moveBy(0, -delta * maxSpeed);
            }
            setDirection(Direction.SOUTH);
        }

        super.draw(batch, parentAlpha);
    }

}
