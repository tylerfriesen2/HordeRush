package core.weapons;

import core.projectiles.Bullet;
import core.projectiles.Projectile;

import java.util.ArrayList;

public class Pistol extends RangedWeapon {

    public Pistol() {
        setCooldown(250);
        setDamage(1.0f);
        setLifetime(3000);
        setVel(5.0f);
    }

    @Override
    public void fire(ArrayList<Projectile> projectiles, float theta, float playerx, float playery, float spawndist) {
        float spawnx = playerx + (float) (spawndist * Math.cos(theta)), spawny = playery + (float) (spawndist * Math.sin(theta));
        projectiles.add(new Bullet(theta, spawnx, spawny, getVel(), getLifetime(), getDamage()));
    }
}
