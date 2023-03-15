package com.duckies.gdx.ninja.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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

    private static TextureRegion[] axeFrames;

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
    }

    public void dispose() {
    }
}