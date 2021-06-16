package core.weapons;

import core.projectiles.Projectile;

import java.util.ArrayList;

public abstract class RangedWeapon extends Weapon {

    protected long cooldown = 1000, lifetime = 1000;
    protected float damage = 1.0f, vel = 3.0f;

    public long getCooldown() {
        return cooldown;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }

    public long getLifetime() {
        return lifetime;
    }

    public void setLifetime(long lifetime) {
        this.lifetime = lifetime;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getVel() {
        return vel;
    }

    public void setVel(float vel) {
        this.vel = vel;
    }

    abstract public void fire(ArrayList<Projectile> projectiles, float theta, float playerx, float playery, float spawndist);

}
