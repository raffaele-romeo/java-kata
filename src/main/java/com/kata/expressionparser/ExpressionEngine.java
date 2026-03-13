package com.kata.expressionparser;

public class ExpressionEngine {

    public static double parse(String expression) {
        var tokenizer = new Tokenizer(expression);
        var parser = new Parser(tokenizer.tokenize());

        return ASTEvaluator.evaluate(parser.expression());
    }

    public static void main(String[] args) {
        var expression = "2 + 3 x (5 / 2) + 59";

        System.out.println(parse(expression));
    }
}
