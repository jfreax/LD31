package de.jdsoft.nyup.Entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import de.jdsoft.nyup.Utils.Collision;

public class Player extends Entity {
    public Player(int x, int y, TiledMap map) {
        super(x, y, map, new Texture("gfx/pacman_anim.png"), 10);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
            if (canGoTo(Direction.WEST, delta)) {
                moveBy(-delta * maxSpeed, 0);
            }
            setDirection(Direction.WEST);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
            if (canGoTo(Direction.EAST, delta)) {
                moveBy(delta * maxSpeed, 0);
            }
            setDirection(Direction.EAST);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) {
            if (canGoTo(Direction.NORTH, delta)) {
                moveBy(0, delta * maxSpeed);
            }
            setDirection(Direction.NORTH);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) {
            if (canGoTo(Direction.SOUTH, delta)) {
                moveBy(0, -delta * maxSpeed);
            }
            setDirection(Direction.SOUTH);
        }

        // get points
        TiledMapTileLayer.Cell pointCell = Collision.getCollisionCell(pointLayer, getX(), getY(), true);
        if (pointCell != null && pointCell.getTile() != null) {
            points++;
        }
        
        super.draw(batch, parentAlpha);
    }
}
