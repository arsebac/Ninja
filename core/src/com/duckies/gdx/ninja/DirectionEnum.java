package com.duckies.gdx.ninja;

import com.badlogic.gdx.Input;

public enum DirectionEnum {
	LEFT(Input.Keys.LEFT, -32, 0), RIGHT(Input.Keys.RIGHT, 32, 0), UP(Input.Keys.UP, 0, 32), DOWN(Input.Keys.DOWN, 0, -32);

	private final int key;
	private final int x;
	private final int y;

	DirectionEnum(int key, int x, int y) {
		this.key = key;
		this.x = x;
		this.y = y;
	}

	public int getKey() {
		return key;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
