package com.game.prefabs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class PlatfomPrefab {
    private Body body;
    private TextureRegion texture;
    private World world;

    public PlatfomPrefab(World world, TextureRegion texture,float x, float y){
        this.world = world;
        this.texture = new TextureRegion(texture);
        createPlatformBody(x, y);

    }
    private void createPlatformBody(float x, float y) {
        BodyDef bd = new BodyDef();
        bd.position.set(x, y);
        bd.type = BodyDef.BodyType.KinematicBody;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f,0.2f);// Tamaño de la plataforma

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;


        body = world.createBody(bd);
        body.createFixture(fixDef);
        shape.dispose();
    }

    public void draw(SpriteBatch spriteBatch) {
        Vector2 position = body.getPosition();
        spriteBatch.draw(texture,
                position.x - 2,
                position.y - 0.5f,
                4, 1);
    }

    public Body getBody() {
        return body;
    }
}
