package android94.chess;

public class Queen extends Piece {

    public Queen (String color, boolean hasMoved) {
        super(color, hasMoved);
    }

    // copy constructor
    public Queen (Piece other) {
        super(other.color, other.hasMoved);
    }

}
