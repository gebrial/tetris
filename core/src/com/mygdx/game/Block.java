package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import static com.badlogic.gdx.math.MathUtils.round;

/**
 * Created by sanjaya on 29/03/16.
 */
	/*
	 * Piece on the game board.
	 */
class Block {
    private int column; // column starts from left, increases right
    private int row; // row starts from bottom, increases up
    private Color color;
    private final Tetris game;

    public Block(Tetris tetris, int column, int row){
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

    public void setColumn(int column) {
        this.column = column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    /*
     * @return	True if this and otherBlock are in same position
     */
    public boolean overlaps(Block otherBlock) {
        return (column == otherBlock.column) && (row == otherBlock.row);
    }

    /*
     * @return	True if this is one square above otherBlock
     * 			False otherwise
     */
    public boolean isAbove(Block otherBlock){
        return (column == otherBlock.column) && (row - 1 == otherBlock.row);
    }

    public boolean isRightof(Block otherBlock) {
        return (column - 1 == otherBlock.getColumn()) && (row == otherBlock.getRow());
    }

    public boolean isLeftof(Block otherBlock) {
        return (column + 1 == otherBlock.getColumn()) && (row == otherBlock.getRow());
    }

    public boolean isBelow(Block otherBlock) {
        return (column == otherBlock.getColumn()) && (row + 1 == otherBlock.getRow());
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

    /*
     * Rotates this block around point specified by x and y by 90 degrees cw
     */
    public void rotateCW(Vector2 pivot){
        Vector2 loc = rotateCWLocation(pivot);
        setColumn(round(loc.x));
        setRow(round(loc.y));
    }

    /*
     * @return  Returns a vector pointing to the location of this block as a result of a rotation of 90 degrees CW.
     */
    public Vector2 rotateCWLocation(Vector2 pivot){
        return new Vector2(pivot)
                .sub((float) column, (float) row)
                .rotate90(-1)
                .add(pivot);
    }

    /*
     * Rotates this block around point specified by x and y by 90 degrees CCW
     */
    public void rotateCCW(Vector2 pivot){
        Vector2 loc = rotateCCWLocation(pivot);
        setColumn(round(loc.x));
        setRow(round(loc.y));
    }

    /*
     * @return  Returns a vector pointing to the location of this block as a result of a rotation of 90 degrees CCW.
     */
    public Vector2 rotateCCWLocation(Vector2 pivot){
        return new Vector2(pivot)
                .sub((float) column, (float) row)
                .rotate90(1)
                .add(pivot);
    }

}