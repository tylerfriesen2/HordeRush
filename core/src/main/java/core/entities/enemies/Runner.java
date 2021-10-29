package core.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import core.GameClass;
import core.utils.Point;

public class Runner extends Enemy {

    private static final int FRAME_COLS = 2, FRAME_ROWS = 1;

    float vel = 0.0f, x = 0.0f, A = 24.0f;

    Animation<TextureRegion> animation;
    Texture downTexture;

    public Runner(float x, float y) {
        super();

        setMaxVel(60.0f);
        setMaxHealth(2.0f);
        setHealth(getMaxhealth());
        setDamage(1.0f);
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

        animation = new Animation<>(0.35f, downFrames);
        sprite = new Sprite(animation.getKeyFrame(getStateTime()));

        setPosition(x, y);
    }

    public void update(float delta, float playerx, float playery) {
        super.update(delta, playerx, playery);

        // Get distance to player
        float dist = Point.distance(new Point(sprite.getX(), sprite.getY()), new Point(playerx, playery));

        // Check if this enemy should be aggro
        if (dist < 400) {
            aggro = true;
        } else if (dist >= 640) {
            aggro = false;
            x = (float) (3 * Math.PI / 4);
        }

        if (aggro) {
            // Update animation
            sprite.setRegion(animation.getKeyFrame(getStateTime(), true));

            // Update velocity
            x = x > 2.0f * Math.PI ? 0 : x + (float) Math.toRadians(1.0f);

            vel = A * (float) Math.sin(x * 0.5f) + (getMaxVel() - A);

            // Attack player
            sprite.translate(vel * (float) Math.cos(getTheta()) * delta, vel * (float) Math.sin(getTheta()) * delta);
        }
    }
}
