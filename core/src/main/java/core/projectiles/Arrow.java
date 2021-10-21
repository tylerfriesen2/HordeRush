package core.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import core.GameClass;

public class Arrow extends Projectile {

    public Arrow(float theta, float x, float y, float vel, long lifetime, float damage) {
        super();
        this.vel = vel;
        this.theta = theta;
        this.lifetime = lifetime;
        setDamage(damage);

        loadTexture();
        sprite.setOrigin(0 ,2);
        sprite.setPosition(x, y);
        sprite.setRotation((float) Math.toDegrees(theta));
    }

    public void loadTexture() {
        if (GameClass.getAssetManager().isLoaded(GameClass.getAssets().get("arrow"), Texture.class)) {
            sprite = new Sprite(GameClass.getAssetManager().get(GameClass.getAssets().get("arrow"), Texture.class));
        } else {
            sprite = new Sprite(new Texture(GameClass.getAssets().get("arrow")));
        }
    }

}
