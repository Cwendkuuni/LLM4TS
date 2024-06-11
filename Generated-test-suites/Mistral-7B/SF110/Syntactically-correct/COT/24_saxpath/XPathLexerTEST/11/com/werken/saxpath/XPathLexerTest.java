package com.werken.saxpath;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XPathLexerTest {
    private XPathLexer lexer;
    private String xpath;

    @Before
    public void setUp() {
        xpath = "//test[1]";
        lexer = new XPathLexer(xpath);
    }

    @Test
    public void testNextToken_BasicFunctionality() {
        Token token;
        for (int i = 0; i < xpath.length(); ++i) {
            token = lexer.nextToken();
            assertEquals(Character.toString(xpath.charAt(i)), lexer.getXPath().substring(lexer.getPreviousToken().getStartPosition(), lexer.getCurrentPosition()));
            assertEquals(token.getTokenType(), getTokenType(xpath.charAt(i)));
        }
        assertNull(lexer.nextToken());
    }

    @Test
    public void testNextToken_EdgeCases() {
        String xpathForEdgeCases = "//test[1]/@attribute='value'|id('id')|.//child::node()";
        lexer = new XPathLexer(xpathForEdgeCases);

        Token token;

        token = lexer.nextToken(); // '//'
        assertEquals(TokenType.XPATH_PREFIX, token.getTokenType());

        token = lexer.nextToken(); // 't'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // 'e'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // 's'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // 't'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // '['
        assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());

        token = lexer.nextToken(); // '1'
        assertEquals(TokenType.NUMBER, token.getTokenType());

        token = lexer.nextToken(); // ']'
        assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());

        token = lexer.nextToken(); // '/'
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken(); // 'a'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // 't'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // 't'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // 'r'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // 'i'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // 'b'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // '('
        assertEquals(TokenType.LEFT_PAREN, token.getTokenType());

        token = lexer.nextToken(); // 'i'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // 'd'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // ')'
        assertEquals(TokenType.RIGHT_PAREN, token.getTokenType());

        token = lexer.nextToken(); // '.'
        assertEquals(TokenType.DOT, token.getTokenType());

        token = lexper.nextToken(); // 'c'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // 'h'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // 'i'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // 'l'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // 'd'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // ':'
        assertEquals(TokenType.COLON, token.getTokenType());

        token = lexer.nextToken(); // 'n'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // 'o'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // 'd'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // 'e'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // '('
        assertEquals(TokenType.LEFT_PAREN, token.getTokenType());

        token = lexer.nextToken(); // '.'
        assertEquals(TokenType.DOT, token.getTokenType());

        token = lexer.nextToken(); // 'c'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // 'h'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // 'i'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // 'l'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // 'd'
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        token = lexer.nextToken(); // ')'
        assertEquals(TokenType.RIGHT_PAREN, token.getTokenType());

        token = lexer.nextToken(); // '|'
        assertEquals(TokenType.PIPE, token.getTokenType());

        assertNull(lexer.nextToken());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextToken_ExceptionHandling() {
        lexer = new XPathLexer("");
        lexer.nextToken();
        lexer.nextToken();
        lexer.nextToken();
        lexer.nextToken();
        lexer.nextToken();
        lexer.nextToken();
        lexer.nextToken(); // This should throw an exception because there's no more characters to consume
    }

    @After
    public void tearDown() {
        lexer = null;
        xpath = null;
    }

    private int getTokenType(char c) {
        switch (c) {
            case '$':
                return TokenType.DOLLAR.getValue();
            case '\"':
            case '\'':
                return TokenType.LITERAL.getValue();
            case '/':
                return TokenType.SLASH.getValue();
            case ',':
                return TokenType.COMMA.getValue();
            case '(':
                return TokenType.LEFT_PAREN.getValue();
            case ')':
                return TokenType.RIGHT_PAREN.getValue();
            case '[':
                return TokenType.LEFT_BRACKET.getValue();
            case ']':
                return TokenType.RIGHT_BRACKET.getValue();
            case '+':
                return TokenType.PLUS.getValue();
            case '-':
                return TokenType.MINUS.getValue();
            case '<':
            case '>':
                return TokenType.RELATIONAL_OPERATOR.getValue();
            case '=':
                return TokenType.EQUALS.getValue();
            case '!':
                if (xpath.charAt(lexer.currentPosition() + 1) == '=') {
                    return TokenType.NOT_EQUALS.getValue();
                }
                return TokenType.NOT.getValue();
            case '|':
                return TokenType.PIPE.getValue();
            case '@':
                return TokenType.AT.getValue();
            case ':':
                if (xpath.charAt(lexer.currentPosition() + 1) == ':') {
                    return TokenType.DOUBLE_COLON.getValue();
                }
                return TokenType.COLON.getValue();
            case '*':
                return TokenType.STAR.getValue();
            case '.':
                if (Character.isDigit(xpath.charAt(lexer.currentPosition() + 1))) {
                    return TokenType.NUMBER.getValue();
                }
                return TokenType.DOT.getValue();
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return TokenType.NUMBER.getValue();
            case '\t':
            case '\n':
            case '\r':
            case ' ':
                return TokenType.WHITESPACE.getValue();
            default:
                if (isIdentifierStartChar(c)) {
                    return TokenType.IDENTIFIER_OR_OPERATOR_NAME.getValue();
                }
                return TokenType.INVALID.getValue();
        }
    }

    private boolean isIdentifierStartChar(char c) {
        return c == '_' || Character.isUnicodeIdentifierStart(c);
    }
}

enum TokenType {
    XPATH_PREFIX(1),
    DOLLAR(26),
    LITERAL(27),
    SLASH(11),
    COMMA(32),
    LEFT_PAREN(1),
    RIGHT_PAREN(2),
    LEFT_BRACKET(3),
    RIGHT_BRACKET(4),
    PLUS(5),
    MINUS(6),
    RELATIONAL_OPERATOR(7, 8, 9),
    EQUALS(21),
    NOT(23),
    NOT_EQUALS(22),
    PIPE(17),
    AT(16),
    COLON(18),
    DOUBLE_COLON(19),
    IDENTIFIER(15),
    INVALID(-1),
    NUMBER(31),
    DOT(13),
    STAR(20);

    private final int value;
    private final int[] relationalOperatorValues;

    TokenType(int value) {
        this.value = value;
    }

    TokenType(int value, int... relationalOperatorValues) {
        this.value = value;
        this.relationalOperatorValues = relationalOperatorValues;
    }

    public int getValue() {
        return value;
    }

    public static TokenType getTokenType(int value) {
        for (TokenType tokenType : values()) {
            if (tokenType.value == value) {
                return tokenType;
            }
        }
        throw new IllegalArgumentException("Invalid token type value: " + value);
    }

    public boolean isRelationalOperator() {
        return relationalOperatorValues != null;
    }

    public int getRelationalOperatorValue() {
        if (isRelationalOperator()) {
            return relationalOperatorValues[0];
        }
        throw new IllegalStateException();
    }

    public int getRelationalOperatorValue2() {
        if (isRelationalOperator()) {
            return relationalOperatorValues[1];
        }
        throw new IllegalStateException();
    }
}