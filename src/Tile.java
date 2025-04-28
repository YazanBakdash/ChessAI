import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.*;

public class Tile extends JPanel {
    private final int letterIndex;
    private final int numberIndex;
    public boolean circle;
    public Color color;

    Tile(int letterIndex, int numberIndex, Color color) {
        this.letterIndex = letterIndex;
        this.numberIndex = numberIndex;
        this.color = color;
        this.setLayout(new BorderLayout());
        this.setBackground(color);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Coordinates coords = new Coordinates(letterIndex, numberIndex);
                if (Board.sourcePiece == null) {
                    if (Board.pieces[letterIndex][numberIndex].getColor() == Board.turn) {
                        Board.sourcePiece = Board.pieces[letterIndex][numberIndex];
                        for (Coordinates validMove: Board.sourcePiece.validMoves) {
                            Board.tiles[validMove.x][validMove.y].setCircle(true);
                        }
                    }
                } else {
                    for (Coordinates validMove: Board.sourcePiece.validMoves) {
                        Board.tiles[validMove.x][validMove.y].setCircle(false);
                    }
                    if (Board.sourcePiece.validMoves.contains(coords)) {
                        Action action = new Action(new Coordinates(Board.sourcePiece.letterIndex, Board.sourcePiece.numberIndex), new Coordinates(letterIndex, numberIndex), -1);
                        Board.sourcePiece.move(letterIndex, numberIndex);
                        Board.changeTurns();
                        /*AI.undoMove(action);
                        Board.sourcePiece.move(letterIndex, numberIndex);
                        Board.changeTurns();*/
                    }
                    Board.sourcePiece = null;
                    if (Board.turn == 'b') {
                        AI.move();
                    }
                    // change the color of this tile back
                    Tile.this.setBackground(color);

                    //AI.displayLocations();
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (Board.sourcePiece != null) {
                    Color newColor;
                    if (color.getRed() == 115) {
                        newColor = new Color(75, 109, 42);
                    } else {
                        newColor = new Color(195, 196, 168);
                    }
                    Tile.this.setBackground(newColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (Board.sourcePiece != null) {
                    Tile.this.setBackground(color);
                }
            }
        });
    }

    public void setCircle(boolean c) {
        circle = c;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (circle) {
            Graphics2D g2d = (Graphics2D) g;

            int width = getWidth();
            int height = getHeight();
            int size = Math.min(width, height);

            Color circleColor;
            if (color.getRed() == 115) {
                circleColor = new Color(55, 99, 32);
            } else {
                circleColor = new Color(175, 176, 148);
            }
            g.setColor(circleColor);
            if (Board.pieces[letterIndex][numberIndex].getType() == 'e') {
                int diameter = size / 3;
                int x = (width - diameter) / 2;
                int y = (height - diameter) / 2;
                g.fillOval(x, y, diameter, diameter);
            } else {
                g2d.setStroke(new BasicStroke(7));
                int diameter = size * 17 / 18;
                int x = (width - diameter) / 2;
                int y = (height - diameter) / 2;
                g2d.drawOval(x, y, diameter, diameter);
            }
        }
    }

}
