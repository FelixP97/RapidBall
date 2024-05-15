package com.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Utils {

    public static TextureRegion getRegion(String path){
        return new TextureRegion(new Texture(Gdx.files.internal(path)));
    }
}
