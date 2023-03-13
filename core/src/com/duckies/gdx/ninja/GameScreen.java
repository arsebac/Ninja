package com.duckies.gdx.ninja;

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
import com.duckies.gdx.ninja.actor.TilemapActor;
import com.duckies.gdx.ninja.gui.Box2DWorld;
import com.duckies.gdx.ninja.gui.Control;
import com.duckies.gdx.ninja.gui.SquareMenu;
import com.duckies.gdx.ninja.pojo.PlayerInstance;
import com.duckies.gdx.ninja.progressbar.HealthBar;
import com.duckies.gdx.ninja.progressbar.LoadingBarWithBorders;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class GameScreen implements InputProcessor, Screen {
    private final NinjaGame game;
    private final Box2DWorld box2D;
    private final SquareMenu squareMenu;
    TiledMap tiledMap;

    public OrthographicCamera camera;

    TiledMapRenderer tiledMapRenderer;

    SpriteBatch sb;

    MapLayer objectLayer;

    private DebugTile debugTile;

    private Player player;
    private Player pnj;

    private Stage stage;
    private HealthBar healthBar;
    private LoadingBarWithBorders loadingBarWithBorders;
    private long lastUpdate = 0L;

    public Control control;

    public GameScreen(NinjaGame game) {
        this(game, new PlayerInstance());
    }

    public GameScreen(NinjaGame game, PlayerInstance p) {
        this.game = game;
        stage = new Stage();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();

        tiledMap = new TmxMapLoader().load("farm/Farm.tmx");

        sb = new SpriteBatch();

        tiledMapRenderer = new OrthogonalTiledMapRendererWithSprites(tiledMap);
        Gdx.input.setInputProcessor(this);

        int displayW = Gdx.graphics.getWidth();
        int displayH = Gdx.graphics.getHeight();
        control = new Control(displayW, displayH, ((OrthographicCamera) stage.getCamera()));
        Gdx.input.setInputProcessor(control);
        control.reset = true;

        TiledMapTileLayer pathsLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Paths");
        TiledMapTileLayer backLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Back");

        player = new Player(SpritesEnum.SAM, pathsLayer, backLayer);
        pnj = new Player(SpritesEnum.SHANE, pathsLayer, backLayer);

        addActors();

        objectLayer = tiledMap.getLayers().get(5);
        objectLayer.getObjects().add(pnj.createTextureMapObject(30 * 16, 30 * 16));
        objectLayer.getObjects().add(player.createTextureMapObject(w / 2, h / 2));


        box2D = new Box2DWorld();
        control.reset = true;
        squareMenu = new SquareMenu(this);
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
        debugTile = new DebugTile(tiledMap);
        stage.addActor(debugTile);
    }

    @Override
    public void render(float delta) {

        sb.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        control.processedClick = squareMenu.checkClick(control.mouseClickPos, control.processedClick);
        control.processedClick = squareMenu.build.checkClick(control.mouseClickPos, control.processedClick);
        squareMenu.checkHover(control.mousePos);

        updateCharacterPositionAndTexture();

        updatePnjsPositions();

        camera.update();
        tiledMapRenderer.setView(camera);

        sb.begin();

        tiledMapRenderer.render();
        squareMenu.draw(sb);
        // control.translate(new Vector2(camera.position.x, camera.position.y));

        sb.end();

        box2D.tick(camera, control);
        control.processedClick = true;


        if (System.currentTimeMillis() - lastUpdate > TimeUnit.SECONDS.toMillis(5)) {
            healthBar.setValue(healthBar.getValue() - healthBar.getStepSize());
            loadingBarWithBorders.setValue(loadingBarWithBorders.getValue() + 0.1f);
            lastUpdate = System.currentTimeMillis();
        }

        stage.draw();
        stage.act();
    }

    private void updatePnjsPositions() {
        int size = DirectionEnum.values().length;
        int item = (int) ((System.currentTimeMillis() % 4000) / 1000);
        System.out.println(item);
        pnj.moveIfPossible(Collections.singleton(DirectionEnum.values()[item]));
    }

    private void updateCharacterPositionAndTexture() {
        Set<DirectionEnum> directionAsked = Arrays.stream(DirectionEnum.values()).filter(direction -> Gdx.input.isKeyPressed(direction.getKey()))
                .collect(Collectors.toSet());

        Vector2 translation = player.moveIfPossible(directionAsked);

        if (translation.x != 0f || translation.y != 0f) {
            // We need to update camera position
            camera.translate(translation.x, translation.y);
            debugTile.translate(translation.x, translation.y);
            squareMenu.translate(translation.x, translation.y);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.NUM_1) tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
        if (keycode == Input.Keys.NUM_2) tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
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
        // TODO: check if correct tool is used to remove cell
        for (MapLayer layer : tiledMap.getLayers()) {
            if (!(layer instanceof TiledMapTileLayer tiledLayer) || "Back".equals(layer.getName())) {
                continue;
            }

            TiledMapTileLayer.Cell cell = tiledLayer.getCell(newX, newY);
            if (cell != null) {
                cell.setTile(null);
            }
        }
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
        tiledMap.dispose();

    }
}