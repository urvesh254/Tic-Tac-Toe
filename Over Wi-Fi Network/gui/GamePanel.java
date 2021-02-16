package gui;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    public static final int BOARD_LENGTH = 3;
    private Button[][] board;
    private JLabel gameLabel;
    private JPanel btnPanel;
    private JButton btnStartNew, btnRestart;

    public GamePanel() {
        try {
            this.setLayout(new BorderLayout(5, 5));

            gameLabel = new JLabel("", SwingConstants.CENTER);
            gameLabel.setPreferredSize(new Dimension(500, 70));
            gameLabel.setFont(new Font("Courgette", Font.BOLD, 28));
            this.add(gameLabel, BorderLayout.NORTH);

            JPanel boardPanel = new JPanel(new GridLayout(3, 3, 5, 5));
            this.add(boardPanel, BorderLayout.CENTER);

            board = new Button[BOARD_LENGTH][BOARD_LENGTH];
            Button btn;
            for (int row = 0; row < BOARD_LENGTH; row++) {
                for (int col = 0; col < BOARD_LENGTH; col++) {
                    btn = new Button("", row, col);
                    board[row][col] = btn;
                    boardPanel.add(board[row][col]);
                }
            }

            // Start New Game and Restart Game buttons.
            btnPanel = new JPanel();

            btnStartNew = new JButton("Start New");
            btnStartNew.setFont(new Font("Arial", Font.BOLD, 20));
            btnStartNew.setAlignmentX(CENTER_ALIGNMENT);
            btnPanel.add(btnStartNew);

            btnRestart = new JButton("Restart");
            btnRestart.setFont(new Font("Arial", Font.BOLD, 20));
            btnRestart.setAlignmentX(CENTER_ALIGNMENT);

            btnPanel.add(btnRestart);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean addButtonPanel() {
        try {
            this.add(btnPanel);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean removeButtonPanel() {
        try {
            this.remove(btnPanel);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public JLabel getGameLabel() {
        return this.gameLabel;
    }

    public Button[][] getBoard() {
        return this.board;
    }

    public JButton getStartNewButton() {
        return this.btnStartNew;
    }

    public JButton getRestartButton() {
        return this.btnRestart;
    }
}
