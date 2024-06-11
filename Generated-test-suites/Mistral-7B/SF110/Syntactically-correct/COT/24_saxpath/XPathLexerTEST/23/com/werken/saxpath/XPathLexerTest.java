package com.werken.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XPathLexerTest {
    

    @Test
    public void testEmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testIdentifier() {
        XPathLexer lexer = new XPathLexer("myIdentifier");
        Token token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("myIdentifier", token.getText());
    }

    @Test
    public void testOperatorName() {
        XPathLexer lexer = new XPathLexer("andOperator orOperator");
        Token andOperator = lexer.nextToken();
        assertEquals(TokenType.AND, andOperator.getTokenType());
        Token orOperator = lexer.nextToken();
        assertEquals(TokenType.OR, orOperator.getTokenType());
    }

    @Test
    public void testNumber() {
        XPathLexer lexer = new XPathLexer("123.456");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("123.456", token.getText());
    }

    @Test
    public void testWhitespace() {
        XPathLexer lexer = new XPathLexer(" \t\n myIdentifier \r ");
        Token token = lexer.nextToken();
        assertNull(token);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidXPath() {
        XPathLexer lexer = new XPathLexer("myIdentifier123");
        lexer.nextToken();
    }

    @Test
    public void testDollarSign() {
        XPathLexer lexer = new XPathLexer("$myIdentifier");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOLLAR, token.getTokenType());
    }

    @Test
    public void testDoubleColon() {
        XPathLexer lexer = new XPathLexer("::myIdentifier");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOUBLE_COLON, token.getTokenType());
    }

    @Test
    public void testAtSymbol() {
        XPathLexer lexer = new XPathLexer("@myIdentifier");
        Token token = lexer.nextToken();
        assertEquals(TokenType.AT, token.getTokenType());
    }

    @Test
    public void testColon() {
        XPathLexer lexer = new XPathLexer(":myIdentifier");
        Token token = lexer.nextToken();
        assertEquals(TokenType.COLON, token.getTokenType());
    }

    @Test
    public void testStar() {
        XPathLexer lexer = new XPathLexer("*");
        Token token = lexer.nextToken();
        assertEquals(TokenType.STAR, token.getTokenType());
    }

    @Test
    public void testComma() {
        XPathLexer lexer = new XPathLexer(",");
        Token token = lexer.nextToken();
        assertEquals(TokenType.COMMA, token.getTokenType());
    }

    @Test
    public void testEquals() {
        XPathLexer lexer = new XPathLexer("=");
        Token token = lexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());
    }

    @Test
    public void testMinus() {
        XPathLexer lexer = new XPathLexer("-");
        Token token = lexer.nextToken();
        assertEquals(TokenType.MINUS, token.getTokenType());
    }

    @Test
    public void testPlus() {
        XPathLexer lexer = new XPathLexer("+");
        Token token = lexer.nextToken();
        assertEquals(TokenType.PLUS, token.getTokenType());
    }

    @Test
    public void testLeftParen() {
        XPathLexer lexer = new XPathLexer("(");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LEFT_PAREN, token.getTokenType());
    }

    @Test
    public void testRightParen() {
        XPathLexer lexer = new XPathLexer(")");
        Token token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_PAREN, token.getTokenType());
    }

    @Test
    public void testLeftBracket() {
        XPathLexer lexer = new XPathLexer("[");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
    }

    @Test
    public void testRightBracket() {
        XPathLexer lexer = new XPathLexer("]");
        Token token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
    }

    @Test
    public void testSlashes() {
        XPathLexer lexer = new XPathLexer("/");
        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
    }

    @Test
    public void testLiteral() {
        XPathLexer lexer = new XPathLexer("\"myString\"");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LITERAL, token.getTokenType());
        assertEquals("myString", token.getText());
    }

    @Test
    public void testDots() {
        XPathLexer lexer = new XPathLexer("..");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOTS, token.getTokenType());
    }

    @Test
    public void testNumberWithPeriod() {
        XPathLexer lexer = new XPathLexer("1.2");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("1.2", token.getText());
    }

    @Test
    public void testNumberWithLeadingZero() {
        XPathLexer lexer = new XPathLexer("0123");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("123", token.getText());
    }

    @Test
    public void testRelationalOperator() {
        XPathLexer lexer = new XPathLexer(">=");
        Token token = lexer.nextToken();
        assertEquals(TokenType.GREATER_EQUALS, token.getTokenType());
    }

    @Test
    public void testNotOperator() {
        XPathLexer lexer = new XPathLexer("!=");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NOT_EQUALS, token.getTokenType());
    }

    @Test
    public void testNotKeyword() {
        XPathLexer lexer = new XPathLexer("not()");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NOT_KEYWORD, token.getTokenType());
    }

    @Test
    public void testAndKeyword() {
        XPathLexer lexer = new XPathLexer("and()");
        Token token = lexer.nextToken();
        assertEquals(TokenType.AND_KEYWORD, token.getTokenType());
    }

    @Test
    public void testOrKeyword() {
        XPathLexer lexer = new XPathLexer("or()");
        Token token = lexer.nextToken();
        assertEquals(TokenType.OR_KEYWORD, token.getTokenType());
    }

    @Test
    public void testModOperator() {
        XPathLexer lexer = new XPathLexer("mod(.,.)");
        Token token = lexer.nextToken();
        assertEquals(TokenType.MOD_OPERATOR, token.getTokenType());
    }

    @Test
    public void testDivOperator() {
        XPathLexer lexer = new XPathLexer("div(.,.)");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DIV_OPERATOR, token.getTokenType());
    }

    
}