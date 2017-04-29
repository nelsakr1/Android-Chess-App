package android94.chess;

public class King extends Piece {

    public King (String color, boolean hasMoved) {
        super(color, hasMoved);
    }

    // copy constructor
    public King (Piece other) {
        super(other.color, other.hasMoved);
    }
}
