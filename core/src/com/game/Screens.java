package com.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public abstract class Screens extends InputAdapter implements Screen {
    public static final float SCREEN_WIDTH = 640;
    public static  final  float SCREEN_HEIGHT = 920;

    public static  final  float WORLD_HEIGHT = 11.5f;
    public static  final  float WORLD_WIDTH = 8f;

    public RapidBall game;
    public OrthographicCamera oCamUI;
    public OrthographicCamera oCamBox2D;
    public SpriteBatch spriteBatch;

    public Stage stage;

    public Screens(RapidBall game){
        this.game = game;

        stage = new Stage(new StretchViewport(Screens.SCREEN_WIDTH,Screens.SCREEN_HEIGHT));

        oCamUI = new OrthographicCamera(Screens.SCREEN_WIDTH,Screens.SCREEN_HEIGHT );
        oCamUI.position.set(SCREEN_WIDTH / 2f,SCREEN_HEIGHT / 2f,0);


        oCamBox2D = new OrthographicCamera(Screens.WORLD_WIDTH,Screens.WORLD_HEIGHT);
        oCamBox2D.position.set(WORLD_WIDTH / 2f,WORLD_HEIGHT / 2f,0);

        InputMultiplexer input = new InputMultiplexer(this, stage);
        Gdx.input.setInputProcessor(input);

        spriteBatch = new SpriteBatch();

    }




    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        stage.act(delta);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        draw(delta);

        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        // Actualizar la vista del stage
        stage.getViewport().update(width, height, true);



    }

    public abstract void draw(float delta);

    public abstract void update(float delta);

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
