package de.jdsoft.nyup;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;

public class Player extends Actor {
    private ShapeRenderer renderer = new ShapeRenderer();

    public Player() {
        super();

        this.setSize(100f, 100f);
        this.setPosition(50f, 50f);

        MoveToAction action = new MoveToAction();
        action.setPosition(200, 200);
        action.setDuration(10);
        this.addAction(action);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();

        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
        renderer.translate(getX(), getY(), 0);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.BLUE);
        renderer.rect(getX(), getY(), getWidth(), getHeight());
        renderer.end();

        batch.begin();
    }
}
