package android94.chess;

import java.io.Serializable;

public class Piece implements Serializable {

    String color;
    Boolean hasMoved;
    Boolean canEnPassant;

    public Piece(String color, Boolean hasMoved) {
        this.color = color;
        this.hasMoved = hasMoved;
    }

    // pawn constructor
    public Piece(String color, Boolean hasMoved, Boolean canEnPassant) {
        this.color = color;
        this.hasMoved = hasMoved;
        this.canEnPassant = canEnPassant;
    }

    // copy constructor
    public Piece(Piece other) {
        this.color = other.color;
        this.hasMoved = other.hasMoved;
    }


     // pawn copy constructor
    public Piece(Piece other, boolean isPawn) {
        this.color = other.color;
        this.hasMoved = other.hasMoved;
        this.canEnPassant = other.canEnPassant;
    }
}
