package de.jdsoft.nyup.Level;


import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Timer;
import de.jdsoft.nyup.Entities.Entity;
import de.jdsoft.nyup.Entities.Player;
import de.jdsoft.nyup.Utils.Collision;
import de.jdsoft.nyup.World;

public class Level002 extends Level001 {

    final TiledMapTileLayer[] laserLayer = new TiledMapTileLayer[12];
    final float[] laserIntervalls = new float[12];
    protected MyTask[] laserTasks = new MyTask[12];

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
            laserIntervalls[i] = (rng.nextFloat()+0.2f) * 8.0f;
            laserTasks[i] = new MyTask(i);
            Timer.schedule(laserTasks[i], laserIntervalls[i], laserIntervalls[i]);
        }

        super.levelInit();
    }

    @Override
    public void levelShutdown() {
        if (laserTasks != null) {
            for (MyTask laserTask : laserTasks) {
                if (laserTask != null) {
                    laserTask.cancel();
                }
            }
        }

        for (TiledMapTileLayer aLaserLayer : laserLayer) {
            if (aLaserLayer != null) {
                aLaserLayer.setVisible(false);
            }
        }
    }

    @Override
    public void initActors() {
        super.initActors();
    }

    @Override
    public void checkWonLost(Entity entity) {
        super.checkWonLost(entity);

        if (entity instanceof Player) {
            // die on laser collision
            for (TiledMapTileLayer aLaserLayer : laserLayer) {
                if (aLaserLayer.isVisible()) {
                    TiledMapTileLayer.Cell pointCell = Collision.getCollisionCell(aLaserLayer, player.getX(), player.getY(), false, World.TILE_SIZE / 2.f);
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
