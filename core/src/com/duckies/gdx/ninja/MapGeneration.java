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
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class MapGeneration extends ApplicationAdapter implements InputProcessor {

    private static final int FRAME_COLS = 4, FRAME_ROWS = 4;
    private static final int PLAYER_WIDTH = 45;
    private static final int PLAYER_HEIGHT = 64;

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

    private EnumMap<DirectionEnum, Texture> textureByDirection = new EnumMap<>(DirectionEnum.class);
    private EnumMap<DirectionEnum, Animation<TextureRegion>> animationByDirection = new EnumMap<>(DirectionEnum.class);

    Animation<TextureRegion> idleAnimation;

    private static final Map<DirectionEnum, String> ASSET_PATH_BY_DIRECTION = Map.of(DirectionEnum.LEFT, "hero_left.png", DirectionEnum.RIGHT,
            "hero_right.png", DirectionEnum.UP, "hero_back.png", DirectionEnum.DOWN, "hero2.png");

    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();
        tiledMap = new TmxMapLoader().load("project_tiled/newmap.tmx");
        sb = new SpriteBatch();
        tiledMapRenderer = new OrthogonalTiledMapRendererWithSprites(tiledMap, sb);
        Gdx.input.setInputProcessor(this);

        // V 0.1 : Use 1 frame by direction
        ASSET_PATH_BY_DIRECTION.forEach((direction, path) -> textureByDirection.put(direction, buildTexture(path)));

        // V 0.2 : Use Animation
        // Load the sprite sheet as a Texture
        Texture walkSheet = new Texture(Gdx.files.internal("hero.png"));

        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.

        TextureRegion[] idleTextures = new TextureRegion[1];
        idleTextures[0] = tmp[0][0];

        idleAnimation = new Animation<>(0.066f, idleTextures);
        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);


        for (int i = 0; i < FRAME_ROWS; i++) {
            int index = 0;
            TextureRegion[] walkDirectionFrame = new TextureRegion[FRAME_ROWS];
            for (int j = 0; j < FRAME_COLS; j++) {
                walkDirectionFrame[index++] = tmp[i][j];
            }
            DirectionEnum direction = DirectionEnum.getFromSprintId(i);
            Animation<TextureRegion> animation = new Animation<>(0.15f, walkDirectionFrame);
            animation.setPlayMode(Animation.PlayMode.LOOP);
            animationByDirection.put(direction, animation);


        }

        startOfCurrentDirection = System.currentTimeMillis();

        Texture texture = textureByDirection.get(DirectionEnum.DOWN);

        objectLayer = tiledMap.getLayers().get("players");

        textureRegion = new TextureRegion(texture, PLAYER_WIDTH, PLAYER_HEIGHT);

        TextureMapObject tmo = new TextureMapObject(textureRegion);
        tmo.setX(w / 2);
        tmo.setY(h / 2);

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

        sb.end();
    }

    private void updateCharacterPositionAndTexture() {
        boolean isIdle = true;
        TextureMapObject character = getCharacter();

        for (Map.Entry<DirectionEnum, Animation<TextureRegion>> textureDirectionEntry : animationByDirection.entrySet()) {
            DirectionEnum direction = textureDirectionEntry.getKey();

            if (!Gdx.input.isKeyPressed(direction.getKey())) {
                continue;
            }

            float speedX = direction.getX() * Gdx.graphics.getDeltaTime() * SPEED;
            float speedY = direction.getY() * Gdx.graphics.getDeltaTime() * SPEED;


            float newPositionX = character.getX() + speedX;
            float newPositionY = character.getY() + speedY;

            //if (detectCollision(newPositionX, new))

            TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("foreground");
            int x = (int) ((character.getX() / layer.getHeight()));
            int y = (int) ((character.getY() / layer.getWidth()));
            System.out.println(x + " " + character.getX());
            System.out.println(y + " " + character.getY());
            TiledMapTileLayer.Cell cell = layer.getCell(x, y);
            if (cell != null) {
                System.out.println(cell.getTile().getId());
                System.out.println(cell.getTile().getProperties().get("walkable"));
                System.out.println(cell.getTile().getProperties());
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


            if (currentDirection != direction) {
                // We need to update direction
                System.out.println("Direction have been updated here");
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
            updateCharacterTexture(character, idleAnimation);
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
        return false;
    }

    private void updateCharacterTexture(TextureMapObject character, Animation<TextureRegion> texture) {
        character.setTextureRegion(texture.getKeyFrame((System.currentTimeMillis() - startOfCurrentDirection) / 1000f));
    }

    private TextureMapObject getCharacter() {
        return (TextureMapObject) tiledMap.getLayers().get("players").getObjects().get(0);
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