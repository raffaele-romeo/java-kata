package com.kata.tictactoe;

public record Cell(int rowPos, int colPos) {
    public Cell {
        if (rowPos < 0 || rowPos > 2 || colPos < 0 || colPos > 2) {
            throw new IllegalArgumentException("Invalid row or column position");
        }
    }
}
