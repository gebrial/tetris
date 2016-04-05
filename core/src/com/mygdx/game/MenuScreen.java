package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by sanjaya on 29/03/16.
 */
public class MenuScreen implements Screen {
    final Tetris game;
    private int width, height;
    OrthographicCamera camera;

    public MenuScreen(Tetris tetris){
        game = tetris;

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glViewport(0, 0,
                width, height);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Welcome to Tetris", width/2, height/2);
        game.font.draw(game.batch, "Tap anywhere to begin", width/2, height/2 - 15);
        game.batch.end();
        if(Gdx.input.isKeyPressed(Input.Keys.N)
                || Gdx.input.isTouched()){
            game.setScreen(new GameScreen(game));
            dispose();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Q))
            Gdx.app.exit();
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        camera.setToOrtho(false, width, height);
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

    @Override
    public void dispose() {

    }
}
