import java.util.*;

public class TicTacToe {

	private static final int BOARD_LENGTH = 3;
	private static final char EMPTY_CHAR = '\u0000';

	private static Scanner sc;
	private static Random rand;
	private char[][] board;
	private Player player1, player2;
	private boolean chance;
	private int emptySpace = BOARD_LENGTH * BOARD_LENGTH;
	private boolean running = true;

	// Player Property for game.
	private static class Player {
		static final char X = 'X';
		static final char O = 'O';
		static Player winPlayer;

		String name;
		char ch;

		Player(String name, char ch) {
			this.name = name;
			this.ch = ch;
		}
	}

	public TicTacToe() {
		sc = new Scanner(System.in);
		rand = new Random();
		board = new char[BOARD_LENGTH][BOARD_LENGTH];

		// If chance is true then player 1 chance otherwise player 2 chance.
		chance = rand.nextBoolean();

		// For printing Game rules and information for first time.
		gameInfo();

		// Getting player info first time.
		getPlayerInfo();

		// After getting details of player main function to start game.
		startGame();
	}

	private void gameInfo() {
		System.out.println("\t-------------------------");
		System.out.println("\t|	TIC-TAC-TOE	|");
		System.out.println("\t-------------------------");
		System.out.println("\n- Welcome to the TIC-TAC-TOE Game.\n");
		System.out.println("* RULES FOR TIC-TAC-TOE *\n");

		System.out.println("1. The game is played on a grid that's 3 squares by 3 squares.");
		System.out.println("2. You are X, your friend (or the computer in this case) is O. Players take turns putting their marks in empty squares.");
		System.out.println("3. The first player to get 3 of her marks in a row (up, down, across, or diagonally) is the winner.");
		System.out.println("4. When all 9 squares are full, the game is over. If no player has 3 marks in a row, the game ends in a tie.");
		System.out.println("5. You want to give number between 0-9. Where you want to place the Xs and Os\n");

		System.out.println("- Board of TIC-TAC-TOE with Place No.");
		System.out.println("    1  |  2  |  3  ");
		System.out.println("  -----------------");
		System.out.println("    4  |  5  |  6  ");
		System.out.println("  -----------------");
		System.out.println("    7  |  8  |  9  ");
	}

	private void getPlayerInfo() {
		try {
			String name;
			Character ch;

			System.out.print("\nPlayer 1 Name :- ");
			name = sc.next();
			while (true) {
				System.out.print(String.format("%s, Enter your character : ", name));
				ch = sc.next().charAt(0);
				ch = Character.toUpperCase(ch);

				if (ch == Player.X || ch == Player.O) {
					break;
				} else {
					System.out.println("Please Enter the character 'X' or 'O'.");
				}
			}

			player1 = new Player(name, ch);

			System.out.print("\nPlayer 2 Name :- ");
			name = sc.next();
			player2 = new Player(name, (ch == Player.X ? Player.O : Player.X));

			System.out.println(String.format("\n%s character is \"%c\" and %s character is \"%c\".\n", player1.name, player1.ch, player2.name, player2.ch));
		} catch (NoSuchElementException e) {
		}
	}

	private void startGame() {
		while (running) {
			if (chance) {
				playerTurn(player1);
			} else {
				playerTurn(player2);
			}
		}
		exitMessage();
	}

	private void printBoard() {
		System.out.println();
		for (int row = 0; row < BOARD_LENGTH; row++) {
			System.out.println(String.format("    %c  |  %c  |  %c  ", board[row][0], board[row][1], board[row][2]));
			if (row < BOARD_LENGTH - 1) {
				System.out.println("  -----------------");
			}
		}
	}

	private void playerTurn(Player player) {
		// If all place are filled then game is end.
		if (emptySpace == 0) {
			running = false;
			return;
		}

		try {
			char currChar = player.ch;
			System.out.print(String.format("\n%s, Enter Place No. : ", player.name));
			int index = Integer.parseInt(sc.next());
			int row = (index - 1) / BOARD_LENGTH;
			int col = (index - 1) % BOARD_LENGTH;

			// If selected index is occupid then show error.
			if (isOccupied(row, col)) {
				System.out.println("Sorry, This place is occupied. Select another.");
				return;
			}

			board[row][col] = currChar;
			emptySpace--;
			chance = !chance;
			printBoard();
			checkCondition();
		} catch (NoSuchElementException e) {
		} catch (Exception e) {
			System.out.println("Error: Enter a number between 1-9.\n");
		}
	}

	private void checkCondition() {
		// Check win for all rows.
		for (int row = 0; row < BOARD_LENGTH; row++) {
			if (board[row][0] != EMPTY_CHAR && board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
				Player.winPlayer = (board[row][0] == player1.ch) ? player1 : player2;
				running = false;
				return;
			}
		}

		// Check win for all colums.
		for (int col = 0; col < BOARD_LENGTH; col++) {
			if (board[0][col] != EMPTY_CHAR && board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
				Player.winPlayer = (board[0][col] == player1.ch) ? player1 : player2;
				running = false;
				return;
			}
		}

		// Check For two diagonals.
		if (board[0][0] != EMPTY_CHAR && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
			Player.winPlayer = (board[0][0] == player1.ch) ? player1 : player2;
			running = false;
			return;
		}
		if (board[0][2] != EMPTY_CHAR && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
			Player.winPlayer = (board[0][0] == player1.ch) ? player1 : player2;
			running = false;
			return;
		}
	}

	private boolean isOccupied(int row, int col) {
		return board[row][col] != EMPTY_CHAR ;
	}

	private void exitMessage() {
		if (Player.winPlayer != null) {
			String message = "Congratulation " + Player.winPlayer.name + ", You win the game.";
			System.out.println("\n\t" + message );
		} else {
			System.out.println("\nGame draw.");
		}
		System.out.println("\nThank You for playing the game.\n");
	}

	public static void main(String arg[]) {
		new TicTacToe();
	}
}