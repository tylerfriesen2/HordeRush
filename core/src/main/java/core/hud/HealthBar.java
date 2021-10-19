package core.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import core.GameClass;
import core.entities.Player;

public class HealthBar {

    private float x = 0, y = 0;
    private Sprite healthbar;
    private final BitmapFont healthFont;
    private String healthString = "";

    public HealthBar(float x, float y) {
        this.x = x;
        this.y = y;

        if (GameClass.getAssetManager().isLoaded(GameClass.getAssets().get("healthbar"), Texture.class)) {
            healthbar = new Sprite(GameClass.getAssetManager().get(GameClass.getAssets().get("healthbar"), Texture.class));
        } else {
            healthbar = new Sprite(new Texture("objects/healthbar.png"));
        }

        healthbar.setOrigin(healthbar.getWidth(), 0);
        healthbar.setPosition(x, y);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        parameter.spaceX = 2;
        healthFont = GameClass.getFontGenerator().generateFont(parameter);
    }

    public void update(Player player) {
        float percentage = player.getHealth() / player.getMaxHealth();
        float width = 96 * percentage;
        healthbar.setScale(width, 8);
        healthString = "H: " + player.getHealth() + " / " + player.getMaxHealth();
    }

    public void draw(SpriteBatch batch) {
        healthbar.draw(batch);
        GlyphLayout healthlayout = new GlyphLayout(healthFont, healthString);
        healthFont.draw(batch, healthlayout, x - healthlayout.width, y + 20);
    }

}
