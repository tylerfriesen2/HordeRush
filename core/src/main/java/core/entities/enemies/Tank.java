package core.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import core.GameClass;
import core.utils.Point;

public class Tank extends Enemy {

    private static final int FRAME_COLS = 2, FRAME_ROWS = 1;

    float vel = 0.25f;

    Animation<TextureRegion> animation;
    Texture downTexture;

    public Tank(float x, float y) {
        super();

        setVel(0.25f);
        setMaxhealth(10.0f);
        setHealth(getMaxhealth());
        setDamage(5.0f);
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

        animation = new Animation<>(0.65f, downFrames);
        sprite = new Sprite(animation.getKeyFrame(getStateTime()));
        sprite.setScale(1.5f);

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

            // Update running speed
            if (vel < getVel()) {
                vel *= 1.005f;
            } else {
                vel = getVel();
            }

            // Attack player
            sprite.translate(vel * (float) Math.cos(getTheta()), vel * (float) Math.sin(getTheta()));
        }
    }
}
