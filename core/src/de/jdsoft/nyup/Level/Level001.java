package de.jdsoft.nyup.Level;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import de.jdsoft.nyup.Entities.Entity;
import de.jdsoft.nyup.Entities.Ghost;
import de.jdsoft.nyup.Entities.Player;
import de.jdsoft.nyup.Utils.Collision;

public class Level001 implements LevelRule {
    TiledMap map;

    @Override
    public void init(TiledMap map) {
        this.map = map;
    }

    @Override
    public void onDraw(Entity entity, float delta) {

        if (entity instanceof Player) {
            Player player = (Player) entity;

            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
                if (player.canGoTo(Entity.Direction.WEST, delta)) {
                    player.moveBy(-delta * player.maxSpeed, 0);
                }
                player.setDirection(Entity.Direction.WEST);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
                if (player.canGoTo(Entity.Direction.EAST, delta)) {
                    player.moveBy(delta * player.maxSpeed, 0);
                }
                player.setDirection(Entity.Direction.EAST);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) {
                if (player.canGoTo(Entity.Direction.NORTH, delta)) {
                    player.moveBy(0, delta * player.maxSpeed);
                }
                player.setDirection(Entity.Direction.NORTH);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) {
                if (player.canGoTo(Entity.Direction.SOUTH, delta)) {
                    player.moveBy(0, -delta * player.maxSpeed);
                }
                player.setDirection(Entity.Direction.SOUTH);
            }


            // get points
            TiledMapTileLayer.Cell pointCell = Collision.getCollisionCell(player.pointLayer, player.getX(), player.getY(), true);
            if (pointCell != null && pointCell.getTile() != null) {
                player.points++;
            }
        }
    }

    @Override
    public void onInput(Entity entity, int keyCode) {

    }

    @Override
    public boolean onEntityCollision(Entity entity1, Entity entity2) {
        System.out.println("Collision!");
        return false;
    }

    @Override
    public boolean onWallCollision(Entity entity1) {
        return false;
    }
}
