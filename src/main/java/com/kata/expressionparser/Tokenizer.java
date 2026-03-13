package com.kata.expressionparser;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private final char[] chars;
    private int currentPosition = 0;

    public Tokenizer(String expression) {
        this.chars = expression.toCharArray();
    }

    public List<Token> tokenize() {
        var output = new ArrayList<Token>();

        while (currentPosition < chars.length) {
            var currentElem = chars[currentPosition];

            if (currentElem == ' ') {
                currentPosition++;
            } else if (Character.isDigit(currentElem)) {
                output.add(parseNumber());
            } else {
                var result = parseLiterals(currentElem);

                if (result != null) {
                    output.add(result);
                    currentPosition++;
                } else {
                    throw new IllegalStateException("Expected a literal, got a " + currentElem);
                }
            }
        }

        output.add(new Token(TokenType.EOF, null));

        return output;
    }

    private Token parseNumber() {
        var valueStr = new StringBuilder();

        while (currentPosition < chars.length && Character.isDigit(chars[currentPosition])) {
            valueStr.append(chars[currentPosition]);
            currentPosition++;
        }

        return new Token(TokenType.NUMBER, Integer.parseInt(valueStr.toString()));
    }

    private static Token parseLiterals(char c) {
        return switch (c) {
            case '(' -> new Token(TokenType.LPAREN, null);
            case ')' -> new Token(TokenType.RPAREN, null);
            case '+' -> new Token(TokenType.PLUS, null);
            case '-' -> new Token(TokenType.MINUS, null);
            case '/' -> new Token(TokenType.DIVIDE, null);
            case '*', 'x' -> new Token(TokenType.STAR, null);
            default -> null;
        };
    }
}
