package de.jdsoft.nyup;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class NuypInput implements InputProcessor {

    private World world;

    public NuypInput(World world) {

        this.world = world;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (world.isEnd()) {
            if (keycode == Input.Keys.SPACE || keycode == Input.Keys.ENTER) {
                world.init();
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (world.isEnd()) {
                world.init();
                return true;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
