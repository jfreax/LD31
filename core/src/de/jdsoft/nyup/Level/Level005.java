package de.jdsoft.nyup.Level;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Timer;
import de.jdsoft.nyup.Entities.Entity;
import de.jdsoft.nyup.Entities.Player;
import de.jdsoft.nyup.Utils.Collision;
import de.jdsoft.nyup.World;

public class Level005 extends Level002 {


    private TiledMapTileLayer actionLayer;
    private TiledMapTileLayer laserLayer1;

    @Override
    public void levelInit() {
        overallCoinsInGame = loadTypeFromTo((TiledMapTileLayer) map.getLayers().get("level003"), pointLayer, "coin");
        loadTypeFromTo((TiledMapTileLayer) map.getLayers().get("level003"), pointLayer, "key");

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

        laserLayer1 = (TiledMapTileLayer) map.getLayers().get("level003_laser");
        loadTypeFromTo(laserLayer1, pointLayer, "laser");

        initLaserAnimation(pointLayer, 0.03f);
        //laserLayer1.setVisible(true);

        actionLayer = (TiledMapTileLayer) map.getLayers().get("actions");
        actionLayer.setVisible(true);

        firstKey = true;
    }

    @Override
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

    boolean firstKey = true;

    @Override
    public void colliedWithTile(TILE_TYPE type) {
        super.colliedWithTile(type);

        switch (type) {
            case KEY:
                world.playSound(World.SoundID.PICKUP_MUSHROOM);

                for (int x = 0; x < pointLayer.getWidth(); x++) {
                    for (int y = 0; y < pointLayer.getHeight(); y++) {
                        TiledMapTileLayer.Cell cell = pointLayer.getCell(x, y);
                        if (cell != null &&
                                cell.getTile().getProperties().containsKey("type") &&
                                cell.getTile().getProperties().get("type").equals("laser")) {
                            if (x > 22 || !firstKey) {
                                pointLayer.setCell(x, y, null);
                            }
                        }
                    }
                }
                firstKey = false;

                break;
        }
    }

    @Override
    public void checkWonLost(Entity entity) {
        if (entity instanceof Player) {
            TiledMapTileLayer.Cell portalCell = Collision.getCollisionCell(actionLayer, player.getX(), player.getY(), false, World.TILE_SIZE / 2.f);

            if (portalCell != null && portalCell.getTile() != null) {
                world.won();
            }

            // die on laser collision
            TiledMapTileLayer.Cell pointCell = Collision.getCollisionCell((TiledMapTileLayer) map.getLayers().get("level003"), player.getX(), player.getY(), false, World.TILE_SIZE / 2.f);
            if (pointCell != null &&
                    pointCell.getTile() != null &&
                    pointCell.getTile().getProperties().containsKey("type") &&
                    pointCell.getTile().getProperties().get("type").equals("laser")) {
                world.lost();
            }
        }
    }

    @Override
    public String getLevelHelp() {
        return "Level 5: \nSecret key!";
    }
}
