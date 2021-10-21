package core.entities;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import core.GameClass;
import core.ref.Ref;

public class Player extends Entity {

    private float health = 20.0f, maxHealth = 20.0f;
    private final Rectangle damageRectangle;

    Texture[] textures;

    public Player() {
        textures = new Texture[4];
        if (GameClass.getAssetManager().isLoaded(GameClass.getAssets().get("up"), Texture.class)) {
            textures[0] = GameClass.getAssetManager().get(GameClass.getAssets().get("up"), Texture.class);
        } else {
            textures[0] = new Texture(GameClass.getAssets().get("up"));
        }

        if (GameClass.getAssetManager().isLoaded(GameClass.getAssets().get("down"), Texture.class)) {
            textures[1] = GameClass.getAssetManager().get(GameClass.getAssets().get("down"), Texture.class);
        } else {
            textures[1] = new Texture(GameClass.getAssets().get("down"));
        }

        if (GameClass.getAssetManager().isLoaded(GameClass.getAssets().get("left"), Texture.class)) {
            textures[2] = GameClass.getAssetManager().get(GameClass.getAssets().get("left"), Texture.class);
        } else {
            textures[2] = new Texture(GameClass.getAssets().get("left"));
        }

        if (GameClass.getAssetManager().isLoaded(GameClass.getAssets().get("right"), Texture.class)) {
            textures[3] = GameClass.getAssetManager().get(GameClass.getAssets().get("right"), Texture.class);
        } else {
            textures[3] = new Texture(GameClass.getAssets().get("right"));
        }

        setSprite(new Sprite(textures[1]));

        damageRectangle = new Rectangle();
    }

    public void update(Ref.Direction direction, float xvel, float yvel) {
        sprite.translate(xvel, yvel);
        switch (direction) {
            case UP:
                sprite.setTexture(textures[0]);
                break;
            case DOWN:
                sprite.setTexture(textures[1]);
                break;
            case LEFT:
                sprite.setTexture(textures[2]);
                break;
            case RIGHT:
                sprite.setTexture(textures[3]);
                break;
            default: break;
        }
    }

    public void damage(float amount) {
        health -= amount;
        sprite.setColor(Color.RED);
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = MathUtils.clamp(health, 0, maxHealth);
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    @Override
    public Rectangle getCollisionRectangle() {
        return sprite.getBoundingRectangle();
    }

    @Override
    public Rectangle getDamageRectangle() {
        Rectangle rectangle = sprite.getBoundingRectangle();
        rectangle.setWidth(rectangle.getWidth() - 4);
        rectangle.setHeight(rectangle.getHeight() - 4);
        rectangle.setPosition(rectangle.getX() + 2, rectangle.getY() + 2);
        return rectangle;
    }

}
