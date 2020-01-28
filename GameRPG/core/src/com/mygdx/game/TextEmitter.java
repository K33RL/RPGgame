package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextEmitter {
    private FlyingText[] items;
    private Texture texture;
    private TextureRegion[] regions;


    public TextEmitter() {
        items = new FlyingText[50];
        for (int i = 0; i < items.length; i++) {
            items[i] = new FlyingText();

        }
    }

    public void render(SpriteBatch batch, BitmapFont font20) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].isActive()) {
                font20.draw(batch, items[i].getText(), items[i].getPosition().x, items[i].getPosition().y);
            }
        }
    }


    public void setup(float x, float y, StringBuilder text) {
        for (int i = 0; i < items.length; i++) {
            if (!items[i].isActive()) {
                items[i].setup(x, y, text);
                break;
            }
        }
    }

    public void update(float dt) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].isActive()) {
                items[i].update(dt);
            }

        }
    }
}
