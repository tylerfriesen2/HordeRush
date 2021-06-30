package core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import core.actions.ActionHandler;
import core.screens.MainMenuScreen;

import java.util.HashMap;

public class GameClass extends Game {
	private static AssetManager assetManager;
	private static HashMap<String, String> assets;
	private SpriteBatch[] batches;
	private FitViewport viewport;
	private static ActionHandler actionHandler;
	private static FreeTypeFontGenerator fontGenerator;

	@Override
	public void create() {
		viewport = new FitViewport(640, 480);
		batches = new SpriteBatch[3];
		assetManager = new AssetManager();
		assets = new HashMap<>();
		actionHandler = new ActionHandler();

		for (int i = 0; i < batches.length; ++i) {
			batches[i] = new SpriteBatch();
		}

		loadAssets();

		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void dispose() {
		for (SpriteBatch b : batches) {
			b.dispose();
		}
		assets.clear();
		assetManager.dispose();
		fontGenerator.dispose();
	}

	private void loadAssets() {
		assets.put("player", "player/player.png");
		assetManager.load(assets.get("player"), Texture.class);
		assets.put("bullet", "objects/bullet.png");
		assetManager.load(assets.get("bullet"), Texture.class);
		assets.put("zombie", "enemies/zombie-down.png");
		assetManager.load(assets.get("zombie"), Texture.class);
		assets.put("healthbar", "objects/healthbar.png");
		assetManager.load(assets.get("healthbar"), Texture.class);
		assets.put("pistolammo", "objects/pistolammo.png");
		assetManager.load(assets.get("pistolammo"), Texture.class);
		assets.put("rifleammo", "objects/rifleammo.png");
		assetManager.load(assets.get("rifleammo"), Texture.class);
		assets.put("shotgunammo", "objects/shotgunammo.png");
		assetManager.load(assets.get("shotgunammo"), Texture.class);

		// Generate fonts
		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Reckoner.ttf"));
	}

	public static AssetManager getAssetManager() {
		return assetManager;
	}

	public static HashMap<String, String> getAssets() {
		return assets;
	}

	public SpriteBatch getBatch(int index) {
		return batches[index];
	}

	public Viewport getViewport() { return viewport; }

	public static ActionHandler getActionHandler() {
		return actionHandler;
	}

	public static FreeTypeFontGenerator getFontGenerator() {
		return fontGenerator;
	}
}