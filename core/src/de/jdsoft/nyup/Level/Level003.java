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


    @Override
    public void levelInit() {
        super.levelInit();

        // todo add goal
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
