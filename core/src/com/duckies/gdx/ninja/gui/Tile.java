package com.duckies.gdx.ninja.gui;

import com.badlogic.gdx.graphics.Texture;

public class Tile extends Entity {
    public int size;
    public int row;
    public int col;
    public String code;
    public Texture secondaryTexture;
    public Texture texture;
    public Enums.TileType type;

    public Tile(float x, float y, int size, Enums.TileType type, Texture texture) {
        super();
        pos.x = x * size;
        pos.y = y * size;
        this.size = size;
        this.texture = texture;
        this.col = (int) x;
        this.row = (int) y;
        this.type = type;
        this.code = "";
    }

    public String details() {
        return "x: " + pos.x + " y: " + pos.y + " row: " + row + " col: " + col + " code: " + code + " type: " + type.toString();
    }

    public boolean isGrass() {
        return type == Enums.TileType.GRASS;
    }

    public boolean isWater() {
        return type == Enums.TileType.WATER;
    }

    public boolean isCliff() {
        return type == Enums.TileType.CLIFF;
    }

    public boolean isPassable() {
        return !isWater() && !isCliff();
    }

    public boolean isNotPassable() {
        return !isPassable();
    }

    public boolean isAllWater() {
        return code.equals("000000000");
    }

    public boolean notIsAllWater() {
        return !isAllWater();
    }
}