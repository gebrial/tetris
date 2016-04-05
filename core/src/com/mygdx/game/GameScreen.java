package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import static com.badlogic.gdx.math.MathUtils.floor;

/**
 * Created by sanjaya on 29/03/16.
 */
public class GameScreen implements Screen {
    private final Tetris game;
    private OrthographicCamera camera;
    private int height, width;
    private int viewBlockSize, cameraBlockSize;

    public GameScreen(Tetris tetris){
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();

        game = tetris;
        game.start();

        height = Gdx.graphics.getHeight();
        width = Gdx.graphics.getWidth();

        viewBlockSize = height*12 < width*25 ? height/25: width/12;
        cameraBlockSize = 60;

        setupInputProcessor();
    }

    /*
     * Setup event handling for android swipes.
     */
    private void setupInputProcessor(){
        Gdx.input.setInputProcessor(new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.DirectionListener() {
            @Override
            public void onUp() {
                if(!game.isPaused()) {
                    int pos = Gdx.input.getX();
                    if (pos < width / 2)
                        game.getPiece().rotateCW();
                    else
                        game.getPiece().rotateCCW();
                }
            }

            @Override
            public void onRight() {
                if(!game.isPaused())
                    game.getPiece().moveRight();
            }

            @Override
            public void onLeft() {
                if(!game.isPaused())
                    game.getPiece().moveLeft();
            }

            @Override
            public void onDown() {
                if(!game.isPaused())
                    game.getPiece().moveDown();
            }
        }));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        checkInput();

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        if(!game.isPaused())
            game.update();

        drawBoard();
        drawNextPiece();
        drawScore();
    }

    /*
     * Poll for input
     */
    private void checkInput(){
        if(!game.isPaused()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                game.getPiece().moveLeft();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                game.getPiece().moveRight();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                game.getPiece().moveDown();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                game.rotatePieceCCW();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                game.rotatePieceCW();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
                Gdx.app.exit();
            }
            if (//Gdx.input.isTouched() ||
                    Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                game.pause();
            }
        } else
        if (//Gdx.input.isTouched() ||
                Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            game.resume();
    }

    private void drawScore() {
        Gdx.gl.glViewport(viewBlockSize, height - 2*viewBlockSize,
                viewBlockSize*5, viewBlockSize);

        // print score
        camera.setToOrtho(false, viewBlockSize*5, viewBlockSize);
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.draw(game.batch, Integer.toString(game.getScore()), 0, viewBlockSize);
        game.batch.end();
    }

    private void drawNextPiece(){
        Gdx.gl.glViewport(width - 5*viewBlockSize, height - 3*viewBlockSize,
                viewBlockSize*4, viewBlockSize*2);

        int size = cameraBlockSize;
        camera.setToOrtho(false, size*4, size*2);

        // draw next piece
        game.shapeRenderer.setProjectionMatrix(camera.combined);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(game.getNextPiece().getColor());
        for(Block block : game.getNextPiece())
            game.shapeRenderer.box((block.getColumn() - floor(game.getNextPiece().getPivot().x) + 1)*size + 1,
                    (block.getRow() - floor(game.getNextPiece().getPivot().y))*size + 1, 0,
                    size - 2, size - 2, 0);
        game.shapeRenderer.end();
    }

    /*
     * Draw everything on screen
     */
    private void drawBoard(){

        Gdx.gl.glViewport(width/2 - viewBlockSize*5, viewBlockSize,
                viewBlockSize*10, viewBlockSize*20);

        int size = cameraBlockSize;
        camera.setToOrtho(false, size*10, size*20);
        game.shapeRenderer.setProjectionMatrix(camera.combined);

        // draw blocks
        Color current = null;
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for(Block block : game) {
            if (!block.getColor().equals(current)) {
                current = block.getColor();
                game.shapeRenderer.end();
                game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                game.shapeRenderer.setColor(current);
            }
            game.shapeRenderer.box(block.getColumn() * size + 1, block.getRow() * size + 1, 0, size - 2, size - 2, 0);
        }
        game.shapeRenderer.end();

        // draw current piece
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(game.getPiece().getColor());
        for(Block block : game.getPiece())
            game.shapeRenderer.box(block.getColumn()*size + 1, block.getRow()*size + 1, 0, size - 2, size - 2, 0);
        game.shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        viewBlockSize = height*12 < width*25 ? height/25: width/12;
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