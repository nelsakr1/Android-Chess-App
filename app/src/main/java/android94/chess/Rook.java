package android94.chess;

public class Rook extends Piece {

    public Rook (String color, boolean hasMoved) {
        super(color, hasMoved);
    }

    // copy constructor
    public Rook (Piece other) {
        super(other.color, other.hasMoved);
    }
}