package core.weapons;

import core.projectiles.Bullet;
import core.projectiles.Projectile;

import java.util.ArrayList;

public class Pistol extends RangedWeapon {

    public Pistol() {
        setName("Pistol");
        setCooldown(300);
        setDamage(1.5f);
        setLifetime(3000);
        setVel(300.0f);
        setAmmo(17);
        setMagazine(17);
        setRounds(42);
        setReloadtime(0.5f);
        setSemi(true);
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
