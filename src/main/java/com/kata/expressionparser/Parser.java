package com.kata.expressionparser;

import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int currentToken = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token peek() {
        return tokens.get(currentToken);
    }

    private Token advance() {
        return tokens.get(currentToken++);
    }

    private boolean check(TokenType tokenType) {
        return peek().type() == tokenType;
    }

    public Expr expression() {
        var leftExpr = factor();

        while (check(TokenType.PLUS) || check(TokenType.MINUS)) {
            var operand = advance();
            var rightExpr = factor();
            leftExpr = new BinaryExpr(leftExpr, operand.type(), rightExpr);
        }

        return leftExpr;
    }

    private Expr factor() {
        var leftExpr = term();

        while (check(TokenType.DIVIDE) || check(TokenType.STAR)) {
            var operand = advance();
            var rightExpr = term();

            leftExpr = new BinaryExpr(leftExpr, operand.type(), rightExpr);
        }

        return leftExpr;
    }

    private Expr term() {
        if (TokenType.LPAREN == peek().type()) {
            advance();
            var expression = expression();

            if (TokenType.RPAREN == peek().type()) {
                advance();
                return expression;
            }

            throw new IllegalStateException("Expected a closing parenthesis in the expression");
        }

        if (TokenType.NUMBER == peek().type()) {
            return new NumberExpr(advance().value());
        }

        throw new IllegalStateException("Malformed expression");
    }
}
