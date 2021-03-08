import java.io.Serializable;

public class GameRunning implements Serializable {
    public int row;
    public int col;
    public boolean chance;

    public GameRunning(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
