package de.jdsoft.nyup.Level;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import de.jdsoft.nyup.Entities.Entity;
import de.jdsoft.nyup.Entities.Ghost;
import de.jdsoft.nyup.Entities.Player;
import de.jdsoft.nyup.Utils.Collision;
import de.jdsoft.nyup.World;

public class Level000 implements LevelRule {
    protected World world;
    protected TiledMap map;
    protected Player player;

    protected int mapWidth = 0;
    protected int mapHeight = 0;

    protected int overallCoinsInGame = 0;
    protected TiledMapTileLayer wallLayer;
    protected TiledMapTileLayer pointLayer;

    @Override
    public void init(World world) {
        this.world = world;
        this.map = world.getMap();

        this.initActors();

        wallLayer = (TiledMapTileLayer) map.getLayers().get("wall");
        pointLayer = (TiledMapTileLayer) map.getLayers().get("point");

        mapWidth = wallLayer.getWidth();
        mapHeight = wallLayer.getHeight();

        levelInit();
    }

    @Override
    public void levelInit() {
        for (int x = 0; x < pointLayer.getWidth(); x++) {
            for (int y = 0; y < pointLayer.getHeight(); y++) {
                pointLayer.setCell(x, y, null);
            }
        }

        TiledMapTileLayer templatePointL = (TiledMapTileLayer) map.getLayers().get("level000");
        overallCoinsInGame = 0;
        for ( int x = 0; x < pointLayer.getWidth(); x++) {
            for ( int y = 0; y < pointLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell templCell = templatePointL.getCell(x, y);
                if (templCell!= null) {
                    templCell.getTile().getProperties().put("type", "coin");
                    pointLayer.setCell(x, y, templCell);
                    overallCoinsInGame++;
                }
            }
        }

//        TextureRegion goldTextureRegion = new TextureRegion(world.getMapTexture(), 96, 64, 32, 32);
//        StaticTiledMapTile newGoldTile = new StaticTiledMapTile(goldTextureRegion);
//        newGoldTile.getProperties().put("type", "gold");
//        int numberOfGold = 200;
//        for (int i = 0; i < numberOfGold; i++) {
//            int x = rng.nextInt(wallLayer.getWidth());
//            int y = rng.nextInt(wallLayer.getHeight());
//
//            if (wallLayer.getCell(x, y) == null) {
//                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
//                cell.setTile(newGoldTile);
//                pointLayer.setCell(x, y, cell);
//            }
//        }

        TextureRegion mushroomTextureRegion = new TextureRegion(world.getMapTexture(), 128, 96, 32, 32);
        StaticTiledMapTile mushroomTile = new StaticTiledMapTile(mushroomTextureRegion);
        mushroomTile.getProperties().put("type", "mushroom");

        int numberOfMushrooms = 10;
        for (int i = 0; i < numberOfMushrooms; i++) {
            int x = rng.nextInt(wallLayer.getWidth());
            int y = rng.nextInt(wallLayer.getHeight());

            if (wallLayer.getCell(x, y) == null && pointLayer.getCell(x, y) == null) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(mushroomTile);

                pointLayer.setCell(x, y, cell);
            }
        }
    }

    public void initActors() {
        player = new Player(1, 3, map, this);
        world.addActor(player);
        world.addActor(new Ghost(2, 5, new Color(1.0f, 0.5f, 0.4f, 1f), map, this));
        world.addActor(new Ghost(14, 12, new Color(0.5f, 1.0f, 0.6f, 1.0f), map, this));
        world.addActor(new Ghost(14, 13, new Color(0.5f, 0.6f, 1.0f, 1.0f), map, this));
        world.setKeyboardFocus(player);
    }

    @Override
    public String getLevelHelp() {
        return "Level 0: \nClassic Pacman";
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
                if (pointCell.getTile().getProperties().containsKey("type")) {
                    if (pointCell.getTile().getProperties().get("type").equals("mushroom")) {
                        // yeahh, a mushroom
                    } else if (pointCell.getTile().getProperties().get("type").equals("coin")) {
                        overallCoinsInGame--;
                    }
                }
                player.points++;
            }
        }

        System.out.println(overallCoinsInGame);

        // check if we had won the game
        if (entity instanceof Player) {

            if (overallCoinsInGame <= 0) {
                world.won();
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

    @Override
    public int getMapHeight() {
        return mapHeight;
    }

    @Override
    public int getMapWidth() {
        return mapWidth;
    }

    @Override
    public Entity getPlayer() {
        return player;
    }
}
