import java.io.Serializable;

public class Player implements Serializable {
    public static final String X = "X";
    public static final String O = "O";
    private String name;
    private String ch;
    private boolean turn;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getChar() {
        return this.ch;
    }

    public void setChar(String ch) {
        this.ch = ch;
    }

    public boolean getTurn() {
        return this.turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }
}
