package com.duckies.gdx.ninja;

import java.util.EnumMap;
import java.util.Map;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector3;

public class MapGeneration extends ApplicationAdapter implements InputProcessor {
    Texture img;
    TiledMap tiledMap;
    OrthographicCamera camera;
    TiledMapRenderer tiledMapRenderer;
    SpriteBatch sb;
    MapLayer objectLayer;

    TextureRegion textureRegion;


    private EnumMap<DirectionEnum, Texture> textureByDirection = new EnumMap<>(DirectionEnum.class);

    private static final Map<DirectionEnum, String> ASSET_PATH_BY_DIRECTION = Map.of(DirectionEnum.LEFT, "hero_left.png"
    , DirectionEnum.RIGHT, "hero_right.png", DirectionEnum.UP, "hero_back.png", DirectionEnum.DOWN, "hero2.png");

    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();
        tiledMap = new TmxMapLoader().load("map/map.tmx");
        sb = new SpriteBatch();
        tiledMapRenderer = new OrthogonalTiledMapRendererWithSprites(tiledMap, sb);
        Gdx.input.setInputProcessor(this);

        ASSET_PATH_BY_DIRECTION.forEach((direction, path) -> textureByDirection.put(direction, buildTexture(path)));

        Texture texture = textureByDirection.get(DirectionEnum.DOWN);

        objectLayer = tiledMap.getLayers().get("objects");
        textureRegion = new TextureRegion(texture, 64, 64);

        TextureMapObject tmo = new TextureMapObject(textureRegion);
        tmo.setX(w/2);
        tmo.setY(h/2);

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
        camera.update();
        tiledMapRenderer.setView(camera);

        sb.begin();
        tiledMapRenderer.render();

        sb.end();
    }

    @Override
    public boolean keyDown(int keycode) {

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        for (Map.Entry<DirectionEnum, Texture> textureDirectionEntry : textureByDirection.entrySet()) {
            DirectionEnum direction = textureDirectionEntry.getKey();
            if (keycode != direction.getKey()) {
                continue;
            }

            camera.translate(direction.getX(), direction.getY());
            TextureMapObject character = (TextureMapObject) tiledMap.getLayers().get("objects").getObjects().get(2);
            character.setX(character.getX() + direction.getX());
            character.setY(character.getY() + direction.getY());


            character.getTextureRegion().setTexture(textureDirectionEntry.getValue());

        }
        if (keycode == Input.Keys.NUM_1)
            tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
        if (keycode == Input.Keys.NUM_2)
            tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
        return false;
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