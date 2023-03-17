package com.duckies.gdx.ninja.gui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class Media {
    // TILES
    public static Texture grass01, grass02, grass03, grass04;
    public static Texture grassLeft, grassRight;
    public static Texture grassLeftUpperEdge, grassRightUpperEdge;
    public static Texture grassTop, grassTopRight, grassTopLeft;
    public static Texture water01, water02, water03, water04;
    public static Texture cliff, water;

    // HERO
    public static Texture hero;

    // Entity
    public static Texture tree;
    public static Texture birdWalk, birdFly, birdPeck, birdShadow;

    // Texture Regions
    public static TextureRegion[] birdWalkFrames, birdFlyFrames, birdPeckFrames;

    // Animations
    public static Animation<TextureRegion> birdWalkAnim, birdPeckAnim, birdFlyAnim;

    //GUI
    public static Texture squareMenu, mainBack, pinkButton;
    public static Texture iconBuild, iconSettings, iconResources;
    public static Texture selector;
    public static Texture close_menu;

    public static Texture axe;
    public static Texture pickaxe;
    public static Texture fishingrod;
    public static Texture hoe;

    public static Texture wateringcan;
    public static Texture sword;

    private static final Map<String, Texture> ITEM_BY_TEXTURE = new HashMap<>();

    public static final Map<String, Set<Integer>> ALLOWED_INTERACTION_BY_TOOL = new HashMap<>();
    static {

        // GUI
        squareMenu = new Texture(Gdx.files.internal("gui/square_menu.png"));
        mainBack = new Texture(Gdx.files.internal("gui/main_background.png"));
        pinkButton = new Texture(Gdx.files.internal("gui/pink_button.png"));
        selector = new Texture(Gdx.files.internal("gui/selector.png"));

        // ICONS
        iconBuild = new Texture(Gdx.files.internal("gui/icons/build.png"));
        iconSettings = new Texture(Gdx.files.internal("gui/icons/settings.png"));
        iconResources = new Texture(Gdx.files.internal("gui/icons/resources.png"));
        close_menu = new Texture(Gdx.files.internal("gui/icons/close_menu.png"));

        axe = new Texture(Gdx.files.internal("data/items/axe.png"));
        ITEM_BY_TEXTURE.put("axe", axe);
        fishingrod = new Texture(Gdx.files.internal("data/items/fishingrod.png"));
        ITEM_BY_TEXTURE.put("fishingrod", fishingrod);
        hoe = new Texture(Gdx.files.internal("data/items/hoe.png"));
        ITEM_BY_TEXTURE.put("hoe", hoe);
        pickaxe = new Texture(Gdx.files.internal("data/items/pickaxe.png"));
        ITEM_BY_TEXTURE.put("pickaxe", pickaxe);
        wateringcan = new Texture(Gdx.files.internal("data/items/wateringcan.png"));
        ITEM_BY_TEXTURE.put("wateringcan", wateringcan);
        sword = new Texture(Gdx.files.internal("data/items/sword.png"));
        ITEM_BY_TEXTURE.put("sword", sword);

        ALLOWED_INTERACTION_BY_TOOL.put("pickaxe", Set.of(18));
        ALLOWED_INTERACTION_BY_TOOL.put("axe", Set.of(19));

        HashSet<Integer> value = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            value.add(i);
        }
        for (int i = 1900; i < 2000; i++) {
            value.add(i);
        }
        ALLOWED_INTERACTION_BY_TOOL.put("sword", value);


        JsonValue items = new JsonReader().parse(Gdx.files.internal("items.json")).get("media");


    }

    public static Texture getItemById(String name) {
        return ITEM_BY_TEXTURE.get(name);
    }

    public void dispose() {
    }
}