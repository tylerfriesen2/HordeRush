package core.projectiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Projectile {

    protected Sprite sprite;
    float vel = 0, theta = 0, damage = 1.0f;
    long lifetime = 1000, spawn = 0;
    boolean alive = true;

    public Projectile() {
        spawn = System.currentTimeMillis();
    }

    public void update(float delta) {
        float alpha = 1 - (float) (System.currentTimeMillis() - spawn) / lifetime;

        sprite.setAlpha(MathUtils.clamp(alpha, 0, 1));
        sprite.translate((float) (vel * Math.cos(theta) * delta), (float) (vel * Math.sin(theta) * delta));

        if (System.currentTimeMillis() - spawn > lifetime) {
            alive = false;
        }
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public float getVel() {
        return vel;
    }

    public void setVel(float vel) {
        this.vel = vel;
    }

    public float getTheta() {
        return theta;
    }

    public void setTheta(float theta) {
        this.theta = theta;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public long getLifetime() {
        return lifetime;
    }

    public void setLifetime(long lifetime) {
        this.lifetime = lifetime;
    }

    public long getSpawn() {
        return spawn;
    }

    public void setSpawn(long spawn) {
        this.spawn = spawn;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Rectangle getBoundingRectangle() {
        return sprite.getBoundingRectangle();
    }

}
