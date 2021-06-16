package core.entities;

public class Enemy extends Entity {

    protected float health = 1, stateTime = 0, theta = 0, vel = 0.75f;
    protected boolean alive = true, dangerous = true;

    public void update(float delta, float playerx, float playery) {
        stateTime += delta;
        alive = health > 0;

        float anglex = playerx - getX(), angley = playery - getY();
        theta = (float) Math.atan2(angley, anglex);
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public void damage(float amount) {
        health -= amount;
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

    public void setDangerous(boolean dangerous) {
        this.dangerous = dangerous;
    }

    public boolean isDangerous() {
        return dangerous;
    }
}
