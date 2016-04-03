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
class Block implements Comparable<Block>{
    private int column; // column starts from left, increases right
    private int row; // row starts from bottom, increases up
    private Color color;
    private final Tetris game;

    public Block(Tetris tetris, int column, int row, Color c){
        this.column = column;
        this.row = row;

        game = tetris;
        color = c;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
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
                .rotate90(1)
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
                .rotate90(-1)
                .add(pivot);
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Block o) {
        return this.color.toIntBits() - o.color.toIntBits();
    }
}