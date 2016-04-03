package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Iterator;

public class Tetris extends Game implements Iterable<Block>{
	SpriteBatch batch;
	BitmapFont font;
	ShapeRenderer shapeRenderer;

	private Array<Block> blocks; // cemented blocks
	private Piece piece; // movable blocks
	private long lastMoved; // in nanoseconds
	private long moveTime; // in nanoseconds
	private int rows;
	private int columns;
	private int score;
	private boolean fourRowsCleared;

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

		rows = 20;
		columns = 10;
		blocks = new Array<Block>(true, rows*columns);

		score = 0;
		fourRowsCleared = false;

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
		score = 0;
		fourRowsCleared = false;
		piece = new Piece(this);
		lastMoved = TimeUtils.nanoTime();
		blocks.clear();
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

		// Only true when piece hits bottom and new piece spawns
		// must also clear full rows now
		if(!piece.moveDown()){
			blocks.addAll(piece.blocks);

			int rowsCleared = clearRows();
			score += rowsCleared*100;
			if(rowsCleared == 4) {
				score += 400;
				if(fourRowsCleared)
					score += 400;
				fourRowsCleared = true;
			} else
				fourRowsCleared = false;

			blocks.sort();
			piece = new Piece(this);
			for(Block p : piece)
				for(Block b : blocks)
					if(p.overlaps(b)){
						Screen game = getScreen();
						setScreen(new LossScreen(this));
						game.dispose();
					}
		}
	}

	/*
	 * Clears any rows on board filled with blocks.
	 * Move all blocks above removed rows down.
	 */
	private int clearRows(){
		int rowsCleared = 0;
		int[] rowSquares = new int[rows];

		for(int row = 0; row < rows; row++)
			rowSquares[row] = 0;

		for(Block block : blocks)
			rowSquares[block.getRow()]++;

		for(int row = 0; row < rows; row++){ // check every row
			if(rowSquares[row] == columns){
				rowsCleared++;
				// if row full, remove any square that is in that row
				Iterator<Block> iter = blocks.iterator();
				while(iter.hasNext()){
					Block block = iter.next();
					if(block.getRow() == row)
						iter.remove();
					else if(block.getRow() > row)
						block.moveDown();
				}

				// lower row values from rows above
				for(int row2 = row; row2 < rows - 1; row2++){
					rowSquares[row2] = rowSquares[row2 + 1];
				}
				rowSquares[rows-1] = 0; // set top row value to 0
				row--; // check this row again since we have moved everything down
			}
		}

		return rowsCleared;
	}

	/*
	 * @return	True if there is a block on the space specified by column and row that is not part of movable piece.
	 * 			False otherwise
	 */
	public boolean occupiedNotPiece(int column, int row){
		for(Block b : blocks)
			if(b.getColumn() == column && b.getRow() == row)
				return true;

		return false;
	}

	public boolean rotatePieceCW(){
		return piece.rotateCW();
	}

	public boolean rotatePieceCCW(){
		return piece.rotateCCW();
	}

	/**
	 * Returns an iterator over elements of type {@code T}.
	 *
	 * @return an Iterator.
	 */
	@Override
	public Iterator iterator() {
		return blocks.iterator();
	}

	public int getScore() {
		return score;
	}
}