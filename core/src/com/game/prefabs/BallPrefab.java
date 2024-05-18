package com.game.prefabs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class BallPrefab {
    private Body body;
    private TextureRegion texture;
    private float radius;
    private World world;

    public BallPrefab(World world, TextureRegion texture, float x, float y, float radius){
        this.world = world;
        this.texture = new TextureRegion(texture);
        this.radius = radius;
        createBallBody(x, y);
    }
    private void createBallBody(float x, float y) {
        BodyDef bd = new BodyDef();
        bd.position.set(x, y);
        bd.type = BodyDef.BodyType.DynamicBody;

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 15;
        fixDef.friction = 0.5f;
        fixDef.restitution = 0.5f;

        body = world.createBody(bd);
        body.createFixture(fixDef);
        shape.dispose();
    }
    public void draw(SpriteBatch spriteBatch) {
        Vector2 position = body.getPosition();
        spriteBatch.draw(texture,
                position.x - radius,
                position.y - radius,
                radius * 2,
                radius * 2);
    }

    public Body getBody() {
        return body;
    }
}
