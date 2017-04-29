package android94.chess;

public class Pawn extends Piece {

    public Pawn(String color, boolean hasMoved, boolean canEnPassant) {
        super(color, hasMoved, canEnPassant);
    }

    // copy constructor
    public Pawn (Piece other, boolean isPawn) {
        super(other.color, other.hasMoved, other.canEnPassant);
    }
}