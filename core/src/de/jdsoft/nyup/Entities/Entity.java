package de.jdsoft.nyup.Entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.utils.Pool;
import de.jdsoft.nyup.Level.LevelRule;
import de.jdsoft.nyup.Nuyp;
import de.jdsoft.nyup.Utils.Collision;
import de.jdsoft.nyup.World;


public class Entity extends Actor {
    private LevelRule level;
    private Rectangle bounds = new Rectangle();
    private int lifes = 1;


    public enum Direction {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }

    protected final TiledMapTileLayer wallLayer;
    public final TiledMapTileLayer pointLayer;

    public int points = 0;

    public float maxSpeed = 100.0f;
    protected float rotationSpeed = 0.15f;


    protected static int frames;
    protected TextureRegion[] animFrames;
    protected Animation animation;

    private float stateTime;
    private Pool<RotateToAction> pool;


    public Entity(int x, int y, TiledMap map, Texture animTexture, int frames, LevelRule level) {
        super();

        this.level = level;

        this.wallLayer = (TiledMapTileLayer) map.getLayers().get("wall");
        this.pointLayer = (TiledMapTileLayer) map.getLayers().get("point");

        this.setPosition(x, y);
        this.setSize(World.T2W_X, World.T2W_Y);

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
    public void act(float delta) {
        super.act(delta);
        level.act(this, delta);
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

    @Override
    public void moveBy(float x, float y) {
        super.moveBy(x, y);
        updateBounds();
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        updateBounds();
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        updateBounds();
    }

    public void setDirection(Direction direction) {
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

        action.setDuration(rotationSpeed);
        this.addAction(action);
    }

    public boolean canGoTo(Direction direction, float delta) {
        if (level.onWallCollision(this)) {
            return true;
        }

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

        if (y < 0.0f || x < 0.0f || y + World.T2W_Y >= Nuyp.WORLD_HEIGHT-World.ACTIONBAR_HEIGHT || x + World.T2W_X >= Nuyp.WORLD_WIDTH) {
            return false;
        }

        return Collision.getCollisionCell(wallLayer, x, y) == null;
    }

    public void setSpeed(int speed) {
        this.maxSpeed = speed;
    }


    public int getPoints() {
        return points;
    }

    public int getLifes() {
        return lifes;
    }

    public void setLifes(int lifes) {
        this.lifes = lifes;
    }

    public void setPosition(int x, int y) {
        setPosition(x * World.T2W_X, y * World.T2W_Y);
    }

    private void updateBounds() {
        bounds.set(getX(), getY(), getWidth(), getHeight());
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
