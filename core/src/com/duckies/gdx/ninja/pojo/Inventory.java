package com.duckies.gdx.ninja.pojo;

import java.util.List;

public class Inventory {

    private List<ItemInstance> items;

    private int capacity;

    public List<ItemInstance> getItems() {
        return items;
    }

    public void setItems(List<ItemInstance> items) {
        this.items = items;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
