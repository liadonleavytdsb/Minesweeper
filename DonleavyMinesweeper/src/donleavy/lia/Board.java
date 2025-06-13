package donleavy.lia;

import java.util.Random;
/***
* The board class manages the minesweeper game board.
* It handles creating the board, mine placement and adjacent mine counts.
*/

public class Board {
	int numMines = 10; // Total number of mines to place on the game board.
	Tile[][] board;
	
	/***
	 * Creates the 8x8 Minesweeper board.
	 * Calls manageMines() so all 10 mines get placed and the remaining tiles get an adjMine value.
	 */
	public Board() {
		board = new Tile[8][8];
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				board[r][c] = new Tile();
			}
		}
		manageMines();
	}
	
	/***
	 * Gets a tile at a specific row and column.
	 *
	 * @param r Row index
	 * @param c Column index
	 * @return the Tile at the specific location
	 */
	public Tile getTile(int r, int c) {
		return board[r][c];
	}
	
	/***
	 * Randomly paces mines on the board and calculates the number of adjacent mines for each tile.
	 */
	public void manageMines() {
		Random rand = new Random();
		int placed = 0;
		
		// Randomly place 10 mines
		while (placed < numMines) {
			int r = rand.nextInt(8);
			int c = rand.nextInt(8);
			
			if (!board[r][c].isMine) {
				board[r][c].setMine();
				placed++;
			}
		}
		
		// Calculate the number of adjacent mines for each tile
		for (int r=0; r<8; r++) {
			for (int c=0; c<8; c++) {
				int count = 0;
				
				// Check all 8 of the surrounding tiles
				for (int dr=-1; dr<=1; dr++) {
					for (int dc=-1; dc<=1; dc++) {
						int newR = r + dr;
						int newC = c + dc;
						
						// Skip the out-of-bound tiles
						if (newR >=0 && newR<8 && newC>=0 && newC<8) {
							if (board[newR][newC].isMine()) {
								count++;
							}
						}
					}
				}
				board[r][c].adjMines = count;
			}
		}
	}
	
	/***
	 * Generates a string version of the board in the console
	 *
	 * @return a text-based version of the current board
	 */
	public String toString() {
		String s = "";
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				s += board[r][c] + " ";
			}
			s += "\n";
		}
		return s;
	}
}

