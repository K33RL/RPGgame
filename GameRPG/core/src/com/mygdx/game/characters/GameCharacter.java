package com.mygdx.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Items.Weapon;

public abstract class GameCharacter {

    GameScreen gameScreen;
    Texture texture;
    Texture textureHp;
    Vector2 position;
    Vector2 direction;
    TextureRegion[] regions;
    float animationTimer;
    float secondsPerFrame;
    Sound sound;




    float speed;
    float hp, hpMax;
    float attackTimer;
    Weapon weapon;

    Vector2 temp;
    StringBuilder stringHelper;

    float damageEffectTimer;

    public boolean isAlive(){
        return hp>0;
    }

    public GameCharacter() {
        temp = new Vector2(0,0);
        stringHelper = new StringBuilder();
    }

    public void render(SpriteBatch batch , BitmapFont font32) {
        if (damageEffectTimer > 0.0f) {
            batch.setColor(1, 1 - damageEffectTimer, 1 - damageEffectTimer, 1);
        }

        int frameIndex = (int) (animationTimer / secondsPerFrame) % regions.length;

        batch.draw(regions[frameIndex], position.x - 40, position.y - 40);
        batch.setColor(1, 1, 1, 1);



        batch.setColor(0, 0, 0, 1);
        batch.draw(textureHp, position.x - 42, position.y + 80 - 42, 84, 16);
        batch.setColor(1, 0, 0, 1);
        batch.draw(textureHp, position.x - 40, position.y + 80 - 40, 0, 0, hp / hpMax * 80, 12, 1, 1, 0, 0, 0, 80, 20, false, false);
        batch.setColor(1, 1, 1, 1);
        stringHelper.setLength(0);
        stringHelper.append((int)hp);
        font32.draw(batch, String.valueOf((int)hp), position.x -40, position.y  +80 - 22, 80,1,false);

    }

    public Vector2 getPosition() {
        return position;
    }

    public abstract void update(float dt);


    public void checkScreenBounce() {
        if (position.x > 1280.0f) {
            position.x = 1280.0f;
        }
        if (position.x < 0.0f) {
            position.x = 0.0f;
        }
        if (position.y > 720.0f) {
            position.y = 720.0f;
        }
        if (position.y < 0.0f) {
            position.y = 0.0f;
        }
    }

    public void takeDamage( float amount) {
        hp -= amount;
        damageEffectTimer += 0.5f;
        if (damageEffectTimer > 1.0f) {
            damageEffectTimer = 1.0f;
        }
        sound = Gdx.audio.newSound(Gdx.files.internal("Sound_4.wav"));
        sound.setVolume(sound.play(),0.05f);
    }

    public void moveForward(float dt){
        if (gameScreen.getMap().isCellOPassable(temp.set(position).mulAdd(direction, speed * dt))) {
            position.set(temp);
        } else if (gameScreen.getMap().isCellOPassable(temp.set(position).mulAdd(direction, speed * dt).set(temp.x, position.y))) {
            position.set(temp);
        } else if (gameScreen.getMap().isCellOPassable(temp.set(position).mulAdd(direction, speed * dt).set(position.x, temp.y))) {
            position.set(temp);
        }
    }
}
