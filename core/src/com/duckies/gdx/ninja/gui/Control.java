package com.duckies.gdx.ninja.gui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Control extends InputAdapter implements InputProcessor {
    // CAMERA
    OrthographicCamera camera;

    // DIRECTIONS
    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;

    // ACTIONS
    public boolean interact;

    // MOUSE
    public boolean leftMouseBtn;
    public boolean rightMouseBtn;
    public boolean processedClick = true;
    public Vector2 mouseClickPos = new Vector2();
    public Vector2 mousePos = new Vector2();
    public Vector2 mapClickPos = new Vector2();

    // DEBUG
    public boolean debug;
    public boolean reset;
    public boolean inventory;

    // SCREEN
    int screenWidth;
    int screenHeight;

    private List<InputProcessor> processors = new ArrayList<>();

    public Control(int screenWidth, int screenHeight, OrthographicCamera camera) {
        this.camera = camera;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void addInputProcessor(InputProcessor processor) {
        this.processors.add(processor);
    }


    private void setMouseClickedPos(int screenX, int screenY) {
        // Set mouse position (flip screen Y)
        mouseClickPos.set(screenX, screenHeight - screenY);
        mapClickPos.set(get_map_coords(mouseClickPos));
    }

    public Vector2 get_map_coords(Vector2 mouseCoords) {
        Vector3 v3 = new Vector3(mouseCoords.x, screenHeight - mouseCoords.y, 0);
        this.camera.unproject(v3);
        return new Vector2(v3.x, v3.y);
    }

    @Override
    public boolean keyDown(int keyCode) {
        switch (keyCode) {
            case Keys.DOWN, Keys.S -> down = true;
            case Keys.UP, Keys.W -> up = true;
            case Keys.LEFT, Keys.A -> left = true;
            case Keys.RIGHT, Keys.D -> right = true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.DOWN, Keys.S -> down = false;
            case Keys.UP, Keys.W -> up = false;
            case Keys.LEFT, Keys.A -> left = false;
            case Keys.RIGHT, Keys.D -> right = false;
            case Keys.E -> interact = true;
            case Keys.ESCAPE -> Gdx.app.exit();
            case Keys.BACKSPACE -> debug = !debug;
            case Keys.R -> reset = true;
            case Keys.I -> inventory = true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (pointer == 0 && button == 0) {
            leftMouseBtn = true;
        } else if (pointer == 0 && button == 0) {
            rightMouseBtn = true;
        }

        setMouseClickedPos(screenX, screenY);

        for (InputProcessor processor : this.processors) {
            processor.touchDown(screenX, screenY, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pointer == 0 && button == 0) {
            leftMouseBtn = false;
            processedClick = false;
        } else if (pointer == 0 && button == 0) {
            rightMouseBtn = false;
        }

        setMouseClickedPos(screenX, screenY);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        setMouseClickedPos(screenX, screenY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mousePos.set(screenX, screenHeight - screenY);
        return false;
    }

    public boolean scrolled(int amount) {
        return false;
    }

}