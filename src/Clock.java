import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Clock extends JLabel{
    private int elapsedTime; // Time in seconds
    public Timer timer;
    private Color color = new Color(180, 180, 180);

    public Clock(int time) {
        elapsedTime = time;
        int minutes = time / 60;
        int seconds = time % 60;
        setFont(new Font("Arial", Font.BOLD, 25));
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 40));
        setText(String.format("   %02d:%02d", minutes, seconds));
        timer = new Timer(1000, new ActionListener() { // 1000 ms = 1 second
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime--; // Increment time by 1 second
                int minutes = elapsedTime / 60;
                int seconds = elapsedTime % 60;
                setText(String.format("   %02d:%02d", minutes, seconds));
            }
        });
    }

    public void start() {
        timer.start();
        color = Color.WHITE;
        repaint();
    }
    public void stop() {
        timer.stop();
        color = new Color(180, 180, 180);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw filled rectangle
        g2d.setColor(new Color(70, 70, 70));
        g2d.fillRect(-40, 5, getWidth() + 30, getHeight() - 10);

        // Set color for the text
        g2d.setColor(color);

        // Draw the text on top of the rectangle
        g2d.setFont(getFont());
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(getText(), x, y);
    }
}
