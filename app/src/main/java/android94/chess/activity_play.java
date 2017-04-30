package android94.chess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class activity_play extends AppCompatActivity {

    public static boolean whiteTurn;
    public static Piece[][] board;
    public static int startRow;
    public static int startCol;
    public static int endRow;
    public static int endCol;
    public static View startView;
    public static View endView;
    public static int[][] validMoves;

    public static boolean gameOver;
    public static boolean checkmate;
    public static boolean stalemate;
    public static boolean whiteWins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        gameOver = false;
        checkmate = false;
        stalemate = false;
        whiteWins = false;

        startRow = -1;
        startCol = -1;
        endRow = -1;
        endCol = -1;

        whiteTurn = true;
        board = Gameplay.createNewBoard();

        // place pieces in their initial positions
        ((ImageButton)findViewById(R.id.a1)).setImageResource(R.drawable.wrook);
        ((ImageButton)findViewById(R.id.b1)).setImageResource(R.drawable.wknight);
        ((ImageButton)findViewById(R.id.c1)).setImageResource(R.drawable.wbishop);
        ((ImageButton)findViewById(R.id.d1)).setImageResource(R.drawable.wqueen);
        ((ImageButton)findViewById(R.id.e1)).setImageResource(R.drawable.wking);
        ((ImageButton)findViewById(R.id.f1)).setImageResource(R.drawable.wbishop);
        ((ImageButton)findViewById(R.id.g1)).setImageResource(R.drawable.wknight);
        ((ImageButton)findViewById(R.id.h1)).setImageResource(R.drawable.wrook);
        ((ImageButton)findViewById(R.id.a2)).setImageResource(R.drawable.wpawn);
        ((ImageButton)findViewById(R.id.b2)).setImageResource(R.drawable.wpawn);
        ((ImageButton)findViewById(R.id.c2)).setImageResource(R.drawable.wpawn);
        ((ImageButton)findViewById(R.id.d2)).setImageResource(R.drawable.wpawn);
        ((ImageButton)findViewById(R.id.e2)).setImageResource(R.drawable.wpawn);
        ((ImageButton)findViewById(R.id.f2)).setImageResource(R.drawable.wpawn);
        ((ImageButton)findViewById(R.id.g2)).setImageResource(R.drawable.wpawn);
        ((ImageButton)findViewById(R.id.h2)).setImageResource(R.drawable.wpawn);

        ((ImageButton)findViewById(R.id.a8)).setImageResource(R.drawable.brook);
        ((ImageButton)findViewById(R.id.b8)).setImageResource(R.drawable.bknight);
        ((ImageButton)findViewById(R.id.c8)).setImageResource(R.drawable.bbishop);
        ((ImageButton)findViewById(R.id.d8)).setImageResource(R.drawable.bqueen);
        ((ImageButton)findViewById(R.id.e8)).setImageResource(R.drawable.bking);
        ((ImageButton)findViewById(R.id.f8)).setImageResource(R.drawable.bbishop);
        ((ImageButton)findViewById(R.id.g8)).setImageResource(R.drawable.bknight);
        ((ImageButton)findViewById(R.id.h8)).setImageResource(R.drawable.brook);
        ((ImageButton)findViewById(R.id.a7)).setImageResource(R.drawable.bpawn);
        ((ImageButton)findViewById(R.id.b7)).setImageResource(R.drawable.bpawn);
        ((ImageButton)findViewById(R.id.c7)).setImageResource(R.drawable.bpawn);
        ((ImageButton)findViewById(R.id.d7)).setImageResource(R.drawable.bpawn);
        ((ImageButton)findViewById(R.id.e7)).setImageResource(R.drawable.bpawn);
        ((ImageButton)findViewById(R.id.f7)).setImageResource(R.drawable.bpawn);
        ((ImageButton)findViewById(R.id.g7)).setImageResource(R.drawable.bpawn);
        ((ImageButton)findViewById(R.id.h7)).setImageResource(R.drawable.bpawn);

    }

    public static void resolveMove (int row, int col, View view) {

        // set start and end positions
        if (startRow == -1 || startCol == -1) {
            startRow = row;
            startCol = col;
            startView = view;
            return;
        }
        else {
            endRow = row;
            endCol = col;
            endView = view;
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

            // move pieces on GUI board
            changeDrawable();

            // reset selected movement
            startRow = -1;
            startCol = -1;
            endRow = -1;
            endCol = -1;
            startView = null;
            endView = null;

            // check for end game conditions
            endGameConditions();

            whiteTurn = !whiteTurn;

        }
        else {
            startRow = -1;
            startCol = -1;
            endRow = -1;
            endCol = -1;
            startView = null;
            endView = null;
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

    public void ai (View view) {

    }

    public void undo (View view) {

    }

    public void draw (View view) {

    }

    public void resign (View view) {

    }

    public static void changeDrawable () {

        ImageButton startPos = null;
        ImageButton endPos = null;

        // find starting position ImageButton to change
        if (startCol == 0) {

            switch (startRow) {

                case 0:
                    startPos = (ImageButton) startView.findViewById(R.id.a1);
                    break;

                case 1:
                    startPos = (ImageButton) startView.findViewById(R.id.a2);
                    break;

                case 2:
                    startPos = (ImageButton) startView.findViewById(R.id.a3);
                    break;

                case 3:
                    startPos = (ImageButton) startView.findViewById(R.id.a4);
                    break;

                case 4:
                    startPos = (ImageButton) startView.findViewById(R.id.a5);
                    break;

                case 5:
                    startPos = (ImageButton) startView.findViewById(R.id.a6);
                    break;

                case 6:
                    startPos = (ImageButton) startView.findViewById(R.id.a7);
                    break;

                case 7:
                    startPos = (ImageButton) startView.findViewById(R.id.a8);
                    break;

            }
        }
        else if (startCol == 1) {

            switch (startRow) {

                case 0:
                    startPos = (ImageButton) startView.findViewById(R.id.b1);
                    break;

                case 1:
                    startPos = (ImageButton) startView.findViewById(R.id.b2);
                    break;

                case 2:
                    startPos = (ImageButton) startView.findViewById(R.id.b3);
                    break;

                case 3:
                    startPos = (ImageButton) startView.findViewById(R.id.b4);
                    break;

                case 4:
                    startPos = (ImageButton) startView.findViewById(R.id.b5);
                    break;

                case 5:
                    startPos = (ImageButton) startView.findViewById(R.id.b6);
                    break;

                case 6:
                    startPos = (ImageButton) startView.findViewById(R.id.b7);
                    break;

                case 7:
                    startPos = (ImageButton) startView.findViewById(R.id.b8);
                    break;

            }
        }
        else if (startCol == 2) {

            switch (startRow) {

                case 0:
                    startPos = (ImageButton) startView.findViewById(R.id.c1);
                    break;

                case 1:
                    startPos = (ImageButton) startView.findViewById(R.id.c2);
                    break;

                case 2:
                    startPos = (ImageButton) startView.findViewById(R.id.c3);
                    break;

                case 3:
                    startPos = (ImageButton) startView.findViewById(R.id.c4);
                    break;

                case 4:
                    startPos = (ImageButton) startView.findViewById(R.id.c5);
                    break;

                case 5:
                    startPos = (ImageButton) startView.findViewById(R.id.c6);
                    break;

                case 6:
                    startPos = (ImageButton) startView.findViewById(R.id.c7);
                    break;

                case 7:
                    startPos = (ImageButton) startView.findViewById(R.id.c8);
                    break;

            }
        }
        else if (startCol == 3) {

            switch (startRow) {

                case 0:
                    startPos = (ImageButton) startView.findViewById(R.id.d1);
                    break;

                case 1:
                    startPos = (ImageButton) startView.findViewById(R.id.d2);
                    break;

                case 2:
                    startPos = (ImageButton) startView.findViewById(R.id.d3);
                    break;

                case 3:
                    startPos = (ImageButton) startView.findViewById(R.id.d4);
                    break;

                case 4:
                    startPos = (ImageButton) startView.findViewById(R.id.d5);
                    break;

                case 5:
                    startPos = (ImageButton) startView.findViewById(R.id.d6);
                    break;

                case 6:
                    startPos = (ImageButton) startView.findViewById(R.id.d7);
                    break;

                case 7:
                    startPos = (ImageButton) startView.findViewById(R.id.d8);
                    break;

            }
        }
        else if (startCol == 4) {

            switch (startRow) {

                case 0:
                    startPos = (ImageButton) startView.findViewById(R.id.e1);
                    break;

                case 1:
                    startPos = (ImageButton) startView.findViewById(R.id.e2);
                    break;

                case 2:
                    startPos = (ImageButton) startView.findViewById(R.id.e3);
                    break;

                case 3:
                    startPos = (ImageButton) startView.findViewById(R.id.e4);
                    break;

                case 4:
                    startPos = (ImageButton) startView.findViewById(R.id.e5);
                    break;

                case 5:
                    startPos = (ImageButton) startView.findViewById(R.id.e6);
                    break;

                case 6:
                    startPos = (ImageButton) startView.findViewById(R.id.e7);
                    break;

                case 7:
                    startPos = (ImageButton) startView.findViewById(R.id.e8);
                    break;

            }
        }
        else if (startCol == 5) {

            switch (startRow) {

                case 0:
                    startPos = (ImageButton) startView.findViewById(R.id.f1);
                    break;

                case 1:
                    startPos = (ImageButton) startView.findViewById(R.id.f2);
                    break;

                case 2:
                    startPos = (ImageButton) startView.findViewById(R.id.f3);
                    break;

                case 3:
                    startPos = (ImageButton) startView.findViewById(R.id.f4);
                    break;

                case 4:
                    startPos = (ImageButton) startView.findViewById(R.id.f5);
                    break;

                case 5:
                    startPos = (ImageButton) startView.findViewById(R.id.f6);
                    break;

                case 6:
                    startPos = (ImageButton) startView.findViewById(R.id.f7);
                    break;

                case 7:
                    startPos = (ImageButton) startView.findViewById(R.id.f8);
                    break;

            }
        }
        else if (startCol == 6) {

            switch (startRow) {

                case 0:
                    startPos = (ImageButton) startView.findViewById(R.id.g1);
                    break;

                case 1:
                    startPos = (ImageButton) startView.findViewById(R.id.g2);
                    break;

                case 2:
                    startPos = (ImageButton) startView.findViewById(R.id.g3);
                    break;

                case 3:
                    startPos = (ImageButton) startView.findViewById(R.id.g4);
                    break;

                case 4:
                    startPos = (ImageButton) startView.findViewById(R.id.g5);
                    break;

                case 5:
                    startPos = (ImageButton) startView.findViewById(R.id.g6);
                    break;

                case 6:
                    startPos = (ImageButton) startView.findViewById(R.id.g7);
                    break;

                case 7:
                    startPos = (ImageButton) startView.findViewById(R.id.g8);
                    break;

            }
        }
        else if (startCol == 7) {

            switch (startRow) {

                case 0:
                    startPos = (ImageButton) startView.findViewById(R.id.h1);
                    break;

                case 1:
                    startPos = (ImageButton) startView.findViewById(R.id.h2);
                    break;

                case 2:
                    startPos = (ImageButton) startView.findViewById(R.id.h3);
                    break;

                case 3:
                    startPos = (ImageButton) startView.findViewById(R.id.h4);
                    break;

                case 4:
                    startPos = (ImageButton) startView.findViewById(R.id.h5);
                    break;

                case 5:
                    startPos = (ImageButton) startView.findViewById(R.id.h6);
                    break;

                case 6:
                    startPos = (ImageButton) startView.findViewById(R.id.h7);
                    break;

                case 7:
                    startPos = (ImageButton) startView.findViewById(R.id.h8);
                    break;

            }
        }

        // find ending position ImageButton to change
        if (endCol == 0) {

            switch (endRow) {

                case 0:
                    endPos = (ImageButton) endView.findViewById(R.id.a1);
                    break;

                case 1:
                    endPos = (ImageButton) endView.findViewById(R.id.a2);
                    break;

                case 2:
                    endPos = (ImageButton) endView.findViewById(R.id.a3);
                    break;

                case 3:
                    endPos = (ImageButton) endView.findViewById(R.id.a4);
                    break;

                case 4:
                    endPos = (ImageButton) endView.findViewById(R.id.a5);
                    break;

                case 5:
                    endPos = (ImageButton) endView.findViewById(R.id.a6);
                    break;

                case 6:
                    endPos = (ImageButton) endView.findViewById(R.id.a7);
                    break;

                case 7:
                    endPos = (ImageButton) endView.findViewById(R.id.a8);
                    break;

            }
        }
        else if (endCol == 1) {

            switch (endRow) {

                case 0:
                    endPos = (ImageButton) endView.findViewById(R.id.b1);
                    break;

                case 1:
                    endPos = (ImageButton) endView.findViewById(R.id.b2);
                    break;

                case 2:
                    endPos = (ImageButton) endView.findViewById(R.id.b3);
                    break;

                case 3:
                    endPos = (ImageButton) endView.findViewById(R.id.b4);
                    break;

                case 4:
                    endPos = (ImageButton) endView.findViewById(R.id.b5);
                    break;

                case 5:
                    endPos = (ImageButton) endView.findViewById(R.id.b6);
                    break;

                case 6:
                    endPos = (ImageButton) endView.findViewById(R.id.b7);
                    break;

                case 7:
                    endPos = (ImageButton) endView.findViewById(R.id.b8);
                    break;

            }
        }
        else if (endCol == 2) {

            switch (endRow) {

                case 0:
                    endPos = (ImageButton) endView.findViewById(R.id.c1);
                    break;

                case 1:
                    endPos = (ImageButton) endView.findViewById(R.id.c2);
                    break;

                case 2:
                    endPos = (ImageButton) endView.findViewById(R.id.c3);
                    break;

                case 3:
                    endPos = (ImageButton) endView.findViewById(R.id.c4);
                    break;

                case 4:
                    endPos = (ImageButton) endView.findViewById(R.id.c5);
                    break;

                case 5:
                    endPos = (ImageButton) endView.findViewById(R.id.c6);
                    break;

                case 6:
                    endPos = (ImageButton) endView.findViewById(R.id.c7);
                    break;

                case 7:
                    endPos = (ImageButton) endView.findViewById(R.id.c8);
                    break;

            }
        }
        else if (endCol == 3) {

            switch (endRow) {

                case 0:
                    endPos = (ImageButton) endView.findViewById(R.id.d1);
                    break;

                case 1:
                    endPos = (ImageButton) endView.findViewById(R.id.d2);
                    break;

                case 2:
                    endPos = (ImageButton) endView.findViewById(R.id.d3);
                    break;

                case 3:
                    endPos = (ImageButton) endView.findViewById(R.id.d4);
                    break;

                case 4:
                    endPos = (ImageButton) endView.findViewById(R.id.d5);
                    break;

                case 5:
                    endPos = (ImageButton) endView.findViewById(R.id.d6);
                    break;

                case 6:
                    endPos = (ImageButton) endView.findViewById(R.id.d7);
                    break;

                case 7:
                    endPos = (ImageButton) endView.findViewById(R.id.d8);
                    break;

            }
        }
        else if (endCol == 4) {

            switch (endRow) {

                case 0:
                    endPos = (ImageButton) endView.findViewById(R.id.e1);
                    break;

                case 1:
                    endPos = (ImageButton) endView.findViewById(R.id.e2);
                    break;

                case 2:
                    endPos = (ImageButton) endView.findViewById(R.id.e3);
                    break;

                case 3:
                    endPos = (ImageButton) endView.findViewById(R.id.e4);
                    break;

                case 4:
                    endPos = (ImageButton) endView.findViewById(R.id.e5);
                    break;

                case 5:
                    endPos = (ImageButton) endView.findViewById(R.id.e6);
                    break;

                case 6:
                    endPos = (ImageButton) endView.findViewById(R.id.e7);
                    break;

                case 7:
                    endPos = (ImageButton) endView.findViewById(R.id.e8);
                    break;

            }
        }
        else if (endCol == 5) {

            switch (endRow) {

                case 0:
                    endPos = (ImageButton) endView.findViewById(R.id.f1);
                    break;

                case 1:
                    endPos = (ImageButton) endView.findViewById(R.id.f2);
                    break;

                case 2:
                    endPos = (ImageButton) endView.findViewById(R.id.f3);
                    break;

                case 3:
                    endPos = (ImageButton) endView.findViewById(R.id.f4);
                    break;

                case 4:
                    endPos = (ImageButton) endView.findViewById(R.id.f5);
                    break;

                case 5:
                    endPos = (ImageButton) endView.findViewById(R.id.f6);
                    break;

                case 6:
                    endPos = (ImageButton) endView.findViewById(R.id.f7);
                    break;

                case 7:
                    endPos = (ImageButton) endView.findViewById(R.id.f8);
                    break;

            }
        }
        else if (endCol == 6) {

            switch (endRow) {

                case 0:
                    endPos = (ImageButton) endView.findViewById(R.id.g1);
                    break;

                case 1:
                    endPos = (ImageButton) endView.findViewById(R.id.g2);
                    break;

                case 2:
                    endPos = (ImageButton) endView.findViewById(R.id.g3);
                    break;

                case 3:
                    endPos = (ImageButton) endView.findViewById(R.id.g4);
                    break;

                case 4:
                    endPos = (ImageButton) endView.findViewById(R.id.g5);
                    break;

                case 5:
                    endPos = (ImageButton) endView.findViewById(R.id.g6);
                    break;

                case 6:
                    endPos = (ImageButton) endView.findViewById(R.id.g7);
                    break;

                case 7:
                    endPos = (ImageButton) endView.findViewById(R.id.g8);
                    break;

            }
        }
        else if (endCol == 7) {

            switch (endRow) {

                case 0:
                    endPos = (ImageButton) endView.findViewById(R.id.h1);
                    break;

                case 1:
                    endPos = (ImageButton) endView.findViewById(R.id.h2);
                    break;

                case 2:
                    endPos = (ImageButton) endView.findViewById(R.id.h3);
                    break;

                case 3:
                    endPos = (ImageButton) endView.findViewById(R.id.h4);
                    break;

                case 4:
                    endPos = (ImageButton) endView.findViewById(R.id.h5);
                    break;

                case 5:
                    endPos = (ImageButton) endView.findViewById(R.id.h6);
                    break;

                case 6:
                    endPos = (ImageButton) endView.findViewById(R.id.h7);
                    break;

                case 7:
                    endPos = (ImageButton) endView.findViewById(R.id.h8);
                    break;

            }
        }

        // place pieces in the starting position
        if (board[startRow][startCol] == null) {
            startPos.setImageResource(android.R.color.transparent);
        }
        else if (board[startRow][startCol] instanceof Pawn) {
            if (board[startRow][startCol].color.equals("white")) {
                startPos.setImageResource(R.drawable.wpawn);
            }
            else {
                startPos.setImageResource(R.drawable.bpawn);
            }
        }
        else if (board[startRow][startCol] instanceof Rook) {
            if (board[startRow][startCol].color.equals("white")) {
                startPos.setImageResource(R.drawable.wrook);
            }
            else {
                startPos.setImageResource(R.drawable.brook);
            }
        }
        else if (board[startRow][startCol] instanceof Knight) {
            if (board[startRow][startCol].color.equals("white")) {
                startPos.setImageResource(R.drawable.wknight);
            }
            else {
                startPos.setImageResource(R.drawable.bknight);
            }
        }
        else if (board[startRow][startCol] instanceof Bishop) {
            if (board[startRow][startCol].color.equals("white")) {
                startPos.setImageResource(R.drawable.wbishop);
            }
            else {
                startPos.setImageResource(R.drawable.bbishop);
            }
        }
        else if (board[startRow][startCol] instanceof Queen) {
            if (board[startRow][startCol].color.equals("white")) {
                startPos.setImageResource(R.drawable.wqueen);
            }
            else {
                startPos.setImageResource(R.drawable.bqueen);
            }
        }
        else if (board[startRow][startCol] instanceof King) {
            if (board[startRow][startCol].color.equals("white")) {
                startPos.setImageResource(R.drawable.wking);
            }
            else {
                startPos.setImageResource(R.drawable.bking);
            }
        }

        // place pieces in the ending position
        if (board[endRow][endCol] == null) {
           endPos.setImageResource(android.R.color.transparent);
        }
        else if (board[endRow][endCol] instanceof Pawn) {
            if (board[endRow][endCol].color.equals("white")) {
                endPos.setImageResource(R.drawable.wpawn);
            }
            else {
                endPos.setImageResource(R.drawable.bpawn);
            }
        }
        else if (board[endRow][endCol] instanceof Rook) {
            if (board[endRow][endCol].color.equals("white")) {
                endPos.setImageResource(R.drawable.wrook);
            }
            else {
                endPos.setImageResource(R.drawable.brook);
            }
        }
        else if (board[endRow][endCol] instanceof Knight) {
            if (board[endRow][endCol].color.equals("white")) {
                endPos.setImageResource(R.drawable.wknight);
            }
            else {
                endPos.setImageResource(R.drawable.bknight);
            }
        }
        else if (board[endRow][endCol] instanceof Bishop) {
            if (board[endRow][endCol].color.equals("white")) {
                endPos.setImageResource(R.drawable.wbishop);
            }
            else {
                endPos.setImageResource(R.drawable.bbishop);
            }
        }
        else if (board[endRow][endCol] instanceof Queen) {
            if (board[endRow][endCol].color.equals("white")) {
                endPos.setImageResource(R.drawable.wqueen);
            }
            else {
                endPos.setImageResource(R.drawable.bqueen);
            }
        }
        else if (board[endRow][endCol] instanceof King) {
            if (board[endRow][endCol].color.equals("white")) {
                endPos.setImageResource(R.drawable.wking);
            }
            else {
                endPos.setImageResource(R.drawable.bking);
            }
        }

    }

    public void a1 (View view) {
        resolveMove(0,0,view);
    }

    public void a2 (View view) {
        resolveMove(1,0,view);
    }

    public void a3 (View view) {
        resolveMove(2,0,view);
    }

    public void a4 (View view) {
        resolveMove(3,0,view);
    }

    public void a5 (View view) {
        resolveMove(4,0,view);
    }

    public void a6 (View view) {
        resolveMove(5,0,view);
    }

    public void a7 (View view) {
        resolveMove(6,0,view);
    }

    public void a8 (View view) {
        resolveMove(7,0,view);
    }

    public void b1 (View view) {
        resolveMove(0,1,view);
    }

    public void b2 (View view) {
        resolveMove(1,1,view);
    }

    public void b3 (View view) {
        resolveMove(2,1,view);
    }

    public void b4 (View view) {
        resolveMove(3,1,view);
    }

    public void b5 (View view) {
        resolveMove(4,1,view);
    }

    public void b6 (View view) {
        resolveMove(5,1,view);
    }

    public void b7 (View view) {
        resolveMove(6,1,view);
    }

    public void b8 (View view) {
        resolveMove(7,1,view);
    }

    public void c1 (View view) {
        resolveMove(0,2,view);
    }

    public void c2 (View view) {
        resolveMove(1,2,view);
    }

    public void c3 (View view) {
        resolveMove(2,2,view);
    }

    public void c4 (View view) {
        resolveMove(3,2,view);
    }

    public void c5 (View view) {
        resolveMove(4,2,view);
    }

    public void c6 (View view) {
        resolveMove(5,2,view);
    }

    public void c7 (View view) {
        resolveMove(6,2,view);
    }

    public void c8 (View view) {
        resolveMove(7,2,view);
    }

    public void d1 (View view) {
        resolveMove(0,3,view);
    }

    public void d2 (View view) {
        resolveMove(1,3,view);
    }

    public void d3 (View view) {
        resolveMove(2,3,view);
    }

    public void d4 (View view) {
        resolveMove(3,3,view);
    }

    public void d5 (View view) {
        resolveMove(4,3,view);
    }

    public void d6 (View view) {
        resolveMove(5,3,view);
    }

    public void d7 (View view) {
        resolveMove(6,3,view);
    }

    public void d8 (View view) {
        resolveMove(7,3,view);
    }

    public void e1 (View view) {
        resolveMove(0,4,view);
    }

    public void e2 (View view) {
        resolveMove(1,4,view);
    }

    public void e3 (View view) {
        resolveMove(2,4,view);
    }

    public void e4 (View view) {
        resolveMove(3,4,view);
    }

    public void e5 (View view) {
        resolveMove(4,4,view);
    }

    public void e6 (View view) {
        resolveMove(5,4,view);
    }

    public void e7 (View view) {
        resolveMove(6,4,view);
    }

    public void e8 (View view) {
        resolveMove(7,4,view);
    }

    public void f1 (View view) {
        resolveMove(0,5,view);
    }

    public void f2 (View view) {
        resolveMove(1,5,view);
    }

    public void f3 (View view) {
        resolveMove(2,5,view);
    }

    public void f4 (View view) {
        resolveMove(3,5,view);
    }

    public void f5 (View view) {
        resolveMove(4,5,view);
    }

    public void f6 (View view) {
        resolveMove(5,5,view);
    }

    public void f7 (View view) {
        resolveMove(6,5,view);
    }

    public void f8 (View view) {
        resolveMove(7,5,view);
    }

    public void g1 (View view) {
        resolveMove(0,6,view);
    }

    public void g2 (View view) {
        resolveMove(1,6,view);
    }

    public void g3 (View view) {
        resolveMove(2,6,view);
    }

    public void g4 (View view) {
        resolveMove(3,6,view);
    }

    public void g5 (View view) {
        resolveMove(4,6,view);
    }

    public void g6 (View view) {
        resolveMove(5,6,view);
    }

    public void g7 (View view) {
        resolveMove(6,6,view);
    }

    public void g8 (View view) {
        resolveMove(7,6,view);
    }

    public void h1 (View view) {
        resolveMove(0,7,view);
    }

    public void h2 (View view) {
        resolveMove(1,7,view);
    }

    public void h3 (View view) {
        resolveMove(2,7,view);
    }

    public void h4 (View view) {
        resolveMove(3,7,view);
    }

    public void h5 (View view) {
        resolveMove(4,7,view);
    }

    public void h6 (View view) {
        resolveMove(5,7,view);
    }

    public void h7 (View view) {
        resolveMove(6,7,view);
    }

    public void h8 (View view) {
        resolveMove(7,7,view);
    }




}
