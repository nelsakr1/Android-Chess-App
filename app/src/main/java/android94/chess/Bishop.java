package android94.chess;

public class Bishop extends Piece {

    public Bishop (String color, boolean hasMoved) {
        super(color, hasMoved);
    }

    // copy constructor
    public Bishop (Piece other) {
        super(other.color, other.hasMoved);
    }

}