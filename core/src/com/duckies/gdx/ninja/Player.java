package com.duckies.gdx.ninja;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Player {

	private static final int FRAME_COLS = 4, FRAME_ROWS = 14;
	private static final int PLAYER_WIDTH = 16;
	private static final int PLAYER_HEIGHT = 32;

	private static final float SPEED = 5;

	private final TextureRegion textureRegion;

	private DirectionEnum currentDirection;
	private Long startOfCurrentDirection;

	private EnumMap<DirectionEnum, Animation<TextureRegion>> animationByDirection = new EnumMap<>(DirectionEnum.class);

	private final Animation<TextureRegion> idleAnimation;
	private float playerY;
	private float playerX;

	public Player(String spritePath) {

		Texture walkSheet = new Texture(Gdx.files.internal(spritePath));

		// Use the split utility method to create a 2D array of TextureRegions.
		TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);

		// Place the regions into a 1D array in the correct order, starting from the top
		// left, going across first. The Animation constructor requires a 1D array.

		TextureRegion idleTexture =  tmp[0][0];

		idleAnimation = new Animation<>(0.066f, idleTexture);
		idleAnimation.setPlayMode(Animation.PlayMode.LOOP);

		for (int i = 0; i < 4; i++) {
			int index = 0;
			TextureRegion[] walkDirectionFrame = new TextureRegion[FRAME_COLS];
			for (int j = 0; j < FRAME_COLS; j++) {
				walkDirectionFrame[index++] = tmp[i][j];
			}
			DirectionEnum direction = DirectionEnum.getFromSprintId(i);
			Animation<TextureRegion> animation = new Animation<>(0.15f, walkDirectionFrame);
			animation.setPlayMode(Animation.PlayMode.LOOP);
			animationByDirection.put(direction, animation);


		}

		startOfCurrentDirection = System.currentTimeMillis();

		textureRegion = new TextureRegion(idleTexture.getTexture(), PLAYER_WIDTH, PLAYER_HEIGHT);

	}

	public TextureMapObject createTextureMapObject(float x, float y) {

		TextureMapObject tmo = new TextureMapObject(textureRegion);
		this.playerX = x;
		this.playerY = y;
		tmo.setX(x);
		tmo.setY(y);

		return tmo;
	}

	public Vector2 moveIfPossible(Set<DirectionEnum> directionAsked, TiledMapTileLayer collisionLayer,
								  TiledMapTileLayer back) {

		float speed = Gdx.graphics.getDeltaTime() * SPEED;

		// Compute player translation vector with all expected direction
		Vector2 translation = directionAsked.stream()
						.map(direction -> new Vector2(direction.getX(), direction.getY()))
						.reduce(Vector2::add)
						.map(vector -> new Vector2(vector.x * speed, vector.y * speed))
						.orElse(new Vector2());

		if (translation.x == 0 && translation.y == 0) {
			// Idle
			currentDirection = null;

			return new Vector2();
		}

		// Player is moving.
		updateCurrentDirection(directionAsked);

		// Check if translation avoid collision


		Set<Vector2> cells = getCellFromPlayerTranslatedCoords(translation, collisionLayer.getTileHeight(),
				collisionLayer.getTileWidth());


		if (anyCollision(cells, collisionLayer, back)) {
			return new Vector2();
		}
		playerX += translation.x;
		playerY += translation.y;

		return translation;

	}

	private static List<Integer> IGNORED = Arrays.asList(23);

	private boolean anyCollision(Set<Vector2> cells, TiledMapTileLayer collisionLayer, TiledMapTileLayer back) {
		for (Vector2 vector : cells) {
			TiledMapTileLayer.Cell cell = collisionLayer.getCell((int) vector.x, (int) vector.y);
			if (cell != null && cell.getTile() != null && ! IGNORED.contains(cell.getTile().getId())) {

				System.out.println("Collition with Cell ( " +  ((int) vector.x) + "," + ((int)vector.y) + ") : " + (cell.getTile().getId()));

				return true;
			}
			cell = back.getCell((int) vector.x, (int) vector.y);
			if (cell != null && cell.getTile() != null && "F".equals(cell.getTile().getProperties().get("Passable"))) {
				System.out.println("Collition with Cell ( " +  ((int) vector.x) + "," + ((int)vector.y) + ") : " + (cell.getTile().getId()) + " : Passable");
				return true;
			}

		}
		return false;
	}

	/**
	 * Get all cases that could be traversed if translation is applied to player
	 *
	 * Depending on translation, check front case
	 *
	 * @param translation
	 * @param tileHeight
	 * @param tileWidth
	 * @return
	 */
	private Set<Vector2> getCellFromPlayerTranslatedCoords(Vector2 translation, int tileHeight, int tileWidth) {
		int currentTileX = (int)(playerX / tileWidth);
		int currentTileY = (int)(playerY / tileWidth);

		// Check for potential tiles in front of player
		Set<Vector2> vector2 = new HashSet<>();

		// If expected translation go to another cell
		if (translation.x != 0 && ((playerX + translation.x) / tileWidth) != currentTileX) {
			vector2.add(new Vector2(currentTileX + (translation.x > 0 ? 1 : -1), currentTileY));
		}

		if (translation.y != 0 && ((playerY + translation.y) / tileHeight) != currentTileY) {
			vector2.add(new Vector2(currentTileX, currentTileY + (translation.y > 0 ? 1 : -1)));
		}

		return vector2;
	}

	private void updateCurrentDirection(Set<DirectionEnum> directionAsked) {
		if (currentDirection == null || !directionAsked.contains(currentDirection)) {
			currentDirection = directionAsked.iterator().next();
			startOfCurrentDirection = System.currentTimeMillis();
		}
	}

	public TextureRegion getTextureRegion() {

		if (currentDirection == null) {
			return idleAnimation.getKeyFrame(0);
		}

		if (startOfCurrentDirection == null) {
			throw new NullPointerException("startOfCurrentDirection in not initialized or null");
		}

		float stateTime = (System.currentTimeMillis() - startOfCurrentDirection) / 1000f;
		return animationByDirection.get(currentDirection).getKeyFrame(stateTime);
	}

	public float getX() {
		return playerX;
	}

	public float getY() {
		return playerY;
	}
}
