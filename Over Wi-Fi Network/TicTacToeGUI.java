import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private CardLayout card;
    private InfoPanel infoPanel;
    private WaitingPanel waitingPanel;
    private GamePanel gamePanel;
    private ClientSocket socket;
    private Thread t;
    private Button[][] board;

    private String playerName;
    private String hostName;
    private int port;

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

    private void assignActionListenerToButtons() {
        // Adding actionlistener for connect button.
        infoPanel.getConnectButton().addActionListener(evt -> {
            if (checkValidation()) {
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

        gamePanel.getStartNewButton().addActionListener(evt -> {
            resetData();
            gamePanel.removeButtonPanel();
        });

        // Assign all button to this action listener.
        for (int row = 0; row < GamePanel.BOARD_LENGTH; row++) {
            for (int col = 0; col < GamePanel.BOARD_LENGTH; col++) {
                board[row][col].addActionListener(this);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        try {
            if (chance == player.getTurn()) {
                Button btn = (Button) evt.getSource();
                int row = btn.row;
                int col = btn.col;

                // If selected index is occupid then show error.
                if (isOccupied(row, col)) {
                    showMessage("Error", "Sorry, This place is occupied. Select another.");
                    return;
                }

                board[row][col].setText(player.getChar());
                chance = !chance;

                socket.sendObject(new GameRunning(row, col));

                setGameLabel();
            } else {
                showMessage("Error", otherPlayer.getName() + "'s Turn.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            socket.sendObject(player);
            otherPlayer = (Player) socket.reciveObject();

            player.setTurn(!otherPlayer.getTurn());
            player.setChar(otherPlayer.getChar().equals(Player.X) ? Player.O : Player.X);

            System.out.println(player);
            System.out.println(otherPlayer);

            setGameLabel();

            card.show(this.getContentPane(), "gamePanel");

            waitingForOtherPlayerResponce();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void waitingForOtherPlayerResponce() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Object obj = socket.reciveObject();

                        if (obj instanceof GameRunning) {
                            GameRunning gr = (GameRunning) obj;
                            board[gr.row][gr.col].setText(otherPlayer.getChar());

                            chance = !chance;

                            setGameLabel();
                        } else if (obj instanceof GameOver) {
                            GameOver gOver = (GameOver) obj;
                            exitMessage(gOver);
                            break;
                        } else {
                            String message = (String) obj;
                            showMessage("Information", message);
                            resetData();
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
            showMessage("Error", e.getMessage());
        }
        return false;
    }

    private void resetData() {
        for (int row = 0; row < GamePanel.BOARD_LENGTH; row++) {
            for (int col = 0; col < GamePanel.BOARD_LENGTH; col++) {
                board[row][col].setText("");
                board[row][col].setEnabled(true);
                board[row][col].setBackground(null);
                board[row][col].removeActionListener(this);
                board[row][col].addActionListener(this);
            }
        }
        chance = true;
        this.card.show(this.getContentPane(), "infoPanel");
        t = new Thread(this);
    }

    private void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
    }

    private void makeDisable(GameOver ge) {
        for (int row = 0; row < GamePanel.BOARD_LENGTH; row++) {
            for (int col = 0; col < GamePanel.BOARD_LENGTH; col++) {
                board[row][col].setEnabled(false);
                board[row][col].removeActionListener(this);
            }
        }

        if (ge.winPlayer != null) {
            board[ge.r1][ge.c1].setEnabled(true);
            board[ge.r1][ge.c1].setBackground(Button.WIN_COLOR);
            board[ge.r2][ge.c2].setEnabled(true);
            board[ge.r2][ge.c2].setBackground(Button.WIN_COLOR);
            board[ge.r3][ge.c3].setEnabled(true);
            board[ge.r3][ge.c3].setBackground(Button.WIN_COLOR);
        }
    }

    private void exitMessage(GameOver ge) {
        makeDisable(ge);
        if (ge.winPlayer != null) {
            if (ge.winPlayer.getName().equals(player.getName())) {
                gamePanel.setGameLabelText("Congratulation, You won.");
            } else {
                gamePanel.setGameLabelText("You lost.");
            }
        } else {
            gamePanel.setGameLabelText("Game Draw.");
        }

        gamePanel.addButtonPanel();
    }

    public static void main(String[] args) {
        new TicTacToeGUI().setVisible(true);
    }
}