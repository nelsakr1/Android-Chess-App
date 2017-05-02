package android94.chess;

import java.util.Arrays;

public class Movement {

    /**
     * Takes the incoming board and moves the piece at the designated starting row and
     * column to the designated ending row and column while taking into account special
     * movements like castling/en passant and returning the updated board with the
     * piece(s) in their new positions.
     *
     * @param	startRow	the row of the piece's current position
     * @param	startColumn	the column of the piece's current position
     * @param	endRow		the row of the position the piece is being moved to
     * @param	endColumn	the column of the position the piece is being moved to
     * @param	board		the current board before the piece is moved
     * @return				the board after the piece is moved
     */
    public static Piece[][] movePiece(int startRow, int startColumn, int endRow, int endColumn, Piece[][] board) {

        // en passant movement
        if (board[startRow][startColumn] instanceof Pawn) {

            // white rightward
            if (endRow == startRow + 1 && endColumn == startColumn + 1) {
                if (board[startRow][startColumn + 1] instanceof Pawn) {
                    if (board[startRow][startColumn + 1].canEnPassant && board[startRow][startColumn + 1].color != board[startRow][startColumn].color) {
                        board[startRow][startColumn + 1] = null;
                    }
                }
            }

            // white leftward
            if (endRow == startRow + 1 && endColumn == startColumn - 1) {
                if (board[startRow][startColumn - 1] instanceof Pawn) {
                    if (board[startRow][startColumn - 1].canEnPassant && board[startRow][startColumn - 1].color != board[startRow][startColumn].color) {
                        board[startRow][startColumn - 1] = null;
                    }
                }
            }

            // black rightward
            if (endRow == startRow - 1 && endColumn == startColumn + 1) {
                if (board[startRow][startColumn + 1] instanceof Pawn) {
                    if (board[startRow][startColumn + 1].canEnPassant && board[startRow][startColumn + 1].color != board[startRow][startColumn].color) {
                        board[startRow][startColumn + 1] = null;
                    }
                }
            }

            // black leftward
            if (endRow == startRow - 1 && endColumn == startColumn - 1) {
                if (board[startRow][startColumn - 1] instanceof Pawn) {
                    if (board[startRow][startColumn - 1].canEnPassant && board[startRow][startColumn - 1].color != board[startRow][startColumn].color) {
                        board[startRow][startColumn - 1] = null;
                    }
                }
            }
        }

        // castling movement
        if (board[startRow][startColumn] instanceof King) {
            if (endRow == 0) {
                if (endColumn == 2) {
                    board[0][2] = new King ("white", true);
                    board[0][3] = new Rook("white", true);
                    board[startRow][startColumn] = null;
                    board[0][0] = null;
                    return board;
                }
                else if (endColumn == 6) {
                    board[0][6] = new King ("white", true);
                    board[0][5] = new Rook("white", true);
                    board[startRow][startColumn] = null;
                    board[0][7] = null;
                    return board;
                }
            }

            if (endRow == 7) {
                if (endColumn == 2) {
                    board[7][2] = new King ("black", true);
                    board[7][3] = new Rook("black", true);
                    board[startRow][startColumn] = null;
                    board[7][0] = null;
                    return board;
                }
                else if (endColumn == 6) {
                    board[7][6] = new King ("black", true);
                    board[7][5] = new Rook("black", true);
                    board[startRow][startColumn] = null;
                    board[7][7] = null;
                    return board;
                }
            }
        }

        // all other movements
        if (board[startRow][startColumn] instanceof Pawn) {
            board[endRow][endColumn] = new Pawn(board[startRow][startColumn], true);
        }
        else if (board[startRow][startColumn] instanceof King) {
            board[endRow][endColumn] = new King(board[startRow][startColumn]);
        }
        else if (board[startRow][startColumn] instanceof Queen) {
            board[endRow][endColumn] = new Queen(board[startRow][startColumn]);
        }
        else if (board[startRow][startColumn] instanceof Bishop) {
            board[endRow][endColumn] = new Bishop(board[startRow][startColumn]);
        }
        else if (board[startRow][startColumn] instanceof Knight) {
            board[endRow][endColumn] = new Knight(board[startRow][startColumn]);
        }
        else if (board[startRow][startColumn] instanceof Rook) {
            board[endRow][endColumn] = new Rook(board[startRow][startColumn]);
        }

        board[startRow][startColumn] = null;
        return board;
    }

    /**
     * Takes in an array of movements for a piece on the board and removes any which
     * would capture a king of the opposite color by testing for any instances of a
     * king being at the location designed in the move list.
     *
     * @param	allMoves		the array containing all the final positions of a piece
     * @param	board			the current board
     * @return	filteredMoves	the array containing moves without the king being taken
     */
    public static int[][] filterMoves (int[][] allMoves, Piece[][] board) {

        // create list of only valid moves to manipulate
        int[][] filteredMoves = allMoves;

        for (int i = 0; i < allMoves[0].length; i++) {

            // skip over "empty" entries
            if (filteredMoves[0][i] == -1) {
                continue;
            }

            if (board[filteredMoves[0][i]][filteredMoves[1][i]] instanceof King) {

                // remove movement from list by making it "empty"
                filteredMoves[0][i] = -1;
                filteredMoves[1][i] = -1;
            }
        }
        return filteredMoves;
    }

    /**
     * Getter method that will retrieve an array containing all the valid moves of a
     * specified piece depending on what type of piece it is by calling the corresponding
     * movement method with the given parameters.
     *
     * @param	row		the row of the piece whose moves are being fetched
     * @param	column	the column of the piece whose moves are being fetched
     * @param	board	the current board
     * @return			the array containing movements of the desired piece
     *
     * @see #getPawnMoves(int, int, Piece[][])
     * @see #getKingMoves(int, int, Piece[][])
     * @see #getKnightMoves(int, int, Piece[][])
     * @see #getRookMoves(int, int, Piece[][])
     * @see #getBishopMoves(int, int, Piece[][])
     * @see #getQueenMoves(int, int, Piece[][])
     */
    public static int[][] getAllMoves(int row, int column, Piece[][] board) {

        if (board[row][column] instanceof Pawn) {
            return getPawnMoves(row, column, board);
        }
        else if (board[row][column] instanceof King) {
            return getKingMoves(row, column, board);
        }
        else if (board[row][column] instanceof Knight) {
            return getKnightMoves(row, column, board);
        }
        else if (board[row][column] instanceof Rook) {
            return getRookMoves(row, column, board);
        }
        else if (board[row][column] instanceof Bishop) {
            return getBishopMoves(row, column, board);
        }
        else if (board[row][column] instanceof Queen) {
            return getQueenMoves(row, column, board);
        }
        return null;
    }

    /**
     * Getter method that will retrieve an array containing all the valid moves of a
     * specified piece depending on what type of piece it is by calling the corresponding
     * movement method with the given parameters and then filter out any moves that can
     * capture a king.
     *
     * @param	row		the row of the piece whose moves are being fetched
     * @param	column	the column of the piece whose moves are being fetched
     * @param	board	the current board
     * @return			the array containing moves without the king being taken
     *
     * @see #filterMoves(int[][], Piece[][])
     * @see #getPawnMoves(int, int, Piece[][])
     * @see #getKingMoves(int, int, Piece[][])
     * @see #getKnightMoves(int, int, Piece[][])
     * @see #getRookMoves(int, int, Piece[][])
     * @see #getBishopMoves(int, int, Piece[][])
     * @see #getQueenMoves(int, int, Piece[][])
     */
    public static int[][] getValidMoves(int row, int column, Piece[][] board) {

        if (board[row][column] instanceof Pawn) {
            return filterMoves(getPawnMoves(row, column, board), board);
        }
        else if (board[row][column] instanceof King) {
            return filterMoves(getKingMoves(row, column, board),board);
        }
        else if (board[row][column] instanceof Knight) {
            return filterMoves(getKnightMoves(row, column, board), board);
        }
        else if (board[row][column] instanceof Rook) {
            return filterMoves(getRookMoves(row, column, board), board);
        }
        else if (board[row][column] instanceof Bishop) {
            return filterMoves(getBishopMoves(row, column, board), board);
        }
        else if (board[row][column] instanceof Queen) {
            return filterMoves(getQueenMoves(row, column, board), board);
        }
        return null;
    }

    /**
     * Method that will get all the valid movements of a pawn at the specified row
     * and column on the specified board. This list will include movements that can
     * capture a king of the opposite color.
     *
     * @param	row		the row of the piece whose moves are being fetched
     * @param	column	the column of the piece whose moves are being fetched
     * @param	board	the current board
     * @return			the array containing the movements
     */
    public static int[][] getPawnMoves (int row, int column, Piece[][] board) {

        // creates valid move array and fills it with -1
        int[][] validMoves = new int[2][28];
        for (int i = 0; i <= validMoves.length - 1; i++) {
            Arrays.fill(validMoves[i], -1);
        }

        // keeps track of the position in the 2D array
        int pointer = 0;

        // ===== white movement =====
        if (board[row][column].color.equals("white")) {

            // pawn can move forward 2 spaces on its initial movement
            if (!board[row][column].hasMoved) {
                if (board[row + 2][column] == null) {
                    validMoves[0][pointer] = row + 2;
                    validMoves[1][pointer] = column;
                    pointer++;
                }
            }

            // directly in front of the pawn
            if (board[row + 1][column] == null) {
                validMoves[0][pointer] = row + 1;
                validMoves[1][pointer] = column;
                pointer++;
            }

            // diagonal to the pawn
            if (column != 7 && board[row + 1][column + 1]  != null) {
                if (board[row + 1][column + 1].color.equals("black")) {
                    validMoves[0][pointer] = row + 1;
                    validMoves[1][pointer] = column + 1;
                    pointer++;
                }
            }
            if (column != 0 && board[row + 1][column - 1]  != null) {
                if (board[row + 1][column - 1].color.equals("black")) {
                    validMoves[0][pointer] = row + 1;
                    validMoves[1][pointer] = column - 1;
                    pointer++;
                }
            }

            // en passant movement
            if (column > 0) {
                if (board[row][column - 1] instanceof Pawn) {
                    if (board[row][column - 1].canEnPassant && !board[row][column - 1].color.equals(board[row][column].color)) {
                        validMoves[0][pointer] = row + 1;
                        validMoves[1][pointer] = column - 1;
                        pointer++;
                    }
                }
            }
            if (column < 7) {
                if (board[row][column + 1] instanceof Pawn) {
                    if (board[row][column + 1].canEnPassant && !board[row][column + 1].color.equals(board[row][column].color)) {
                        validMoves[0][pointer] = row + 1;
                        validMoves[1][pointer] = column + 1;
                        pointer++;
                    }
                }
            }
        }

        // ===== black movement =====
        if (board[row][column].color.equals("black")) {

            // pawn can move forward 2 spaces on its initial movement
            if (!board[row][column].hasMoved) {
                if (board[row - 2][column] == null) {
                    validMoves[0][pointer] = row - 2;
                    validMoves[1][pointer] = column;
                    pointer++;
                }
            }

            // directly in front of the pawn
            if (board[row - 1][column] == null) {
                validMoves[0][pointer] = row - 1;
                validMoves[1][pointer] = column;
                pointer++;
            }

            // diagonal to the pawn
            if (column != 7 && board[row - 1][column + 1]  != null) {
                if (board[row - 1][column + 1].color.equals("white")) {
                    validMoves[0][pointer] = row - 1;
                    validMoves[1][pointer] = column + 1;
                    pointer++;
                }
            }
            if (column != 0 && board[row - 1][column - 1]  != null) {
                if (board[row - 1][column - 1].color.equals("white")) {
                    validMoves[0][pointer] = row - 1;
                    validMoves[1][pointer] = column - 1;
                    pointer++;
                }
            }

            // en passant movement
            if (column > 0) {
                if (board[row][column - 1] instanceof Pawn) {
                    if (board[row][column - 1].canEnPassant && !board[row][column - 1].color.equals(board[row][column].color)) {
                        validMoves[0][pointer] = row - 1;
                        validMoves[1][pointer] = column - 1;
                        pointer++;
                    }
                }
            }
            if (column < 7) {
                if (board[row][column + 1] instanceof Pawn) {
                    if (board[row][column + 1].canEnPassant && !board[row][column + 1].color.equals(board[row][column].color)) {
                        validMoves[0][pointer] = row - 1;
                        validMoves[1][pointer] = column + 1;
                        pointer++;
                    }
                }
            }
        }
        return validMoves;
    }

    /**
     * Method that will get all the valid movements of a King at the specified row
     * and column on the specified board.
     *
     * @param	row		the row of the piece whose moves are being fetched
     * @param	column	the column of the piece whose moves are being fetched
     * @param	board	the current board
     * @return			the array containing the movements
     */
    public static int[][] getKingMoves (int row, int column, Piece[][] board) {

        // creates valid move array and fills it with -1
        int[][] validMoves = new int[2][28];
        for (int i = 0; i <= validMoves.length - 1; i++) {
            Arrays.fill(validMoves[i], -1);
        }

        // fill array with 8 possible normal movements of the king
        validMoves[0][0] = row + 1;
        validMoves[1][0] = column - 1;

        validMoves[0][1] = row + 1;
        validMoves[1][1] = column;

        validMoves[0][2] = row + 1;
        validMoves[1][2] = column + 1;

        validMoves[0][3] = row;
        validMoves[1][3] = column - 1;

        validMoves[0][4] = row;
        validMoves[1][4] = column + 1;

        validMoves[0][5] = row - 1;
        validMoves[1][5] = column - 1;

        validMoves[0][6] = row - 1;
        validMoves[1][6] = column;

        validMoves[0][7] = row - 1;
        validMoves[1][7] = column + 1;

        for (int i = 0; i <= 7; i++) {

            // removes movement outside the board
            if (validMoves[0][i] < 0 || validMoves[1][i] < 0 || validMoves[0][i] > 7 || validMoves[1][i] > 7) {
                validMoves[0][i] = -1;
                validMoves[1][i] = -1;
                continue;
            }
        }

        // castling movement
        if (!board[row][column].hasMoved) {

            // for white player
            if (board[row][column].color.equals("white")) {

                // left rook
                if (board[0][0] instanceof Rook) {
                    if (!board[0][0].hasMoved) {
                        if (board[0][3] == null && board[0][2] == null && board[0][1] == null) {
                            validMoves[0][8] = 0;
                            validMoves[1][8] = 2;
                        }
                    }
                }

                // right rook
                if (board[0][7] instanceof Rook) {
                    if (!board[0][7].hasMoved) {
                        if (board[0][5] == null && board[0][6] == null) {
                            validMoves[0][9] = 0;
                            validMoves[1][9] = 6;
                        }
                    }
                }
            }

            // for black player
            if (board[row][column].color.equals("black")) {

                // left rook
                if (board[7][0] instanceof Rook) {
                    if (!board[7][0].hasMoved) {
                        if (board[7][3] == null && board[7][2] == null && board[7][1] == null) {
                            validMoves[0][8] = 7;
                            validMoves[1][8] = 2;
                        }
                    }
                }

                // right rook
                if (board[7][7] instanceof Rook) {
                    if (!board[7][7].hasMoved) {
                        if (board[7][5] == null && board[7][6] == null) {
                            validMoves[0][9] = 7;
                            validMoves[1][9] = 6;
                        }
                    }
                }
            }
        }
        return validMoves;
    }

    /**
     * Method that will get all the valid movements of a knight at the specified row
     * and column on the specified board. This list will include movements that can
     * capture a king of the opposite color.
     *
     * @param	row		the row of the piece whose moves are being fetched
     * @param	column	the column of the piece whose moves are being fetched
     * @param	board	the current board
     * @return			the array containing the movements
     */
    public static int[][] getKnightMoves (int row, int column, Piece[][] board) {

        // creates valid move array and fills it with -1
        int[][] validMoves = new int[2][28];
        for (int i = 0; i <= validMoves.length - 1; i++) {
            Arrays.fill(validMoves[i], -1);
        }

        // keeps track of the position in the 2D array
        int pointer = 0;

        // bottom two movements
        if (row >= 2) {

            // bottom right
            if (column <= 6) {
                if (board[row - 2][column + 1] == null) {
                    validMoves[0][pointer] = row - 2;
                    validMoves[1][pointer] = column + 1;
                    pointer++;
                }
                else if (!board[row - 2][column + 1].color.equals(board[row][column].color)) {
                    validMoves[0][pointer] = row - 2;
                    validMoves[1][pointer] = column + 1;
                    pointer++;
                }
            }

            // bottom left
            if (column >= 1) {
                if (board[row - 2][column - 1] == null) {
                    validMoves[0][pointer] = row - 2;
                    validMoves[1][pointer] = column - 1;
                    pointer++;
                }
                else if (!board[row - 2][column - 1].color.equals(board[row][column].color)) {
                    validMoves[0][pointer] = row - 2;
                    validMoves[1][pointer] = column - 1;
                    pointer++;
                }
            }
        }

        // top two movements
        if (row <= 5) {

            // top right
            if (column <= 6) {
                if (board[row + 2][column + 1] == null) {
                    validMoves[0][pointer] = row + 2;
                    validMoves[1][pointer] = column + 1;
                    pointer++;
                }
                else if (!board[row + 2][column + 1].color.equals(board[row][column].color)) {
                    validMoves[0][pointer] = row + 2;
                    validMoves[1][pointer] = column + 1;
                    pointer++;
                }
            }

            // top left
            if (column >= 1) {
                if (board[row + 2][column - 1] == null) {
                    validMoves[0][pointer] = row + 2;
                    validMoves[1][pointer] = column - 1;
                    pointer++;
                }
                else if (!board[row + 2][column - 1].color.equals(board[row][column].color)) {
                    validMoves[0][pointer] = row + 2;
                    validMoves[1][pointer] = column - 1;
                    pointer++;
                }
            }
        }

        // left two movements
        if (column >= 2) {

            // left top
            if(row <= 6) {
                if (board[row + 1][column - 2] == null) {
                    validMoves[0][pointer] = row + 1;
                    validMoves[1][pointer] = column - 2;
                    pointer++;
                }
                else if (!board[row + 1][column - 2].color.equals(board[row][column].color)) {
                    validMoves[0][pointer] = row + 1;
                    validMoves[1][pointer] = column - 2;
                    pointer++;
                }
            }

            // left bottom
            if (row >= 1) {
                if (board[row - 1][column - 2] == null) {
                    validMoves[0][pointer] = row - 1;
                    validMoves[1][pointer] = column - 2;
                    pointer++;
                }
                else if (!board[row - 1][column - 2].color.equals(board[row][column].color)) {
                    validMoves[0][pointer] = row - 1;
                    validMoves[1][pointer] = column - 2;
                    pointer++;
                }
            }
        }

        // right two movements
        if (column <= 5) {

            // right top
            if (row <= 6) {
                if (board[row + 1][column + 2] == null) {
                    validMoves[0][pointer] = row + 1;
                    validMoves[1][pointer] = column + 2;
                    pointer++;
                }
                else if (!board[row + 1][column + 2].color.equals(board[row][column].color)) {
                    validMoves[0][pointer] = row + 1;
                    validMoves[1][pointer] = column + 2;
                    pointer++;
                }
            }

            // right bottom
            if (row >= 1) {
                if (board[row - 1][column + 2] == null) {
                    validMoves[0][pointer] = row - 1;
                    validMoves[1][pointer] = column + 2;
                    pointer++;
                }
                else if (!board[row - 1][column + 2].color.equals(board[row][column].color)) {
                    validMoves[0][pointer] = row - 1;
                    validMoves[1][pointer] = column + 2;
                    pointer++;
                }
            }
        }
        return validMoves;
    }

    /**
     * Method that will get all the valid movements of a Rook at the specified row
     * and column on the specified board. This list will include movements that can
     * capture a king of the opposite color.
     *
     * @param	row		the row of the piece whose moves are being fetched
     * @param	column	the column of the piece whose moves are being fetched
     * @param	board	the current board
     * @return			the array containing the movements
     */
    public static int[][] getRookMoves (int row, int column, Piece[][] board) {

        // creates valid move array and fills it with -1
        int[][] validMoves = new int[2][28];
        for (int i = 0; i <= validMoves.length - 1; i++) {
            Arrays.fill(validMoves[i], -1);
        }

        // keeps track of the position in the 2D array
        int pointer = 0;

        // upward moves
        for (int i = row; i < 7 ; i++ ) {
            if (board[i + 1][column] == null) {
                validMoves[0][pointer] = i + 1;
                validMoves[1][pointer] = column;
                pointer++;
            }
            else if (board[i + 1][column].color.equals(board[row][column].color)) {
                break;
            }
            else {
                validMoves[0][pointer] = i + 1;
                validMoves[1][pointer] = column;
                pointer++;
                break;
            }
        }

        // downward moves
        for (int i = row; i > 0 ; i-- ) {
            if (board[i - 1][column] == null) {
                validMoves[0][pointer] = i - 1;
                validMoves[1][pointer] = column;
                pointer++;
            }
            else if (board[i - 1][column].color.equals(board[row][column].color)) {
                break;
            }
            else {
                validMoves[0][pointer] = i - 1;
                validMoves[1][pointer] = column;
                pointer++;
                break;
            }
        }

        // leftward moves
        for (int i = column; i > 0 ; i-- ) {
            if (board[row][i - 1] == null) {
                validMoves[0][pointer] = row;
                validMoves[1][pointer] = i - 1;
                pointer++;
            }
            else if (board[row][i - 1].color.equals(board[row][column].color)) {
                break;
            }
            else {
                validMoves[0][pointer] = row;
                validMoves[1][pointer] = i - 1;
                pointer++;
                break;
            }
        }

        // rightward moves
        for (int i = column; i < 7 ; i++ ) {
            if (board[row][i + 1] == null) {
                validMoves[0][pointer] = row;
                validMoves[1][pointer] = i + 1;
                pointer++;
            }
            else if (board[row][i + 1].color.equals(board[row][column].color)) {
                break;
            }
            else {
                validMoves[0][pointer] = row;
                validMoves[1][pointer] = i + 1;
                pointer++;
                break;
            }
        }
        return validMoves;
    }

    /**
     * Method that will get all the valid movements of a bishop at the specified row
     * and column on the specified board. This list will include movements that can
     * capture a king of the opposite color.
     *
     * @param	row		the row of the piece whose moves are being fetched
     * @param	column	the column of the piece whose moves are being fetched
     * @param	board	the current board
     * @return			the array containing the movements
     */
    public static int[][] getBishopMoves (int row, int column, Piece[][] board) {

        // creates valid move array and fills it with -1
        int[][] validMoves = new int[2][28];
        for (int i = 0; i <= validMoves.length - 1; i++) {
            Arrays.fill(validMoves[i], -1);
        }

        // keeps track of the position in the 2D array
        int pointer = 0;

        // up right
        int rowCount = row;
        int columnCount = column;
        while (rowCount < 7 && columnCount < 7) {
            if (board[rowCount + 1][columnCount + 1] == null) {
                validMoves[0][pointer] = rowCount + 1;
                validMoves[1][pointer] = columnCount + 1;
                pointer++;
                rowCount++;
                columnCount++;
            }
            else if (board[rowCount + 1][columnCount + 1].color.equals(board[row][column].color)) {
                break;
            }
            else {
                validMoves[0][pointer] = rowCount + 1;
                validMoves[1][pointer] = columnCount + 1;
                pointer++;
                rowCount++;
                columnCount++;
                break;
            }
        }

        // down left
        rowCount = row;
        columnCount = column;
        while (rowCount > 0 && columnCount > 0) {
            if (board[rowCount - 1][columnCount - 1] == null) {
                validMoves[0][pointer] = rowCount - 1;
                validMoves[1][pointer] = columnCount - 1;
                pointer++;
                rowCount--;
                columnCount--;
            }
            else if (board[rowCount - 1][columnCount - 1].color.equals(board[row][column].color)) {
                break;
            }
            else {
                validMoves[0][pointer] = rowCount - 1;
                validMoves[1][pointer] = columnCount - 1;
                pointer++;
                rowCount--;
                columnCount--;
                break;
            }
        }

        // up left
        rowCount = row;
        columnCount = column;
        while (rowCount < 7 && columnCount > 0) {
            if (board[rowCount + 1][columnCount - 1] == null) {
                validMoves[0][pointer] = rowCount + 1;
                validMoves[1][pointer] = columnCount - 1;
                pointer++;
                rowCount++;
                columnCount--;
            }
            else if (board[rowCount + 1][columnCount - 1].color.equals(board[row][column].color)) {
                break;
            }
            else {
                validMoves[0][pointer] = rowCount + 1;
                validMoves[1][pointer] = columnCount - 1;
                pointer++;
                rowCount++;
                columnCount--;
                break;
            }
        }

        //down right
        rowCount = row;
        columnCount = column;
        while (rowCount > 0 && columnCount < 7) {
            if (board[rowCount - 1][columnCount + 1] == null) {
                validMoves[0][pointer] = rowCount - 1;
                validMoves[1][pointer] = columnCount + 1;
                pointer++;
                rowCount--;
                columnCount++;
            }
            else if (board[rowCount - 1][columnCount + 1].color.equals(board[row][column].color)) {
                break;
            }
            else {
                validMoves[0][pointer] = rowCount - 1;
                validMoves[1][pointer] = columnCount + 1;
                pointer++;
                rowCount--;
                columnCount++;
                break;
            }
        }
        return validMoves;
    }

    /**
     * Method that will get all the valid movements of a queen at the specified row
     * and column on the specified board by combining the move list for a bishop and
     * rook to emulate the queen's movement. This list will include movements that can
     * capture a king of the opposite color.
     *
     * @param	row		the row of the piece whose moves are being fetched
     * @param	column	the column of the piece whose moves are being fetched
     * @param	board	the current board
     * @return			the array containing the movements
     *
     * @see #getRookMoves(int, int, Piece[][])
     * @see #getBishopMoves(int, int, Piece[][])
     */
    public static int[][] getQueenMoves (int row, int column, Piece[][] board) {

        // reuse bishop and rook movement methods to get queen movements
        int[][]xMovements = getBishopMoves(row, column, board);
        int[][]plusMovements = getRookMoves(row, column, board);

        int[][]validMoves = new int[2][56];

        // combine both movesets into one array
        for (int i = 0; i <= 27; i++) {
            validMoves[0][i] = xMovements[0][i];
            validMoves[1][i] = xMovements[1][i];
        }
        for (int i = 28; i <= 55; i++) {
            validMoves[0][i] = plusMovements[0][i - 28];
            validMoves[1][i] = plusMovements[1][i - 28];
        }
        return validMoves;
    }
}
