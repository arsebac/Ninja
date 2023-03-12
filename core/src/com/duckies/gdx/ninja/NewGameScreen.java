package com.duckies.gdx.ninja;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.ScreenUtils;

public class NewGameScreen implements Screen {
	private final NinjaGame game;
	private final Stage stage;
	private OrthographicCamera camera;


	public NewGameScreen(NinjaGame game) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1024, 720);
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		stage = new Stage();

		TextField usernameTextField = new TextField("Enter username : ", skin);
		usernameTextField.setPosition(200, 100);
		usernameTextField.setSize(500, 100);

		stage.addActor(usernameTextField);            // <-- Actor now on stage

		Button button = new Button(skin);
		button.setName("Test");
		button.setPosition(720, 100);
		button.setWidth(100);
		button.setHeight(100);
		button.addCaptureListener(event -> {
			System.out.println("button.addCaptureListener"); // TODO process event enter:
			return false;
		});

		stage.addActor(button);
		Gdx.input.setInputProcessor(stage);
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

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}
}
