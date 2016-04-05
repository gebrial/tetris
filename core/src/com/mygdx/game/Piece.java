package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

import static com.badlogic.gdx.math.MathUtils.floor;
import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Created by sanjaya on 29/03/16.
 */
	/*
	 * Collection of blocks that forms a shape.
	 */
public class Piece implements Iterable<Block>{
    Array<Block> blocks;
    Vector2 pivot;
    final Tetris game;
    private Color color;

    /*
     * Creates new random piece at top of board.
     */
    public Piece(Tetris tetris){
        blocks = new Array<Block>(false, 4);
        game = tetris;

        setRandom();
    }

    private void setRandom(){
        int rand = random(6);

        switch(rand){
            case 0:
                setL();
                break;
            case 1:
                setJ();
                break;
            case 2:
                setO();
                break;
            case 3:
                setT();
                break;
            case 4:
                setI();
                break;
            case 5:
                setS();
                break;
            case 6:
                setZ();
                break;
            default:
                setO();
                break;
        }
    }

    private void setT(){
        blocks.clear();
        Color c = Color.PURPLE;
        color = c;

        int xPivot = floor(game.getColumns()/2);
        int yPivot = game.getRows() - 2;

        blocks.add(new Block(game, xPivot, yPivot, c));
        blocks.add(new Block(game, xPivot + 1, yPivot, c));
        blocks.add(new Block(game, xPivot - 1, yPivot, c));
        blocks.add(new Block(game, xPivot, yPivot + 1, c));

        pivot = new Vector2(xPivot, yPivot);
    }

    private void setS(){
        setSZ(true);
    }

    private void setZ(){
        setSZ(false);
    }

    private void setSZ(boolean Ess){
        blocks.clear();
        Color c = Ess ? Color.LIME: Color.RED;
        color = c;

        int xPivot = floor(game.getColumns()/2);
        int yPivot = game.getRows() - 2;

        int offset = Ess ? 1 : 0;
        blocks.add(new Block(game, xPivot + 1, yPivot + 1 - offset, c));
        blocks.add(new Block(game, xPivot - 1, yPivot + offset, c));
        blocks.add(new Block(game, xPivot, yPivot, c));
        blocks.add(new Block(game, xPivot, yPivot + 1, c));

        pivot = new Vector2(xPivot, yPivot);
    }

    private void setI(){
        blocks.clear();
        Color c = Color.CYAN;
        color = c;

        int xPivot = floor(game.getColumns()/2);
        int yPivot = game.getRows() - 1;

        blocks.add(new Block(game, xPivot, yPivot, c));
        blocks.add(new Block(game, xPivot + 1, yPivot, c));
        blocks.add(new Block(game, xPivot - 1, yPivot, c));
        blocks.add(new Block(game, xPivot + 2, yPivot, c));

        pivot = new Vector2(xPivot, yPivot);
    }

    private void setL(){
        setLJ(true);
    }

    private void setJ(){
        setLJ(false);
    }

    private void setLJ(boolean Ell){
        blocks.clear();
        Color c = Ell ? Color.ORANGE: Color.BLUE;
        color = c;

        int xPivot = floor(game.getColumns()/2);
        int yPivot = game.getRows() - 2;

        int offset = Ell ? -1: 1;
        blocks.add(new Block(game, xPivot + offset, yPivot + 1, c));
        blocks.add(new Block(game, xPivot, yPivot, c));
        blocks.add(new Block(game, xPivot + 1, yPivot, c));
        blocks.add(new Block(game, xPivot - 1, yPivot, c));

        pivot = new Vector2(xPivot, yPivot);
    }

    private void setO(){
        blocks.clear();
        Color c = Color.YELLOW;
        color = c;

        blocks.add(new Block(game, floor(game.getColumns()/2), game.getRows() - 1, c));
        blocks.add(new Block(game, floor(game.getColumns()/2), game.getRows() - 2, c));
        blocks.add(new Block(game, floor(game.getColumns()/2) + 1, game.getRows() - 2, c));
        blocks.add(new Block(game, floor(game.getColumns()/2) + 1, game.getRows() - 1, c));

        float xPivot = (float) (floor(game.getColumns()/2) + 1./2.);
        float yPivot = (float) (game.getRows() - 3./2.);
        pivot = new Vector2(xPivot, yPivot);
    }

    public Vector2 getPivot() {
        return pivot;
    }

    /*
     * Move piece down by one square unit on the board.
     * Do not move down if blocked by other piece or bottom of board.
     * @return	True if the piece had space to move down.
     * 			False if the piece did not have space to move down.
     */
    public boolean moveDown(){
        for(Block thisBlock : blocks)
            if(thisBlock.getRow() == 0)
                return false;


        for(Block outBlock : game)
            for(Block thisBlock : blocks)
                if(thisBlock.isAbove(outBlock))
                    return false;

        pivot.sub(0, 1);
        for(Block block : blocks)
            block.moveDown();
        return true;
    }

    /*
     * Move piece to the left by one square unit on the board.
     * Do not move left if blocked by other piece or bottom of board.
     * @return  True if the piece had space to move left.
     *          False if the piece did not have space to move left.
     */
    public boolean moveLeft() {
        for(Block thisBlock : blocks)
            if(thisBlock.getColumn() == 0)
                return false;

        for(Block outBlock : game)
            for(Block thisBlock : blocks)
                if(thisBlock.isRightof(outBlock))
                    return false;

        pivot.sub(1, 0);
        for(Block block : blocks)
            block.moveLeft();
        return true;
    }

    /*
     * Move piece to the left by one square unit on the board.
     * Do not move left if blocked by other piece or bottom of board.
     * @return  True if the piece had space to move left.
     *          False if the piece did not have space to move left.
     */
    public boolean moveRight() {
        for(Block thisBlock : blocks)
            if(thisBlock.getColumn() + 1 == game.getColumns())
                return false;

        for(Block outBlock : game)
            for(Block thisBlock : blocks)
                if(thisBlock.isLeftof(outBlock))
                    return false;

        pivot.add(1, 0);
        for(Block block : blocks)
            block.moveRight();
        return true;
    }


    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Block> iterator() {
        return blocks.iterator();
    }

    public boolean rotateCCW() {
        for(Block b : blocks) {
            Vector2 loc = b.rotateCCWLocation(pivot);
            if (loc.x < 0 || loc.x >= game.getColumns()
                    || loc.y < 0 || loc.y >= game.getRows()
                    ||game.occupiedNotPiece((int) loc.x, (int) loc.y))
                return false;
        }

        for(Block b : blocks)
            b.rotateCCW(pivot);
        return true;
    }

    public boolean rotateCW() {
        for(Block b : blocks) {
            Vector2 loc = b.rotateCWLocation(pivot);
            if (loc.x < 0 || loc.x >= game.getColumns()
                    || loc.y < 0 || loc.y >= game.getRows()
                    ||game.occupiedNotPiece((int) loc.x, (int) loc.y))
                return false;
        }

        for(Block b : blocks)
            b.rotateCW(pivot);
        return true;
    }

    public Color getColor() {
        return color;
    }
}

