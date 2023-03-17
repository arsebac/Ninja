package com.duckies.gdx.ninja.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.duckies.gdx.ninja.pojo.Inventory;

import java.util.ArrayList;

public class Menu extends Actor {
    public String name;
    public Vector2 position;
    public Texture texture;
    public float width;
    public float height;
    public float scale;
    public Enums.MenuState state;
    public float time;
    public float coolDown;
    public Rectangle hitbox;
    public ArrayList<Button> buttons;

    public Menu(float x, float y, float scale, Texture texture, Inventory inventory) {
        position = new Vector2(x, y);
        this.texture = texture;
        width = texture.getWidth() * scale;
        height = texture.getHeight() * scale;
        buttons = new ArrayList<>();
        hitbox = new Rectangle(x, y, width, height);
        setActive();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (texture != null) batch.draw(texture, position.x, position.y, width, height);
        for (Button b : buttons) {
            b.draw(batch);
        }
    }

    // Render the texture and all of the button textures
    public void draw(SpriteBatch batch, float parentAlpha) {

    }

    // If the player has clicked the mouse then processedClick will be true
    // We check if the mouse position is contained within any of the button Rectangles
    public boolean checkClick(Vector2 pos, boolean processedClick) {
        boolean processed = false;
        if (!processedClick) {
            if (hitbox.contains(pos)) {
                System.out.println("Hit: " + name);
            }

            // Check if a button has been clicked
            for (Button b : buttons) {
                if (b.hitbox.contains(pos)) {
                    if (b.listener != null) b.listener.onClick(b);
                    processed = true;
                    break;
                }
            }
        } else {
            return processedClick;
        }

        return processed;
    }

    // If the mouse is inside of the menu then check if its also inside of a button
    // When the mouse is inside a button then set its state to hovering
    // Else set all buttons to idle
    public void checkHover(Vector2 pos) {
        if (hitbox.contains(pos)) {
            // Check if a button is being hovered over
            for (Button b : buttons) {
                if (b.hitbox.contains(pos)) {
                    b.state = Enums.EnityState.HOVERING;
                } else {
                    b.state = Enums.EnityState.IDLE;
                }
            }
        } else {
            for (Button b : buttons) {
                b.state = Enums.EnityState.IDLE;
            }
        }
    }

    // A function to add multiply buttons to our menu
    // It is possible to add any size grid of buttons with a certain sized padding
    public void addButtons(float offset, int columns, int rows, Texture texture, Texture select, int scale,
                           Inventory inventory) {
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {

                int buttonId = i * rows + j;

                float bx = position.x + (offset + ((i + 1) * offset) + (i * texture.getWidth())) * 2;
                float by = position.y + (offset + ((j + 1) * offset) + (j * texture.getHeight())) * 2;
                float width = texture.getWidth() * 2;
                float height = texture.getHeight() * 2;

                Entity selector = new Entity();
                selector.texture = select;
                selector.width = selector.texture.getWidth() * scale;
                selector.height = selector.texture.getHeight() * scale;
                selector.pos.x = bx - ((selector.width - width) / 2);
                selector.pos.y = by - ((selector.height - height) / 2);

                Button button = new Button(bx, by, width, height, texture, selector, null);

                button.setOnClickListener(
                        b -> {
                            inventory.selected = buttonId;
                        });

                buttons.add(button);
            }
        }
    }

    // Check if the menu is active
    public boolean isActive() {
        return state == Enums.MenuState.ACTIVE;
    }

    // Set meny to active
    public void setActive() {
        state = Enums.MenuState.ACTIVE;
    }

    // Set menu to inactive
    public void setInactive() {
        state = Enums.MenuState.DISABLED;
    }

    // Toggle active state
    public void toggleActive() {
        if (isActive()) {
            setInactive();
        } else {
            setActive();
        }
    }

    public void translate(float x, float y) {
        this.position.x += x;
        this.position.y += y;
        buttons.forEach(b -> {
            b.pos.x += x;
            b.pos.y += y;
            if (b.selector != null) {
                b.selector.pos.x += x;
                b.selector.pos.y += y;
            }
        });
    }
}