package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

/**
 * Created by Stephen on 4/13/2016.
 */
public class ScoreScreen implements Screen {
    Tetris game;

    Table table;
    Stage stage;
    Skin skin;

    public ScoreScreen(Tetris t){
        game = t;

        int width = 320;
        int height = 480;
        int singleSizeWidth = width/5;
        int singleSizeHeight = height/31;

        stage = new Stage(new ExtendViewport(width, height));
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setWidth(stage.getWidth());
        table.align(Align.center | Align.bottom);
        table.setPosition(0, 0);

        skin = new Skin();
        Pixmap pixmap = new Pixmap(singleSizeWidth*3, singleSizeHeight*3, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        skin.add("default", new BitmapFont());

        TextField.TextFieldStyle tfs = new TextField.TextFieldStyle();
        tfs.font = new BitmapFont();
        tfs.fontColor = Color.WHITE;
        skin.add("default", tfs);

        for(int i = 0; i < 10; i++) {
            TextField tf = new TextField(i + " : " + game.getHighScore(i), skin);
            tf.setDisabled(true);
            table.add(tf).row();
        }

        // style options for buttons
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");

        TextButton menuButton = new TextButton("Main Menu", textButtonStyle);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });
        table.add(menuButton);

        stage.addActor(table);

        stage.setDebugAll(true);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
    }
}
