package donleavy.lia;
/***
* Represents a single tile on the minesweeper board.
* Handles the state of each individual tile (mine, flagged, revealed, # of adjacent mines).
*/
public class Tile {
	boolean isMine;
	boolean isRevealed;
	boolean isFlagged;
	int adjMines;
	
	/***
	 * Constructs a new tile with the below specifications.
	 */
	public Tile() {
		isMine = false;
		isRevealed = false;
		isFlagged = false;
		adjMines = 0;
	}
	
	/***
	 * Returns whether or not this tile contains a mine.
	 *
	 * @return true if tile is a mine, false if it is a regular tile.
	 */
	public boolean isMine() {
		return isMine;
	}
	
	/***
	 * Returns the number of adjacent mines to this tile
	 *
	 * @return number of adjacent mines.
	 */
	public int getAdjMines() {
		return adjMines;
	}
	
	/***
	 * Marks this tile as a mine.
	 */
	public void setMine() {
		isMine = true;
	}
	
	/***
	 * Returns whether or not the tile has been clicked on and "revealed."
	 *
	 * @return true if tile is revealed, false if tile is still hidden.
	 */
	public boolean isRevealed() {
		return isRevealed;
	}
	
	/***
	 * Changes the state if a specific tile to be revealed or not.
	 */
	public void setReveal() {
		isRevealed = true;
	}
	
	/***
	 * Makes it so the tile is not revealed.
	 * Only called when reset/play again button is pressed.
	 */
	public void setRevealFalse() {
		isRevealed = false;
	}
	
	/***
	 * Checks if the specified tile has been flagged as a mine.
	 *
	 * @return true if the tile has been flagged as a mine, false if it has not.
	 */
	public boolean isFlagged() {
		return isFlagged;
	}
	
	/***
	 * Sets a tile as flagged as a mine.
	 */
	public void setFlagged() {
		isFlagged = true;
	}
	
	/***
	 * Sets a tile as unflagged.
	 */
	public void setUnflagged() {
		isFlagged = false;
	}
	
	/***
	 * Runs the text-based version of the minesweeper board.
	 *
	 * The tile is represented as "X" if it is a mine, otherwise the number of adjacent mines.
	 */
	@Override
	public String toString() {
		String state = "";
		if (isMine) {
			state = "X";
		}
		else {
			state += adjMines;
		}
		return state;
	}
}
