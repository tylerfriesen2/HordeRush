package core.weapons;

import core.projectiles.Arrow;
import core.projectiles.Bullet;
import core.projectiles.Projectile;

import java.util.ArrayList;

public class Bow extends RangedWeapon {

    public Bow() {
        setName("Bow");
        setCooldown(0);
        setDamage(5.0f);
        setLifetime(2000);
        setVel(4.0f);
        setAmmo(1);
        setMagazine(1);
        setRounds(50);
        setReloadtime(1.5f);
        setSemi(true);
    }

    @Override
    public boolean fire(ArrayList<Projectile> projectiles, float theta, float playerx, float playery, float spawndist) {
        if (getAmmo() > 0 && !isReloading()) {
            float spawnx = playerx + (float) (spawndist * Math.cos(theta)), spawny = playery + (float) (spawndist * Math.sin(theta));
            projectiles.add(new Arrow(theta, spawnx, spawny, getVel(), getLifetime(), getDamage()));
            setAmmo(getAmmo() - 1);
            return true;
        }

        return false;
    }
}
