package de.jdsoft.nyup.Level;


import com.badlogic.gdx.maps.tiled.TiledMap;
import de.jdsoft.nyup.Entities.Entity;

public interface LevelRule {
    public void init(TiledMap map);

    public void onDraw(Entity entity, float delta);
    public void onInput(Entity entity, int keyCode);
    public boolean onEntityCollision(Entity entity1, Entity entity2);

    /**
     *
     * @param entity1
     * @return true if walkable
     */
    public boolean onWallCollision(Entity entity1);
}
