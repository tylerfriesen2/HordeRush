package core.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import core.GameClass;

public abstract class Item {

    protected Sprite sprite;
    protected BitmapFont font;
    protected int quantity = 1;

    public Item() {
        loadSprite();
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 10;
        parameter.borderWidth = 2;
        parameter.borderColor = Color.BLACK;
        font = GameClass.getFontGenerator().generateFont(parameter);
    }

    public abstract void loadSprite();

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
        font.draw(batch, String.valueOf(quantity), sprite.getX(), sprite.getY());
    }

    public Rectangle getBoundingRectangle() {
        return sprite.getBoundingRectangle();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPosition(float x, float y) { sprite.setPosition(x, y); }

}
