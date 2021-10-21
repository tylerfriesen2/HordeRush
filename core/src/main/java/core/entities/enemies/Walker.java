package core.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import core.GameClass;
import core.utils.Point;

public class Walker extends Enemy {

    private static final int FRAME_COLS = 2, FRAME_ROWS = 1;

    float dragvel = 0.1f;

    Animation<TextureRegion> animation;
    Texture downTexture;

    public Walker(float x, float y) {
        super();

        setVel(0.65f);
        setMaxhealth(2.0f);
        setHealth(getMaxhealth());
        setDamage(2.0f);
        if (GameClass.getAssetManager().isLoaded(GameClass.getAssets().get("zombie"), Texture.class)) {
            downTexture = GameClass.getAssetManager().get(GameClass.getAssets().get("zombie"), Texture.class);
        } else {
            downTexture = new Texture(GameClass.getAssets().get("zombie"));
        }

        TextureRegion[][] tmp = TextureRegion.split(downTexture, downTexture.getWidth() / FRAME_COLS, downTexture.getHeight() / FRAME_ROWS);
        TextureRegion[] downFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; ++i) {
            for (int j = 0; j < FRAME_COLS; ++j) {
                downFrames[index++] = tmp[i][j];
            }
        }

        animation = new Animation<>(0.5f, downFrames);
        sprite = new Sprite(animation.getKeyFrame(getStateTime()));

        setPosition(x, y);
    }

    public void update(float delta, float playerx, float playery) {
        super.update(delta, playerx, playery);

        // Get distance to player
        float dist = Point.distance(new Point(sprite.getX(), sprite.getY()), new Point(playerx, playery));

        // Check if this enemy should be aggro
        if (dist < 200 || damaged) {
            // Update animation
            sprite.setRegion(animation.getKeyFrame(getStateTime(), true));

            // Update dragging speed
            if (dragvel >= getVel()) {
                dragvel = 0.075f;
            } else {
                dragvel *= 1.02f;
            }

            // Attack player
            sprite.translate(dragvel * (float) Math.cos(getTheta()), dragvel * (float) Math.sin(getTheta()));
        }
    }

    public Rectangle getCollisionRectangle() {
        return getSprite().getBoundingRectangle();
    }

    @Override
    public Rectangle getDamageRectangle() {
        return new Rectangle(getX() + 2 * getSprite().getScaleX(), getY() + 2 * getSprite().getScaleY(), getWidth() * getSprite().getScaleX() - 4 * getSprite().getScaleX(), getHeight() * getSprite().getScaleY() - 4 * getSprite().getScaleY());
    }
}
