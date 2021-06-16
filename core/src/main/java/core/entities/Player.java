package core.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import core.GameClass;

public class Player extends Entity {

    public Player() {
        if (GameClass.getAssetManager().isLoaded(GameClass.getAssets().get("player"), Texture.class)) {
            setSprite(new Sprite(GameClass.getAssetManager().get(GameClass.getAssets().get("player"), Texture.class)));
        } else {
            setSprite(new Sprite(new Texture(GameClass.getAssets().get("player"))));
        }
    }

    public void update(float xvel, float yvel) {
        sprite.translate(xvel, yvel);
    }
}
