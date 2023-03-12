package com.duckies.gdx.ninja;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.duckies.gdx.ninja.pojo.PlayerInstance;
import com.duckies.gdx.ninja.saving.GameObjectPersistence;

public class NewGameScreen implements Screen {
	private final NinjaGame game;
	private final Stage stage;
	private final Skin skin;


	public NewGameScreen(NinjaGame game) {
		this.game = game;

		OrthographicCamera camera = new OrthographicCamera();
		camera.setToOrtho(false, 1024, 720);

		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		stage = new Stage();

		addActors();

		Gdx.input.setInputProcessor(stage);
	}

	private void createNewGame(String userName) {
		PlayerInstance playerInstance = new PlayerInstance();
		playerInstance.setName(userName);

		GameObjectPersistence.save(new Random().nextInt(), playerInstance);


		game.setScreen(new GameScreen(game, playerInstance));
		dispose();
	}

	private boolean createNewGameOnClick(TextField usernameTextField, Event event) {
		if (!(event instanceof InputEvent e) || e.getType() != InputEvent.Type.touchDown) {
			return false;
		}

		createNewGame(usernameTextField.getText());
		return false;
	}

	private boolean createNewGameOnEnter(TextField usernameTextField, Event event) {
		if (!(event instanceof InputEvent e) || InputEvent.Type.keyTyped != e.getType()) {
			return false;
		}

		if (e.getCharacter() == '\n') {
			String text = usernameTextField.getText();
			createNewGame(text);
		}
		return false;
	}

	private void addActors() {
		addLabelActor();
		TextField textField = addTextFieldActor();
		addButtonActor(textField);
	}

	private void addButtonActor(TextField textField) {
		Button button = new Button(skin);
		button.setName("Test");
		button.setPosition(720, 100);
		button.setWidth(100);
		button.setHeight(100);
		button.addCaptureListener(event -> createNewGameOnClick(textField, event));

		stage.addActor(button);            // <-- Actor now on stage

	}

	private TextField addTextFieldActor() {
		TextField usernameTextField = new TextField("", skin);
		usernameTextField.setPosition(200, 100);
		usernameTextField.setSize(500, 100);
		usernameTextField.addCaptureListener(event -> createNewGameOnEnter(usernameTextField, event));

		stage.addActor(usernameTextField);            // <-- Actor now on stage
		return usernameTextField;
	}

	private void addLabelActor() {
		Label label = new Label("Enter username\n\n", skin);
		label.setAlignment(Align.center);
		label.setFontScale(5);
		stage.addActor(label);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}

	@Override
	public void show() {
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

}
