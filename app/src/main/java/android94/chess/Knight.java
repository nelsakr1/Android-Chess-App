package android94.chess;

public class Knight extends Piece {

    public Knight (String color, boolean hasMoved) {
        super(color, hasMoved);
    }

    // copy constructor
    public Knight (Piece other) {
        super(other.color, other.hasMoved);
    }
}
