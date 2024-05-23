package com.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.game.Assets;
import com.game.RapidBall;
import com.game.Screens;
import com.game.controller.PlayerController;
import com.game.prefabs.BallPrefab;
import com.game.prefabs.PlatfomPrefab;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.Random;

public class Pantalla1 extends Screens {

    Box2DDebugRenderer renderer;
    World oWorld;
    private BallPrefab ballPrefab;
    private PlatfomPrefab platfomPrefab;
    TextureRegion ballTexture,platformTexture;

    private PlayerController playerController;
    private boolean dragging = false;
    private Vector2 lastTouch = new Vector2();
    private Array<PlatfomPrefab> platforms;
    private Random random;

    private float cameraSpeed = 0.5f; // Velocidad inicial de la cámara
    private float cameraAcceleration = 0.01f; // Incremento de la velocidad de la cámara
    private float maxCameraSpeed = 5f; // Velocidad máxima de la cámara

    private boolean isGameOver = false;

    // Definir una variable para el mensaje de Game Over
    private BitmapFont gameOverFont;

    public Pantalla1(RapidBall game) {
        super(game);
        initializeWorld();
        initializeRenderer();
        loadTextures();
        createBall();
        random = new Random();
        createPlatforms();

        // Initialize PlayerController with the BallPrefab instance
        playerController = new PlayerController(ballPrefab);

        // Inicializar y escalar la fuente para el mensaje de Game Over
        gameOverFont = new BitmapFont();
        gameOverFont.getData().setScale(3f);


    }
    private void initializeWorld(){
        Vector2 gravity = new Vector2(0,-9.8f);
        oWorld = new World(gravity,true);
    }

    private void initializeRenderer(){
        renderer = new Box2DDebugRenderer();
    }

    private void loadTextures(){
        ballTexture = new TextureRegion(new Texture(Gdx.files.internal("data/ball.png")));
        platformTexture = new TextureRegion(new Texture(Gdx.files.internal("data/platform.png")));
    }


    private void createBall(){
        ballPrefab = new BallPrefab(oWorld, ballTexture, 4f,11f,0.15f);
    }

    private void createPlatforms(){
        platforms = new Array<PlatfomPrefab>();
        // Crear varias plataformas con posiciones aleatorias
        for (int i = 0; i < 10; i++) {
            float x = random.nextFloat() * (oCamBox2D.viewportWidth - 1) + 0.5f; // Valor aleatorio entre 0.5 y viewportWidth - 0.5
            float y = i * 2f; // Separación vertical de 2 unidades entre plataformas
            platforms.add(new PlatfomPrefab(oWorld, platformTexture, x, y));

        }
    }

    @Override
    public void draw(float delta) {
        oCamUI.update();
        spriteBatch.setProjectionMatrix(oCamBox2D.combined);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();

        // Dibujar la bola utilizando el prefab
        ballPrefab.draw(spriteBatch);
        // Dibujar todas las plataformas
        for (PlatfomPrefab platform : platforms) {
            platform.draw(spriteBatch);
        }

        spriteBatch.end();

        if (!isGameOver) {
            renderer.render(oWorld, oCamBox2D.combined);
        }


        oCamBox2D.update();
        spriteBatch.setProjectionMatrix(oCamUI.combined);

        spriteBatch.begin();
        Assets.font.draw(spriteBatch,"Fps:"+ Gdx.graphics.getFramesPerSecond(),0,20);
        // Mostrar mensaje de Game Over si el juego ha terminado
        if (isGameOver) {
            String gameOverMessage = "Game Over";
            float messageWidth = gameOverFont.getRegion().getRegionWidth();
            float messageHeight = gameOverFont.getCapHeight();
            float messageX = oCamUI.viewportWidth / 2f - messageWidth / 2;
            float messageY = oCamUI.viewportHeight / 2f + messageHeight / 2;
            gameOverFont.draw(spriteBatch, gameOverMessage, messageX, messageY);
        }
        spriteBatch.end();
    }




    @Override
    public void update(float delta) {
        if (isGameOver) {
            return; // No actualizar nada si el juego ha terminado
        }

        oWorld.step(delta,8,6);
        Vector2 ballPosition = ballPrefab.getBody().getPosition();

        // Actualizar la posición de la cámara para que se mueva hacia abajo
        oCamBox2D.position.y -= cameraSpeed * delta;

        // Incrementar la velocidad de la cámara gradualmente
        if (cameraSpeed < maxCameraSpeed) {
            cameraSpeed += cameraAcceleration * delta;
        }

        // Generar nuevas plataformas por debajo de la cámara
        generateNewPlatforms();

        // Actualizar todas las plataformas
        for (PlatfomPrefab platform : platforms) {
            platform.update(delta);
        }

        // Verificar si el jugador sale de los límites de la pantalla
        checkGameOver();

        oCamBox2D.update();
    }

    private void generateNewPlatforms() {
        float cameraBottom = oCamBox2D.position.y - oCamBox2D.viewportHeight / 2;

        // Eliminar plataformas que están fuera de la vista
        Array<PlatfomPrefab> platformsToRemove = new Array<PlatfomPrefab>();
        for (PlatfomPrefab platform : platforms) {
            if (platform.getBody().getPosition().y > oCamBox2D.position.y + oCamBox2D.viewportHeight / 2) {
                platformsToRemove.add(platform);
            }
        }
        platforms.removeAll(platformsToRemove, true);

        // Generar nuevas plataformas por debajo de la cámara
        while (platforms.size < 10) { // Siempre tener 10 plataformas en pantalla
            float x = random.nextFloat() * (oCamBox2D.viewportWidth - 1) + 0.5f;
            float y = cameraBottom - random.nextFloat() * 2f; // Generar en una altura ligeramente por debajo de la vista actual
            platforms.add(new PlatfomPrefab(oWorld, platformTexture, x, y));
            cameraBottom -= 2f; // Separación vertical de 2 unidades entre plataformas
        }
    }

    private void checkGameOver() {
        Vector2 ballPosition = ballPrefab.getBody().getPosition();
        float cameraTop = oCamBox2D.position.y + oCamBox2D.viewportHeight / 2;
        float cameraBottom = oCamBox2D.position.y - oCamBox2D.viewportHeight / 2;

        // Verificar si el jugador se sale de los límites de la pantalla
        if (ballPosition.y > cameraTop || ballPosition.y < cameraBottom) {
            // Acción de game over: puedes mostrar una pantalla de game over, reiniciar el nivel, etc.
            isGameOver = true;
        }
    }

    @Override
    public void dispose() {
        // Libera recursos cuando ya no sean necesarios
        ballTexture.getTexture().dispose();
        platformTexture.getTexture().dispose();
        oWorld.dispose();
        renderer.dispose();
        gameOverFont.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (isGameOver) return false;
        dragging = true;
        lastTouch.set(screenX, screenY);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (isGameOver) return false;
        if (dragging) {
            Vector2 currentTouch = new Vector2(screenX, screenY);
            float deltaX = currentTouch.x - lastTouch.x;
            // Llama al método handleInput del controlador del jugador
            playerController.handleInput(deltaX);
            lastTouch.set(currentTouch);
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (isGameOver) return false;
        dragging = false;
        return true;
    }
}
