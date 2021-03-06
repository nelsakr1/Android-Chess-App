package android94.chess;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Arrays;

public class activity_play extends AppCompatActivity {

    public static boolean whiteTurn;
    public static Piece[][] board;
    public static int startRow;
    public static int startCol;
    public static int endRow;
    public static int endCol;

    public static boolean gameOver;
    public static boolean stalemate;
    public static boolean whiteWins;

    public static int[][] turnRecorder;
    public static int turnCounter;

    public static Context context;
    public static View view;
    public static Piece[][] undoBoard;

    public static boolean aiMove;
    public static boolean undoPossible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        context = this;
        view = ((Activity) context).findViewById(R.id.a1);

        gameOver = false;
        stalemate = false;
        whiteWins = false;

        startRow = -1;
        startCol = -1;
        endRow = -1;
        endCol = -1;

        aiMove = false;
        undoPossible = false;
        undoBoard = new Piece[8][8];

        turnCounter = 0;
        turnRecorder = new int[4][400];
        for (int i = 0; i <= 3; i++) {
            Arrays.fill(turnRecorder[i],-1);
        }

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

            // create an undo board
            for (int x = 0; x <= 7; x++) {
                for (int y = 0; y <= 7; y++) {

                    // skip over empty entries
                    if (board[x][y] == null) {
                        undoBoard[x][y] = null;
                    }
                    // create a deep copy instance of the pieces on the undo board
                    if (board[x][y] instanceof Pawn) {
                        undoBoard[x][y] = new Pawn(board[x][y], true);
                    }
                    if (board[x][y] instanceof King) {
                        undoBoard[x][y] = new King(board[x][y]);
                    }
                    if (board[x][y] instanceof Queen) {
                        undoBoard[x][y] = new Queen(board[x][y]);
                    }
                    if (board[x][y] instanceof Bishop) {
                        undoBoard[x][y] = new Bishop(board[x][y]);
                    }
                    if (board[x][y] instanceof Knight) {
                        undoBoard[x][y] = new Knight(board[x][y]);
                    }
                    if (board[x][y] instanceof Rook) {
                        undoBoard[x][y] = new Rook(board[x][y]);
                    }
                }
            }
            undoPossible = true;

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
                    whiteWins = true;
                    turnRecorder[0][turnCounter] = -2;
                }
                if (!Gameplay.playerInCheck(board, "black") && !Gameplay.hasValidMovesLeft(board, "black")) {
                    gameOver = true;
                    stalemate = true;
                    turnRecorder[0][turnCounter] = -4;
                }
            }
            else {
                if (Gameplay.playerInCheck(board, "white") && !Gameplay.hasValidMovesLeft(board, "white")) {
                    gameOver = true;
                    whiteWins = false;
                    turnRecorder[0][turnCounter] = -3;
                }
                if (!Gameplay.playerInCheck(board, "white") && !Gameplay.hasValidMovesLeft(board, "white")) {
                    gameOver = true;
                    stalemate = true;
                    turnRecorder[0][turnCounter] = -4;
                }
            }

            // move pieces on GUI board
            changeDrawableAlt(board, context);
            // changeDrawable(startRow, startCol, endRow, endCol);

            // record turn
            turnRecorder[0][turnCounter] = startRow;
            turnRecorder[1][turnCounter] = startCol;
            turnRecorder[2][turnCounter] = endRow;
            turnRecorder[3][turnCounter] = endCol;
            turnCounter++;

            // reset selected movement
            startRow = -1;
            startCol = -1;
            endRow = -1;
            endCol = -1;

            // check for end game conditions
            endGameConditions();

            // change turn
            whiteTurn = !whiteTurn;

            if (whiteTurn) {
                if (Gameplay.playerInCheck(board, "white")) {
                    Toast.makeText(view.getContext(), "Check.", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                if (Gameplay.playerInCheck(board, "black" )) {
                    Toast.makeText(view.getContext(), "Check.", Toast.LENGTH_SHORT).show();
                }
            }

        }
        else {
            if (!aiMove) {
                Toast.makeText(view.getContext(), "Invalid Move.", Toast.LENGTH_SHORT).show();

                startRow = -1;
                startCol = -1;
                endRow = -1;
                endCol = -1;
            }
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
        int[][] validMoves = Movement.getValidMoves(startRow, startCol, board);

        for (int i = 0; i < validMoves[0].length; i++) {

            // castling
            if (validMoves[0][i] == endRow && validMoves[1][i] == endCol) {

                // create a new copy board
                Piece[][] copyBoard = new Piece[8][8];

                for (int x = 0; x <= 7; x++) {
                    for (int y = 0; y <= 7; y++) {

                        // skip over empty entries
                        if (board[x][y] == null) {
                            continue;
                        }

                        // create a deep copy instance of the pieces on the copy board
                        if (board[x][y] instanceof Pawn) {
                            copyBoard[x][y] = new Pawn(board[x][y], true);
                        }
                        if (board[x][y] instanceof King) {
                            copyBoard[x][y] = new King(board[x][y]);
                        }
                        if (board[x][y] instanceof Queen) {
                            copyBoard[x][y] = new Queen(board[x][y]);
                        }
                        if (board[x][y] instanceof Bishop) {
                            copyBoard[x][y] = new Bishop(board[x][y]);
                        }
                        if (board[x][y] instanceof Knight) {
                            copyBoard[x][y] = new Knight(board[x][y]);
                        }
                        if (board[x][y] instanceof Rook) {
                            copyBoard[x][y] = new Rook(board[x][y]);
                        }
                    }
                }

                copyBoard = Movement.movePiece(startRow, startCol, endRow, endCol, copyBoard);

                if (whiteTurn) {
                    if (!Gameplay.playerInCheck(copyBoard, "white")) {
                        return true;
                    }
                }
                else {
                    if (!Gameplay.playerInCheck(copyBoard, "black")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void endGameConditions () {

        if (gameOver) {
            Intent intent = new Intent(context, activity_gameover.class);

            Bundle bundle = new Bundle();
            bundle.putSerializable("recordserial", turnRecorder);

            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }

    public void aiAlt (View view) {

        boolean moveSucessful = false;
        boolean turnTracker;
        aiMove = true;

        if (whiteTurn) {
            turnTracker = true;
        }
        else {
            turnTracker = false;
        }

        while(!moveSucessful) {

            startRow = -1;
            startCol = -1;
            endRow = -1;
            endCol = 1;

            int randomStartRow = (int) Math.floor(Math.random() * 7.01);
            int randomStartCol = (int) Math.floor(Math.random() * 7.01);
            int randomEndRow = (int) Math.floor(Math.random() * 7.01);
            int randomEndCol = (int) Math.floor(Math.random() * 7.01);

            resolveMove(randomStartRow, randomStartCol);
            resolveMove(randomEndRow, randomEndCol);

            if (turnTracker != whiteTurn) {
                moveSucessful = true;
                aiMove = false;
            }

        }
    }

    public void undo (View view) {

        if (undoPossible) {

            // copy undo board to board
            for (int x = 0; x <= 7; x++) {
                for (int y = 0; y <= 7; y++) {

                    // skip over empty entries
                    if (undoBoard[x][y] == null) {
                        board[x][y] = null;
                    }

                    // create a deep copy instance of the pieces on the board
                    if (undoBoard[x][y] instanceof Pawn) {
                        board[x][y] = new Pawn(undoBoard[x][y], true);
                    }
                    if (undoBoard[x][y] instanceof King) {
                        board[x][y] = new King(undoBoard[x][y]);
                    }
                    if (undoBoard[x][y] instanceof Queen) {
                        board[x][y] = new Queen(undoBoard[x][y]);
                    }
                    if (undoBoard[x][y] instanceof Bishop) {
                        board[x][y] = new Bishop(undoBoard[x][y]);
                    }
                    if (undoBoard[x][y] instanceof Knight) {
                        board[x][y] = new Knight(undoBoard[x][y]);
                    }
                    if (undoBoard[x][y] instanceof Rook) {
                        board[x][y] = new Rook(undoBoard[x][y]);
                    }
                }
            }
            changeDrawableAlt(board, context);
            undoPossible = false;
            whiteTurn = !whiteTurn;

            turnCounter--;
            turnRecorder[0][turnCounter] = -1;
            turnRecorder[1][turnCounter] = -1;
            turnRecorder[2][turnCounter] = -1;
            turnRecorder[3][turnCounter] = -1;
        }
        else {
            Toast.makeText(view.getContext(), "Unable to undo move.", Toast.LENGTH_SHORT).show();
        }

    }

    public void draw (View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder
                .setMessage("Agree to a draw?")
                .setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        gameOver = true;
                        stalemate = true;
                        turnRecorder[0][turnCounter] = -4;
                        endGameConditions();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void resign (View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder
                .setMessage("Forfeit the game?")
                .setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (whiteTurn) {
                            gameOver = true;
                            whiteWins = false;
                            turnRecorder[0][turnCounter] = -3;
                        }
                        else {
                            gameOver = true;
                            whiteWins = true;
                            turnRecorder[0][turnCounter] = -2;
                        }
                        endGameConditions();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public static void changeDrawableAlt (Piece[][] changeBoard, Context context) {

        ImageButton current = null;

        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {

                if (j == 0) {
                    switch (i) {

                        case 0:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.a1);
                            break;
                        case 1:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.a2);
                            break;
                        case 2:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.a3);
                            break;
                        case 3:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.a4);
                            break;
                        case 4:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.a5);
                            break;
                        case 5:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.a6);
                            break;
                        case 6:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.a7);
                            break;
                        case 7:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.a8);
                            break;

                    }
                }
                else if (j == 1) {
                    switch (i) {

                        case 0:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.b1);
                            break;
                        case 1:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.b2);
                            break;
                        case 2:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.b3);
                            break;
                        case 3:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.b4);
                            break;
                        case 4:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.b5);
                            break;
                        case 5:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.b6);
                            break;
                        case 6:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.b7);
                            break;
                        case 7:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.b8);
                            break;

                    }
                }
                else if (j == 2) {
                    switch (i) {

                        case 0:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.c1);
                            break;
                        case 1:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.c2);
                            break;
                        case 2:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.c3);
                            break;
                        case 3:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.c4);
                            break;
                        case 4:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.c5);
                            break;
                        case 5:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.c6);
                            break;
                        case 6:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.c7);
                            break;
                        case 7:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.c8);
                            break;

                    }
                }
                else if (j == 3) {
                    switch (i) {

                        case 0:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.d1);
                            break;
                        case 1:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.d2);
                            break;
                        case 2:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.d3);
                            break;
                        case 3:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.d4);
                            break;
                        case 4:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.d5);
                            break;
                        case 5:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.d6);
                            break;
                        case 6:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.d7);
                            break;
                        case 7:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.d8);
                            break;

                    }
                }
                else if (j == 4) {
                    switch (i) {

                        case 0:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.e1);
                            break;
                        case 1:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.e2);
                            break;
                        case 2:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.e3);
                            break;
                        case 3:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.e4);
                            break;
                        case 4:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.e5);
                            break;
                        case 5:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.e6);
                            break;
                        case 6:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.e7);
                            break;
                        case 7:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.e8);
                            break;

                    }
                }
                else if (j == 5) {
                    switch (i) {

                        case 0:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.f1);
                            break;
                        case 1:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.f2);
                            break;
                        case 2:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.f3);
                            break;
                        case 3:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.f4);
                            break;
                        case 4:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.f5);
                            break;
                        case 5:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.f6);
                            break;
                        case 6:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.f7);
                            break;
                        case 7:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.f8);
                            break;

                    }
                }
                else if (j == 6) {
                    switch (i) {

                        case 0:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.g1);
                            break;
                        case 1:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.g2);
                            break;
                        case 2:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.g3);
                            break;
                        case 3:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.g4);
                            break;
                        case 4:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.g5);
                            break;
                        case 5:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.g6);
                            break;
                        case 6:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.g7);
                            break;
                        case 7:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.g8);
                            break;

                    }
                }
                else if (j == 7) {
                    switch (i) {

                        case 0:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.h1);
                            break;
                        case 1:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.h2);
                            break;
                        case 2:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.h3);
                            break;
                        case 3:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.h4);
                            break;
                        case 4:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.h5);
                            break;
                        case 5:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.h6);
                            break;
                        case 6:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.h7);
                            break;
                        case 7:
                            current = (ImageButton) ((Activity) context).findViewById(R.id.h8);
                            break;

                    }
                }


                if (changeBoard[i][j] == null) {
                    current.setImageResource(android.R.color.transparent);
                }
                else if (changeBoard[i][j] instanceof Pawn) {
                    if (changeBoard[i][j].color.equals("white")) {
                        current.setImageResource(R.drawable.wpawn);
                    }
                    else {
                        current.setImageResource(R.drawable.bpawn);
                    }
                }
                else if (changeBoard[i][j] instanceof Rook) {
                    if (changeBoard[i][j].color.equals("white")) {
                        current.setImageResource(R.drawable.wrook);
                    }
                    else {
                        current.setImageResource(R.drawable.brook);
                    }
                }
                else if (changeBoard[i][j] instanceof Knight) {
                    if (changeBoard[i][j].color.equals("white")) {
                        current.setImageResource(R.drawable.wknight);
                    }
                    else {
                        current.setImageResource(R.drawable.bknight);
                    }
                }
                else if (changeBoard[i][j] instanceof Bishop) {
                    if (changeBoard[i][j].color.equals("white")) {
                        current.setImageResource(R.drawable.wbishop);
                    }
                    else {
                        current.setImageResource(R.drawable.bbishop);
                    }
                }
                else if (changeBoard[i][j] instanceof Queen) {
                    if (changeBoard[i][j].color.equals("white")) {
                        current.setImageResource(R.drawable.wqueen);
                    }
                    else {
                        current.setImageResource(R.drawable.bqueen);
                    }
                }
                else if (changeBoard[i][j] instanceof King) {
                    if (changeBoard[i][j].color.equals("white")) {
                        current.setImageResource(R.drawable.wking);
                    }
                    else {
                        current.setImageResource(R.drawable.bking);
                    }
                }
            }
        }

    }

    public void a1 (View view) {
        resolveMove(0,0);
    }

    public void a2 (View view) {
        resolveMove(1,0);
    }

    public void a3 (View view) {
        resolveMove(2,0);
    }

    public void a4 (View view) {
        resolveMove(3,0);
    }

    public void a5 (View view) {
        resolveMove(4,0);
    }

    public void a6 (View view) {
        resolveMove(5,0);
    }

    public void a7 (View view) {
        resolveMove(6,0);
    }

    public void a8 (View view) {
        resolveMove(7,0);
    }

    public void b1 (View view) {
        resolveMove(0,1);
    }

    public void b2 (View view) {
        resolveMove(1,1);
    }

    public void b3 (View view) {
        resolveMove(2,1);
    }

    public void b4 (View view) {
        resolveMove(3,1);
    }

    public void b5 (View view) {
        resolveMove(4,1);
    }

    public void b6 (View view) {
        resolveMove(5,1);
    }

    public void b7 (View view) {
        resolveMove(6,1);
    }

    public void b8 (View view) {
        resolveMove(7,1);
    }

    public void c1 (View view) {
        resolveMove(0,2);
    }

    public void c2 (View view) {
        resolveMove(1,2);
    }

    public void c3 (View view) {
        resolveMove(2,2);
    }

    public void c4 (View view) {
        resolveMove(3,2);
    }

    public void c5 (View view) {
        resolveMove(4,2);
    }

    public void c6 (View view) {
        resolveMove(5,2);
    }

    public void c7 (View view) {
        resolveMove(6,2);
    }

    public void c8 (View view) {
        resolveMove(7,2);
    }

    public void d1 (View view) {
        resolveMove(0,3);
    }

    public void d2 (View view) {
        resolveMove(1,3);
    }

    public void d3 (View view) {
        resolveMove(2,3);
    }

    public void d4 (View view) {
        resolveMove(3,3);
    }

    public void d5 (View view) {
        resolveMove(4,3);
    }

    public void d6 (View view) {
        resolveMove(5,3);
    }

    public void d7 (View view) {
        resolveMove(6,3);
    }

    public void d8 (View view) {
        resolveMove(7,3);
    }

    public void e1 (View view) {
        resolveMove(0,4);
    }

    public void e2 (View view) {
        resolveMove(1,4);
    }

    public void e3 (View view) {
        resolveMove(2,4);
    }

    public void e4 (View view) {
        resolveMove(3,4);
    }

    public void e5 (View view) {
        resolveMove(4,4);
    }

    public void e6 (View view) {
        resolveMove(5,4);
    }

    public void e7 (View view) {
        resolveMove(6,4);
    }

    public void e8 (View view) {
        resolveMove(7,4);
    }

    public void f1 (View view) {
        resolveMove(0,5);
    }

    public void f2 (View view) {
        resolveMove(1,5);
    }

    public void f3 (View view) {
        resolveMove(2,5);
    }

    public void f4 (View view) {
        resolveMove(3,5);
    }

    public void f5 (View view) {
        resolveMove(4,5);
    }

    public void f6 (View view) {
        resolveMove(5,5);
    }

    public void f7 (View view) {
        resolveMove(6,5);
    }

    public void f8 (View view) {
        resolveMove(7,5);
    }

    public void g1 (View view) {
        resolveMove(0,6);
    }

    public void g2 (View view) {
        resolveMove(1,6);
    }

    public void g3 (View view) {
        resolveMove(2,6);
    }

    public void g4 (View view) {
        resolveMove(3,6);
    }

    public void g5 (View view) {
        resolveMove(4,6);
    }

    public void g6 (View view) {
        resolveMove(5,6);
    }

    public void g7 (View view) {
        resolveMove(6,6);
    }

    public void g8 (View view) {
        resolveMove(7,6);
    }

    public void h1 (View view) {
        resolveMove(0,7);
    }

    public void h2 (View view) {
        resolveMove(1,7);
    }

    public void h3 (View view) {
        resolveMove(2,7);
    }

    public void h4 (View view) {
        resolveMove(3,7);
    }

    public void h5 (View view) {
        resolveMove(4,7);
    }

    public void h6 (View view) {
        resolveMove(5,7);
    }

    public void h7 (View view) {
        resolveMove(6,7);
    }

    public void h8 (View view) {
        resolveMove(7,7);
    }


}
