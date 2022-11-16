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

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private String source;
    private List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;

    public Lexer(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        addToken(TokenType.EOF);

        return tokens;
    }

    private void scanToken() {
        char c = advance();

        if (Character.isLetter(c)) {
            string();
            return;
        }

        if (Character.isDigit(c)) {
            number();
            return;
        }

        switch (c) {
            case '(' -> addToken(TokenType.LEFT_PARENTHESIS);
            case ')' -> addToken(TokenType.RIGHT_PARENTHESIS);
            case '+' -> addToken(TokenType.PLUS);
            case '-' -> addToken(TokenType.MINUS);
            case '*' -> addToken(match('*') ? TokenType.DOUBLE_STAR : TokenType.STAR);
            case '/' -> addToken(TokenType.SLASH);
            case '%' -> addToken(TokenType.PERCENT);
            case ' ' -> {}

            default -> System.err.println("Unexpected character: " + c);

        }
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void addToken(TokenType type) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text));
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char peek() {
        if (isAtEnd()) return '\0';

        return source.charAt(current);
    }

    private void string() {
        while (Character.isLetter(peek()) && !isAtEnd()) {
            advance();
        }

        addToken(TokenType.STRING);
    }

    private void number() {
        while(Character.isDigit(peek()) && !isAtEnd()) advance();

        addToken(TokenType.NUMBER);
    }

}
