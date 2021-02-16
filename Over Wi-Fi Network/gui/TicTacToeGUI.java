import javax.swing.JFrame;
import javax.swing.WindowConstants;

/* 
    
 */

public class TicTacToeGUI extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public TicTacToeGUI() {
        try {
            // InfoPanel obj = new InfoPanel();
            GamePanel obj = new GamePanel();
            add(obj);

            setTitle("Tic Tac Toe");
            setSize(500, 500);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
        } catch (Exception e) {
            System.out.println("Something wrong.");
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        new TicTacToeGUI();
    }
}