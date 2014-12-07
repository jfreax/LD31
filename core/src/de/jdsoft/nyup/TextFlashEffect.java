package de.jdsoft.nyup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.utils.Disposable;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class TextFlashEffect extends Actor implements Disposable {

    private BitmapFont font;
    private CharSequence text;
    private BitmapFont.TextBounds bounds;

    private boolean visible = false;
    private boolean running = false;

    private int runs = 0;

    private float deltaSinceRun = 0.0f;

    public TextFlashEffect(BitmapFont font, CharSequence text) {

        this.font = font;
        setText(text);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        float delta = Gdx.graphics.getDeltaTime();
        this.act(delta);

        if (visible) {
            if (!running) {
                deltaSinceRun += delta;
            }

            float scale = getScaleX() - 0.2f;
            float x = getX() - (bounds.width / 2.f);
            float y = getY() - (bounds.height / 2.f);

            float max = (10 - deltaSinceRun*10);
            for (int i = 0; i < max; i++) {
                scale += 0.02f;

                font.setScale(scale);
                font.setColor(1, 1, 1, i/10.f);
                font.draw(batch, text, getX() - (bounds.width / 2.f), getY() - (bounds.height / 2.f));
            }

            font.setScale(getScaleX());
            font.setColor(1, 1, 1, 1);
            font.draw(batch, text, x, y);
        }
    }

    public void start() {
        start(0.1f);
    }

    public void start(float startScale) {
        this.setScale(startScale);

        ScaleToAction action = new ScaleToAction();
        action.setScale(1.0f);
        action.setDuration(0.9f);
        action.setInterpolation(Interpolation.swingOut);
        this.addAction(sequence(action, run(new Runnable() {
            @Override
            public void run() {
                running = false;
            }
        })));

        runs++;
        running = true;
        deltaSinceRun = 0.0f;

        show();
    }


    @Override
    public void dispose() {
        font.dispose();
    }

    public void hide() {
        visible = false;
    }

    public void show() {
        visible = true;
    }

    public boolean running() {
        return running;
    }

    public void setText(CharSequence text) {
        this.text = text;
        bounds = font.getBounds(text);
    }

    public int runs() {
        return runs;
    }
}
