package com.kata.tictactoe;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Game {
    private final Player player1;
    private final Player player2;
    private final Supplier<Cell> input;
    private final Consumer<String> consumer;
    private final Board board;

    public Game(String player1Name, String player2Name, Supplier<Cell> input, Consumer<String> consumer) {
        Objects.requireNonNull(player1Name);
        Objects.requireNonNull(player2Name);
        Objects.requireNonNull(input);
        Objects.requireNonNull(consumer);

        this.player1 = new Player(player1Name, Mark.O);
        this.player2 = new Player(player2Name, Mark.X);
        this.input = input;
        this.consumer = consumer;
        this.board = new Board();
    }

    public void play() {
        var currentPlayer = player1;

        while (true) {
            consumer.accept("%s it is your turn ".formatted(currentPlayer.name()));
            var cell = input.get();

            if (board.place(currentPlayer.mark(), cell)) {
                if (board.isWinner(currentPlayer.mark())) {
                    consumer.accept("%s is the winner".formatted(currentPlayer.name()));
                    return;
                }

                if (board.isFull()) {
                    consumer.accept("No winners");
                    return;
                }

                currentPlayer = currentPlayer == player1 ? player2 : player1;
            } else {
                consumer.accept("%s the selected cell is full. Choose another cell ".formatted(currentPlayer.name()));
            }
        }
    }
}
