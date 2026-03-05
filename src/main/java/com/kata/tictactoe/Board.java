package com.kata.tictactoe;

import java.util.Arrays;

public class Board {
    private final char[][] board;

    public Board() {
        board = new char[3][3];
        for (char[] row : board) {
            Arrays.fill(row, ' ');
        }
    }

    @Override
    public String toString() {
        var output = new StringBuilder();

        for (char[] row: board) {
            output.append(Arrays.toString(row))
                    .append("\n");
        }

        return output.toString();
    }

    public boolean place(Mark mark, Cell cell) {
        if (isFree(cell)) {
            board[cell.rowPos()][cell.colPos()] = mark.toChar();
            return true;
        }

        return false;
    }

    public boolean isFree(Cell cell) {
        return board[cell.rowPos()][cell.colPos()] == ' ';
    }

    public boolean isWinner(Mark mark) {
        for (int i = 0; i < 3; i++) {
            //check row
            if (board[i][0] == board[i][1] &&
                    board[i][1] == board[i][2] &&
                    board[i][2] == mark.toChar()) {
                return true;
            }

            // check column
            if (board[0][i] == board[1][i] &&
                    board[1][i] == board[2][i] &&
                    board[2][i] == mark.toChar()) {
                return true;
            }
        }

        // check diagonal
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[2][2] == mark.toChar()) {
            return true;
        }

        return board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[2][0] == mark.toChar();
    }

    public boolean isFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ')
                    return false;
            }
        }

        return true;
    }


}
