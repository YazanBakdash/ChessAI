import javax.swing.*;
import java.awt.*;

public class Main {
    private static final String FEN = null;
    public static JFrame background;
    public static Clock wClock;
    public static Clock bClock;
    public static Name wName;
    public static Name bName;
    public static Color bgColor;
    public static Board board;
    public static JLayeredPane layeredPane;

    public static void main(String[] args) {
        bgColor = new Color(60, 60, 60);
        background = new JFrame("Chess");
        background.setSize(740, 840);
        background.setLocationRelativeTo(null);
        background.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        background.setResizable(false);
        background.setVisible(true);
        ImageIcon icon = new ImageIcon("src/resources/pieces/wp.png");
        background.setIconImage(icon.getImage());
        background.getContentPane().setBackground(bgColor);

        // Set the layout of the background to BorderLayout
        background.setLayout(new BorderLayout());

        // Create the layered pane
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(740, 840));
        background.add(layeredPane, BorderLayout.CENTER);

        // side bars
        JPanel leftBar = new JPanel();
        leftBar.setBackground(bgColor);
        leftBar.setPreferredSize(new Dimension(10, 820));
        background.add(leftBar, BorderLayout.WEST);
        JPanel rightBar = new JPanel();
        rightBar.setBackground(bgColor);
        rightBar.setPreferredSize(new Dimension(10, 820));
        background.add(rightBar, BorderLayout.EAST);

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout(0, 0));
        topBar.setBackground(bgColor);
        topBar.setPreferredSize(new Dimension(720, 60));
        background.add(topBar, BorderLayout.NORTH);

        // Main board (center)
        Main.board = new Board(true, FEN);
        background.add(board, BorderLayout.CENTER);

        // Bottom bar
        JPanel bottomBar = new JPanel(new BorderLayout(0, 0));
        bottomBar.setBackground(bgColor);
        bottomBar.setPreferredSize(new Dimension(720, 60));
        background.add(bottomBar, BorderLayout.SOUTH);

        wName = new Name("You", 'w');
        bName = new Name("Computer", 'b');
        if (FEN != null) {
            wName.updateScore(Board.score);
            bName.updateScore(Board.score);
        }
        bottomBar.add(wName);
        topBar.add(bName);
        wClock = new Clock(600);
        bClock = new Clock(600);
        bottomBar.add(wClock, BorderLayout.EAST);
        topBar.add(bClock, BorderLayout.EAST);


        background.revalidate();
        background.repaint();
    }
}