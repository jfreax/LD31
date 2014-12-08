package de.jdsoft.nyup.Level;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
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

    boolean canEatGhosts = false;
    private Timer.Task unEatTask;
    private Timer.Task unfastenTask;

    @Override
    public void init(World world) {
        this.world = world;
        this.map = world.getMap();

        this.initActors();

        wallLayer = (TiledMapTileLayer) map.getLayers().get("wall");
        pointLayer = (TiledMapTileLayer) map.getLayers().get("point");

        mapWidth = wallLayer.getWidth();
        mapHeight = wallLayer.getHeight();

        for (int x = 0; x < pointLayer.getWidth(); x++) {
            for (int y = 0; y < pointLayer.getHeight(); y++) {
                pointLayer.setCell(x, y, null);
            }
        }

        levelInit();
    }

    @Override
    public void levelInit() {
        overallCoinsInGame = loadTypeFromTo((TiledMapTileLayer) map.getLayers().get("level000"), pointLayer, "coin");
        loadTypeFromTo((TiledMapTileLayer) map.getLayers().get("level000_speed"), pointLayer, "speed");


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

    @Override
    public void levelShutdown() {

    }

    public void initActors() {
        player = new Player(1, 3, map, this);
        world.addActor(player);
        world.addActor(new Ghost(12, 5, new Color(1.0f, 0.5f, 0.4f, 1f), map, this));
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

            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
                if (player.canGoTo(Entity.Direction.WEST, delta)) {
                    player.moveBy(-delta * player.maxSpeed, 0);
                }
                player.setDirection(Entity.Direction.WEST);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                if (player.canGoTo(Entity.Direction.EAST, delta)) {
                    player.moveBy(delta * player.maxSpeed, 0);
                }
                player.setDirection(Entity.Direction.EAST);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
                if (player.canGoTo(Entity.Direction.NORTH, delta)) {
                    player.moveBy(0, delta * player.maxSpeed);
                }
                player.setDirection(Entity.Direction.NORTH);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
                if (player.canGoTo(Entity.Direction.SOUTH, delta)) {
                    player.moveBy(0, -delta * player.maxSpeed);
                }
                player.setDirection(Entity.Direction.SOUTH);
            }


            // get points
            TiledMapTileLayer.Cell pointCell = Collision.getCollisionCell(pointLayer, player.getX(), player.getY(), true);
            if (pointCell != null && pointCell.getTile() != null) {
                if (pointCell.getTile().getProperties().containsKey("type")) {
                    if (pointCell.getTile().getProperties().get("type").equals("mushroom")) {
                        colliedWithTile(TILE_TYPE.MUSHROOM);
                    } else if (pointCell.getTile().getProperties().get("type").equals("coin")) {
                        colliedWithTile(TILE_TYPE.COIN);
                    } else if (pointCell.getTile().getProperties().get("type").equals("speed")) {
                        colliedWithTile(TILE_TYPE.SPEED);
                    } else if (pointCell.getTile().getProperties().get("type").equals("key")) {
                        colliedWithTile(TILE_TYPE.KEY);
                    }
                }
                player.points++;
            }
        }

        checkWonLost(entity);
    }

    @Override
    public void checkWonLost(Entity entity) {
        // check if we had won the game
        if (entity instanceof Player) {

            if (overallCoinsInGame <= 0) {
                world.won();
            }
        }
    }

    @Override
    public void colliedWithTile(TILE_TYPE type) {
        switch (type) {
            case MUSHROOM:
                world.playSound(World.SoundID.PICKUP_MUSHROOM);
                setCanEatGhosts(true);

                break;
            case COIN:
                world.playSound(World.SoundID.PICKUP_COIN);
                overallCoinsInGame--;
                break;

            case SPEED:
                world.playSound(World.SoundID.PICKUP_2);

                if (unfastenTask != null) {
                    unfastenTask.cancel();
                } else {
                    player.maxSpeed *= 1.5f;
                }

                unfastenTask = new Timer.Task() {
                    @Override
                    public void run() {
                        player.maxSpeed /= 1.5f;
                        unfastenTask = null;
                    }
                };
                Timer.schedule(unfastenTask, 1.f);

                break;
        }
    }

    @Override
    public void onInput(Entity entity, int keyCode) {

    }

    @Override
    public boolean onEntityCollision(Entity entity1, Entity entity2) {
        if (entity1 instanceof Player) {
            if (entity2 instanceof Ghost) {
                if (canEatGhosts) {
                    world.playSound(World.SoundID.KILL_GHOST);
                    player.points += 10;
                    removeEntity(entity2);
                } else {
                    world.lost();
                }
            }
        }

        return false;
    }

    protected int loadTypeFromTo(TiledMapTileLayer layer, TiledMapTileLayer targetLayer, String type) {
        int loaded = 0;

        for ( int x = 0; x < layer.getWidth(); x++) {
            for ( int y = 0; y < layer.getHeight(); y++) {
                TiledMapTileLayer.Cell templCell = layer.getCell(x, y);
                if (templCell!= null &&
                        templCell.getTile().getProperties().containsKey("type") &&
                        templCell.getTile().getProperties().get("type").equals(type)) {
                    templCell.getTile().getProperties().put("type", type);
                    targetLayer.setCell(x, y, templCell);
                    loaded++;
                }
            }
        }

        return loaded;
    }

    private void removeEntity(Entity entity) {
        world.getActors().removeValue(entity, false);
    }

    public void initLaserAnimation(TiledMapTileLayer layer, float intervall) {
        String[] names =  { "left", "right", "top", "bottom", "horizontal", "vertical", "yvertical", "ytop", "ybottom" };

        for (String name : names) {
            TiledMapTileSet tileset = map.getTileSets().getTileSet("laser");

            Array<StaticTiledMapTile> leftLaserTiles = new Array<StaticTiledMapTile>();
            for (TiledMapTile tile : tileset) {
                Object property = tile.getProperties().get(name);
                if (property != null) {
                    leftLaserTiles.add(new StaticTiledMapTile(tile.getTextureRegion()));
                }
            }

            //TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("laser1");
            for (int x = 0; x < layer.getWidth(); x++) {
                for (int y = 0; y < layer.getHeight(); y++) {
                    TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                    if (cell != null) {
                        Object property = cell.getTile().getProperties().get(name);
                        if (property != null) {
                            cell.setTile(new AnimatedTiledMapTile(intervall, leftLaserTiles));
                            cell.getTile().getProperties().put("type", "laser");
                        }
                    }
                }
            }
        }
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
    public Player getPlayer() {
        return player;
    }

    public void setCanEatGhosts(boolean canEatGhosts) {
        this.canEatGhosts = canEatGhosts;

        for(Actor act : world.getActors()) {
            if (act instanceof Ghost) {
                Ghost ghost = (Ghost) act;
                ghost.canGetEaten(canEatGhosts);

                if (canEatGhosts) {
                    if (unEatTask != null) {
                        unEatTask.cancel();
                    }

                    unEatTask = new Timer.Task() {
                        @Override
                        public void run() {
                            setCanEatGhosts(false);
                        }
                    };

                    Timer.schedule(unEatTask, 8.0f);
                }
            }
        }

    }
}
