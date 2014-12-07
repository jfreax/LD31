package de.jdsoft.nyup.AI;


import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import de.jdsoft.nyup.Entities.Entity;
import de.jdsoft.nyup.Level.LevelRule;

public interface EntityAI {
    public void init(float x, float y, TiledMap map, Entity ghost, LevelRule level);
    public Vector2 nextPosition();
    public void setPosition(float x, float y);
}
