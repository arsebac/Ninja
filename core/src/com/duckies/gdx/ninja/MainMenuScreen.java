package com.duckies.gdx.ninja;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen, InputProcessor {

    final NinjaGame game;
    private final TextureRegion textureRegion;

    OrthographicCamera camera;

    public MainMenuScreen(final NinjaGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);


        Texture menu = new Texture(Gdx.files.internal("menu.png"));
        textureRegion = new TextureRegion(menu, 0,186, 295, 60);

        Gdx.input.setInputProcessor(this);

    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Welcome to Ninja!!! ", 300, 10);
        float factor = 2.7f;
        game.batch.draw(textureRegion, 0,0, 295 * factor, 60* factor);
        game.batch.end();


    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        System.out.println("(" + screenX +"," + screenY + ")");

        if (screenY < 247) {
            return false;
        }

        if (screenX < 251) {
            System.out.println("New Game");
            game.setScreen(new NewGameScreen(game));
            dispose();
        } else if (screenX < 508) {

            game.setScreen(new LoadGameScreen(game));
            dispose();
            System.out.println("Load Game");
        } else if (screenX < 765) {

            System.out.println("Coop");
            game.setScreen(new GameScreen(game));
        } else {
            System.out.println("Exit");
            System.exit(0);

        }

        return false;
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

    @Override
    public void dispose() {

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
