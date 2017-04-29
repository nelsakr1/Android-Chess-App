package android94.chess;

public class Gameplay {

    /**
     * Method that will create an 8x8 object array of Pieces and populate them with
     * the pieces in their correct starting positions.
     *
     * @return	newBoard	the newly created board with all its pieces placed
     */
    public static Piece[][] createNewBoard() {

        // create 8x8 array of Piece objects
        Piece[][] newBoard = new Piece[8][8];

        // place white pawns in row 2
        for (int i = 0; i <= 7; i++) {
            newBoard[1][i] = new Pawn("white", false, false);
        }

        // place black pawns in row 7
        for (int i = 0; i <= 7; i++) {
            newBoard[6][i] = new Pawn("black", false, false);
        }

        // place remaining white pieces
        newBoard[0][0] = new Rook("white", false);
        newBoard[0][1] = new Knight("white", false);
        newBoard[0][2] = new Bishop("white", false);
        newBoard[0][3] = new Queen("white", false);
        newBoard[0][4] = new King("white", false);
        newBoard[0][5] = new Bishop("white", false);
        newBoard[0][6] = new Knight("white", false);
        newBoard[0][7] = new Rook("white", false);

        // place remaining black pieces
        newBoard[7][0] = new Rook("black", false);
        newBoard[7][1] = new Knight("black", false);
        newBoard[7][2] = new Bishop("black", false);
        newBoard[7][3] = new Queen("black", false);
        newBoard[7][4] = new King("black", false);
        newBoard[7][5] = new Bishop("black", false);
        newBoard[7][6] = new Knight("black", false);
        newBoard[7][7] = new Rook("black", false);

        return newBoard;
    }

    /**
     * Method that will get all the possible movements of every piece of the opposite
     * color and check if any of those pieces can capture the king.
     *
     * @param	board		the current board
     * @param	playerColor	the color of the player being checked against
     * @return				boolean containing whether or not the player is in check
     */
    public static boolean playerInCheck(Piece[][] board, String playerColor) {

        // traverse the entire board
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {

                // skip over empty spots on the board
                if (board[i][j] == null) {
                    continue;
                }

                // skips pieces that belong to the current turn's player
                if (playerColor.equals("white") && board[i][j].color.equals("white")) {
                    continue;
                }
                if (playerColor.equals("black") && board[i][j].color.equals("black")) {
                    continue;
                }

                // get the move list for each piece
                int[][] allMoves = Movement.getAllMoves(i, j, board);

                // traverse the move list
                for (int k = 0; k < allMoves[1].length - 1; k++) {

                    // skip over "empty" entries
                    if (allMoves[0][k] == -1) {
                        continue;
                    }

                    // see if there is any instance of a piece being able to capture a king
                    if (board[allMoves[0][k]][allMoves[1][k]] instanceof King) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Method that will check if any possible movements for the specified color are
     * available by moving each piece to any of its possible positions on a dummy
     * board and checking if the player is still in check.
     *
     * @param	board		the current board
     * @param	playerColor	the color of the player being checked against
     * @return				boolean containing whether the player has any moves left
     */
    public static boolean hasValidMovesLeft (Piece[][] board, String playerColor) {

        // traverse the entire board
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {

                // skip over empty spots on the board
                if (board[i][j] == null) {
                    continue;
                }

                // skips pieces that do not belong
                if (playerColor.equals("white") && board[i][j].color.equals("black")) {
                    continue;
                }
                if (playerColor.equals("black") && board[i][j].color.equals("white")) {
                    continue;
                }

                // generate the valid move list for the current piece
                int[][] validMoves = Movement.getValidMoves(i, j, board);

                // traverse the valid move list
                for (int k = 0; k < validMoves[0].length - 1; k++) {

                    // skip over any "empty" entries
                    if (validMoves[0][k] == -1) {
                        continue;
                    }

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

                    // check if any of the moves will remove the check
                    copyBoard = Movement.movePiece(i, j, validMoves[0][k], validMoves[1][k], copyBoard);
                    if (!playerInCheck(copyBoard, playerColor)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
