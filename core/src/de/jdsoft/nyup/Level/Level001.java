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

public class Level001 extends Level000 {

    @Override
    public void init(World world) {
        this.world = world;
        this.map = world.getMap();

        this.initActors();

        TiledMapTileLayer wallLayer = (TiledMapTileLayer) map.getLayers().get("wall");
        TiledMapTileLayer pointLayer = (TiledMapTileLayer) map.getLayers().get("point");

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
    public String getLevelHelp() {
        return "Level 1:\nClassic Pacman. Difficulty++";
    }
}
