package core.weapons;

import core.projectiles.Bullet;
import core.projectiles.Projectile;

import java.util.ArrayList;

public class Shotgun extends RangedWeapon {

    private int spreadcount = 10;

    public Shotgun() {
        setName("Shotgun");
        setCooldown(750);
        setLifetime(400);
        setDamage(0.6f);
        setVel(9.5f);
        setAmmo(12);
        setMagazine(12);
        setReloadtime(2.0f);
        setSemi(true);
    }

    @Override
    public boolean fire(ArrayList<Projectile> projectiles, float theta, float playerx, float playery, float spawndist) {
        if (getAmmo() > 0 && !isReloading()) {
            for (int i = 0; i < spreadcount; i++) {
                float variance = (float) Math.toRadians((Math.floor(Math.random() * 10) - 5));
                float spawnx = playerx + (float) (spawndist * Math.cos(theta + variance)), spawny = playery + (float) (spawndist * Math.sin(theta + variance));
                projectiles.add(new Bullet(theta + variance, spawnx, spawny, getVel(), getLifetime(), getDamage()));
            }
            setAmmo(getAmmo() - 1);
            return true;
        }

        return false;
    }

    public int getSpreadcount() {
        return spreadcount;
    }

    public void setSpreadcount(int spreadcount) {
        this.spreadcount = spreadcount;
    }
}
