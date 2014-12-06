package de.jdsoft.nyup;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.utils.Pool;


public class Player extends Actor {

    private final TiledMapTileLayer wallLayer;
    private final TiledMapTileLayer pointLayer;

    private int points = 0;

    public enum Direction {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }

    private static final int FRAME_COLS = 7;
    private float maxSpeed = 80.0f;

    Texture animTexture;
    TextureRegion[] animFrames;
    Animation animation;
    float stateTime;

    TextureRegion currentFrame;

    Pool<RotateToAction> pool;


    private ShapeRenderer renderer = new ShapeRenderer();

    public Player(TiledMap map) {
        super();

        this.wallLayer = (TiledMapTileLayer) map.getLayers().get("wall");
        this.pointLayer = (TiledMapTileLayer) map.getLayers().get("point");

        this.setSize(100f, 100f);
        this.setPosition(50f, 50f);
        this.setTouchable(Touchable.enabled);

        //this.setBounds(getX(), getY(), getWidth(), getHeight());

        new MoveToAction();

        this.addListener(new InputListener() {

        });

        // animation setup
        animTexture = new Texture("gfx/pacman_anim.png");
        TextureRegion[][] tmp = TextureRegion.split(animTexture, animTexture.getWidth() / FRAME_COLS, animTexture.getHeight());
        animFrames = new TextureRegion[FRAME_COLS];
        int index = 0;
        for (int j = 0; j < FRAME_COLS; j++) {
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
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
            if (canGoTo(Direction.WEST, delta)) {
                moveBy(-delta * maxSpeed, 0);
            }
            setDirection(Direction.WEST);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
            if (canGoTo(Direction.EAST, delta)) {
                moveBy(delta * maxSpeed, 0);
            }
            setDirection(Direction.EAST);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) {
            if (canGoTo(Direction.NORTH, delta)) {
                moveBy(0, delta * maxSpeed);
            }
            setDirection(Direction.NORTH);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) {
            if (canGoTo(Direction.SOUTH, delta)) {
                moveBy(0, -delta * maxSpeed);
            }
            setDirection(Direction.SOUTH);
        }

        // get points
        TiledMapTileLayer.Cell pointCell = getCollisionCell(pointLayer, getX(), getY(), true);
        if (pointCell != null && pointCell.getTile() != null) {
            points++;
        }


        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = animation.getKeyFrame(stateTime, true);

        batch.draw(
                currentFrame, getX(), getY(),
                currentFrame.getRegionWidth() / 2, currentFrame.getRegionHeight() / 2,
                currentFrame.getRegionWidth(), currentFrame.getRegionHeight(),
                1f, 1f,
                getRotation());
    }


    private void setDirection(Direction direction) {
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

    private boolean canGoTo(Direction direction, float delta) {
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

    private TiledMapTileLayer.Cell getCollisionCell(TiledMapTileLayer layer, float x, float y, boolean remove) {
        TiledMapTileLayer.Cell tmp;

        float SMALLER_TILE = World.TILE_SIZE-1;

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

}
