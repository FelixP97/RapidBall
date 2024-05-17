package com.game.pantallas;

import com.badlogic.gdx.Gdx;
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
    TextureRegion ball;
    public Pantalla1(RapidBall game) {
        super(game);
        Vector2 gravity = new Vector2(0,-9.8f);
        oWorld = new World(gravity,true);
        renderer = new Box2DDebugRenderer();

        ball = new TextureRegion(new Texture(Gdx.files.internal("data/ball.png")));

        createFloor();
        createBall();


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

        Body body = oWorld.createBody(bd);
        body.createFixture(fixDef);
        shape.dispose();
    }

    @Override
    public void draw(float delta) {
        oCamUI.update();
        spriteBatch.setProjectionMatrix(oCamUI.combined);

        spriteBatch.begin();
        Assets.font.draw(spriteBatch,"Fps:"+ Gdx.graphics.getFramesPerSecond(),0,20);
        spriteBatch.end();

        oCamBox2D.update();
        spriteBatch.setProjectionMatrix(oCamBox2D.combined);
        spriteBatch.begin();
        drawGameObjects();
        spriteBatch.end();

        renderer.render(oWorld, oCamBox2D.combined);
    }

    private void drawGameObjects(){
        
    }


    @Override
    public void update(float delta) {
        oWorld.step(delta,8,6);
    }

    static public class GameObject{
        static final int Ball = 0;
        static final int Box = 1;
        final int type;
        float angleDeg;
        Vector2 position;

        public GameObject(float x,float y,int type) {
            position = new Vector2(x,y);
            this.type = type;
        }
        public void update(Body body){
            position.x = body.getPosition().x;
            position.y = body.getPosition().y;
            angleDeg = (float) Math.toDegrees(body.getAngle());
        }
    }
}
