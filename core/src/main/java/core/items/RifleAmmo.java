package core.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import core.GameClass;

public class RifleAmmo extends Item {

    public RifleAmmo(int quantity) {
        super();
        setQuantity(quantity);
    }

    @Override
    public void loadSprite() {
        if (GameClass.getAssetManager().isLoaded(GameClass.getAssets().get("rifleammo"), Texture.class)) {
            sprite = new Sprite(GameClass.getAssetManager().get(GameClass.getAssets().get("rifleammo"), Texture.class));
        } else {
            sprite = new Sprite(new Texture(GameClass.getAssets().get("rifleammo")));
        }
    }
}
