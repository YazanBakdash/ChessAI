import javax.swing.*;
import java.util.*;

public class Pawn extends Piece {
    // instance variables
    public int enPassant;
    
    // cosntructors
    public Pawn(char c, int l, int n){
        super(c, l, n);
        type = 'p';
        enPassant = -1;
        value = 1;
        if (color == 'b') {
            value *= -1;
        }
        ImageIcon image = new ImageIcon("src/resources/pieces/" + color + type + ".png");
        this.setIcon(image);

        this.setHorizontalAlignment(JLabel.CENTER);
        this.setVerticalAlignment(JLabel.CENTER);
    }

    // setters and getters
    @Override
    public int getEnPassant() {
        return enPassant;
    }
    @Override
    public void setEnPassant(int n) {
        enPassant = n;
    }

    @Override
    public Set<Coordinates> specificValidMoves() {
        if (color == 'w') {
            validMoves = wPawnMoves();
        }
        else {
            validMoves = bPawnMoves();
        }
        return validMoves;
    }

    private Set<Coordinates> wPawnMoves() {
        // moving straight up
        if (Board.pieces[letterIndex][numberIndex + 1].getColor() == 'e') {
            Coordinates coords = new Coordinates(letterIndex, numberIndex + 1);
            validMoves.add(coords);
        }
        if (numberIndex == 1 && Board.pieces[letterIndex][numberIndex + 1].getColor() == 'e' && Board.pieces[letterIndex][numberIndex + 2].getColor() == 'e') {
            Coordinates coords = new Coordinates(letterIndex, numberIndex + 2);
            validMoves.add(coords);
        }

        // taking a piece
        if (letterIndex != 7) {
            if (Board.pieces[letterIndex + 1][numberIndex + 1].getColor() == 'b') {
                Coordinates coords = new Coordinates(letterIndex + 1, numberIndex + 1);
                validMoves.add(coords);
            }
        }
        if (letterIndex != 0) {
            if (Board.pieces[letterIndex - 1][numberIndex + 1].getColor() == 'b') {
                Coordinates coords = new Coordinates(letterIndex - 1, numberIndex + 1);
                validMoves.add(coords);
            }
        }

        // en passant
        if (numberIndex == 4) {
            //left
            int x = letterIndex - 1;
            if (x >= 0 && Board.pieces[x][numberIndex].getEnPassant() == Board.moves && Board.pieces[x][numberIndex].getColor() == 'b') {
                Coordinates coords = new Coordinates(x, numberIndex + 1);
                validMoves.add(coords);
            }
            //right
            x = letterIndex + 1;
            if (x <= 7 && Board.pieces[x][numberIndex].getEnPassant() == Board.moves && Board.pieces[x][numberIndex].getColor() == 'b') {
                Coordinates coords = new Coordinates(x, numberIndex + 1);
                validMoves.add(coords);
            }
        }

        return validMoves;
    }

    private Set<Coordinates> bPawnMoves() {
        // moving straight down
        if (Board.pieces[letterIndex][numberIndex - 1].getColor() == 'e') {
            Coordinates coords = new Coordinates(letterIndex, numberIndex - 1);
            validMoves.add(coords);
        }
        if (numberIndex == 6 && Board.pieces[letterIndex][numberIndex - 1].getColor() == 'e' && Board.pieces[letterIndex][numberIndex - 2].getColor() == 'e') {
            Coordinates coords = new Coordinates(letterIndex, numberIndex - 2);
            validMoves.add(coords);
        }

        // taking a piece
        if (letterIndex != 7) {
            if (Board.pieces[letterIndex + 1][numberIndex - 1].getColor() == 'w') {
                Coordinates coords = new Coordinates(letterIndex + 1, numberIndex - 1);
                validMoves.add(coords);
            }
        }
        if (letterIndex != 0) {
            if (Board.pieces[letterIndex - 1][numberIndex - 1].getColor() == 'w') {
                Coordinates coords = new Coordinates(letterIndex - 1, numberIndex - 1);
                validMoves.add(coords);
            }
        }

        // en passant
        if (numberIndex == 3) {
            //left
            int x = letterIndex - 1;
            if (x >= 0 && Board.pieces[x][numberIndex].getEnPassant() == Board.moves && Board.pieces[x][numberIndex].getColor() == 'w') {
                Coordinates coords = new Coordinates(x, numberIndex - 1);
                validMoves.add(coords);
            }
            //right
            x = letterIndex + 1;
            if (x <= 7 && Board.pieces[x][numberIndex].getEnPassant() == Board.moves && Board.pieces[x][numberIndex].getColor() == 'w') {
                Coordinates coords = new Coordinates(x, numberIndex - 1);
                validMoves.add(coords);
            }
        }

        return validMoves;
    }

    @Override
    public void move(int newX, int newY) {
        // en passant
        if (newX != letterIndex && Board.pieces[newX][newY].getColor() == 'e' && Board.pieces[newX][numberIndex].getEnPassant() == Board.moves - 1) {
            Board.score -= Board.pieces[newX][numberIndex].value;
            Board.pieceLocations.get(Board.pieces[newX][numberIndex].color).remove(new Coordinates(newX, numberIndex));
            replace(new Piece('e', newX, numberIndex), newX, numberIndex);
        }

        if (newY == 0 || newY == 7) {
            replace(new Queen(color, newX, newY), newX, newY);
            Board.pieceLocations.get(Board.pieces[letterIndex][numberIndex].color).remove(new Coordinates(letterIndex, numberIndex));
            if (color == 'w') {
                Board.score += 8;
            } else {
                Board.score -= 8;
            }
        } else {
            replace(this, newX, newY);
        }

        // allow opponent to en passant
        if (numberIndex - newY == 2 || newY - numberIndex == 2 && enPassant == -1) {
            enPassant = Board.moves;
        }

        // make this current tile empty
        replace(new Piece('e', letterIndex, numberIndex), letterIndex, numberIndex);

        Board.pieces[newX][newY].setLetterIndex(newX);
        Board.pieces[newX][newY].setNumberIndex(newY);
    }
}
