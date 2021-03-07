import java.net.*;
import java.util.Arrays;

import javax.swing.SwingUtilities;

import java.io.*;
import java.lang.reflect.Array;

public class Server {
    private static final int BOARD_LENGTH = GamePanel.BOARD_LENGTH;

    private static int PORT = 3334;
    private static String HOST_NAME;
    private static String HOST_ADDRESS;

    private PlayerInfo player1, player2;
    private Player winPlayer = null;
    private boolean chance = true;
    private boolean isRunning = true;
    private String[][] board = new String[BOARD_LENGTH][BOARD_LENGTH];
    private int emptySpace = BOARD_LENGTH * BOARD_LENGTH;

    private class PlayerInfo {
        Socket socket;
        Player player;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;

        public PlayerInfo(Socket socket) throws Exception {
            this.socket = socket;
            this.oos = new ObjectOutputStream(this.socket.getOutputStream());
            this.ois = new ObjectInputStream(this.socket.getInputStream());

        }

        public void sendObject(Object object) throws Exception {
            this.oos.writeObject(object);
        }

        public Object receiveObject() throws Exception {
            return this.ois.readObject();
        }

    }

    public Server(Socket player1Socket, Socket player2Socket) throws Exception {
        this.player1 = new PlayerInfo(player1Socket);
        this.player2 = new PlayerInfo(player2Socket);

        for (int row = 0; row < BOARD_LENGTH; row++) {
            for (int col = 0; col < BOARD_LENGTH; col++) {
                board[row][col] = "";
            }
        }

        gameStart();
    }

    private void printBoard() {
        for (String[] s : board) {
            System.out.println(Arrays.toString(s));
        }
    }

    private void gameStart() throws Exception {

        // First taking players info.
        player1.player = (Player) player1.receiveObject();
        player2.player = (Player) player2.receiveObject();

        // Setting turn and assign character to the players.
        player1.player.setChar(Player.X);
        player1.player.setTurn(chance);
        player2.player.setChar(Player.O);
        player2.player.setTurn(!chance);

        // Send them back to the other player info. 
        player1.sendObject(player2.player);
        player2.sendObject(player1.player);

        while (isRunning) {
            if (chance) {
                playTurn(player1);
            } else {
                playTurn(player2);
            }
            printBoard();
            System.out.println("empty space = " + emptySpace);

            if (emptySpace == 0 && winPlayer == null) {
                exitMessage(new GameOver());
                // makeDisable(-1, -1, -1, -1, -1, -1);
            }
        }
    }

    private void playTurn(PlayerInfo player) throws Exception {
        String ch = player.player.getChar();

        GameRunning gr = (GameRunning) player.receiveObject();

        // setting character in board.
        board[gr.row][gr.col] = ch;

        if (player == player1) {
            player2.sendObject(gr);
        } else {
            player1.sendObject(gr);
        }

        emptySpace--;
        chance = !chance;
        GameOver gOver = checkCondition();
        if (gOver != null) {
            exitMessage(gOver);
        }
    }

    private GameOver checkCondition() {
        // Check win for all rows.
        for (int row = 0; row < BOARD_LENGTH; row++) {
            if (isOccupied(row, 0) && isEquals(board[row][0], board[row][1], board[row][2])) {
                winPlayer = board[row][0].equals(player1.player.getChar()) ? player1.player : player2.player;
                return new GameOver(row, 0, row, 1, row, 2);
            }
        }

        // Check win for all colums.
        for (int col = 0; col < BOARD_LENGTH; col++) {
            if (isOccupied(0, col) && isEquals(board[0][col], board[1][col], board[2][col])) {
                winPlayer = board[0][col].equals(player1.player.getChar()) ? player1.player : player2.player;
                return new GameOver(0, col, 1, col, 2, col);
            }
        }

        // Check For two diagonals.
        if (isOccupied(0, 0) && isEquals(board[0][0], board[1][1], board[2][2])) {
            winPlayer = board[0][0].equals(player1.player.getChar()) ? player1.player : player2.player;
            return new GameOver(0, 0, 1, 1, 2, 2);
        }
        if (isOccupied(0, 2) && isEquals(board[0][2], board[1][1], board[2][0])) {
            winPlayer = board[0][2].equals(player1.player.getChar()) ? player1.player : player2.player;
            return new GameOver(0, 2, 1, 1, 2, 0);
        }

        return null;
    }

    private boolean isOccupied(int row, int col) {
        return !board[row][col].isEmpty();
    }

    private boolean isEquals(String s1, String s2, String s3) {
        return s1.equals(s2) && s2.equals(s3);
    }

    private void exitMessage(GameOver gOver) throws Exception {
        gOver.winPlayer = winPlayer;
        player1.sendObject(gOver);
        player2.sendObject(gOver);

        isRunning = false;
    }

    public static void main(String[] args) {

        try {
            HOST_NAME = InetAddress.getLocalHost().getHostName();
            HOST_ADDRESS = InetAddress.getLocalHost().getHostAddress();

            ServerSocket server = new ServerSocket(PORT);
            System.out.println(String.format("\nServer HOST NAME : %s", HOST_NAME));
            System.out.println(String.format("Server HOST ADDRESS : %s", HOST_ADDRESS));
            System.out.println(String.format("Server is started on Port No.: %d\n", PORT));
            while (true) {
                System.out.println("Waiting for new players.");
                Socket player1Socket = server.accept();
                System.out.println("Player 1 is connected.");
                Socket player2Socket = server.accept();
                System.out.println("Player 2 is connected.");

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new Server(player1Socket, player2Socket);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
