import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToe extends JFrame implements ActionListener {

	private static final int BOARD_LENGTH = 3;

	private CardLayout card;
	private JPanel startPanel;
	private JPanel infoPanel;
	private JPanel gamePanel, boardPanel, btnPanel;
	private JLabel gameLabel;
	private Button[][] board;
	private Player player1, player2;
	private boolean chance = true;
	private int emptySpace = BOARD_LENGTH * BOARD_LENGTH;

	// Player Property for game.
	private static class Player {
		static final String X = "X";
		static final String O = "O";
		static Player winPlayer;

		String name;
		String ch;

		Player(String name, String ch) {
			this.name = name;
			this.ch = ch;
		}
	}

	private static class Button extends JButton {
		static final Color WIN_COLOR = new Color(66, 217, 20);
		int row, col;

		public Button(String text, int row, int col) {
			super(text);
			this.row = row;
			this.col = col;
			setFont(new Font("MV Boli", Font.BOLD, 50));
		}

		static boolean equals(Button b1, Button b2, Button b3) {
			return b1.getText().equals(b2.getText()) && b2.getText().equals(b3.getText());
		}

		@Override
		public String toString() {
			return getText();
		}
	}

	public TicTacToe() {

		this.setTitle("Tic Tac Tae"); //Title Frame
		card = new CardLayout();
		this.setLayout(card);

		// // Initialize the startPanel
		startPanelInitialize();

		// // Getting player info first time.
		getPlayerInfo();

		// // Initialize the gamePanel
		gamePanelInitialize();

		this.setResizable(false);
		this.setSize(500, 500); 		//frame size
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		playTurn((chance ? player1 : player2), (Button)e.getSource());

		// If all place are filled then game is end.
		if (emptySpace == 0 && Player.winPlayer == null) {
			exitMessage();
			makeDisable(-1, -1, -1, -1, -1, -1);
		}
	}

	private void startPanelInitialize() {
		startPanel = new JPanel();
		startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.Y_AXIS));

		// JLabel gameTitle = new JLabel("<html><u width=\"5\">Tic Tac Tae</u></html>");
		JLabel gameTitle = new JLabel("Tic Tac Tae");
		gameTitle.setFont(new Font("Lemon", Font.BOLD, 40));
		gameTitle.setAlignmentX(CENTER_ALIGNMENT);
		gameTitle.setBorder(new EmptyBorder(15, 0, 0, 0));
		startPanel.add(gameTitle);

		String RULES = "\n- Welcome to the TIC-TAC-TOE Game.\n\n"
		               + "* RULES FOR TIC-TAC-TOE *\n"
		               + "1. The game is played on a grid that's 3 squares by 3 squares."
		               + "\n2. You are X, your friend (or the computer in this case) is O. Players take turns putting their marks in empty squares."
		               + "\n3. The first player to get 3 of her marks in a row (up, down, across, or diagonally) is the winner."
		               + "\n4. When all 9 squares are full, the game is over. If no player has 3 marks in a row, the game ends in a tie."
		               + "\n5. You want to select button from board where you want to place the Xs and Os\n";

		JTextArea gameInfo = new JTextArea(RULES);
		gameInfo.setFont(new Font("Arial", Font.PLAIN, 15));
		gameInfo.setLineWrap(true);
		gameInfo.setWrapStyleWord(true);
		gameInfo.setOpaque(false);
		gameInfo.setEditable(false);
		gameInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		gameInfo.setAlignmentX(CENTER_ALIGNMENT);
		startPanel.add(gameInfo);

		// startPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JButton btnNext = new JButton("Next");
		btnNext.setFont(new Font("Arial", Font.BOLD, 20));
		btnNext.setAlignmentX(CENTER_ALIGNMENT);
		btnNext.addActionListener( e -> {
			card.show(getContentPane(), "infoPanel");
		});
		startPanel.add(btnNext);

		startPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		this.add(startPanel, "startPanel");
	}

	private void getPlayerInfo() {
		infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setAlignmentX(CENTER_ALIGNMENT);
		infoPanel.setAlignmentY(CENTER_ALIGNMENT);

		// Player 1 information Panel.
		JPanel panel1 = getPlayerPanel("Player 1");

		panel1.add(Box.createRigidArea(new Dimension(10, 20)));

		JPanel nameInfo = new JPanel();

		JLabel playerNameLabel = new JLabel("Name :  ");
		playerNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
		nameInfo.add(playerNameLabel);

		JTextField player1Name = new JTextField("");
		player1Name.setFont(new Font("Arial", Font.PLAIN, 18));
		player1Name.setPreferredSize(new Dimension(150, 30));
		nameInfo.add(player1Name);

		JPanel player1_btn = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));

		ButtonGroup player1_bg = new ButtonGroup();
		JRadioButton p1_X = new JRadioButton("X");
		JRadioButton p1_O = new JRadioButton("O");
		p1_X.setFont(new Font("Arial", Font.BOLD, 30));
		p1_O.setFont(new Font("Arial", Font.BOLD, 30));
		p1_X.setSelected(true);
		p1_X.setActionCommand("X");
		p1_O.setActionCommand("O");
		player1_bg.add(p1_X);
		player1_bg.add(p1_O);

		player1_btn.add(p1_X);
		player1_btn.add(p1_O);

		panel1.add(nameInfo);
		panel1.add(player1_btn);

		// Player 2 information Panel.
		JPanel panel2 = getPlayerPanel("Player 2");

		panel2.add(Box.createRigidArea(new Dimension(10, 20)));

		nameInfo = new JPanel();

		playerNameLabel = new JLabel("Name :  ");
		playerNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
		nameInfo.add(playerNameLabel);

		JTextField player2Name = new JTextField("");
		player2Name.setFont(new Font("Arial", Font.PLAIN, 18));
		player2Name.setPreferredSize(new Dimension(150, 30));
		nameInfo.add(player2Name);

		JPanel player2_btn = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));

		ButtonGroup player2_bg = new ButtonGroup();
		JRadioButton p2_X = new JRadioButton("X");
		JRadioButton p2_O = new JRadioButton("O");
		p2_X.setFont(new Font("Arial", Font.BOLD, 30));
		p2_O.setFont(new Font("Arial", Font.BOLD, 30));
		p2_O.setSelected(true);
		p2_X.setActionCommand("X");
		p2_O.setActionCommand("O");
		player2_bg.add(p2_X);
		player2_bg.add(p2_O);

		player2_btn.add(p2_X);
		player2_btn.add(p2_O);

		panel2.add(nameInfo);
		panel2.add(player2_btn);

		p1_X.addActionListener(e-> {
			p2_X.setSelected(false);
			p2_O.setSelected(true);
		});
		p1_O.addActionListener(e-> {
			p2_X.setSelected(true);
			p2_O.setSelected(false);
		});
		p2_X.addActionListener(e-> {
			p1_X.setSelected(false);
			p1_O.setSelected(true);
		});
		p2_O.addActionListener(e-> {
			p1_X.setSelected(true);
			p1_O.setSelected(false);
		});


		// Button for starting the game.
		JButton btnStart = new JButton("Start Game");
		btnStart.setFont(new Font("Arial", Font.BOLD, 20));
		btnStart.setAlignmentX(CENTER_ALIGNMENT);
		btnStart.addActionListener( e -> {
			String p1Name = player1Name.getText();
			String p2Name = player2Name.getText();
			if (p1Name.isEmpty() || p2Name.isEmpty()) {
				showMessage("Error", "Player name is not Empty.");
			} else{
				player1 = new Player(p1Name, player1_bg.getSelection().getActionCommand());
				player2 = new Player(p2Name, player2_bg.getSelection().getActionCommand());
				gameLabel.setText(String.format("%s's Turn (%s)", player1.name, player1.ch));
				card.show(getContentPane(), "gamePanel");
			}
			player1Name.setText("");
			player2Name.setText("");
		});


		infoPanel.add(panel1);
		infoPanel.add(panel2);
		infoPanel.add(Box.createRigidArea(new Dimension(10, 10)));
		infoPanel.add(btnStart);

		this.add(infoPanel, "infoPanel");
	}

	private void gamePanelInitialize() {

		gamePanel = new JPanel(new BorderLayout(5, 5));

		gameLabel = new JLabel("", SwingConstants.CENTER);
		gameLabel.setPreferredSize(new Dimension(500, 70));
		gameLabel.setFont(new Font("Courgette", Font.BOLD, 28));
		gamePanel.add(gameLabel, BorderLayout.NORTH);

		boardPanel = new JPanel(new GridLayout(3, 3, 5, 5));
		gamePanel.add(boardPanel, BorderLayout.CENTER);

		board = new Button[BOARD_LENGTH][BOARD_LENGTH];
		Button btn;
		for (int row = 0; row < BOARD_LENGTH; row++) {
			for (int col = 0; col < BOARD_LENGTH; col++) {
				btn = new Button("", row, col);
				btn.addActionListener(this);
				board[row][col] = btn;
				boardPanel.add(board[row][col]);
			}
		}

		// Start New Game and Restart Game buttons.
		btnPanel = new JPanel();

		JButton btnStartNew = new JButton("Start New");
		btnStartNew.setFont(new Font("Arial", Font.BOLD, 20));
		btnStartNew.setAlignmentX(CENTER_ALIGNMENT);
		btnStartNew.addActionListener(e-> {
			int result = JOptionPane.showConfirmDialog(null, "Do you want to start the new game? ", "Confirm Dialog", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (result == JOptionPane.YES_OPTION) {
				resetGamePanel(true);
				card.show(getContentPane(), "startPanel");
			}
		});
		btnPanel.add(btnStartNew);

		JButton btnRestart = new JButton("Restart");
		btnRestart.setFont(new Font("Arial", Font.BOLD, 20));
		btnRestart.setAlignmentX(CENTER_ALIGNMENT);
		btnRestart.addActionListener(e-> {
			int result = JOptionPane.showConfirmDialog(null, "Do you want to restart the game?", "Confirm Dialog", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (result == JOptionPane.YES_OPTION) {
				resetGamePanel(false);
				card.show(getContentPane(), "gamePanel");
			}
		});
		btnPanel.add(btnRestart);


		this.add(gamePanel, "gamePanel");
	}

	private JPanel getPlayerPanel(String title) {
		LineBorder blackBorder = new LineBorder(Color.black, 2);
		TitledBorder titleBorder;

		JPanel panel = new JPanel();
		titleBorder = BorderFactory.createTitledBorder(blackBorder, title);
		titleBorder.setTitleFont(new Font("Arial", Font.PLAIN, 15));
		panel.setBorder(titleBorder);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(CENTER_ALIGNMENT);
		panel.setAlignmentY(CENTER_ALIGNMENT);
		panel.setMaximumSize(new Dimension(350, 200));

		return panel;
	}

	private void playTurn(Player player, Button btn) {

		int row = btn.row;
		int col = btn.col;

		// If selected index is occupid then show error.
		if (isOccupied(row, col)) {
			showMessage("Error", "Sorry, This place is occupied. Select another.");
			return;
		}

		board[row][col].setText(player.ch);
		emptySpace--;
		chance = !chance;
		Player p = chance ? player1 : player2;
		gameLabel.setText(String.format("%s's Turn (%S)", p.name , p.ch));
		if (checkCondition()) {
			exitMessage();
		}
	}

	private boolean checkCondition() {
		// Check win for all rows.
		for (int row = 0; row < BOARD_LENGTH; row++) {
			if (isOccupied(row, 0) && Button.equals(board[row][0], board[row][1], board[row][2])) {
				Player.winPlayer = board[row][0].getText().equals(player1.ch) ? player1 : player2;
				makeDisable(row, 0, row, 1, row, 2);
				return true;
			}
		}

		// Check win for all colums.
		for (int col = 0; col < BOARD_LENGTH; col++) {
			if (isOccupied(0, col) && Button.equals(board[0][col], board[1][col], board[2][col])) {
				Player.winPlayer = board[0][col].getText().equals(player1.ch) ? player1 : player2;
				makeDisable(0, col, 1, col, 2, col);
				return true;
			}
		}

		// Check For two diagonals.
		if (isOccupied(0, 0) && Button.equals(board[0][0], board[1][1], board[2][2])) {
			Player.winPlayer = board[0][0].getText().equals(player1.ch) ? player1 : player2;
			makeDisable(0, 0, 1, 1, 2, 2);
			return true;
		}
		if (isOccupied(0, 2) && Button.equals(board[0][2], board[1][1], board[2][0])) {
			Player.winPlayer = board[0][2].getText().equals(player1.ch) ? player1 : player2;
			makeDisable(0, 2, 1, 1, 2, 0);
			return true;
		}

		return false;
	}

	private boolean isOccupied(int row, int col) {
		return !board[row][col].getText().isEmpty();
	}

	private void showMessage(String title, String message) {
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	private void resetGamePanel(boolean isStartNew) {
		for (int row = 0; row < BOARD_LENGTH; row++) {
			for (int col = 0; col < BOARD_LENGTH; col++) {
				board[row][col].setText("");
				board[row][col].setEnabled(true);
				board[row][col].setBackground(null);
				board[row][col].addActionListener(this);
			}
		}

		if (isStartNew) {
			player1 = player2 = null;
		} else {
			gameLabel.setText(String.format("%s's Turn (%s)", player1.name, player1.ch));
		}

		try {
			gamePanel.remove(btnPanel);
			validate();
		} catch (Exception e) {}

		chance = true;
		Player.winPlayer = null;
		emptySpace = BOARD_LENGTH * BOARD_LENGTH;

	}

	private void makeDisable(int r1, int c1, int r2, int c2, int r3, int c3) {
		for (int row = 0; row < BOARD_LENGTH; row++) {
			for (int col = 0; col < BOARD_LENGTH; col++) {
				board[row][col].setEnabled(false);
				board[row][col].removeActionListener(this);
			}
		}

		if (r1 != -1) {
			board[r1][c1].setEnabled(true);
			board[r1][c1].setBackground(Button.WIN_COLOR);
			board[r2][c2].setEnabled(true);
			board[r2][c2].setBackground(Button.WIN_COLOR);
			board[r3][c3].setEnabled(true);
			board[r3][c3].setBackground(Button.WIN_COLOR);
		}
	}

	private void exitMessage() {
		String message = "";
		if (Player.winPlayer != null) {
			message = "Congratulation " + Player.winPlayer.name + ", You win the game.";
			gameLabel.setText("Congratulation " + Player.winPlayer.name);
		} else {
			message = "Game Draw.";
			gameLabel.setText(message);
		}

		gamePanel.add(btnPanel, BorderLayout.SOUTH);
		validate();

		showMessage("Result", message);
		card.show(getContentPane(), "exitPanel");
	}

	public static void main(String arg[]) {
		new TicTacToe().setVisible(true);;
	}
}