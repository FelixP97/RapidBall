package com.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.utils.Learn;

public class MainManuScreen extends Screens{
    ScrollPane scroll;

    public MainManuScreen(RapidBall game) {
        super(game);

        Table menu = new  Table();
        menu.setFillParent(true);
        menu.defaults().uniform().fillY();

        for(final Learn learn : Learn.values()){
            TextButton bt = new TextButton(learn.name, Assets.textButtonStyle);
            bt.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    MainManuScreen.this.game.setScreen(getScreen(learn));
                }
            });

            menu.row().padTop(20).height(50);
            menu.add(bt).fillX();
        }

        scroll = new ScrollPane(menu,Assets.scrollPaneStyle);
        scroll.setSize(500,SCREEN_HEIGHT);
        scroll.setPosition(150,0);
        stage.addActor(scroll);
    }

    private Screens getScreen(Learn learn){
        switch (learn){
            default:
                return new Learn1(game);

        }
    }

    @Override
    public void draw(float delta) {

    }

    @Override
    public void update(float delta) {

    }
}
