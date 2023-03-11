package com.duckies.gdx.ninja;

import java.util.Arrays;
import java.util.NoSuchElementException;

import com.badlogic.gdx.Input;

public enum DirectionEnum {
	LEFT(Input.Keys.LEFT, -21, 0, 3),
	RIGHT(Input.Keys.RIGHT, 21, 0, 1),
	UP(Input.Keys.UP, 0, 21, 2),
	DOWN(Input.Keys.DOWN, 0, -21, 0);

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
		return Arrays.stream(values()).filter(val -> val.getSprintId() == id).findFirst().orElseThrow(() -> new NoSuchElementException("Direction " + id + " not found"));
	}
}
