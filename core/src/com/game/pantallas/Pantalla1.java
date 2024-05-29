package com.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.game.Assets;
import com.game.RapidBall;
import com.game.Screens;
import com.game.controller.DistanceCounterController;
import com.game.controller.GameCameraController;
import com.game.controller.PlayerController;
import com.game.prefabs.BallPrefab;
import com.game.prefabs.PlatfomPrefab;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.Random;

public class Pantalla1 extends Screens {

    Box2DDebugRenderer renderer;
    World oWorld;
    private BallPrefab ballPrefab;
    TextureRegion ballTexture,platformTexture;
    private PlayerController playerController;
    private boolean dragging = false;
    private Vector2 lastTouch = new Vector2();
    private Array<PlatfomPrefab> platforms;
    private Random random;
    private boolean isGameOver = false;
    private BitmapFont gameOverFont;
    private BitmapFont restartFont;
    private String restartMessage = "Jugar de nuevo";
    private float restartX, restartY;

    private GameCameraController gameCamera;
    private DistanceCounterController distanceCounter;

    public Pantalla1(RapidBall game) {
        super(game);
        initializeWorld();
        initializeRenderer();
        loadTextures();
        createBall();
        random = new Random();
        createPlatforms();

        playerController = new PlayerController(ballPrefab);

        gameOverFont = new BitmapFont();
        gameOverFont.getData().setScale(3f);

        restartFont = new BitmapFont();
        restartFont.getData().setScale(2.25f);

        restartX = oCamUI.viewportWidth / 2f - restartFont.getRegion().getRegionWidth() / 2;
        restartY = oCamUI.viewportHeight / 2f - gameOverFont.getCapHeight() / 2 - 50; // Ajustar la posición según sea necesario

        // Inicializar GameCamera
        gameCamera = new GameCameraController(oCamBox2D.viewportWidth, oCamBox2D.viewportHeight);
        gameCamera.setActive(true); // Activar la cámara al inicio

        // Inicializar DistanceCounter
        distanceCounter = new DistanceCounterController();
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
        gameCamera.update(delta);

        oCamUI.update();
        spriteBatch.setProjectionMatrix(gameCamera.getCamera().combined);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();

        ballPrefab.draw(spriteBatch);

        for (PlatfomPrefab platform : platforms) {
            platform.draw(spriteBatch);
        }

        spriteBatch.end();

       // if (!isGameOver) {
         //   renderer.render(oWorld, gameCamera.getCamera().combined);
        //}

        spriteBatch.setProjectionMatrix(oCamUI.combined);

        spriteBatch.begin();
        // Dibujar el contador de distancia en la esquina superior derecha
        float distanceX = oCamUI.viewportWidth - 200; // Ajustar según sea necesario
        float distanceY = oCamUI.viewportHeight - 20; // Ajustar según sea necesario
        distanceCounter.draw(spriteBatch, distanceX, distanceY);


        Assets.font.draw(spriteBatch,"Fps:"+ Gdx.graphics.getFramesPerSecond(),0,20);
        // Mostrar mensaje de Game Over si el juego ha terminado
        if (isGameOver) {
            String gameOverMessage = "Game Over";
            float messageWidth = gameOverFont.getRegion().getRegionWidth();
            float messageHeight = gameOverFont.getCapHeight();
            float messageX = oCamUI.viewportWidth / 2f - messageWidth / 2;
            float messageY = oCamUI.viewportHeight / 2f + messageHeight / 2;
            gameOverFont.draw(spriteBatch, gameOverMessage, messageX, messageY);

            restartX = oCamUI.viewportWidth / 2f - restartFont.getRegion().getRegionWidth() / 2;
            restartY = messageY - 50; // Debajo del mensaje de Game Over
            restartFont.draw(spriteBatch, restartMessage, restartX, restartY);
        }
        spriteBatch.end();
    }
    @Override
    public void update(float delta) {
        if (isGameOver) {
            return;
        }

        oWorld.step(delta,8,6);
        Vector2 ballPosition = ballPrefab.getBody().getPosition();

        // Actualizar contador de distancia
        distanceCounter.update(delta, ballPrefab.getBody().getLinearVelocity().len());

        gameCamera.update(delta);

        generateNewPlatforms();

        for (PlatfomPrefab platform : platforms) {
            platform.update(delta);
        }

        checkGameOver();

    }

    private void generateNewPlatforms() {
        float cameraBottom = gameCamera.getCamera().position.y - gameCamera.getCamera().viewportHeight / 2;

        Array<PlatfomPrefab> platformsToRemove = new Array<PlatfomPrefab>();
        for (PlatfomPrefab platform : platforms) {
            if (platform.getBody().getPosition().y > gameCamera.getCamera().position.y + gameCamera.getCamera().viewportHeight / 2) {
                platformsToRemove.add(platform);
            }
        }
        platforms.removeAll(platformsToRemove, true);

        while (platforms.size < 10) {
            float x = random.nextFloat() * (gameCamera.getCamera().viewportWidth - 1) + 0.5f;
            float y = cameraBottom - random.nextFloat() * 2f;
            platforms.add(new PlatfomPrefab(oWorld, platformTexture, x, y));
            cameraBottom -= 2f;
        }
    }

    private void checkGameOver() {
        Vector2 ballPosition = ballPrefab.getBody().getPosition();
        float cameraTop = gameCamera.getCamera().position.y + gameCamera.getCamera().viewportHeight / 2;
        float cameraBottom = gameCamera.getCamera().position.y - gameCamera.getCamera().viewportHeight / 2;

        if (ballPosition.y > cameraTop || ballPosition.y < cameraBottom) {
            isGameOver = true;
            gameCamera.setActive(false); // Detener la cámara
        }
    }

    @Override
    public void dispose() {
        ballTexture.getTexture().dispose();
        platformTexture.getTexture().dispose();
        oWorld.dispose();
        renderer.dispose();
        gameOverFont.dispose();
        restartFont.dispose();
        distanceCounter.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (isGameOver) {
            Vector3 touchPos = new Vector3(screenX, screenY, 0);
            oCamUI.unproject(touchPos);

            if (touchPos.x >= restartX && touchPos.x <= restartX + restartFont.getRegion().getRegionWidth()
                    && touchPos.y >= restartY - restartFont.getCapHeight() && touchPos.y <= restartY) {
                restartGame();
            }
            return true;
        }
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

    private void restartGame() {
        initializeWorld();
        createBall();
        createPlatforms();
        playerController = new PlayerController(ballPrefab);
        isGameOver = false;
        gameCamera.reset();
        gameCamera.setActive(true); // Reactivar la cámara
        distanceCounter.reset(); // Reiniciar el contador de distancia
    }
}