package core.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import core.GameClass;

public class Bullet extends Projectile {

    public Bullet(float theta, float x, float y, float vel, long lifetime, float damage) {
        super();
        this.vel = vel;
        this.theta = theta;
        this.lifetime = lifetime;
        setDamage(damage);

        loadTexture();
        sprite.setOriginCenter();
        sprite.setPosition(x - 4, y - 4);
    }

    public void loadTexture() {
        if (GameClass.getAssetManager().isLoaded(GameClass.getAssets().get("bullet"), Texture.class)) {
            sprite = new Sprite(GameClass.getAssetManager().get(GameClass.getAssets().get("bullet"), Texture.class));
        } else {
            sprite = new Sprite(new Texture(GameClass.getAssets().get("bullet")));
        }
    }

}
