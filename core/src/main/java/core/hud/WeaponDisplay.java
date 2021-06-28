package core.hud;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import core.GameClass;
import core.weapons.RangedWeapon;

public class WeaponDisplay {

    private float x = 0, y = 0;
    private int ammo = 0, rounds = 0;
    private BitmapFont nameFont, ammoFont, reloadingFont;
    private String name = "", ammoString = "";
    private boolean reloading = false;

    public WeaponDisplay(float x, float y) {
        this.x = x; this.y = y;
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.spaceX = 2;
        nameFont = GameClass.getFontGenerator().generateFont(parameter);

        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        ammoFont = GameClass.getFontGenerator().generateFont(parameter);

        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        reloadingFont = GameClass.getFontGenerator().generateFont(parameter);
    }

    public void update(RangedWeapon weapon) {
        name = weapon.getName();
        ammo = weapon.getAmmo();
        rounds = weapon.getRounds();
        ammoString = String.format("%d / %d", ammo, rounds);
        reloading = weapon.isReloading();
    }

    public void draw(SpriteBatch batch) {
        GlyphLayout ammolayout = new GlyphLayout(ammoFont, ammoString);
        ammoFont.draw(batch, ammolayout, x - ammolayout.width, y);

        GlyphLayout namelayout = new GlyphLayout(nameFont, name);
        nameFont.draw(batch, namelayout, x - namelayout.width, y + 16);

        if (reloading) {
            GlyphLayout reloadinglayout = new GlyphLayout(reloadingFont, "Reloading");
            reloadingFont.draw(batch, reloadinglayout, x - ammolayout.width - 8 - reloadinglayout.width, y - 2);
        }
    }

}
