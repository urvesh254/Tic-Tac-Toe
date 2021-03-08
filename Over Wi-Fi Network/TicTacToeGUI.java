import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.awt.event.ActionEvent;

import java.awt.CardLayout;

public class TicTacToeGUI extends JFrame implements ActionListener, Runnable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    private CardLayout card;
    private InfoPanel infoPanel;
    private WaitingPanel waitingPanel;
    private GamePanel gamePanel;
    private ClientSocket socket;
    private Thread t;
    private Button[][] board;

    private String playerName;
    private String hostName = "UKPatel";
    private int port = 3334;

    private boolean chance = true;
    private Player player;
    private Player otherPlayer;

    public TicTacToeGUI() {
        try {
            t = new Thread(this);

            card = new CardLayout();
            this.setLayout(card);

            infoPanel = new InfoPanel();
            waitingPanel = new WaitingPanel();
            gamePanel = new GamePanel();

            board = gamePanel.getBoard();
            assignActionListenerToButtons();

            this.add(infoPanel, "infoPanel");
            this.add(waitingPanel, "waitingPanel");
            this.add(gamePanel, "gamePanel");

            this.setTitle("Tic Tac Toe");
            this.setSize(500, 500);
            this.setResizable(false);
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        } catch (Exception e) {
            System.out.println("Something wrong.");
            System.out.println(e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        System.out.println("button pressed.");
        try {
            System.out.println(chance + " " + player.getTurn());
            if (chance == player.getTurn()) {
                Button btn = (Button) evt.getSource();
                int row = btn.row;
                int col = btn.col;

                // If selected index is occupid then show error.
                System.out.println(isOccupied(row, col));
                if (isOccupied(row, col)) {
                    showMessage("Error", "Sorry, This place is occupied. Select another.");
                    return;
                }

                board[row][col].setText(player.getChar());
                System.out.println("back to action perform.");
                chance = !chance;

                waitingForOtherPlayerResponce(otherPlayer);
                // socket.sendObject(new GameRunning(!chance, row, col));
                socket.sendObject(new GameRunning(row, col));

                setGameLabel();
            } else {
                showMessage("Error", otherPlayer.getName() + "'s Turn.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void waitingForOtherPlayerResponce(Player p1) {
        if (chance == p1.getTurn()) {
            System.out.println("In waiting player " + player.getName() + " " + chance + " " + otherPlayer.getTurn());
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Object obj = socket.reciveObject();

                        if (obj instanceof GameRunning) {
                            GameRunning gr = (GameRunning) obj;
                            board[gr.row][gr.col].setText(p1.getChar());

                            chance = !chance;

                            setGameLabel();
                        } else {
                            GameOver gOver = (GameOver) obj;
                            System.out.println(gOver.isWin);
                            System.out.println(gOver.winPlayer.getName());
                            System.out.println("Game is over.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void assignActionListenerToButtons() {

        // Adding actionlistener for connect button.
        infoPanel.getConnectButton().addActionListener(evt -> {
            if (checkValidation()) {
                playerName = infoPanel.getPlayerName();
                socket = new ClientSocket(hostName, port);
                try {
                    socket.connectToServer();
                    System.out.println("Connected to server.");
                    card.show(this.getContentPane(), "waitingPanel");
                    t.start();

                } catch (UnknownHostException e) {
                    showMessage("Unknown Host Error", "Server is not running at Host Name : " + hostName);
                } catch (Exception e) {
                    showMessage("Error", "Something is wrong.");
                    e.printStackTrace();
                }
            }
        });

        // Assign all button to this action listener.
        for (int row = 0; row < GamePanel.BOARD_LENGTH; row++) {
            for (int col = 0; col < GamePanel.BOARD_LENGTH; col++) {
                board[row][col].addActionListener(this);
            }
        }
    }

    @Override
    public void run() {
        try {
            socket.sendObject(player);
            otherPlayer = (Player) socket.reciveObject();

            player.setTurn(!otherPlayer.getTurn());
            player.setChar(otherPlayer.getChar().equals(Player.X) ? Player.O : Player.X);

            // System.out.println("player : " + player.getName() + " " + player.getChar());
            // System.out.println("otherPlayer : " + otherPlayer.getName() + " " + otherPlayer.getChar());
            System.out.println(player);
            System.out.println(otherPlayer);

            setGameLabel();

            card.show(this.getContentPane(), "gamePanel");

            waitingForOtherPlayerResponce(otherPlayer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setGameLabel() {

        if (player.getTurn() == chance) {
            gamePanel.setGameLabelText(String.format("Your Turn (%s)", player.getChar()));
        } else {
            gamePanel.setGameLabelText(String.format("%s's Turn (%s)", otherPlayer.getName(), otherPlayer.getChar()));
        }

    }

    private boolean isOccupied(int row, int col) {
        return !board[row][col].getText().isEmpty();
    }

    private boolean checkValidation() {
        try {
            playerName = infoPanel.getPlayerName();
            if (playerName.isEmpty()) {
                throw new Exception("Player name should not be empty");
            }
            player = new Player(playerName);

            hostName = infoPanel.getServerAddress();
            if (hostName.isEmpty()) {
                throw new Exception("Server Host Name/IPV4 Address should not be empty.");
            }

            port = Integer.parseInt(infoPanel.getServerPortNo());
            if (port < 1 && port > 65535) {
                throw new NumberFormatException();
            }

            socket = new ClientSocket(hostName, port);

            return true;
        } catch (NumberFormatException e) {
            showMessage("Error", "Port should be number from 1-65536");
        } catch (Exception e) {
            // System.out.println(e.getMessage());
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