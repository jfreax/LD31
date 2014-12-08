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

public class Level003 extends Level002 {


    private TiledMapTileLayer actionLayer;

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

        actionLayer = (TiledMapTileLayer) map.getLayers().get("actions");
        actionLayer.setVisible(true);

    }

    @Override
    public void checkWonLost(Entity entity) {
        if (entity instanceof Player) {
            TiledMapTileLayer.Cell portalCell = Collision.getCollisionCell(actionLayer, player.getX(), player.getY(), false, World.T2W_X / 2.f);

            if (portalCell != null && portalCell.getTile() != null) {
                world.won();
            }
        }
    }

    @Override
    public String getLevelHelp() {
        return "Level 3: \nGoal!";
    }
}
