package com.werken.saxpath;

import static org.junit.Assert.*;

import org.junit.Test;

public class XPathLexerTest {

    @Test
    public void testXPathLexerEmptyString() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test
    public void testXPathLexerSingleCharacter() {
        XPathLexer lexer = new XPathLexer("a");
        Token token = lexer.nextToken();
        assertEquals(TokenType.AND, token.getTokenType());
    }

    @Test
    public void testXPathLexerSingleCharacterIdentifier() {
        XPathLexer lexer = new XPathLexer("id");
        Token token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
    }

    @Test
    public void testXPathLexerWhitespace() {
        XPathLexer lexer = new XPathLexer(" \t a");
        Token token = lexer.nextToken();
        assertEquals(TokenType.AND, token.getTokenType());
    }

    @Test
    public void testXPathLexerNumber() {
        XPathLexer lexer = new XPathLexer("123");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
    }

    @Test
    public void testXPathLexerNumberWithDecimalPoint() {
        XPathLexer lexer = new XPathLexer("123.45");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
    }

    @Test
    public void testXPathLexerStringLiteral() {
        XPathLexer lexer = new XPathLexer("'abc'");
        Token token = lexer.nextToken();
        assertEquals(TokenType.STRING_LITERAL, token.getTokenType());
    }

    @Test
    public void testXPathLexerStringLiteralWithSingleQuoteEscape() {
        XPathLexer lexer = new XPathLexer("'\''");
        Token token = lexer.nextToken();
        assertEquals(TokenType.STRING_LITERAL, token.getTokenType());
    }

    @Test
    public void testXPathLexerStringLiteralWithDoubleQuoteEscape() {
        XPathLexer lexer = new XPathLexer("\"\\\"\"");
        Token token = lexer.nextToken();
        assertEquals(TokenType.STRING_LITERAL, token.getTokenType());
    }

    @Test
    public void testXPathLexerDollarSign() {
        XPathLexer lexer = new XPathLexer("$");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOLLAR_SIGN, token.getTokenType());
    }

    @Test
    public void testXPathLexerSlash() {
        XPathLexer lexer = new XPathLexer("/");
        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
    }

    @Test
    public void testXPathLexerDoubleSlash() {
        XPathLexer lexer = new XPathLexer("/ /");
        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
    }

    @Test
    public void testXPathLexerComma() {
        XPathLexer lexer = new XPathLexer(",");
        Token token = lexer.nextToken();
        assertEquals(TokenType.COMMA, token.getTokenType());
    }

    @Test
    public void testXPathLexerLeftParenthesis() {
        XPathLexer lexer = new XPathLexer("(");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LEFT_PAREN, token.getTokenType());
    }

    @Test
    public void testXPathLexerRightParenthesis() {
        XPathLexer lexer = new XPathLexer(")");
        Token token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_PAREN, token.getTokenType());
    }

    @Test
    public void testXPathLexerLeftBracket() {
        XPathLexer lexer = new XPathLexer("[");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
    }

    @Test
    public void testXPathLexerRightBracket() {
        XPathLexer lexer = new XPathLexer("]");
        Token token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
    }

    @Test
    public void testXPathLexerPlus() {
        XPathLexer lexer = new XPathLexer("+");
        Token token = lexer.nextToken();
        assertEquals(TokenType.PLUS, token.getTokenType());
    }

    @Test
    public void testXPathLexerMinus() {
        XPathLexer lexer = new XPathLexer("-");
        Token token = lexer.nextToken();
        assertEquals(TokenType.MINUS, token.getTokenType());
    }

    @Test
    public void testXPathLexerRelationalOperatorLessThan() {
        XPathLexer lexer = new XPathLexer("<");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LESS_THAN, token.getTokenType());
    }

    @Test
    public void testXPathLexerRelationalOperatorLessThanOrEqual() {
        XPathLexer lexer = new XPathLexer("<=");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LESS_THAN_OR_EQUAL, token.getTokenType());
    }

    @Test
    public void testXPathLexerRelationalOperatorGreaterThan() {
        XPathLexer lexer = new XPathLexer(">");
        Token token = lexer.nextToken();
        assertEquals(TokenType.GREATER_THAN, token.getTokenType());
    }

    @Test
    public void testXPathLexerRelationalOperatorGreaterThanOrEqual() {
        XPathLexer lexer = new XPathLexer(">=");
        Token token = lexer.nextToken();
        assertEquals(TokenType.GREATER_THAN_OR_EQUAL, token.getTokenType());
    }

    @Test
    public void testXPathLexerEquals() {
        XPathLexer lexer = new XPathLexer("=");
        Token token = lexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());
    }

    @Test
    public void testXPathLexerNotEquals() {
        XPathLexer lexer = new XPathLexer("!=");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NOT_EQUALS, token.getTokenType());
    }

    @Test
    public void testXPathLexerNot() {
        XPathLexer lexer = new XPathLexer("!");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NOT, token.getTokenType());
    }

    @Test
    public void testXPathLexerPipe() {
        XPathLexer lexer = new XPathLexer("|");
        Token token = lexer.nextToken();
        assertEquals(TokenType.PIPE, token.getTokenType());
    }

    @Test
    public void testXPathLexerAtSign() {
        XPathLexer lexer = new XPathLexer("@");
        Token token = lexer.nextToken();
        assertEquals(TokenType.AT_SIGN, token.getTokenType());
    }

    @Test
    public void testXPathLexerDoubleColon() {
        XPathLexer lexer = new XPathLexer("::");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOUBLE_COLON, token.getTokenType());
    }

    @Test
    public void testXPathLexerColon() {
        XPathLexer lexer = new XPathLexer(":");
        Token token = lexer.nextToken();
        assertEquals(TokenType.COLON, token.getTokenType());
    }

    @Test
    public void testXPathLexerStar() {
        XPathLexer lexer = new XPathLexer("*");
        Token token = lexer.nextToken();
        assertEquals(TokenType.ASTERISK, token.getTokenType());
    }
}