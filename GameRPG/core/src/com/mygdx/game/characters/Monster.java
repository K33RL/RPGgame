package com.mygdx.game.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Items.Weapon;

public class Monster extends GameCharacter {
    private Vector2 direction;
    private float activityRadius;
    private float moveTimer;


    public Monster(GameScreen gameScreen) {
        this.texture = new Texture("fireS.png");
        this.regions = new TextureRegion(texture).split(96,112)[0];
        textureHp = new Texture("Bar.png");

        this.position = new Vector2(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        while (!gameScreen.getMap().isCellOPassable(position)) {
            this.position = new Vector2(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        }

        this.direction = new Vector2(0, 0);
        this.secondsPerFrame = 0.2f;
        this.speed = 50.0f;
        this.activityRadius = 200.0f;
        this.gameScreen = gameScreen;
        this.hpMax = 50;
        this.hp = this.hpMax;
        this.weapon = new Weapon("Wooden Sword", 50.f, 0.8f, 5.0f);
    }


    @Override
    public void update(float dt) {

        damageEffectTimer -= dt;
        animationTimer += dt;


        if (damageEffectTimer < 0.0f) {
            damageEffectTimer = 0.0f;
        }

        float dst = gameScreen.getHero().getPosition().dst(this.position);
        if (dst < activityRadius) {
            direction.set(gameScreen.getHero().getPosition()).sub(this.position).nor();
        } else {
            moveTimer -= dt;
            if (moveTimer < 0.0f) {
                moveTimer = MathUtils.random(1.0f, 4.0f);
                direction.set(MathUtils.random(-1.0f, 1.0f), MathUtils.random(-1.0f, 1.0f));
                direction.nor();
            }
        }

        temp.set(position).mulAdd(direction, speed * dt);
        if (gameScreen.getMap().isCellOPassable(temp)) {
            position.set(temp);
        }

      //  moveForward(dt);

        if (dst < weapon.getAttackRadius()) {
            attackTimer += dt;
            if (attackTimer >= weapon.getAttackPeriod()) {
                attackTimer = 0.0f;
                gameScreen.getHero().takeDamage(weapon.getDamage());
            }
        }
        checkScreenBounce();
    }
}