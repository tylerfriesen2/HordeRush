package core.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;
import core.GameClass;
import core.entities.Entity;

public class Enemy extends Entity {

    protected float health = 1, maxhealth = 1, stateTime = 0, theta = 0, vel = 0.75f, damage = 1.0f;
    protected boolean alive = true, dangerous = true, disposable = false;
    protected Sprite healthbar;

    public Enemy() {
        super();

        if (GameClass.getAssetManager().isLoaded(GameClass.getAssets().get("healthbar"), Texture.class)) {
            healthbar = new Sprite(GameClass.getAssetManager().get(GameClass.getAssets().get("healthbar"), Texture.class));
        } else {
            healthbar = new Sprite(new Texture(GameClass.getAssets().get("healthbar")));
        }

        healthbar.setOrigin(0, healthbar.getOriginY());
    }


    public void update(float delta, float playerx, float playery) {
        stateTime += delta;
        alive = health > 0;

        float anglex = playerx - getX(), angley = playery - getY();
        theta = (float) Math.atan2(angley, anglex);

        healthbar.setPosition(sprite.getX(), sprite.getY() + sprite.getHeight() + 4);

        if (health <= 0) { die(); }
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);

        if (health < maxhealth) {
            float scale = health / maxhealth * sprite.getWidth();
            healthbar.setScale(MathUtils.clamp(scale, 0, sprite.getWidth()), 2);
            healthbar.draw(batch);
        }
    }

    public void die() {
        setDisposable(true);
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getMaxhealth() {
        return maxhealth;
    }

    public void setMaxhealth(float maxhealth) {
        this.maxhealth = maxhealth;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void damage(float amount) {
        health -= amount;
        sprite.setColor(new Color(1.0f, 0.5f, 0.5f, 1.0f));
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                sprite.setColor(Color.WHITE);
            }
        }, 0.25f);
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public float getTheta() {
        return theta;
    }

    public void setTheta(float theta) {
        this.theta = theta;
    }

    public float getVel() {
        return vel;
    }

    public void setVel(float vel) {
        this.vel = vel;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isDangerous() {
        return dangerous;
    }

    public void setDangerous(boolean dangerous) {
        this.dangerous = dangerous;
    }

    public boolean isDisposable() {
        return disposable;
    }

    public void setDisposable(boolean disposable) {
        this.disposable = disposable;
    }
}
