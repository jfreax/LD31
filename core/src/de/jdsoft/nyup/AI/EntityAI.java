package de.jdsoft.nyup.AI;


import com.badlogic.gdx.maps.tiled.TiledMap;
import de.jdsoft.nyup.Utils.Vector2i;

public interface EntityAI {
    public void init(int x, int y, TiledMap map);
    public Vector2i nextPosition();
    public void setPosition(int x, int y);
}
