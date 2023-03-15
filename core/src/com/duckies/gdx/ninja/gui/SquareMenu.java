package com.duckies.gdx.ninja.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.duckies.gdx.ninja.GameScreen;
import com.duckies.gdx.ninja.pojo.Inventory;

public class SquareMenu extends Menu {

    public InventoryActor inventoryActor;

    public final GameScreen game;

    public SquareMenu(final GameScreen game, Inventory inventory) {
        super(0, 0, 2, Media.squareMenu);

        this.game = game;

        int scale = 2;
        addButtons(3, 2, 2, Media.pinkButton, Media.selector, scale);

        Button btn = buttons.get(0);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(Button b) {

            }
        });

        btn = buttons.get(1);
        btn.icon = Media.iconSettings;
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(Button b) {
                System.out.println("Settings.");
            }
        });

        btn = buttons.get(2);
        btn.icon = Media.iconResources;
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(Button b) {
                game.control.inventory = true;
            }
        });

        btn = buttons.get(3);
        btn.icon = Media.iconBuild;
        buttons.get(3).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(Button b) {
                inventoryActor.toggleActive();
            }
        });

        // BUILDING
        inventoryActor = new InventoryActor(position.x + width, 0, 2, Media.mainBack, inventory);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        inventoryActor.draw(batch, parentAlpha);
    }

    @Override
    public void checkHover(Vector2 pos) {
        super.checkHover(pos);
        inventoryActor.checkHover(pos);
    }

    public void translate(float x, float y) {
        super.translate(x, y);
        inventoryActor.translate(x, y);
    }

}