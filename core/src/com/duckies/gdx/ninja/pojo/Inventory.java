package com.duckies.gdx.ninja.pojo;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    public int selected;
    private List<ItemInstance> items = new ArrayList<>();

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

    public String getSelectedItemName() {
        ItemInstance selectedItem = items.get(selected);
        return selectedItem == null ? null : selectedItem.getType();
    }
}
