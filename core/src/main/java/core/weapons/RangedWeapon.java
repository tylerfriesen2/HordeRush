package core.weapons;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Timer;
import core.GameClass;
import core.actions.DelayAction;
import core.actions.RunnableAction;
import core.projectiles.Projectile;

import java.util.ArrayList;

public abstract class RangedWeapon extends Weapon {

    protected String name = "";
    protected long cooldown = 1000, lifetime = 1000;
    protected float damage = 1.0f, vel = 3.0f, reloadtime = 1.0f;
    protected int ammo = 10, magazine = 10, rounds = 0;
    boolean reloading = false, semi = false;

    public void reload() {
        if (rounds > 0) {
            reloading = true;
            GameClass.getActionHandler().addAction(new DelayAction(reloadtime));
            GameClass.getActionHandler().addAction(new RunnableAction(new Runnable() {
                @Override
                public void run() {
                    int reloadamount = Math.min(magazine - ammo, rounds);
                    ammo += reloadamount;
                    rounds -= reloadamount;

                    reloading = false;
                }
            }));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public float getReloadtime() {
        return reloadtime;
    }

    public void setReloadtime(float reloadtime) {
        this.reloadtime = reloadtime;
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

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public int getAmmo() {
        return ammo;
    }

    public void setMagazine(int magazine) {
        this.magazine = magazine;
    }

    public int getMagazine() {
        return magazine;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public void setReloading(boolean reloading) {
        this.reloading = reloading;
    }

    public boolean isReloading() {
        return reloading;
    }

    public boolean isSemi() {
        return semi;
    }

    public void setSemi(boolean semi) {
        this.semi = semi;
    }

    abstract public boolean fire(ArrayList<Projectile> projectiles, float theta, float playerx, float playery, float spawndist);

}
