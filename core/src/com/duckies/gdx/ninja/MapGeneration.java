package com.duckies.gdx.ninja;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.math.Vector2;

public class MapGeneration extends ApplicationAdapter implements InputProcessor {

    private static final int FRAME_COLS = 4, FRAME_ROWS = 14;
    private static final int PLAYER_WIDTH = 16;
    private static final int PLAYER_HEIGHT = 16;

    Texture img;
    TiledMap tiledMap;
    OrthographicCamera camera;
    TiledMapRenderer tiledMapRenderer;
    SpriteBatch sb;
    MapLayer objectLayer;


    private DirectionEnum currentDirection;
    private Long startOfCurrentDirection;

    private DebugTile debugTile;
    private Player player;


    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();
        tiledMap = new TmxMapLoader().load("farm/Farm.tmx");
        sb = new SpriteBatch();
        tiledMapRenderer = new OrthogonalTiledMapRendererWithSprites(tiledMap);
        Gdx.input.setInputProcessor(this);

        player = new Player("Sam.png");


        startOfCurrentDirection = System.currentTimeMillis();

        debugTile = new DebugTile(tiledMap);

        TextureMapObject tmo = player.createTextureMapObject(w / 2, h / 2);

        objectLayer = tiledMap.getLayers().get(5);

        objectLayer.getObjects().add(tmo);
    }

    private static Texture buildTexture(String path) {
        return new Texture(Gdx.files.internal(path));
    }

    @Override
    public void render() {

        sb.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateCharacterPositionAndTexture();

        camera.update();
        tiledMapRenderer.setView(camera);

        sb.begin();
        tiledMapRenderer.render();

      //  Vector3 projected = camera.project(new Vector3(textX, textY, 0));
        debugTile.draw(sb);
        sb.end();
    }

    private void updateCharacterPositionAndTexture() {
        TextureMapObject character = getCharacter();

        Set<DirectionEnum> directionAsked = Arrays.stream(DirectionEnum.values())
                .filter(direction -> Gdx.input.isKeyPressed(direction.getKey()))
                .collect(Collectors.toSet());

        Vector2 translation = player.moveIfPossible(directionAsked, (TiledMapTileLayer) tiledMap.getLayers().get(
                "Paths"), (TiledMapTileLayer) tiledMap.getLayers().get("Back"));

        if (translation.x != 0f || translation.y != 0f) {
            // We need to update camera position
            camera.translate(translation.x, translation.y);
            debugTile.translate(translation.x, translation.y);

            character.setX(player.getX());
            character.setY(player.getY());

        }

        character.setTextureRegion(player.getTextureRegion());

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
    private TextureMapObject getCharacter() {
        return (TextureMapObject) tiledMap.getLayers().get(5).getObjects().get(tiledMap.getLayers().get(5).getObjects().getCount() - 1);
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
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


}