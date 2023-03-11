package com.duckies.gdx.ninja;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Map;

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

    TextureRegion textureRegion;

    private static final float SPEED = 15;

    private DirectionEnum currentDirection;
    private Long startOfCurrentDirection;

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


        startOfCurrentDirection = System.currentTimeMillis();

        debugTile = new DebugTile(tiledMap);

        TextureMapObject tmo = player.createTextureMapObject(w / 2, h / 2);

        objectLayer = tiledMap.getLayers().get(5);

        addProgressBarToLayer();

        objectLayer.getObjects().add(tmo);
    }

    private void addProgressBarToLayer() {

        Skin skin = new Skin(Gdx.files.classpath("data/uiskin.json"));
        Pixmap pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("green", new Texture(pixmap));

        progressBar = new ProgressBar(0.0f, 100f, 2, false, skin);

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

        progressBar.draw(sb, 1);

        //  Vector3 projected = camera.project(new Vector3(textX, textY, 0));
        debugTile.draw(sb);
        sb.end();

    }

    private void updateCharacterPositionAndTexture() {
        boolean isIdle = true;
        TextureMapObject character = getCharacter();

        for (Map.Entry<DirectionEnum, Animation<TextureRegion>> textureDirectionEntry : player.getAnimationByDirection().entrySet()) {
            DirectionEnum direction = textureDirectionEntry.getKey();

            if (!Gdx.input.isKeyPressed(direction.getKey())) {
                continue;
            }

            float speedX = direction.getX() * Gdx.graphics.getDeltaTime() * SPEED;
            float speedY = direction.getY() * Gdx.graphics.getDeltaTime() * SPEED;


            float newPositionX = character.getX() + speedX;
            float newPositionY = character.getY() + speedY;

            //if (detectCollision(newPositionX, new))

            progressBar.setValue(character.getX() / 25);
            progressBar.setX(character.getX());
            progressBar.setY(character.getY() / 200);

            TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("Front");

            int x = (int) ((character.getX() / layer.getTileHeight()));
            int y = (int) ((character.getY() / layer.getTileWidth()));
            TiledMapTileLayer.Cell cell = layer.getCell(x, y);
            if (cell != null) {
                if (cell.getTile().getProperties().get("walkable") != null) {
                    boolean walkable = Boolean.parseBoolean(cell.getTile().getProperties().get("walkable").toString());
                    if (!walkable) {
                        isIdle = true;
                        break;
                    }
                }
            }

            camera.translate(speedX, speedY);
            character.setX(newPositionX);
            character.setY(newPositionY);
            debugTile.translate(speedX, speedY);

//            progressBarObject.setX(newPositionX);
//            progressBarObject.setY(newPositionY - Gdx.graphics.getHeight() / 2);

            if (currentDirection != direction) {
                // We need to update direction
                currentDirection = direction;
                startOfCurrentDirection = System.currentTimeMillis();
            }
            updateCharacterTexture(character, textureDirectionEntry.getValue());


            isIdle = false;


        }

        if (isIdle) {
            if (currentDirection != null) {
                currentDirection = null;
                startOfCurrentDirection = System.currentTimeMillis();
            }
            updateCharacterTexture(character, player.getIdleTexture());
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

    private void updateCharacterTexture(TextureMapObject character, Animation<TextureRegion> texture) {
        character.setTextureRegion(texture.getKeyFrame((System.currentTimeMillis() - startOfCurrentDirection) / 1000f));
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