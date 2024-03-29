package com.duckies.gdx.ninja.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.duckies.gdx.ninja.pojo.Inventory;
import com.duckies.gdx.ninja.pojo.ItemInstance;

public class InventoryActor extends Menu {

    private Inventory inventory;

    public InventoryActor(float x, float y, int scale, Texture mainBack, Inventory inventory) {
        super(x, y, 2, mainBack, inventory);
        this.inventory = inventory;

        addButtons(3, 14, 2, Media.pinkButton, Media.selector, 2, inventory);
        setInactive();

        Button close = new Button(0, 0, Media.close_menu.getWidth() * scale, Media.close_menu.getHeight() * scale, Media.close_menu, null, null);
        close.pos.x = x + width - (Media.close_menu.getWidth() * scale) - (6 * scale);
        close.pos.y = height - (Media.close_menu.getHeight() * scale) - (6 * scale);
        close.updateHitbox();
        close.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(Button b) {
                        toggleActive();
                    }
                });
        buttons.add(close);
    }

    public void draw(Batch batch, float parentAlpha) {

        for (int i = 0; i < buttons.size(); i++) {
            ItemInstance itemInstance = null;
            if (inventory.getItems() != null && i < inventory.getItems().size()) {
                itemInstance = inventory.getItems().get(i);
            }
            Button button = buttons.get(i);
            if (itemInstance != null) {
                button.content = Media.getItemById(itemInstance.getType());
            } else {
                button.content = null;
            }

            button.state = inventory.selected == i ? Enums.EnityState.SELECTED : Enums.EnityState.NONE;
        }
        if (isActive()) {
            super.draw(batch, parentAlpha);
        }
    }

    public void translate(float x, float y) {
        super.translate(x, y);
    }

}