package core.weapons;

import core.projectiles.Bullet;
import core.projectiles.Projectile;

import java.util.ArrayList;

public class Rifle extends RangedWeapon {

    public Rifle() {
        setName("Rifle");
        setCooldown(100);
        setDamage(0.6f);
        setLifetime(3000);
        setVel(7.5f);
        setAmmo(32);
        setMagazine(32);
        setReloadtime(1.0f);
        setSemi(false);
    }

    @Override
    public boolean fire(ArrayList<Projectile> projectiles, float theta, float playerx, float playery, float spawndist) {
        if (getAmmo() > 0 && !isReloading()) {
            float spawnx = playerx + (float) (spawndist * Math.cos(theta)), spawny = playery + (float) (spawndist * Math.sin(theta));
            projectiles.add(new Bullet(theta, spawnx, spawny, getVel(), getLifetime(), getDamage()));
            setAmmo(getAmmo() - 1);
            return true;
        }

        return false;
    }
}
