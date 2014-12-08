package de.jdsoft.nyup.Level;


import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import de.jdsoft.nyup.Entities.Entity;
import de.jdsoft.nyup.Entities.Player;
import de.jdsoft.nyup.Utils.Collision;
import de.jdsoft.nyup.World;

public class Level004 extends Level002 {

    private TiledMapTileLayer actionLayer;

    float lifeLostTime = 0.8f;
    float timeToLostLife = lifeLostTime;

    int maxLifes = 50;

    @Override
    public void levelInit() {
        super.levelInit();

        actionLayer = (TiledMapTileLayer) map.getLayers().get("actions");
        actionLayer.setVisible(true);
    }

    @Override
    public void initActors() {
        super.initActors();

        player.setLifes(25);
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
                if (aLaserLayer != null && aLaserLayer.isVisible()) {
                    TiledMapTileLayer.Cell pointCell = Collision.getCollisionCell(aLaserLayer, player.getX(), player.getY(), false, World.T2W_X / 2.f);
                    if (pointCell != null && pointCell.getTile() != null) {
                        timeToLostLife += delta * 2.0f;

                        if (timeToLostLife >= lifeLostTime) {
                            timeToLostLife = lifeLostTime / 2.0f;

                            if (player.getLifes() < maxLifes) {
                                player.setLifes(player.getLifes() + 1);
                            }
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

        if (entity instanceof Player) {
            TiledMapTileLayer.Cell portalCell = Collision.getCollisionCell(actionLayer, player.getX(), player.getY(), false, World.T2W_X / 2.f);

            if (portalCell != null && portalCell.getTile() != null) {
                world.won();
            }
        }
    }

    @Override
    public String getLevelHelp() {
        return "Level 4: \nI need energy!";
    }
}
