package de.jdsoft.nyup.Entities;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import de.jdsoft.nyup.Level.LevelRule;
import de.jdsoft.nyup.World;

public class Player extends Entity {
    private int oldPositionX = 0;
    private int oldPositionY = 0;
    private int steps = 0;

    public Player(int x, int y, TiledMap map, LevelRule level) {
        super(x, y, map, new Texture("gfx/pacman_anim.png"), 10, level);

        oldPositionX = (int) (getX() / World.TILE_SIZE);
        oldPositionY = (int) (getX() / World.TILE_SIZE);;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        int newPositionX = (int) (getX() / World.TILE_SIZE);
        int newPositionY = (int) (getY() / World.TILE_SIZE);

        if (newPositionX != oldPositionX || newPositionY != oldPositionY) {
            oldPositionX = newPositionX;
            oldPositionY = newPositionY;

            steps++;
        }
    }

    public int getSteps() {
        return steps;
    }
}
