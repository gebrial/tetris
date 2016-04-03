package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by sanjaya on 29/03/16.
 */
public class GameScreen implements Screen {
    private final Tetris game;
    private OrthographicCamera camera;
    private int height, width;

    public GameScreen(Tetris tetris){
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);

        game = tetris;
        game.start();

        height = Gdx.graphics.getHeight();
        width = Gdx.graphics.getWidth();

        Gdx.input.setInputProcessor(new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.DirectionListener() {

            @Override
            public void onUp() {
                // TODO Auto-generated method stub
                int pos = Gdx.input.getX();
                if(pos < width/2)
                    game.getPiece().rotateCW();
                else
                    game.getPiece().rotateCCW();
            }

            @Override
            public void onRight() {
                // TODO Auto-generated method stub
                game.getPiece().moveRight();
            }

            @Override
            public void onLeft() {
                // TODO Auto-generated method stub
                game.getPiece().moveLeft();
            }

            @Override
            public void onDown() {
                // TODO Auto-generated method stub
                game.getPiece().moveDown();
            }
        }));
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
            game.rotatePieceCCW();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            game.rotatePieceCW();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            Gdx.app.exit();
        }


        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.update();


        int size = 60;
        game.shapeRenderer.setProjectionMatrix(camera.combined);
        Color current = null;
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for(Block block : game) {
            if (!block.getColor().equals(current)) {
                current = block.getColor();
                game.shapeRenderer.end();
                game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                game.shapeRenderer.setColor(current);
            }
            game.shapeRenderer.box(block.getColumn() * size + 1, block.getRow() * size + 1, 0, size - 1, size - 1, 0);
        }
        game.shapeRenderer.end();

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(game.getPiece().getColor());
        for(Block block : game.getPiece())
            game.shapeRenderer.box(block.getColumn()*size + 1, block.getRow()*size + 1, 0, size - 1, size - 1, 0);
        game.shapeRenderer.end();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.draw(game.batch, Integer.toString(game.getScore()), 10, height - 10);
        game.batch.end();

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