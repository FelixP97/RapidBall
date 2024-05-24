package com.game.controller;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameCameraController {
    private OrthographicCamera camera;
    private float cameraSpeed;
    private float cameraAcceleration;
    private float maxCameraSpeed;
    private boolean active;

    public GameCameraController(float viewportWidth, float viewportHeight){
        this.camera = new OrthographicCamera(viewportWidth, viewportHeight);
        this.camera.position.set(viewportWidth / 2, viewportHeight / 2, 0);
        this.camera.update();

        // Inicializar valores de la cámara
        this.cameraSpeed = 0.5f;
        this.cameraAcceleration = 0.01f;
        this.maxCameraSpeed = 5f;
    }

    public void update(float delta) {
        if (!active) return;

        camera.position.y -= cameraSpeed * delta;

        if (cameraSpeed < maxCameraSpeed) {
            cameraSpeed += cameraAcceleration * delta;
        }

        camera.update();
    }
    public OrthographicCamera getCamera() {
        return camera;
    }

    public void reset() {
        this.camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        this.cameraSpeed = 0.5f;
        this.camera.update();
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}
