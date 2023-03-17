package com.duckies.gdx.ninja.pojo;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

public class PlayerInstance {

    private int currentHealth;

    private String name;

    private String currentMapName;

    private float positionX;

    private float positionY;

    private Inventory inventory = new Inventory();

    private float lastSave;
    private int id;
    public PlayerInstance() {
        currentHealth = 100;
        id = new Random().nextInt();
        name = "player1";
        currentMapName = "farm.tmx";
        // setPosition(new Vector2(67, 47));
        setPosition(new Vector2(40, 40));
        lastSave = System.currentTimeMillis();

        inventory.getItems().add(new ItemInstance("pickaxe"));
        inventory.getItems().add(new ItemInstance("wateringcan"));
        inventory.getItems().add(new ItemInstance("axe"));
        inventory.getItems().add(new ItemInstance("fishingrod"));
        inventory.getItems().add(new ItemInstance("hoe"));
        inventory.getItems().add(new ItemInstance("sword"));
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


    public float getLastSave() {
        return lastSave;
    }

    public void setLastSave(float lastSave) {
        this.lastSave = lastSave;
    }

    public Inventory getInventory() {
        if (inventory == null) {
            inventory = new Inventory();
        }
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
