package com.duckies.gdx.ninja.progressbar;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;

/**
 * The progress bar which reassembles the behaviour of the health bar.
 *
 * @author serhiy
 */
public class HealthBar extends ProgressBar {

    /**
     * @param width  of the health bar
     * @param height of the health bar
     */
    public HealthBar(int width, int height) {
        super(0f, 100f, 10f, false, new ProgressBarStyle());
        getStyle().background = Utils.getColoredDrawable(width, height, Color.RED);
        getStyle().knob = Utils.getColoredDrawable(0, height, Color.GREEN);
        getStyle().knobBefore = Utils.getColoredDrawable(width, height, Color.GREEN);

        setWidth(width);
        setHeight(height);

        setAnimateDuration(0.0f);
        setValue(100f);

        setAnimateDuration(0.25f);
    }
}