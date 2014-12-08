package de.jdsoft.nyup.Level;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.scenes.scene2d.Actor;
import de.jdsoft.nyup.Entities.Entity;
import de.jdsoft.nyup.Entities.Ghost;
import de.jdsoft.nyup.Entities.Player;
import de.jdsoft.nyup.Utils.Collision;
import de.jdsoft.nyup.World;

public class Level006 extends Level002 {


    private TiledMapTileLayer actionLayer;

    @Override
    public void levelInit() {
        overallCoinsInGame = loadTypeFromTo((TiledMapTileLayer) map.getLayers().get("level000"), pointLayer, "coin");
        loadTypeFromTo((TiledMapTileLayer) map.getLayers().get("level000_speed"), pointLayer, "speed");


        TextureRegion textureRegion = new TextureRegion(world.getMapTexture(), 96, 64, 32, 32);
        StaticTiledMapTile coinTile = new StaticTiledMapTile(textureRegion);
        coinTile.getProperties().put("type", "coin");


        for (int i = 0; i < 50; i++) {
            int x = rng.nextInt(wallLayer.getWidth());
            int y = rng.nextInt(wallLayer.getHeight());

            if (wallLayer.getCell(x, y) == null && pointLayer.getCell(x, y) == null) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(coinTile);

                pointLayer.setCell(x, y, cell);
            }
        }


        StaticTiledMapTile mushroomTile = new StaticTiledMapTile(textureRegion);
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

        actionLayer = (TiledMapTileLayer) map.getLayers().get("actions");
        actionLayer.setVisible(true);

    }

    @Override
    public void checkWonLost(Entity entity) {
        if (entity instanceof Player) {
            TiledMapTileLayer.Cell portalCell = Collision.getCollisionCell(actionLayer, player.getX(), player.getY(), false, World.TILE_SIZE / 2.f);

            if (portalCell != null && portalCell.getTile() != null) {
                if (player.points == 42) {
                    world.won();
                }
            }

            boolean foundGhost = false;
            for (Actor actor : world.getActors()) {
                if (actor instanceof Ghost) {
                    foundGhost = true;
                    break;
                }
            }

            if (!foundGhost) {
                world.lost();
            }
        }
    }

    @Override
    public String getLevelHelp() {
        return "Level 3: \n42!";
    }
}
