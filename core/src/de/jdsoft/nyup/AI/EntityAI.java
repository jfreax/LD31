package de.jdsoft.nyup.AI;


import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public interface EntityAI {
    public void init(float x, float y, TiledMap map);
    public Vector2 nextPosition();
    public void setPosition(float x, float y);
}
