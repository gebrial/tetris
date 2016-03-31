package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by sanjaya on 29/03/16.
 */
	/*
	 * Piece on the game board.
	 */
class Square {
    private int column; // column starts from left, increases right
    private int row; // row starts from bottom, increases up
    private Color color;
    private final Tetris game;

    public Square(Tetris tetris, int column, int row){
        this.column = column;
        this.row = row;

        game = tetris;
    }

    public int getRow(){
        return row;
    }

    public int getColumn(){
        return column;
    }

    /*
     * @return	True if this and otherSquare are in same position
     */
    public boolean overlaps(Square otherSquare) {
        return (column == otherSquare.column) && (row == otherSquare.row);
    }

    /*
     * @return	True if this is one square above otherSquare
     * 			False otherwise
     */
    public boolean isAbove(Square otherSquare){
        return (column == otherSquare.column) && (row - 1 == otherSquare.row);
    }

    public boolean isRightof(Square otherSquare) {
        return (column - 1 == otherSquare.getColumn()) && (row == otherSquare.getRow());
    }

    public boolean isLeftof(Square otherSquare) {
        return (column + 1 == otherSquare.getColumn()) && (row == otherSquare.getRow());
    }

    public boolean isBelow(Square otherSquare) {
        return (column == otherSquare.getColumn()) && (row + 1 == otherSquare.getRow());
    }

    /*
     * Move this square down by one unit, with no checks
     */
    public void moveDown(){
        row--;
    }

    public void moveLeft() {
        column--;
    }

    public void moveRight() {
        column++;
    }

    public void render(){

    }
}