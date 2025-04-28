import javax.swing.*;
import java.util.*;
public class Bishop extends Piece{
    // constructors
    public Bishop(char c, int l, int n){
        super(c, l, n);
        value = 3;
        if (color == 'b') {
            value *= -1;
        }
        type = 'b';
        ImageIcon image = new ImageIcon("src/resources/pieces/" + color + type + ".png");
        this.setIcon(image);

        this.setHorizontalAlignment(JLabel.CENTER);
        this.setVerticalAlignment(JLabel.CENTER);
    }

    // custom methods
    @Override
    public Set<Coordinates> specificValidMoves() {
        char oppColor = enemyColor(color);

        Set<Coordinates> specificMoves = new HashSet<>();
        // top right
        int i = 1;
        while (letterIndex + i != 8 && numberIndex + i != 8) {
            Coordinates coords = new Coordinates(letterIndex + i, numberIndex + i);
            specificMoves.add(coords);
            char pieceColor = Board.pieces[coords.x][coords.y].getColor();
            if (pieceColor != 'e') {
                // if it's the enemy king, then only allow the enemy to move to tiles that block/take this piece
                if (Board.turn == color && pieceColor == oppColor && Board.pieces[coords.x][coords.y].getType() == 'k'){
                    Board.onlyPossibilities.add(new Coordinates(letterIndex, numberIndex));
                    Board.onlyPossibilities.addAll(specificMoves);
                    if (letterIndex + i + 1 <= 7 && numberIndex + i + 1 <= 7) {
                        specificMoves.add(new Coordinates(letterIndex + i + 1, numberIndex + i + 1));
                    }
                }

                // if it's the enemy's piece and their king will be found beyond this piece, then check to see if this piece is pinned
                if (Board.turn == color && pieceColor == oppColor && Board.kings.get(oppColor).y - numberIndex == Board.kings.get(oppColor).x - letterIndex && Board.kings.get(oppColor).y > numberIndex + i) {
                    for (int j = 1; j + letterIndex + i < 8 && j + numberIndex + i < 8; j++) {
                        if (numberIndex + i + j == Board.kings.get(oppColor).y) {
                            // the piece is pinned
                            Board.pieces[coords.x][coords.y].pinned = 'a';
                            break;
                        }
                        else if (Board.pieces[letterIndex + i + j][numberIndex + i + j].getColor() != 'e') {
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
        // bottom right
        i = 1;
        while (letterIndex + i != 8 && numberIndex - i != -1) {
            Coordinates coords = new Coordinates(letterIndex + i, numberIndex - i);
            specificMoves.add(coords);
            char pieceColor = Board.pieces[coords.x][coords.y].getColor();
            if (pieceColor != 'e') {
                // if it's the enemy king, then only allow the enemy to move to tiles that block/take this piece
                if (Board.turn == color && pieceColor == oppColor && Board.pieces[coords.x][coords.y].getType() == 'k'){
                    Board.onlyPossibilities.add(new Coordinates(letterIndex, numberIndex));
                    Board.onlyPossibilities.addAll(specificMoves);
                    if (letterIndex + i + 1 <= 7 && numberIndex - i - 1 >= 0) {
                        specificMoves.add(new Coordinates(letterIndex + i + 1, numberIndex - i - 1));
                    }
                }

                // if it's the enemy's piece and their king will be found beyond this piece, then check to see if this piece is pinned
                if (Board.turn == color && pieceColor == oppColor && Board.kings.get(oppColor).y - numberIndex == letterIndex - Board.kings.get(oppColor).x && Board.kings.get(oppColor).y < numberIndex - i) {
                    for (int j = 1; j + letterIndex + i < 8 && j - numberIndex - i >= 0; j++) {
                        if (numberIndex - i - j == Board.kings.get(oppColor).y) {
                            // the piece is pinned
                            Board.pieces[coords.x][coords.y].pinned = 'd';
                            break;
                        }
                        else if (Board.pieces[letterIndex + i + j][numberIndex - i - j].getColor() != 'e') {
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
        // bottom left
        i = 1;
        while (letterIndex - i != -1 && numberIndex - i != -1) {
            Coordinates coords = new Coordinates(letterIndex - i, numberIndex - i);
            specificMoves.add(coords);
            char pieceColor = Board.pieces[coords.x][coords.y].getColor();
            if (pieceColor != 'e') {
                // if it's the enemy king, then only allow the enemy to move to tiles that block/take this piece
                if (Board.turn == color && pieceColor == oppColor && Board.pieces[coords.x][coords.y].getType() == 'k'){
                    Board.onlyPossibilities.add(new Coordinates(letterIndex, numberIndex));
                    Board.onlyPossibilities.addAll(specificMoves);
                    if (letterIndex - i - 1 >= 0 && numberIndex - i - 1 >= 0) {
                        specificMoves.add(new Coordinates(letterIndex - i - 1, numberIndex - i - 1));
                    }
                }

                // if it's the enemy's piece and their king will be found beyond this piece, then check to see if this piece is pinned
                if (Board.turn == color && pieceColor == oppColor && Board.kings.get(oppColor).y - numberIndex == Board.kings.get(oppColor).x - letterIndex && Board.kings.get(oppColor).y < numberIndex - i) {
                    for (int j = 1; letterIndex - i - j >= 0 && numberIndex - i - j >= 0; j++) {
                        if (numberIndex - i - j == Board.kings.get(oppColor).y) {
                            // the piece is pinned
                            Board.pieces[coords.x][coords.y].pinned = 'a';
                            break;
                        }
                        else if (Board.pieces[letterIndex - i - j][numberIndex - i - j].getColor() != 'e') {
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
        // top left
        i = 1;
        while (letterIndex - i != -1 && numberIndex + i != 8) {
            Coordinates coords = new Coordinates(letterIndex - i, numberIndex + i);
            specificMoves.add(coords);
            char pieceColor = Board.pieces[coords.x][coords.y].getColor();
            if (pieceColor != 'e') {
                // if it's the enemy king, then only allow the enemy to move to tiles that block/take this piece
                if (Board.turn == color && pieceColor == oppColor && Board.pieces[coords.x][coords.y].getType() == 'k'){
                    Board.onlyPossibilities.add(new Coordinates(letterIndex, numberIndex));
                    Board.onlyPossibilities.addAll(specificMoves);
                    if (letterIndex - i - 1 >= 0 && numberIndex + i + 1 <= 7) {
                        specificMoves.add(new Coordinates(letterIndex - i - 1, numberIndex + i + 1));
                    }
                }

                // if it's the enemy's piece and their king will be found beyond this piece, then check to see if this piece is pinned
                if (Board.turn == color && pieceColor == oppColor && Board.kings.get(oppColor).y - numberIndex == letterIndex - Board.kings.get(oppColor).x && Board.kings.get(oppColor).y > numberIndex + i) {
                    for (int j = 1; letterIndex - i - j >= 0 && numberIndex + i + j < 8; j++) {
                        if (numberIndex + i + j == Board.kings.get(oppColor).y) {
                            // the piece is pinned
                            Board.pieces[coords.x][coords.y].pinned = 'd';
                            break;
                        }
                        else if (Board.pieces[letterIndex - i - j][numberIndex + i + j].getColor() != 'e') {
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
}
