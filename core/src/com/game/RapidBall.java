package com.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class RapidBall extends Game {


    @Override
    public void create() {
        Assets.load();
        setScreen(new MainManuScreen(this));
    }
}
