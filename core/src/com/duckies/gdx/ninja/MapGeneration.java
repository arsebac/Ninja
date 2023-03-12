package com.duckies.gdx.ninja;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.badlogic.gdx.math.Vector2;

public class MapGeneration extends ApplicationAdapter implements InputProcessor {
    TiledMap tiledMap;

    OrthographicCamera camera;

    TiledMapRenderer tiledMapRenderer;

    SpriteBatch sb;

    MapLayer objectLayer;

    private DebugTile debugTile;

    private Player player;

    private ProgressBar progressBar;

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

        debugTile = new DebugTile(tiledMap);

        TextureMapObject tmo = player.createTextureMapObject(w / 2, h / 2);

        objectLayer = tiledMap.getLayers().get(5);

        addProgressBar();

        objectLayer.getObjects().add(tmo);
    }

    private void addProgressBar() {
        ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle();
        Texture texture = new Texture(Gdx.files.classpath("textBox.png"));
        style.background = new TextureRegionDrawable(new TextureRegion(texture));
        progressBar = new ProgressBar(0.0f, 100f, 1, false, style);
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

        progressBar.draw(sb, 1);

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

            progressBar.setValue(character.getX() / 25);
            progressBar.setX(character.getX());
            progressBar.setY(character.getY() / 200);

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