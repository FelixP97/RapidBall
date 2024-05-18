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

public class Pantalla1 extends Screens {

    Box2DDebugRenderer renderer;
    World oWorld;
    TextureRegion ballTexture;

    private Body ballBody;
    public Pantalla1(RapidBall game) {
        super(game);
        initializeWorld();
        initializeRenderer();
        loadTextures();
        createFloor();
        createBall();


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
        BodyDef bd = new BodyDef();
        bd.position.set(4f,11f);
        bd.type = BodyDef.BodyType.DynamicBody;

        CircleShape shape = new CircleShape();
        shape.setRadius(.15f);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 15;
        fixDef.friction = .5f;
        fixDef.restitution = .5f;

        ballBody = oWorld.createBody(bd);
        ballBody.createFixture(fixDef);
        shape.dispose();
    }

    @Override
    public void draw(float delta) {
        oCamUI.update();
        spriteBatch.setProjectionMatrix(oCamBox2D.combined);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        // Dibujar la textura de la bola en la posición del cuerpo
        Vector2 ballPosition = ballBody.getPosition();
        spriteBatch.draw(ballTexture,
                ballPosition.x - 0.15f,
                ballPosition.y - 0.15f,
                0.3f,
                0.3f);

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
        oWorld.dispose();
        renderer.dispose();
        super.dispose();
    }
}
