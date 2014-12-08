package de.jdsoft.nyup.Level;


import de.jdsoft.nyup.Entities.Entity;
import de.jdsoft.nyup.Entities.Player;
import de.jdsoft.nyup.World;

import java.util.Random;

public interface LevelRule {
    enum TILE_TYPE {
        COIN,
        MUSHROOM,
        KEY, LASER, SPEED
    }

    Random rng = new Random();

    public void init(World world);
    public void levelInit();

    public void levelShutdown();

    public String getLevelHelp();

    public void act(Entity entity, float delta);
    public void checkWonLost(Entity entity);

    public void colliedWithTile(TILE_TYPE type);

    public void onInput(Entity entity, int keyCode);
    public boolean onEntityCollision(Entity entity1, Entity entity2);

    /**
     *
     * @param entity1
     * @return true if walkable
     */
    public boolean onWallCollision(Entity entity1);

    public int getMapHeight();
    public int getMapWidth();

    Player getPlayer();
}
