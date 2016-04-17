package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;


/**
 * Created by sanjaya on 29/03/16.
 */
public class MainScreen implements Screen {
    final Tetris game;

    Stage stage;
    Skin skin;

    Array<Button> buttons;

    public MainScreen(Tetris tetris){
        game = tetris;

        int width = 320;
        int height = 480;
        int singleSizeWidth = width/5;
        int singleSizeHeight = height/31;

        buttons = new Array<Button>(true, 4);

        stage = new Stage();
        stage.setViewport(new ExtendViewport(width, height, new OrthographicCamera()));
        Gdx.input.setInputProcessor(stage);

        // set up button texture/resource
        skin = new Skin();
        Pixmap pixmap = new Pixmap(singleSizeWidth*3, singleSizeHeight*3, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        skin.add("default", new BitmapFont()); // do not use existing BitmapFont, it will render black boxes if used elsewhere

        // style options for buttons
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");

        // add styles to button skin
        skin.add("default", textButtonStyle);

        // Create/Add buttons to stage
        final TextButton playButton = new TextButton("PLAY", textButtonStyle);
        playButton.setPosition(singleSizeWidth, singleSizeHeight*14);
        stage.addActor(playButton);
        buttons.add(playButton);
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });

        final TextButton optionsButton = new TextButton("OPTIONS", textButtonStyle);
        optionsButton.setPosition(singleSizeWidth, singleSizeHeight*10);
        stage.addActor(optionsButton);
        buttons.add(optionsButton);
        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new OptionScreen(game));
                dispose();
            }
        });

        final TextButton scoresButton = new TextButton("SCORES", textButtonStyle);
        scoresButton.setPosition(singleSizeWidth, singleSizeHeight*6);
        stage.addActor(scoresButton);
        buttons.add(scoresButton);
        scoresButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new ScoreScreen(game));
                dispose();
            }
        });

        final TextButton exitButton = new TextButton("EXIT", textButtonStyle);
        exitButton.setPosition(singleSizeWidth, singleSizeHeight*2);
        stage.addActor(exitButton);
        buttons.add(exitButton);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
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
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
        skin.dispose();
    }
}
