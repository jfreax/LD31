package de.jdsoft.nyup.Entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.utils.Pool;
import de.jdsoft.nyup.Nuyp;
import de.jdsoft.nyup.World;


public class Entity extends Actor {
    public enum Direction {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }

    protected final TiledMapTileLayer wallLayer;
    protected final TiledMapTileLayer pointLayer;

    protected int points = 0;

    protected float maxSpeed = 80.0f;

    protected static int frames;
    protected TextureRegion[] animFrames;
    protected Animation animation;

    private float stateTime;
    private Pool<RotateToAction> pool;


    public Entity(int x, int y, TiledMap map, Texture animTexture, int frames) {
        super();

        this.wallLayer = (TiledMapTileLayer) map.getLayers().get("wall");
        this.pointLayer = (TiledMapTileLayer) map.getLayers().get("point");

        this.setPosition(x, y);

        // animation setup
        Entity.frames = frames;
        TextureRegion[][] tmp = TextureRegion.split(animTexture, animTexture.getWidth() / Entity.frames, animTexture.getHeight());
        animFrames = new TextureRegion[Entity.frames];
        int index = 0;
        for (int j = 0; j < Entity.frames; j++) {
            animFrames[index++] = tmp[0][j];
        }
        animation = new Animation(0.05f, animFrames);
        animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        stateTime = 0f;

        // actions
        pool = new Pool<RotateToAction>() {
            protected RotateToAction newObject() {
                return new RotateToAction();
            }
        };
    }


    @Override
    public Actor hit(float arg0, float arg1, boolean flag) {
        return super.hit(arg0, arg1, flag);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);

        batch.draw(
                currentFrame, getX(), getY(),
                currentFrame.getRegionWidth() / 2, currentFrame.getRegionHeight() / 2,
                currentFrame.getRegionWidth(), currentFrame.getRegionHeight(),
                1f, 1f,
                getRotation());
    }


    protected void setDirection(Direction direction) {
        RotateToAction action = pool.obtain();
        action.setPool(pool);

        switch (direction) {
            case NORTH:
                action.setRotation(90);
                break;
            case EAST:
                action.setRotation(0);
                break;
            case SOUTH:
                action.setRotation(-90);
                break;
            case WEST: /* fall through */
            default:
                if (action.getRotation() > 0) {
                    action.setRotation(180);
                } else {
                    action.setRotation(-180);
                }
                break;
        }

        action.setDuration(0.15f);
        this.addAction(action);
    }

    protected boolean canGoTo(Direction direction, float delta) {
        float x = getX();
        float y = getY();

        switch (direction) {
            case NORTH:
                y += delta * maxSpeed;
                break;
            case EAST:
                x += delta * maxSpeed;
                break;
            case SOUTH:
                y -= delta * maxSpeed;
                break;
            case WEST: /* fall through */
            default:
                x -= delta * maxSpeed;
                break;
        }

        if (y < 0.0f || x < 0.0f || y + World.TILE_SIZE >= Nuyp.WORLD_HEIGHT || x + World.TILE_SIZE >= Nuyp.WORLD_WIDTH) {
            return false;
        }

        return getCollisionCell(wallLayer, x, y) == null;
    }

    private TiledMapTileLayer.Cell getCollisionCell(TiledMapTileLayer layer, float x, float y) {
        return getCollisionCell(layer, x, y, false);
    }

    protected TiledMapTileLayer.Cell getCollisionCell(TiledMapTileLayer layer, float x, float y, boolean remove) {
        TiledMapTileLayer.Cell tmp;

        float SMALLER_TILE = World.TILE_SIZE-2;

        for ( int i = 0; i <= 1; i++) {
            for ( int j = 0; j <= 1; j++) {
                tmp = layer.getCell((int) ((x + SMALLER_TILE*i) / World.TILE_SIZE), (int) ((y + SMALLER_TILE*j) / World.TILE_SIZE));
                if (tmp != null) {
                    if (remove) {
                        layer.setCell((int) ((x + SMALLER_TILE*i) / World.TILE_SIZE), (int) ((y + SMALLER_TILE*j) / World.TILE_SIZE), null);
                    }
                    return tmp;
                }
            }
        }

        return null;
    }

    public int getPoints() {
        return points;
    }

    public void setPosition(int x, int y) {
        setPosition( x * World.TILE_SIZE, y * World.TILE_SIZE );
    }

}
