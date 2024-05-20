package com.game.controller;

import com.badlogic.gdx.math.Vector2;
import com.game.prefabs.BallPrefab;

public class PlayerController {
    private BallPrefab player;

    public PlayerController(BallPrefab player){
        this.player = player;
    }

    public void handleInput(float deltaX){
        if (deltaX > 0) {
            // Mueve hacia la derecha
            player.getBody().applyForceToCenter(new Vector2(5, 0), true);
        } else if (deltaX < 0) {
            // Mueve hacia la izquierda
            player.getBody().applyForceToCenter(new Vector2(-5, 0), true);
        }
    }
}
