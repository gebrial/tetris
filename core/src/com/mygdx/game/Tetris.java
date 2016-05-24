package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Iterator;

public class Tetris extends Game implements Iterable<Block>{
	SpriteBatch batch;
	BitmapFont font;
	ShapeRenderer shapeRenderer;
	Preferences scores, options;

	private Array<Block> blocks; // cemented blocks
	private Piece piece; // movable blocks
	private Piece nextPiece;
	private long lastMoved; // in nanoseconds
	private long pausedTime; // in nanoseconds
	private long moveTime; // in nanoseconds
	private static long defaultMoveTime = 1000000000; // in nano seconds
	private int rows;
	private int columns;
	private int score;
	private boolean fourRowsCleared;
	private int totalRowsCleared;
	private boolean paused;
	private int garbageStart;

	// keys used for preferences
	private static String garbageLines = "garbageLines";
	private static String highScore = "highScore"; // each highScore key is succeeded by an int from 0 to 9, e.g., highScore + "3"

	boolean isPaused(){
		return paused;
	}

	int getColumns() {
		return columns;
	}

	int getRows() {
		return rows;
	}

	Piece getPiece() {
		return piece;
	}

	Piece getNextPiece() {
		return nextPiece;
	}

	/*
	 * Initialize the value of the number of garbage lines from the options prefs
	 */
	void setGarbageStart(){
		setGarbageStart(options.getInteger(garbageLines));
	}

	/*
	 * Set the number of garbage lines to garbageStart
	 * and change the value in the options preferences.
	 */
	void setGarbageStart(int garbageStart) {
		if(garbageStart < 0)
			this.garbageStart = 0;
		else if(garbageStart > rows - 5)
			this.garbageStart = rows - 5;
		else
			this.garbageStart = garbageStart;

		options.putInteger(garbageLines, this.garbageStart);
		options.flush();
	}

	int getGarbageStart() {
		return garbageStart;
	}

	@Override
	public void create () {
		Gdx.app.log("Tetris", "created");

		batch = new SpriteBatch();
		font = new BitmapFont();
		shapeRenderer = new ShapeRenderer();

		rows = 20;
		columns = 10;
		blocks = new Array<Block>(true, rows*columns);

		setupPrefs();
		setGarbageStart();

		this.setScreen(new MainScreen(this));

//		Gdx.graphics.setContinuousRendering(false);
//		Gdx.graphics.requestRendering();
	}

	private void setupPrefs(){
		scores = Gdx.app.getPreferences("scores");
		for(int i = 0; i < 10; i++)
			if(!scores.contains(highScore + i))
				scores.putInteger(highScore + i, 0);
		scores.flush();

		options = Gdx.app.getPreferences("options");
		if(!options.contains(garbageLines))
			setGarbageStart(0);
		options.flush();
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
		paused = false;
		score = 0;
		fourRowsCleared = false;
		piece = new Piece(this);
		nextPiece = new Piece(this);
		lastMoved = TimeUtils.nanoTime();
		blocks.clear();
		totalRowsCleared = 0;
		moveTime = getMoveTime(totalRowsCleared);

		RandomXS128 rand = new RandomXS128();
		for(int i = 0; i < garbageStart; i++){
			for(int j = 0; j < columns; j++){
				if(rand.nextBoolean())
					blocks.add(new Block(this, j, i));
			}
		}
	}

	/*
	 * Finish the current game.
	 */
	public void finish(){
		setHighScore();
	}

	public void pause(){
		paused = true;
		pausedTime = TimeUtils.nanoTime();
	}

	public void resume(){
		paused = false;
		lastMoved += TimeUtils.timeSinceNanos(pausedTime);
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

			totalRowsCleared += rowsCleared;
			moveTime = getMoveTime(totalRowsCleared);

			blocks.sort();
			piece = nextPiece;
			nextPiece = new Piece(this);
			for(Block p : piece)
				for(Block b : blocks)
					if(p.overlaps(b)){
						setHighScore();
						Screen game = getScreen();
						setScreen(new LossScreen(this));
						game.dispose();
						return;
					}
		}
	}

	private static long getMoveTime(int totalRowsCleared){
		double moveTimeTemp = 1./MathUtils.log(10, 6*totalRowsCleared + 10);
		moveTimeTemp *= defaultMoveTime;
		return (long) moveTimeTemp;
	}

	/*
	 * Resets the lastMoved time of the current piece to now.
	 */
	public void resetTimer(){
		lastMoved = TimeUtils.nanoTime();
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

	public int getHighScore(){
		return getHighScore(0);
	}

	/*
	 * gets the i'th high score
	 * @param i		integer greater than or equal to 0
	 */
	public int getHighScore(int i){
		return scores.getInteger(highScore + i);
	}

	/*
	 * updates high score stats with current score
	 */
	private void setHighScore(){
		// if current score less than lowest score on high scores table, do nothing
		if(getScore() < scores.getInteger(highScore + 9))
			return;

		for(int i = 9; i > 0; i--){
			// copy (i-1)'th score to i'th score
			scores.putInteger(highScore + i, scores.getInteger(highScore + (i-1)));
			if(getScore() < scores.getInteger(highScore + i)) {
				// if current score less than (i-1)'th score
				// set i'th score to current score
				scores.putInteger(highScore + i, getScore());
				scores.flush();
				return;
			}
		}

		scores.putInteger(highScore + "0", getScore());
		scores.flush();
	}
}