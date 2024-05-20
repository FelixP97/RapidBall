package com.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.game.Assets;
import com.game.RapidBall;
import com.game.Screens;
import com.game.controller.PlayerController;
import com.game.prefabs.BallPrefab;
import com.game.prefabs.PlatfomPrefab;

public class Pantalla1 extends Screens {

    Box2DDebugRenderer renderer;
    World oWorld;
    private BallPrefab ballPrefab;
    private PlatfomPrefab platfomPrefab;
    TextureRegion ballTexture,platformTexture;

    private PlayerController playerController;
    private boolean dragging = false;
    private Vector2 lastTouch = new Vector2();

    public Pantalla1(RapidBall game) {
        super(game);
        initializeWorld();
        initializeRenderer();
        loadTextures();
        createFloor();
        createBall();
        createPlatform();

        // Initialize PlayerController with the BallPrefab instance
        playerController = new PlayerController(ballPrefab);


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
    private void createFloor(){
        BodyDef bd = new BodyDef();
        bd.position.set(0,0.5f);
        bd.type = BodyDef.BodyType.StaticBody;

        EdgeShape shape = new EdgeShape();
        shape.set(0,0,WORLD_WIDTH,0);

        FixtureDef fixDef= new FixtureDef();
        fixDef.shape = shape;

        Body oBody = oWorld.createBody(bd);
        oBody.createFixture(fixDef);
        shape.dispose();
    }

    private void createBall(){
        ballPrefab = new BallPrefab(oWorld, ballTexture, 4f,11f,0.15f);
    }

    private void createPlatform(){
        platfomPrefab = new PlatfomPrefab(oWorld,platformTexture,4f,5f);
    }

    @Override
    public void draw(float delta) {
        oCamUI.update();
        spriteBatch.setProjectionMatrix(oCamBox2D.combined);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();

        // Dibujar la bola utilizando el prefab
        ballPrefab.draw(spriteBatch);
        platfomPrefab.draw(spriteBatch);

        spriteBatch.end();


        renderer.render(oWorld, oCamBox2D.combined);

        oCamBox2D.update();
        spriteBatch.setProjectionMatrix(oCamUI.combined);

        spriteBatch.begin();
        Assets.font.draw(spriteBatch,"Fps:"+ Gdx.graphics.getFramesPerSecond(),0,20);
        spriteBatch.end();
    }




    @Override
    public void update(float delta) {
        oWorld.step(delta,8,6);
    }

    @Override
    public void dispose() {
        // Libera recursos cuando ya no sean necesarios
        ballTexture.getTexture().dispose();
        platformTexture.getTexture().dispose();
        oWorld.dispose();
        renderer.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        dragging = true;
        lastTouch.set(screenX, screenY);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
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
        dragging = false;
        return true;
    }
}
