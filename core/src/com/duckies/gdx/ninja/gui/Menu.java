package com.duckies.gdx.ninja.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Menu {
    public String name;
    public Vector2 pos;
    public Texture texture;
    public float width;
    public float height;
    public float scale;
    public Enums.MenuState state;
    public float time;
    public float coolDown;
    public Rectangle hitbox;
    public ArrayList<Button> buttons;

    public Menu(float x, float y, float scale, Texture texture) {
        pos = new Vector2(x, y);
        this.texture = texture;
        width = texture.getWidth() * scale;
        height = texture.getHeight() * scale;
        buttons = new ArrayList<Button>();
        hitbox = new Rectangle(x, y, width, height);
        setActive();
    }

    // Render the texture and all of the button textures
    public void draw(SpriteBatch batch) {
        if (texture != null) batch.draw(texture, pos.x, pos.y, width, height);
        for (Button b : buttons) {
            b.draw(batch);
        }
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
    public void addButtons(float offset, int columns, int rows, Texture texture, Texture select, int scale) {
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                float bx = pos.x + (offset + ((i + 1) * offset) + (i * texture.getWidth())) * 2;
                float by = pos.y + (offset + ((j + 1) * offset) + (j * texture.getHeight())) * 2;
                float width = texture.getWidth() * 2;
                float height = texture.getHeight() * 2;

                Entity selector = new Entity();
                selector.texture = select;
                selector.width = selector.texture.getWidth() * scale;
                selector.height = selector.texture.getHeight() * scale;
                selector.pos.x = bx - ((selector.width - width) / 2);
                selector.pos.y = by - ((selector.height - height) / 2);

                buttons.add(new Button(bx, by, width, height, texture, selector));
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
}