package com.kata.tictactoe;

public enum Mark {
    X,
    O;

    public char toChar() {
        return switch (this) {
            case O -> 'O';
            case X -> 'X';
        };
    }
}
