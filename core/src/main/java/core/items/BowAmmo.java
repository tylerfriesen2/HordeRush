package core.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import core.GameClass;

public class BowAmmo extends Item {

    public BowAmmo(int quantity) {
        super();
        setQuantity(quantity);
    }

    @Override
    public void loadSprite() {
        if (GameClass.getAssetManager().isLoaded(GameClass.getAssets().get("bowammo"), Texture.class)) {
            sprite = new Sprite(GameClass.getAssetManager().get(GameClass.getAssets().get("bowammo"), Texture.class));
        } else {
            sprite = new Sprite(new Texture(GameClass.getAssets().get("bowammo")));
        }
    }

}
