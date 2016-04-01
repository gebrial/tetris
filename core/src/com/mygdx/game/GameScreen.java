package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by sanjaya on 29/03/16.
 */
public class GameScreen implements Screen {
    private final Tetris game;
    private OrthographicCamera camera;

    public GameScreen(Tetris tetris){
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);

        game = tetris;
        game.start();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            game.getPiece().moveLeft();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            game.getPiece().moveRight();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            game.getPiece().moveDown();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            boolean rot = game.rotatePieceCCW();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            boolean rot = game.rotatePieceCW();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            Gdx.app.exit();
        }


        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.update();


        game.shapeRenderer.setProjectionMatrix(camera.combined);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(Color.RED);
        for(Block block : game)
            game.shapeRenderer.box(block.getColumn()*40, block.getRow()*40, 0, 40, 40, 0);
        for(Block block : game.getPiece())
            game.shapeRenderer.box(block.getColumn()*40, block.getRow()*40, 0, 40, 40, 0);
        game.shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {

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