package com.duckies.gdx.ninja.pojo;

import com.badlogic.gdx.math.Vector2;

public class PlayerInstance {

    private int currentHealth;

    private String name;

    private String currentMapName;

    private float positionX;

    private float positionY;

    public PlayerInstance() {
        currentHealth = 100;
        name = "player1";
        currentMapName = "farm.tmx";
        setPosition(new Vector2(32, 22.5f));
    }

    public PlayerInstance(int currentHealth, String name, String currentMapName, Vector2 position) {
        this.currentHealth = currentHealth;
        this.name = name;
        this.currentMapName = currentMapName;
        this.setPosition(position);
    }

    @Override
    public String toString() {
        return "PlayerInstance{" +
                "currentHealth=" + currentHealth +
                ", name='" + name + '\'' +
                ", currentMapName='" + currentMapName + '\'' +
                ", positionX=" + positionX +
                ", positionY=" + positionY +
                '}';
    }

    public Vector2 getPosition() {
        return new Vector2(positionX, positionY);
    }

    public void setPosition(Vector2 position) {
        this.positionX = position.x;
        this.positionY = position.y;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentMapName() {
        return currentMapName;
    }

    public void setCurrentMapName(String currentMapName) {
        this.currentMapName = currentMapName;
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }


}
