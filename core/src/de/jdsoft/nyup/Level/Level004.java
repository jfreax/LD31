package de.jdsoft.nyup.Level;


import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import de.jdsoft.nyup.Entities.Entity;
import de.jdsoft.nyup.Entities.Player;
import de.jdsoft.nyup.Utils.Collision;
import de.jdsoft.nyup.World;

public class Level004 extends Level003 {

    private TiledMapTileLayer actionLayer;

    float lifeLostTime = 0.5f;
    float timeToLostLife = lifeLostTime;

    @Override
    public void levelInit() {
        super.levelInit();

        actionLayer = (TiledMapTileLayer) map.getLayers().get("actions");
        actionLayer.setVisible(true);
    }

    @Override
    public void initActors() {
        super.initActors();

        player.setLifes(20);
    }

    @Override
    public void act(Entity entity, float delta) {
        super.act(entity, delta);


        if (entity instanceof Player) {
            timeToLostLife -= delta;
            if (timeToLostLife <= 0.f) {
                timeToLostLife = lifeLostTime;
                player.setLifes(player.getLifes() - 1);
            }

            for (TiledMapTileLayer aLaserLayer : laserLayer) {
                if (aLaserLayer.isVisible()) {
                    TiledMapTileLayer.Cell pointCell = Collision.getCollisionCell(aLaserLayer, player.getX(), player.getY(), false, World.TILE_SIZE / 2.f);
                    if (pointCell != null && pointCell.getTile() != null) {
                        timeToLostLife += delta * 1.75f;

                        if (timeToLostLife >= lifeLostTime) {
                            timeToLostLife = lifeLostTime / 2.0f;
                            player.setLifes(player.getLifes() + 1);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void checkWonLost(Entity entity) {
        if (player.getLifes() <= 0) {
            world.lost();
        }
    }

    @Override
    public String getLevelHelp() {
        return "Level 4: \nI need energy!";
    }
}
