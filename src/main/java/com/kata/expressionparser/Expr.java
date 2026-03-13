package com.kata.expressionparser;

public sealed interface Expr permits NumberExpr, BinaryExpr {
}

record NumberExpr(int value) implements Expr {
}

record BinaryExpr(Expr left, TokenType op, Expr right) implements Expr {
}