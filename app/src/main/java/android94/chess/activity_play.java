package android94.chess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class activity_play extends AppCompatActivity {

    public static boolean whiteTurn;
    public static Piece[][] board;
    public static int startRow;
    public static int startCol;
    public static int endRow;
    public static int endCol;
    public static int[][] validMoves;

    public static boolean gameOver = false;
    public static boolean checkmate = false;
    public static boolean stalemate = false;
    public static boolean whiteWins = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        boolean gameOver = false;
        boolean checkmate = false;
        boolean stalemate = false;
        boolean drawOffered = false;
        boolean draw = false;
        boolean resign = false;
        boolean whiteWins = false;

        startRow = -1;
        startCol = -1;
        endRow = -1;
        endCol = -1;

        whiteTurn = true;
        board = Gameplay.createNewBoard();

    }

    public static void resolveMove (int row, int col) {

        // set start and end positions
        if (startRow == -1 || startCol == -1) {
            startRow = row;
            startCol = col;
            return;
        }
        else {
            endRow = row;
            endCol = col;
        }

        if (isValidMove()) {

            // piece movement
            board = Movement.movePiece(startRow, startCol, endRow, endCol, board);

            // remove en passant tag
            for (int i = 0; i <= 7; i++) {
                for (int j = 0; j <= 7; j++) {
                    if (board[i][j] instanceof Pawn) {
                        board[i][j].canEnPassant = false;
                    }
                }
            }

            // en passant setting
            if (board[endRow][endCol] instanceof Pawn) {
                if (!board[endRow][endCol].hasMoved) {
                    board[endRow][endCol].canEnPassant = true;
                }
            }

            // change piece status to has moved before
            board[endRow][endCol].hasMoved = true;


            // promotion behavior
            if (board[endRow][endCol] instanceof Pawn) {

                // for white Queen
                if (endRow == 7) {
                    board[endRow][endCol] = new Queen("white", true);
                }

                // for black Queen
                if (endRow == 0) {
                    board[endRow][endCol] = new Queen("black", true);
                }
            }

            // check for checkmate/stalemate conditions
            if (whiteTurn) {
                if (Gameplay.playerInCheck(board, "black") && !Gameplay.hasValidMovesLeft(board, "black")) {
                    gameOver = true;
                    checkmate = true;
                    whiteWins = true;
                }
                if (!Gameplay.playerInCheck(board, "black") && !Gameplay.hasValidMovesLeft(board, "black")) {
                    gameOver = true;
                    stalemate = true;
                }
            }
            else {
                if (Gameplay.playerInCheck(board, "white") && !Gameplay.hasValidMovesLeft(board, "white")) {
                    gameOver = true;
                    checkmate = true;
                    whiteWins = false;
                }
                if (!Gameplay.playerInCheck(board, "white") && !Gameplay.hasValidMovesLeft(board, "white")) {
                    gameOver = true;
                    stalemate = true;
                }
            }

            // check for end game conditions
            endGameConditions();

        }
        else {
            startRow = -1;
            startCol = -1;
            endRow = -1;
            endCol = -1;
            return;
        }

    }

    public static boolean isValidMove () {

        // makes sure player is not moving an empty spot
        if (board[startRow][startCol] == null) {
            return false;

        }

        // makes sure player is picking a piece of their own color
        if (whiteTurn) {
            if (!board[startRow][startCol].color.equals("white")) {
                return false;
            }
        }
        else {
            if (!board[startRow][startCol].color.equals("black")) {
                return false;
            }
        }

        // check input move against list of valid movements
        validMoves = Movement.getValidMoves(startRow, startCol, board);

        for (int i = 0; i < validMoves[0].length; i++) {

            if (validMoves[0][i] == endRow && validMoves[1][i] == endCol) {
                return true;
            }
        }
        return false;
    }

    public static void endGameConditions () {

    }

    public static void ai () {

    }

    public static void undo () {

    }

    public static void draw () {

    }

    public static void resign () {

    }

    public static void a1 () {
        resolveMove(1,1);
    }




}
