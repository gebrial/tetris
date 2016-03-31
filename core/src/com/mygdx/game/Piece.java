package com.mygdx.game;

import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

import static com.badlogic.gdx.math.MathUtils.floor;

/**
 * Created by sanjaya on 29/03/16.
 */
	/*
	 * Collection of squares that forms a shape.
	 */
public class Piece implements Iterable<Square>{
    Array<Square> squares;
    Tetris game;

    /*
     * Creates new random piece at top of board.
     */
    public Piece(Tetris tetris){
        squares = new Array<Square>(false, 4);
        game = tetris;

        // TODO: spawn random piece, not just square
        squares.add(new Square(game, floor(game.getColumns()/2), game.getRows()));
        squares.add(new Square(game, floor(game.getColumns()/2), game.getRows() + 1));
        squares.add(new Square(game, floor(game.getColumns()/2) + 1, game.getRows() + 1));
        squares.add(new Square(game, floor(game.getColumns()/2) + 1, game.getRows()));
    }

    /*
     * Move piece down by one square unit on the board.
     * Do not move down if blocked by other piece or bottom of board.
     * @return	True if the piece had space to move down.
     * 			False if the piece did not have space to move down.
     */
    public boolean moveDown(){
        for(Square thisSquare : squares)
            if(thisSquare.getRow() == 0)
                return false;


        for(Square outSquare : game)
            for(Square thisSquare : squares)
                if(thisSquare.isAbove(outSquare))
                    return false;

        for(Square square : squares)
            square.moveDown();
        return true;
    }

    /*
     * Move piece to the left by one square unit on the board.
     * Do not move left if blocked by other piece or bottom of board.
     * @return  True if the piece had space to move left.
     *          False if the piece did not have space to move left.
     */
    public boolean moveLeft() {
        for(Square thisSquare : squares)
            if(thisSquare.getColumn() == 0)
                return false;

        for(Square outSquare : game)
            for(Square thisSquare : squares)
                if(thisSquare.isRightof(outSquare))
                    return false;

        for(Square square : squares)
            square.moveLeft();
        return true;
    }

    /*
     * Move piece to the left by one square unit on the board.
     * Do not move left if blocked by other piece or bottom of board.
     * @return  True if the piece had space to move left.
     *          False if the piece did not have space to move left.
     */
    public boolean moveRight() {
        for(Square thisSquare : squares)
            if(thisSquare.getColumn() + 1 == game.getColumns())
                return false;

        for(Square outSquare : game)
            for(Square thisSquare : squares)
                if(thisSquare.isLeftof(outSquare))
                    return false;

        for(Square square : squares)
            square.moveRight();
        return true;
    }


    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Square> iterator() {
        return squares.iterator();
    }
}

