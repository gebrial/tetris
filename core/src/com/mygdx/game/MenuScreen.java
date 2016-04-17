package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

/**
 * Created by sanjaya on 4/17/2016.
 */
public abstract class MenuScreen implements Screen {
    Tetris game;

    private Stage stage;
    private Table table;

    int width, height;

    public MenuScreen(Tetris tetris){
        Gdx.app.log("MenuScreen", "attached");

        game = tetris;

        width = 320;
        height = 480;

        stage = new Stage(new ExtendViewport(width, height));
        Gdx.input.setInputProcessor(stage);

        table = createTable();

        stage.addActor(table);
    }

    private Table createTable(){
        Table table = new Table(createSkin());
        table.setWidth(stage.getWidth());
        table.align(Align.center | Align.bottom);
        table.setPosition(0, 0);
        return table;
    }

    private Skin createSkin(){
        int singleSizeWidth = width/5;
        int singleSizeHeight = height/31;


        Skin skin = new Skin();
        Pixmap pixmap = new Pixmap(singleSizeWidth*3, singleSizeHeight*3, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        skin.add("default", new BitmapFont());

        // style options for buttons
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle); // add styles to button skin

        // font style for text
        TextField.TextFieldStyle tfs = new TextField.TextFieldStyle();
        tfs.font = new BitmapFont();
        tfs.fontColor = Color.WHITE;
        skin.add("default", tfs);

        return skin;
    }

    /*
     * Adds a new button to the current row and creates a new row.
     */
    void addNewButton(String text, EventListener listener){
        TextButton button = new TextButton(text, table.getSkin());
        button.addListener(listener);

        table.add(button).row();
    }

    /*
     * Adds a new text field to the current row and creates new row.
     * Returns the text field so that you can get the text if disabled == false
     */
    TextField addTextField(String text, boolean disabled){
        TextField textField = new TextField(text, table.getSkin());
        textField.setDisabled(disabled);
        table.add(textField).row();

        return textField;
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        Gdx.app.log("MenuScreen", "called show");
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * @param width
     * @param height
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
        Gdx.app.log("MenuScreen", "called resize");

        stage.getViewport().update(width, height, false);
    }

    /**
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {
        Gdx.app.log("MenuScreen", "called pause");

    }

    /**
     * @see ApplicationListener#resume()
     */
    @Override
    public void resume() {
        Gdx.app.log("MenuScreen", "called resume");

    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {
        Gdx.app.log("MenuScreen", "called hide");

    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        Gdx.app.log("MenuScreen", "called dispose");

        stage.dispose();
    }
}
