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
        super.levelInit();

        actionLayer = (TiledMapTileLayer) map.getLayers().get("actions");
        actionLayer.setVisible(true);

    }

    @Override
    public void act(Entity entity, float delta) {
        super.act(entity, delta);

        if (entity instanceof Player) {
            TiledMapTileLayer.Cell portalCell = Collision.getCollisionCell(actionLayer, player.getX(), player.getY(), false, World.TILE_SIZE / 2.f);

            if (portalCell != null && portalCell.getTile() != null) {
                world.won();
            }
        }
    }

    @Override
    public void checkWon(Entity entity) {
        //super.checkWon(entity);
    }

    @Override
    public String getLevelHelp() {
        return "Level 3: \nGoal!";
    }
}
