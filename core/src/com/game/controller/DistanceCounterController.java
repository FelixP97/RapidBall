package com.game.controller;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class DistanceCounterController {
    private float distance;
    private BitmapFont font;
    public DistanceCounterController(){
        this.distance = 0;
        this.font = new BitmapFont();
        font.getData().setScale(2.25f);
    }

    public void update(float delta, float speed) {
        distance += speed * delta;
    }

    public void reset() {
        distance = 0;
    }

    public void draw(SpriteBatch spriteBatch, float x, float y) {
        font.draw(spriteBatch, "Distance: " + (int) distance, x, y);
    }

    public float getDistance() {
        return distance;
    }

    public void dispose() {
        font.dispose();
    }
}
