package com.duckies.gdx.ninja;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum SpritesEnum {
	SAM( 14, 4,"Sam.png"),
	SHANE( 13, 4,"Shane.png");


	private final int rows;
	private final int cols;
	private final String path;

	SpritesEnum(int rows, int cols, String path) {
		this.rows = rows;
		this.cols = cols;
		this.path = path;
	}

	public EnumMap<DirectionEnum, Animation<TextureRegion>> buildAnimationByTexture() {
		Texture texture = new Texture(Gdx.files.internal(this.path));

		TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / this.cols,texture.getHeight() / this.rows);

		EnumMap<DirectionEnum, Animation<TextureRegion>> animationByDirection = new EnumMap<>(DirectionEnum.class);

		for (int i = 0; i < 4; i++) {
			int index = 0;
			TextureRegion[] walkDirectionFrame = new TextureRegion[cols];
			for (int j = 0; j < cols; j++) {
				walkDirectionFrame[index++] = tmp[i][j];
			}

			DirectionEnum direction = DirectionEnum.getFromSprintId(i);
			Animation<TextureRegion> animation = new Animation<>(0.15f, walkDirectionFrame);
			animation.setPlayMode(Animation.PlayMode.LOOP);
			animationByDirection.put(direction, animation);

		}

		return animationByDirection;
	}
}
