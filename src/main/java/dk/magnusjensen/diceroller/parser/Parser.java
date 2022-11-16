/*
 * Copyright 2022 Magnus Jensen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dk.magnusjensen.diceroller.parser;

import dk.magnusjensen.diceroller.framework.dies.NormalDice;
import dk.magnusjensen.diceroller.framework.Operator;
import dk.magnusjensen.diceroller.framework.modifiers.KeepModifier;
import dk.magnusjensen.diceroller.framework.modifiers.Modifier;
import dk.magnusjensen.diceroller.framework.nodes.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser for parsing roll20 style dice rolls.
 */
public class Parser {
    private List<Token> tokens;
    private int current;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.current = 0;
    }

    public Node term() {
        Node left = factor();

        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = previous();
            Node right = factor();
            left =  new ArithmeticNode(left, right, Operator.fromString(operator.getValue()));
        }

        return left;
    }

    Node factor() {
        Node left = exponetation();

        while (match(TokenType.SLASH, TokenType.STAR, TokenType.PERCENT)) {
            Token operator = previous();
            Node right = exponetation();
            left =  new ArithmeticNode(left, right, Operator.fromString(operator.getValue()));
        }

        return left;
    }

    Node exponetation() {
        Node left = unary();

        while (match(TokenType.DOUBLE_STAR)) {
            Node right = unary();
            left =  new ArithmeticNode(left, right, Operator.EXPONENTATION);
        }

        return left;
    }

    Node unary() {
        if (match(TokenType.MINUS)) {
            Node right = unary();
            return new NegateNode(right);
        }

        return primary();
    }

    Node primary() {
        if (match(TokenType.NUMBER)) {
            if (peek().getTokenType() == TokenType.STRING) {
                if (peek().getValue().equals("d")) {
                    return diceRoll();
                } else {
                    throw new RuntimeException("Expected character after number to be 'd'");
                }
            }
            return new NumberNode(Integer.parseInt(previous().getValue()));
        }

        if (match(TokenType.LEFT_PARENTHESIS)) {
            Node node = term();
            consume(TokenType.RIGHT_PARENTHESIS, "Expected ')' after expression.");
            return new ParenthesisNode(node);
        }

        throw new RuntimeException(peek() + " Expected expression.");
    }

    Node diceRoll() {
        Token numOfRolls = previous();

        // Consume 'd'
        consume(TokenType.STRING, "Expected character after number.");

        Token diceSides = consume(TokenType.NUMBER, "Expected dice side to be number");


        List<Modifier> modifiers = new ArrayList<>();
        if (match(TokenType.STRING)) {
            Token stringMod = previous();
            switch (stringMod.getValue()) {
                case "k" -> {
                    // TODO: Validate to only have one keep in the modifier list.
                    Token amountToKeep = consume(TokenType.NUMBER, "Expected number after keep modifier");
                    modifiers.add(new KeepModifier(Integer.parseInt(amountToKeep.getValue()), true));
                }
                case "kl" -> {
                    // TODO: Validate to only have one keep in the modifier list.
                    Token amountToKeep = consume(TokenType.NUMBER, "Expected number after keep modifier");
                    modifiers.add(new KeepModifier(Integer.parseInt(amountToKeep.getValue()), false));
                }
            }
        }

        return new DiceNode(
            Integer.parseInt(numOfRolls.getValue()),
            new NormalDice(Integer.parseInt(diceSides.getValue())),
            modifiers
            );
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getTokenType() == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().getTokenType() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();

        throw new RuntimeException("" + peek() + message);
    }
}
