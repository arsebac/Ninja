package com.duckies.gdx.ninja;

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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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
    Texture texture;
    Sprite sprite;

    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();
        tiledMap = new TmxMapLoader().load("map/map.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        Gdx.input.setInputProcessor(this);

        sb = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("bucket.png"));
        sprite = new Sprite(texture);


        // Get the width and height of our maps
        // Then halve it, as our sprites are 64x64 not 32x32 that our map is made of
        int mapWidth = tiledMap.getProperties().get("width", Integer.class) / 2;
        int mapHeight = tiledMap.getProperties().get("height", Integer.class) / 2;

        // Create a new map layer
        TiledMapTileLayer tileLayer = new TiledMapTileLayer(mapWidth, mapHeight, 64, 64);

        // Create a cell(tile) to add to the layer
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();

        // The sprite/tilesheet behind our new layer is a single image (our sprite)
        // Create a TextureRegion that is the entire size of our texture
        TextureRegion textureRegion = new TextureRegion(texture, 64, 64);

        // Now set the graphic for our cell to our newly created region
        cell.setTile(new StaticTiledMapTile(textureRegion));

        // Now set the cell at position 4,10 ( 8,20 in map coordinates ).  This is the position of a tree
        // Relative to 0,0 in our map which is the bottom left corner
        tileLayer.setCell(4, 10, cell);

        // Ok, I admit, this part is a gross hack.
        // Get the current top most layer from the map and store it
        MapLayer tempLayer = tiledMap.getLayers().get(tiledMap.getLayers().getCount() - 1);
        // Now remove it
        tiledMap.getLayers().remove(tiledMap.getLayers().getCount() - 1);
        // Now add our newly created layer
        tiledMap.getLayers().add(tileLayer);
        // Now add it back, now our new layer is not the top most one.
        tiledMap.getLayers().add(tempLayer);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sprite.draw(sb);
        sb.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.LEFT)
            camera.translate(-32, 0);
        if (keycode == Input.Keys.RIGHT)
            camera.translate(32, 0);
        if (keycode == Input.Keys.UP)
            camera.translate(0, -32);
        if (keycode == Input.Keys.DOWN)
            camera.translate(0, 32);
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
        Vector3 clickCoordinates = new Vector3(screenX, screenY, 0);
        Vector3 position = camera.unproject(clickCoordinates);
        sprite.setPosition(position.x, position.y);
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