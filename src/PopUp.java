import javax.swing.*;
import java.awt.*;

public class PopUp extends JDialog {
    public PopUp(String title) {
        super(Main.background, title, true);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        panel.setBackground(Main.bgColor);
        getContentPane().setBackground(Main.bgColor);
        setResizable(false);

        String message;
        if (Board.winner == 'd') {
            message = "It's a draw!";
        } else if (Board.winner == 'w') {
            message = "White wins!";
        } else {
            message = "Black wins!";
        }
        JLabel messageLabel = new JLabel(message, JLabel.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        messageLabel.setForeground(Color.white);
        panel.add(messageLabel, BorderLayout.CENTER);

        JButton button = new Button();
        panel.add(button, BorderLayout.SOUTH);

        add(panel);
        setSize(200, 130);
        setLocationRelativeTo(Main.background);
        setVisible(true);
    }
}