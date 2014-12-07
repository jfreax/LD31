package de.jdsoft.nyup.Level;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import de.jdsoft.nyup.Entities.Entity;
import de.jdsoft.nyup.Entities.Ghost;
import de.jdsoft.nyup.Entities.Player;
import de.jdsoft.nyup.Utils.Collision;
import de.jdsoft.nyup.World;

import java.util.Random;

public class Level001 implements LevelRule {
    Random rng = new Random();

    private World world;
    TiledMap map;

    @Override
    public void init(World world) {
        this.world = world;
        this.map = world.getMap();

        TiledMapTileLayer wallLayer = (TiledMapTileLayer) map.getLayers().get("wall");
        TiledMapTileLayer pointLayer = (TiledMapTileLayer) map.getLayers().get("point");

        for (int x = 0; x < pointLayer.getWidth(); x++) {
            for (int y = 0; y < pointLayer.getHeight(); y++) {
                pointLayer.setCell(x, y, null);
            }
        }

        TextureRegion goldTextureRegion = new TextureRegion(world.getMapTexture(), 96, 64, 32, 32);
        int numberOfGold = 200;
        for (int i = 0; i < numberOfGold; i++) {
            int x = rng.nextInt(wallLayer.getWidth());
            int y = rng.nextInt(wallLayer.getHeight());

            if (wallLayer.getCell(x, y) == null) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(new StaticTiledMapTile(goldTextureRegion));
                pointLayer.setCell(x, y, cell);
            }
        }
    }

    @Override
    public void act(Entity entity, float delta) {

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
        if (entity1 instanceof Player) {
            if (entity2 instanceof Ghost) {
                world.lost();
            }
        }
        if (entity2 instanceof Ghost) {
            if (entity1 instanceof  Player) {
                world.lost();
            }
        }
        return false;
    }

    @Override
    public boolean onWallCollision(Entity entity1) {
        return false;
    }
}
