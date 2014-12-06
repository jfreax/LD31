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

    private final TiledMap map;
    private final TiledMapTileLayer wallLayer;

    public enum Direction {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }

    private static final int FRAME_COLS = 7;
    private float maxSpeed = 40.0f;

    Texture animTexture;
    TextureRegion[] animFrames;
    Animation animation;
    float stateTime;

    TextureRegion currentFrame;

    Pool<RotateToAction> pool;


    private ShapeRenderer renderer = new ShapeRenderer();

    public Player(TiledMap map) {
        super();

        this.map = map;
        this.wallLayer = (TiledMapTileLayer) map.getLayers().get(0);

        this.setSize(100f, 100f);
        this.setPosition(50f, 50f);
        this.setTouchable(Touchable.enabled);

        //this.setBounds(getX(), getY(), getWidth(), getHeight());

        new MoveToAction();

        this.addListener(new InputListener() {

        });

        // animation setup
        animTexture = new Texture(Gdx.files.internal("gfx/pacman_anim.png"));
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

        action.setDuration(0.25f);
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

        return wallLayer.getCell((int) (x / World.TILE_SIZE), (int) (y / World.TILE_SIZE)) == null &&
                wallLayer.getCell((int) ((x + World.TILE_SIZE-1) / World.TILE_SIZE), (int) (y / World.TILE_SIZE)) == null &&
                wallLayer.getCell((int) (x / World.TILE_SIZE), (int) ((y + World.TILE_SIZE-1) / World.TILE_SIZE)) == null &&
                wallLayer.getCell((int) ((x + World.TILE_SIZE-1) / World.TILE_SIZE), (int) ((y + World.TILE_SIZE-1) / World.TILE_SIZE)) == null;
    }
}
