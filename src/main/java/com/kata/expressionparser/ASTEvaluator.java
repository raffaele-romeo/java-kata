package com.kata.expressionparser;

public class ASTEvaluator {
    public static Double evaluate(Expr expr) {
        return switch (expr) {
            case NumberExpr e -> e.value() * 1.0;
            case BinaryExpr b -> switch (b.op()) {
                case PLUS -> evaluate(b.left()) + evaluate(b.right());
                case MINUS -> evaluate(b.left()) - evaluate(b.right());
                case STAR -> evaluate(b.left()) * evaluate(b.right());
                case DIVIDE -> evaluate(b.left()) / evaluate(b.right());
                default -> throw new IllegalStateException("Invalid operation");
            };
        };
    }
}
