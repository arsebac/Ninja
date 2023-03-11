package com.duckies.gdx.ninja;

import java.util.Arrays;

import com.badlogic.gdx.Input;

public enum DirectionEnum {
	LEFT(Input.Keys.LEFT, -32, 0, 1),
	RIGHT(Input.Keys.RIGHT, 32, 0, 2),
	UP(Input.Keys.UP, 0, 32, 3),
	DOWN(Input.Keys.DOWN, 0, -32, 0);

	private final int key;
	private final int x;
	private final int y;
	private final int sprintId;

	DirectionEnum(int key, int x, int y, int sprintId) {
		this.key = key;
		this.x = x;
		this.y = y;
		this.sprintId = sprintId;
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

	public int getSprintId() {
		return sprintId;
	}

	public static DirectionEnum getFromSprintId(int id) {
		return Arrays.stream(values()).filter(val -> val.getSprintId() == id).findFirst().orElseThrow();
	}
}
