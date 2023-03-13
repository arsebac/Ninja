package com.duckies.gdx.ninja.gui;

public class Enums {
    
    public enum TileType {
        GRASS,
        WATER,
        CLIFF
    }
    
    public enum EntityType {
        HERO, 
        TREE, 
        BIRD
    }
    
    public enum EnityState {
        NONE,
        IDLE,
        FEEDING,
        WALKING,
        FLYING,
        HOVERING, 
        LANDING
    }
    
    public enum MenuState {
        ACTIVE,
        DISABLED,
        HOVEROVER,
        CLICKED
    }

}