package com.duckies.gdx.ninja;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.duckies.gdx.ninja.pojo.PlayerInstance;
import com.duckies.gdx.ninja.saving.GameObjectPersistence;

public class LoadGameScreen implements Screen, InputProcessor {
	final NinjaGame game;

	private final Stage stage;

	private final Table container;

	private final List<PlayerInstance> playerInstances;


	public LoadGameScreen(NinjaGame game) {
		this.game = game;

		Gdx.input.setInputProcessor(this);


		playerInstances = GameObjectPersistence.loadAll(PlayerInstance.class);
		stage = new Stage();
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		container = new Table();
		stage.addActor(container);
		container.setFillParent(true);

		Table table = new Table();

		final ScrollPane scroll = new ScrollPane(table, skin);
		scroll.setScrollingDisabled(true,false);

		table.pad(5).defaults().expandX().space(4);

		table.row();
		Label l = new Label("Select save\n\n", skin);
		l.setAlignment(Align.center);
		l.setFontScale(5);
		table.add(l).width(300);

		int gameSaveId = 0;

		for (PlayerInstance playerInstance : playerInstances) {
			table.row();

			Label label=new Label("---------------\n[" + gameSaveId + "] " + playerInstance.getName() +
					"\n---------------\n\n", skin);
			label.setAlignment(Align.center);
			label.setWrap(true);
			table.add(label).width(Gdx.graphics.getWidth());


			gameSaveId++;
		}

		container.add(scroll).expand().fill();

	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO : add better touch element detection

		if (screenY < 435) {
			System.out.println("Touch too high to be a selection");
			return false;
		}
		int selectionId = (screenY - 435) / 125;
		System.out.println("Touch spotted : (" + screenX + "," + screenY +") => " + selectionId);

		PlayerInstance playerInstance = playerInstances.get(selectionId);

		if (playerInstance != null) {
			game.setScreen(new GameScreen(game, playerInstance));
			dispose();
		}
		return false;
	}


	@Override
	public void dispose() {
		stage.dispose();
	}


	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}
}
