package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by sanjaya on 4/17/2016.
 */
class OptionScreen extends MenuScreen {
    private TextField garbageLines;

    OptionScreen(Tetris g){
        super(g);

        Gdx.app.log("OptionScreen", "attached");

        garbageLines = addGarbageLinesOption();
    }

    private TextField addGarbageLinesOption(){
        addTextField("Number of Garbage Lines:", true);

        TextField garbageLines = addTextField(Integer.toString(game.getGarbageStart()), false);
        garbageLines.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        garbageLines.setMaxLength(2);

        addNewButton("Main Menu", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainScreen(game));
                dispose();
            }
        });

        return garbageLines;
    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        Gdx.app.log("OptionScreen", "called dispose");

        game.setGarbageStart(Integer.parseInt("0" + garbageLines.getText()));
        super.dispose();
    }
}
