package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Iterator;

public class Tetris extends Game implements Iterable<Square>{
	SpriteBatch batch;
	BitmapFont font;
	ShapeRenderer shapeRenderer;

	private Array<Square> squares;
	private Piece piece;
	private long lastMoved; // in nanoseconds
	private long moveTime; // in nanoseconds
	private int rows;
	private int columns;

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public Piece getPiece() {
		return piece;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		shapeRenderer = new ShapeRenderer();

		squares = new Array<Square>(false, 128);
		rows = 16;
		columns = 8;

		lastMoved = TimeUtils.nanoTime();
		moveTime = 1000000000; // 1billion nanoseconds = 1 second

		this.setScreen(new MenuScreen(this));

//		Gdx.graphics.setContinuousRendering(false);
//		Gdx.graphics.requestRendering();
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose(){
		batch.dispose();
		font.dispose();
		shapeRenderer.dispose();
	}

	/*
	 * Call when game starts.
	 * Sets up clock.
	 * Call update before rendering.
	 */
	public void start(){
		piece = new Piece(this);
		lastMoved = TimeUtils.nanoTime();
	}

	/*
	 * Checks how long since piece was last moved.
	 * If time since last moved > moveTime, then update lastMoved time and move piece.
	 * If piece can't move, freeze piece, clear full rows, and spawn new piece.
	 */
	public void update(){
		if(TimeUtils.timeSinceNanos(lastMoved) < moveTime){
			return;
		}

		lastMoved = TimeUtils.nanoTime();
		if(!piece.moveDown()){
			squares.addAll(piece.squares);
			clearRows();
			piece = new Piece(this);
		}
	}

	/*
	 * Clears any rows on board filled with squares.
	 * Move all squares above removed rows down.
	 */
	private void clearRows(){
		int[] rowSquares = new int[rows];

		for(int row = 0; row < rows; row++)
			rowSquares[row] = 0;

		for(Square square : squares)
			rowSquares[square.getRow()]++;

		for(int row = 0; row < rows; row++){ // check every row
			if(rowSquares[row] == columns){
				// if row full, remove any square that is in that row
				Iterator<Square> iter = squares.iterator();
				while(iter.hasNext()){
					Square square = iter.next();
					if(square.getRow() == row)
						iter.remove();
					else if(square.getRow() > row)
						square.moveDown();
				}

				// lower row values from rows above
				for(int row2 = row; row2 < rows - 1; row2++){
					rowSquares[row2] = rowSquares[row2 + 1];
				}
				rowSquares[rows-1] = 0; // set top row value to 0
				row--; // check this row again since we have moved everything down
			}
		}
	}

	/**
	 * Returns an iterator over elements of type {@code T}.
	 *
	 * @return an Iterator.
	 */
	@Override
	public Iterator iterator() {
		return squares.iterator();
	}
}