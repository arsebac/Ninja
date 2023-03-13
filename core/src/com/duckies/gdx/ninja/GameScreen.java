package com.duckies.gdx.ninja;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.duckies.gdx.ninja.pojo.PlayerInstance;
import com.duckies.gdx.ninja.pojo.Warp;
import com.duckies.gdx.ninja.progressbar.HealthBar;
import com.duckies.gdx.ninja.progressbar.LoadingBarWithBorders;
import com.duckies.gdx.ninja.saving.GameObjectPersistence;

public class GameScreen implements InputProcessor, Screen {
    private final NinjaGame game;

    private final TiledMapWrapper tileMapWrapper;

    private final PlayerInstance playerInstance;

    OrthographicCamera camera;

    SpriteBatch sb;

    private DebugTile debugTile;

    private final Player player;

    private final Player pnj;

    private final Stage stage;

    private HealthBar healthBar;

    private LoadingBarWithBorders loadingBarWithBorders;

    private long lastUpdate = 0L;

    public GameScreen(NinjaGame game) {
        this(game, new PlayerInstance());
    }
    public GameScreen(NinjaGame game, PlayerInstance p) {
        this.playerInstance = p;
        this.game = game;
        stage = new Stage();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();

        tileMapWrapper = new TiledMapWrapper(p.getCurrentMapName());


        float x = playerInstance.getPositionX() * tileMapWrapper.getTileWidth();
        float y = playerInstance.getPositionY() * tileMapWrapper.getTileHeight();

        sb = new SpriteBatch();

        Gdx.input.setInputProcessor(this);

        player = new Player(SpritesEnum.SAM, tileMapWrapper);
        pnj = new Player(SpritesEnum.SHANE, tileMapWrapper);

        addActors();

        tileMapWrapper.getObjectLayerObjects().add(pnj.createTextureMapObject(w / 2, h / 2));

        // Rotate camera in order to let her
        tileMapWrapper.getObjectLayerObjects().add(player.createTextureMapObject(x,y));
        // Place camera with player centered
        camera.translate(x - camera.position.x, y - camera.position.y);
    }

    private void addActors() {
        addHealthBarActor();
        addLoadingBarActor();
        addDebugTileActor();
    }

    private void addHealthBarActor() {
        healthBar = new HealthBar(100, 10);
        healthBar.setPosition(30, 30);
        stage.addActor(healthBar);
    }

    private void addLoadingBarActor() {
        loadingBarWithBorders = new LoadingBarWithBorders(100, 20);
        loadingBarWithBorders.setPosition(30, 5);
        stage.addActor(loadingBarWithBorders);
    }

    private void addDebugTileActor() {
        debugTile = new DebugTile(tileMapWrapper, player);
        stage.addActor(debugTile);
    }

    @Override
    public void render(float delta) {

        sb.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateCharacterPositionAndTexture();

        updatePnjsPositions();

        camera.update();
        tileMapWrapper.getTiledMapRenderer().setView(camera);


        sb.begin();

        tileMapWrapper.getTiledMapRenderer().render();

        sb.end();


        if (System.currentTimeMillis() - lastUpdate > TimeUnit.SECONDS.toMillis(5)) {
            healthBar.setValue(healthBar.getValue() - healthBar.getStepSize());
            loadingBarWithBorders.setValue(loadingBarWithBorders.getValue() + 0.1f);
            lastUpdate = System.currentTimeMillis();
        }

        stage.draw();
        stage.act();
    }

    private void updatePnjsPositions() {
        int item = (int) ((System.currentTimeMillis() % 4000) / 1000);
        pnj.moveIfPossible(Collections.singleton(DirectionEnum.values()[item]));
    }

    private void updateCharacterPositionAndTexture() {
        Set<DirectionEnum> directionAsked = Arrays.stream(DirectionEnum.values())
                .filter(direction -> Gdx.input.isKeyPressed(direction.getKey()))
                .collect(Collectors.toSet());

        Vector2 translation = player.moveIfPossible(directionAsked);

        if (translation.x != 0f || translation.y != 0f) {
            // We need to update camera position
            camera.translate(translation.x, translation.y);

            Warp warp = tileMapWrapper.getWarp(player.getTileCellX(), player.getTileCellY());

            if (System.currentTimeMillis() - playerInstance.getLastSave() > 10000) {
                playerInstance.setLastSave(System.currentTimeMillis());
                GameObjectPersistence.save(playerInstance.getId(), playerInstance);
            }

            if (warp != null) {
                System.out.println("doWarpMap " + warp.getArrivalMap());
                tileMapWrapper.doWarpMap(warp);
                playerInstance.setPosition(warp.getArrivalCoors());
                playerInstance.setCurrentMapName(warp.getArrivalMap() + ".tmx");

                game.setScreen(new GameScreen(game, playerInstance));
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.NUM_3) debugTile.switchVisibility();
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        // Compute difference between middle of the screen and touch point

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        Vector2 diffWithMiddleOfScreen = new Vector2(screenX - w / 2, (h - screenY) - h / 2);

        double distance = Math.sqrt(diffWithMiddleOfScreen.x * diffWithMiddleOfScreen.x + diffWithMiddleOfScreen.y + diffWithMiddleOfScreen.y);
        if (distance > 100) {
            System.out.println("Too far : " + distance);
            return false;
        }

        int newX = (int) (player.getX() + diffWithMiddleOfScreen.x) / 16;
        int newY = (int) (player.getY() + diffWithMiddleOfScreen.y) / 16;


        // Remove tiles
        tileMapWrapper.removeCellTile(newX, newY);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
        stage.dispose();
        tileMapWrapper.dispose();
    }
}