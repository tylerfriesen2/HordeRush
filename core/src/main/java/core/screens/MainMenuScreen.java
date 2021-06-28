package core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import core.GameClass;

public class MainMenuScreen implements Screen {

	GameClass core;

	public MainMenuScreen(GameClass core) {
		this.core = core;
	}

	@Override
	public void show() {
		core.setScreen(new GameScreen(core));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		core.getBatch(2).begin();

		core.getBatch(2).end();
	}

	@Override
	public void resize(int width, int height) {
		// Resize your screen here. The parameters represent the new window size.
	}

	@Override
	public void pause() {
		// Invoked when your application is paused.
	}

	@Override
	public void resume() {
		// Invoked when your application is resumed after pause.
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {

	}
}