package com.duckies.gdx.ninja;

import java.util.EnumMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;

public class Player {

	private static final int FRAME_COLS = 4, FRAME_ROWS = 14;
	private static final int PLAYER_WIDTH = 16;
	private static final int PLAYER_HEIGHT = 16;


	private static final float SPEED = 15;

	private final TextureRegion textureRegion;

	private DirectionEnum currentDirection;
	private Long startOfCurrentDirection;

	private EnumMap<DirectionEnum, Animation<TextureRegion>> animationByDirection = new EnumMap<>(DirectionEnum.class);

	private final Animation<TextureRegion> idleAnimation;

	public Player(String spritePath) {

		Texture walkSheet = new Texture(Gdx.files.internal("Sam.png"));

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
		tmo.setX(x);
		tmo.setY(y);

		return tmo;
	}

	public Animation<TextureRegion> getIdleTexture() {
		return idleAnimation;
	}

	public EnumMap<DirectionEnum, Animation<TextureRegion>> getAnimationByDirection() {
		return animationByDirection;
	}
}
