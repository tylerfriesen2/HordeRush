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

		assets.put("up", "player/up.png");
		assetManager.load(assets.get("up"), Texture.class);

		assets.put("down", "player/down.png");
		assetManager.load(assets.get("down"), Texture.class);

		assets.put("left", "player/left.png");
		assetManager.load(assets.get("left"), Texture.class);

		assets.put("right", "player/right.png");
		assetManager.load(assets.get("right"), Texture.class);

		assets.put("bullet", "objects/bullet.png");
		assetManager.load(assets.get("bullet"), Texture.class);

		assets.put("arrow", "objects/arrow.png");
		assetManager.load(assets.get("arrow"), Texture.class);

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

		assets.put("bowammo", "objects/bowammo.png");
		assetManager.load(assets.get("bowammo"), Texture.class);

		assets.put("pistolcursor", "interface/pistolcursor.png");
		assetManager.load(assets.get("pistolcursor"), Texture.class);

		assets.put("shotguncursor", "interface/shotguncursor.png");
		assetManager.load(assets.get("shotguncursor"), Texture.class);

		assets.put("riflecursor", "interface/riflecursor.png");
		assetManager.load(assets.get("riflecursor"), Texture.class);

		assets.put("bowcursor", "interface/bowcursor.png");
		assetManager.load(assets.get("bowcursor"), Texture.class);

		assets.put("healthpack", "objects/healthpack.png");
		assetManager.load(assets.get("healthpack"), Texture.class);

		assetManager.finishLoading();

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