package donleavy.lia;
	
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
	
/**
* GUIDriver: Main GUI class for Minesweeper game using JavaFX.
*/
public class GUIDriver extends Application {
	NewButton[][] tile = new NewButton[8][8];
	Board board = new Board();
	int flagsPlaced = 0;
	Scene scene;
	
	/***
	 * Set up the JavaFX GUI.
	 */
	public void start(Stage stage) throws Exception {
		System.out.println(board);
		VBox root = new VBox(10);
		root.setBackground(Background.EMPTY);
		HBox header = new HBox(10);
		GridPane gp = new GridPane();
		gp.setVgap(10);
		gp.setHgap(10);
		
		// Title Label
		Font titleFont = new Font("Times New Roman", 50);
		Label title = new Label("Minesweeper!!");
		title.setFont(titleFont);
		
		// Info Label
		Label info = new Label("Flags Placed: 0 || Mines Left: 10");
		Font infoFont = new Font("Times New Roman", 20);
		info.setFont(infoFont);
		
		// Help Button (Instructions)
		Button helpBTN = new Button("?");
		helpBTN.setOnAction(e -> {
			info.setText("Left Mouse Click: Reveal Tile \nRight Mouse Click: Flag Tile As Mine \nWhen All Mines Are Flagged, You Win!");
		});
		
		// Reset/Play Again Button
		Button reset = new Button("Play Again");
		reset.setFont(infoFont);
		reset.setVisible(false);
		reset.setOnAction(e -> {
			// Create a new game board with new mine locations
			board = new Board();
			System.out.println(board);
			
			// Reset flagsPlaced and the info label
			flagsPlaced = 0;			
			info.setText("Flags Placed: 0 || Mines Left: 10");
			
			// Reset All Buttons
			for (int r = 0; r <8; r++) {
				for (int c = 0; c < 8; c++) {
					Tile t = board.getTile(r, c);
					
					// Fix states of each tile
					t.setUnflagged();
					t.setRevealFalse();
					
					// Reset appearances
					NewButton nb = tile[r][c];
					nb.setText("");
					nb.setStyle("-fx-background-color: #1c7032");
					nb.setMouseTransparent(false);
					root.setStyle("");
					reset.setVisible(false);
				}
			}
		});
		
		// Create game tiles and control mouse click events
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				NewButton tempButton = new NewButton(r, c);
				tempButton.setPrefSize(50, 40);
				tempButton.setStyle("-fx-background-color: #1c7032");
				tempButton.setTextFill(Color.BLACK);
				tile[r][c] = tempButton;
				gp.add(tile[r][c], c, r);
				
				// Mouse Click Events
				tile[r][c].setOnMouseClicked(event -> {
					NewButton nb = (NewButton) event.getSource();
					int r2 = nb.getRow();
					int c2 = nb.getCol();
					Tile clicked = board.getTile(r2, c2);
					
					// Right Click (Flagging Tiles)
					if (event.getButton() == MouseButton.SECONDARY) {
						if (!clicked.isRevealed()) {
							if (!clicked.isFlagged()) {
								clicked.setFlagged();
								flagsPlaced++;
								info.setText("Flags Placed: " + flagsPlaced + " || Mines Left: " + (10 - flagsPlaced));
								nb.setText("🚩");
								nb.setStyle("-fx-background-color: #89a888");
							}
							else {
								clicked.setUnflagged();
								flagsPlaced--;
								info.setText("Flags Placed: " + flagsPlaced + " || Mines Left: " + (10 - flagsPlaced));
								nb.setText("");
								nb.setStyle("-fx-background-color: #1c7032");
							}
							if (checkWin()) {
								disableBoard();
								root.setStyle("-fx-background-color: #d4edda");
								info.setText("🎉 Congrats! All Mines Have Been Flagged, You Win!! 🎉");
								reset.setVisible(true);
							}
						}
					}
					
					// Left Click (Reveal Tiles)
					if (event.getButton() == MouseButton.PRIMARY) {
						if (!clicked.isFlagged() && !clicked.isRevealed()) {
							info.setText("Flags Placed: " + flagsPlaced + " || Mines Left: " + (10 - flagsPlaced));
							clicked.setReveal();
							nb.setText(update(r2, c2));
							nb.setStyle("-fx-background-color: #f7deb2");
							
							// Check for Win
							if (checkWin()) {
								disableBoard();
								root.setStyle("-fx-background-color: #d4edda");
								info.setText("🎉 Congrats! All Mines Have Been Flagged, You Win!! 🎉");
								reset.setVisible(true);
							}
								
							// Check For Game Over
							if (clicked.isMine()) {
								revealAllMines();
								disableBoard();
								info.setText("Game Over! You Revealed A Mine");
   								root.setStyle("-fx-background-color: #f8d7da");
   								reset.setVisible(true);
							}
						}
					}
				});
			}
		}
		gp.setAlignment(Pos.CENTER);
		header.getChildren().addAll(title, helpBTN);
		header.setAlignment(Pos.CENTER);
		root.getChildren().addAll(header, info, gp, reset);
		root.setAlignment(Pos.CENTER);
		
		scene = new Scene(root, 600, 650);
		stage.setScene(scene);
		stage.show();
	}
	
	/***
	 * Updates the text displaying on a revealed tile.
	 *
	 * @param r Row index.
	 * @param c Column index.
	 * @return String that states the number of adjacent mines.
	 */
	public String update(int r, int c) {
		// Updates the text on the tile if it is revealed
		Tile t = board.getTile(r, c);
		String textFill = "";
		
		if (t.isRevealed()) {
			if (t.isMine()) {
				textFill = "X";
			} 
			else {
				textFill = String.valueOf(t.getAdjMines());
			}
		}
		return textFill;
	}
	
	/***
	 * Reveals all mines on the board after a game over.
	 */
	public void revealAllMines() {
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				Tile t = board.getTile(r, c);
				if (t.isMine()) {
					t.setReveal();
					tile[r][c].setText("💣");
					tile[r][c].setStyle("-fx-background-color: #c44d4d");
				}
			}
		}
	}
	
	/***
	 * Disables all tiles on the board (called after a win/loss).
	 */
	public void disableBoard() {
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				tile[r][c].setMouseTransparent(true); //Note: Found this online- link is under "resources" in trello board.
			}
		}
	}
	
	/***
	 * Checks if player has flagged all mines
	 *
	 * @return True if the player has flagged all mines-- and therefore wins, else false.
	 */
	public boolean checkWin() {
		for (int r = 0; r < 8; r++) {
		 	for (int c = 0; c < 8; c++) {
		 		Tile t = board.getTile(r, c);
		 		
		 		// If it is a mine, it must be flagged
		 		if (t.isMine() && !t.isFlagged()) {
		 			return false;
		 		}
		 		
		 		// If it is safe (not a mine) it must be revealed
		 		if (!t.isMine() && !t.isRevealed()) {
		 			return false;
		 		}
		 	}
		}
		// If pass both checks, then the player wins
		return true;
	}
	
	/***
	 * Launch the game.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
