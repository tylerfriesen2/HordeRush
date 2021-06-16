package core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import core.screens.MainMenuScreen;

import java.util.HashMap;

public class GameClass extends Game {
	private static AssetManager assetManager;
	private static HashMap<String, String> assets;
	private SpriteBatch batch;
	private FitViewport viewport;

	@Override
	public void create() {
		viewport = new FitViewport(640, 480);
		batch = new SpriteBatch();
		assetManager = new AssetManager();
		assets = new HashMap<>();

		loadAssets();

		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void dispose() {
		batch.dispose();
		assets.clear();
		assetManager.dispose();
	}

	private void loadAssets() {
		assets.put("player", "player/player.png");
		assetManager.load(assets.get("player"), Texture.class);
		assets.put("bullet", "objects/bullet.png");
		assetManager.load(assets.get("bullet"), Texture.class);
		assets.put("zombie", "enemies/zombie-down.png");
		assetManager.load(assets.get("zombie"), Texture.class);
	}

	public static AssetManager getAssetManager() {
		return assetManager;
	}

	public static HashMap<String, String> getAssets() {
		return assets;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public Viewport getViewport() { return viewport; }
}