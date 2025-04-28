import javax.swing.*;
import java.util.*;
public class Rook extends Piece{
    // instance variables
    boolean hasMoved;

    // constructors
    public Rook(char c, int l, int n, boolean h){
        super(c, l, n);
        type = 'r';
        value = 5;
        if (color == 'b') {
            value *= -1;
        }
        hasMoved = h;
        ImageIcon image = new ImageIcon("src/resources/pieces/" + color + type + ".png");
        this.setIcon(image);

        this.setHorizontalAlignment(JLabel.CENTER);
        this.setVerticalAlignment(JLabel.CENTER);
    }

    // setters and getters
    @Override
    public void setHasMoved(boolean moved) {
        hasMoved = moved;
    }
    @Override
    public boolean getHasMoved() {
        return hasMoved;
    }

    // custom methods
    @Override
    public Set<Coordinates> specificValidMoves() {
        char oppColor = enemyColor(color);
        Set<Coordinates> specificMoves = new HashSet<>();
        // up
        int i = 1;
        while (numberIndex + i != 8) {
            Coordinates coords = new Coordinates(letterIndex, numberIndex + i);
            specificMoves.add(coords);
            char pieceColor = Board.pieces[coords.x][coords.y].getColor();
            if (pieceColor != 'e') {
                // if it's the enemy king, then only allow the enemy to move to tiles that block/take this piece
                if (Board.turn == color && pieceColor == oppColor && Board.pieces[coords.x][coords.y].getType() == 'k') {
                    Board.onlyPossibilities.add(new Coordinates(letterIndex, numberIndex));
                    Board.onlyPossibilities.addAll(specificMoves);
                    if (numberIndex + i + 1 <= 7) {
                        specificMoves.add(new Coordinates(letterIndex, numberIndex + i + 1));
                    }
                }
                // if it's the enemy's piece and their king will be found beyond this piece, then check to see if this piece is pinned
                else if (Board.turn == color && pieceColor == oppColor && Board.kings.get(oppColor).x == letterIndex && Board.kings.get(oppColor).y > numberIndex + i) {
                    for (int y = numberIndex + i + 1; y < 8; y++) {
                        if (y == Board.kings.get(oppColor).y) {
                            // the piece is pinned
                            Board.pieces[coords.x][coords.y].pinned = 'v';
                            break;
                        }
                        else if (Board.pieces[letterIndex][y].getColor() != 'e') {
                            break;
                        }
                    }
                }
                break;
            }
            i++;
        }
        validMoves.addAll(specificMoves);
        specificMoves.clear();
        // down
        i = 1;
        while (numberIndex - i != -1) {
            Coordinates coords = new Coordinates(letterIndex, numberIndex - i);
            specificMoves.add(coords);
            char pieceColor = Board.pieces[coords.x][coords.y].getColor();
            if (pieceColor != 'e') {
                // if it's the enemy king, then only allow the enemy to move to tiles that block/take this piece
                if (Board.turn == color && pieceColor == oppColor && Board.pieces[coords.x][coords.y].getType() == 'k'){
                    Board.onlyPossibilities.add(new Coordinates(letterIndex, numberIndex));
                    Board.onlyPossibilities.addAll(specificMoves);
                    if (numberIndex - i - 1 >= 0) {
                        specificMoves.add(new Coordinates(letterIndex, numberIndex - i - 1));
                    }
                }

                // if it's the enemy's piece and their king will be found beyond this piece, then check to see if this piece is pinned
                if (Board.turn == color && pieceColor == oppColor && Board.kings.get(oppColor).x == letterIndex && Board.kings.get(oppColor).y < numberIndex - i) {
                    for (int y = numberIndex - i - 1; y >= 0; y--) {
                        if (y == Board.kings.get(oppColor).y) {
                            // the piece is pinned
                            Board.pieces[coords.x][coords.y].pinned = 'v';
                            break;
                        }
                        else if (Board.pieces[letterIndex][y].getColor() != 'e') {
                            break;
                        }
                    }
                }
                break;
            }
            i++;
        }
        validMoves.addAll(specificMoves);
        specificMoves.clear();
        // right
        i = 1;
        while (letterIndex + i != 8) {
            Coordinates coords = new Coordinates(letterIndex + i, numberIndex);
            specificMoves.add(coords);
            char pieceColor = Board.pieces[coords.x][coords.y].getColor();
            if (pieceColor != 'e') {
                // if it's the enemy king, then only allow the enemy to move to tiles that block/take this piece
                if (Board.turn == color && pieceColor == oppColor && Board.pieces[coords.x][coords.y].getType() == 'k'){
                    Board.onlyPossibilities.add(new Coordinates(letterIndex, numberIndex));
                    Board.onlyPossibilities.addAll(specificMoves);
                    if (letterIndex + i + 1 <= 7) {
                        specificMoves.add(new Coordinates(letterIndex + i + 1, numberIndex));
                    }
                }

                // if it's the enemy's piece and their king will be found beyond this piece, then check to see if this piece is pinned
                if (Board.turn == color && pieceColor == oppColor && Board.kings.get(oppColor).y == numberIndex && Board.kings.get(oppColor).x > letterIndex + i) {
                    for (int x = letterIndex + i + 1; x < 8; x++) {
                        if (x == Board.kings.get(oppColor).x) {
                            // the piece is pinned
                            Board.pieces[coords.x][coords.y].pinned = 'h';
                            break;
                        }
                        else if (Board.pieces[x][numberIndex].getColor() != 'e') {
                            break;
                        }
                    }
                }
                break;
            }
            i++;
        }
        validMoves.addAll(specificMoves);
        specificMoves.clear();
        // left
        i = 1;
        while (letterIndex - i != -1) {
            Coordinates coords = new Coordinates(letterIndex - i, numberIndex);
            specificMoves.add(coords);
            char pieceColor = Board.pieces[coords.x][coords.y].getColor();
            if (pieceColor != 'e') {
                // if it's the enemy king, then only allow the enemy to move to tiles that block/take this piece
                if (Board.turn == color && pieceColor == oppColor && Board.pieces[coords.x][coords.y].getType() == 'k'){
                    Board.onlyPossibilities.add(new Coordinates(letterIndex, numberIndex));
                    Board.onlyPossibilities.addAll(specificMoves);
                    if (letterIndex - i - 1 >= 0) {
                        specificMoves.add(new Coordinates(letterIndex - i - 1, numberIndex));
                    }
                }

                // if it's the enemy's piece and their king will be found beyond this piece, then check to see if this piece is pinned
                if (Board.turn == color && pieceColor == oppColor && Board.kings.get(oppColor).y == numberIndex && Board.kings.get(oppColor).x < letterIndex - i) {
                    for (int x = letterIndex - i - 1; x >= 0; x--) {
                        if (x == Board.kings.get(oppColor).x) {
                            // the piece is pinned
                            Board.pieces[coords.x][coords.y].pinned = 'h';
                            break;
                        }
                        else if (Board.pieces[x][numberIndex].getColor() != 'e') {
                            break;
                        }
                    }
                }
                break;
            }
            i++;
        }
        validMoves.addAll(specificMoves);

        return validMoves;
    }

    @Override
    public void move(int newX, int newY) {
        super.move(newX, newY);
        hasMoved = true;
    }
}
