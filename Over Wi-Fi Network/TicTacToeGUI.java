import javax.sound.sampled.Port;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.CardLayout;

public class TicTacToeGUI extends JFrame implements ActionListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private CardLayout card;
    private InfoPanel infoPanel;
    private GamePanel gamePanel;
    private ClientSocket socket;

    private String playerName;
    private String hostName;
    private int port;

    public TicTacToeGUI() {
        try {
            card = new CardLayout();
            this.setLayout(card);

            infoPanel = new InfoPanel();
            gamePanel = new GamePanel();

            assignActionListenerToButtons();

            this.add(infoPanel, "infoPanel");
            this.add(gamePanel, "gamePanel");

            setTitle("Tic Tac Toe");
            setSize(500, 500);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        } catch (Exception e) {
            System.out.println("Something wrong.");
            System.out.println(e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    private void assignActionListenerToButtons() {

        // Adding actionlistener for connect button.
        infoPanel.getConnectButton().addActionListener(evt -> {
            if (checkValidation()) {
                socket = new ClientSocket(hostName, port);
                try {
                    socket.connectToServer();
                } catch (Exception e) {
                    showMessage("Eoor", "Something is wrong.");
                }
                // card.show(this.getContentPane(), "waitingPanel");
                card.show(this.getContentPane(), "gamePanel");
            }
        });

        // Assign all button to this action listener.
        Button[][] board = gamePanel.getBoard();
        for (int row = 0; row < GamePanel.BOARD_LENGTH; row++) {
            for (int col = 0; col < GamePanel.BOARD_LENGTH; col++) {
                board[row][col].addActionListener(this);
            }
        }

    }

    private boolean checkValidation() {
        try {
            playerName = infoPanel.getPlayerName();
            if (playerName.isEmpty()) {
                throw new Exception("Player name should not be empty");
            }

            hostName = infoPanel.getServerAddress();
            if (hostName.isEmpty()) {
                throw new Exception("Server Host Name/IPV4 Address should not be empty.");
            }

            port = Integer.parseInt(infoPanel.getServerPortNo());
            if (port < 1 && port > 65535) {
                throw new NumberFormatException();
            }

            return true;
        } catch (NumberFormatException e) {
            showMessage("Error", "Port should be number from 1-65536");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            showMessage("Error", e.getMessage());
        }
        return false;
    }

    private void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        new TicTacToeGUI().setVisible(true);
    }
}