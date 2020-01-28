package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class FlyingText {

    private Vector2 position;
    private Vector2 velocity;
    private StringBuilder text;
    private float time;
    private float timeMax;
    public boolean active;

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    public Vector2 getPosition() {
        return position;
    }

    public StringBuilder getText() {
        return text;
    }

    public void setup(float x, float y, StringBuilder text) {
        this.position.set(x, y);
        this.velocity.set(10,40);
        this.text.setLength(0);
        this.text.append(text);
        this.time = 0.0f;
        this.active = true;
    }


    public FlyingText() {
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0,0);
        this.text = new StringBuilder();
        this.timeMax = 5.0f;
        this.time = 0.0f;
        this.active = true;
    }

    public void update(float dt) {
        this.time += dt;
        position.mulAdd(velocity,dt);
        if (time > timeMax) {
            deactivate();
        }
    }

}
