package com.game.pantallas;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.game.RapidBall;
import com.game.Screens;

public class Pantalla1 extends Screens {

    Box2DDebugRenderer renderer;
    World oWorld;
    public Pantalla1(RapidBall game) {
        super(game);
        Vector2 gravity = new Vector2(0,-9.8f);
        oWorld = new World(gravity,true);
        renderer = new Box2DDebugRenderer();
    }

    @Override
    public void draw(float delta) {

    }

    @Override
    public void update(float delta) {

    }
}
