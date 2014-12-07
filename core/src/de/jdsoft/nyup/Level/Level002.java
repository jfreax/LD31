package de.jdsoft.nyup.Level;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Timer;
import de.jdsoft.nyup.Entities.Entity;
import de.jdsoft.nyup.Entities.Ghost;
import de.jdsoft.nyup.Entities.Player;
import de.jdsoft.nyup.Utils.Collision;
import de.jdsoft.nyup.World;

public class Level002 extends Level000 {

    final TiledMapTileLayer[] laserLayer = new TiledMapTileLayer[12];
    final float[] laserIntervalls = new float[12];

    class MyTask extends Timer.Task {
        private int i;

        public MyTask(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            laserLayer[i].setVisible(!laserLayer[i].isVisible());
            if (laserLayer[i].isVisible()) {
                world.playSound(World.SoundID.LASER_START, 0.2f);
            }
        }
    }

    @Override
    public void levelInit() {
        for (int i = 0; i < laserLayer.length; i++) {
            TiledMapTileLayer lLayer = (TiledMapTileLayer) map.getLayers().get("laser"+i);
            initLaserAnimation(lLayer, 0.03f);

            laserLayer[i] = lLayer;
            laserIntervalls[i] = (rng.nextFloat()+0.1f) * 8.0f;
            Timer.schedule(new MyTask(i), laserIntervalls[i], laserIntervalls[i]);
        }

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

    @Override
    public void initActors() {
        player = new Player(1, 3, map, this);
        world.addActor(player);

        TiledMapTileLayer ghostLayer = (TiledMapTileLayer) map.getLayers().get("ghosts1");

        for (int x = 0; x < ghostLayer.getWidth(); x++) {
            for (int y = 0; y < ghostLayer.getHeight(); y++) {
                if (ghostLayer.getCell(x, y) != null) {
                    Ghost newGhost = new Ghost(x, y, new Color(rng.nextFloat(), rng.nextFloat(), rng.nextFloat(), 1f), map, this);
                    newGhost.setSpeed(rng.nextInt(50) + 50);
                    world.addActor(newGhost);
                }
            }
        }

        world.setKeyboardFocus(player);
    }

    @Override
    public void act(Entity entity, float delta) {
        super.act(entity, delta);

        if (entity instanceof Player) {
            // die on laser collision
            for (int i = 0; i < laserLayer.length; i++) {
                if (laserLayer[i].isVisible()) {
                    TiledMapTileLayer.Cell pointCell = Collision.getCollisionCell(laserLayer[i], player.getX(), player.getY(), false, World.TILE_SIZE / 2.f);
                    if (pointCell != null && pointCell.getTile() != null) {
                        world.lost();
                    }
                }
            }
        }
    }

    @Override
    public String getLevelHelp() {
        return "Level 2: \nAttention! Attention!";
    }
}
