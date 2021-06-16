package core.weapons;

import core.projectiles.Bullet;
import core.projectiles.Projectile;

import java.util.ArrayList;

public class Shotgun extends RangedWeapon {

    private int spreadcount = 10;

    public Shotgun() {
        setCooldown(1000);
        setDamage(0.5f);
        setLifetime(500);
        setVel(7.5f);
    }

    @Override
    public void fire(ArrayList<Projectile> projectiles, float theta, float playerx, float playery, float spawndist) {
        for (int i = 0; i < spreadcount; i++) {
            float variance = (float) Math.toRadians((Math.floor(Math.random() * 10) - 5));
            float spawnx = playerx + (float) (spawndist * Math.cos(theta + variance)), spawny = playery + (float) (spawndist * Math.sin(theta + variance));
            projectiles.add(new Bullet(theta + variance, spawnx, spawny, getVel(), getLifetime(), getDamage()));
        }
    }

    public int getSpreadcount() {
        return spreadcount;
    }

    public void setSpreadcount(int spreadcount) {
        this.spreadcount = spreadcount;
    }
}
