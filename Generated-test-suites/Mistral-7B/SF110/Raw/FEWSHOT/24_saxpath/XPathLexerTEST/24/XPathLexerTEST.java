package com.werken.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XPathLexerTest {

    @Test
    public void testNextToken_EmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        assertNull(lexer.nextToken());
    }

    @Test
    public void testNextToken_SingleCharacter() {
        XPathLexer lexer = new XPathLexer("a");
        Token token = lexer.nextToken();
        assertEquals(TokenType.AND, token.getTokenType());
        assertEquals("a", token.getText());
    }

    @Test
    public void testNextToken_MultipleCharacters() {
        XPathLexer lexer = new XPathLexer("and");
        Token token = lexer.nextToken();
        assertEquals(TokenType.AND, token.getTokenType());
        assertEquals("and", token.getText());
    }

    @Test
    public void testNextToken_WhiteSpace() {
        XPathLexer lexer = new XPathLexer("   xpath   ");
        Token token = lexer.nextToken();
        assertEquals(TokenType.WHITESPACE, token.getTokenType());
        assertEquals("", token.getText());
    }

    @Test
    public void testNextToken_Slashes() {
        XPathLexer lexer = new XPathLexer("/foo/bar");
        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASHES, token.getTokenType());
        assertEquals("/", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASHES, token.getTokenType());
        assertEquals("/", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("bar", token.getText());

        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testNextToken_Identifier() {
        XPathLexer lexer = new XPathLexer("foo");
        Token token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getText());
    }

    @Test
    public void testNextToken_Number() {
        XPathLexer lexer = new XPathLexer("123");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("123", token.getText());
    }

    @Test
    public void testNextToken_OperatorName() {
        XPathLexer lexer = new XPathLexer("mod");
        Token token = lexer.nextToken();
        assertEquals(TokenType.MOD, token.getTokenType());
        assertEquals("mod", token.getText());
    }

    @Test
    public void testNextToken_ReservedWord() {
        XPathLexer lexer = new XPathLexer("div");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DIV, token.getTokenType());
        assertEquals("div", token.getText());
    }

    @Test
    public void testNextToken_RelationalOperator() {
        XPathLexer lexer = new XPathLexer("<=");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LESS_OR_EQUAL, token.getTokenType());
        assertEquals("<=", token.getText());
    }

    @Test
    public void testNextToken_Equals() {
        XPathLexer lexer = new XPathLexer("=");
        Token token = lexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());
        assertEquals("=", token.getText());
    }

    @Test
    public void testNextToken_NotEquals() {
        XPathLexer lexer = new XPathLexer("!=");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NOT_EQUALS, token.getTokenType());
        assertEquals("!=", token.getText());
    }

    @Test
    public void testNextToken_Not() {
        XPathLexer lexer = new XPathLexer("not");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NOT, token.getTokenType());
        assertEquals("not", token.getText());
    }

    @Test
    public void testNextToken_And() {
        XPathLexer lexer = new XPathLexer("and");
        Token token = lexer.nextToken();
        assertEquals(TokenType.AND, token.getTokenType());
        assertEquals("and", token.getText());
    }

    @Test
    public void testNextToken_Or() {
        XPathLexer lexer = new XPathLexer("or");
        Token token = lexer.nextToken();
        assertEquals(TokenType.OR, token.getTokenType());
        assertEquals("or", token.getText());
    }

    @Test
    public void testNextToken_Comma() {
        XPathLexer lexer = new XPathLexer(",");
        Token token = lexer.nextToken();
        assertEquals(TokenType.COMMA, token.getTokenType());
        assertEquals(",", token.getText());
    }

    @Test
    public void testNextToken_Dollar() {
        XPathLexer lexer = new XPathLexer("$");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOLLAR, token.getTokenType());
        assertEquals("$", token.getText());
    }

    @Test
    public void testNextToken_Pipe() {
        XPathLexer lexer = new XPathLexer("|");
        Token token = lexer.nextToken();
        assertEquals(TokenType.PIPE, token.getTokenType());
        assertEquals("|", token.getText());
    }

    @Test
    public void testNextToken_At() {
        XPathLexer lexer = new XPathLexer("@");
        Token token = lexer.nextToken();
        assertEquals(TokenType.AT, token.getTokenType());
        assertEquals("@", token.getText());
    }

    @Test
    public void testNextToken_Colon() {
        XPathLexer lexer = new XPathLexer(":");
        Token token = lexer.nextToken();
        assertEquals(TokenType.COLON, token.getTokenType());
        assertEquals(":", token.getText());
    }

    @Test
    public void testNextToken_DoubleColon() {
        XPathLexer lexer = new XPathLexer("::");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOUBLE_COLON, token.getTokenType());
        assertEquals("::", token.getText());
    }

    @Test
    public void testNextToken_Literal() {
        XPathLexer lexer = new XPathLexer("'foo'");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LITERAL, token.getTokenType());
        assertEquals("'foo'", token.getText());
    }

    @Test
    public void testNextToken_Dot() {
        XPathLexer lexer = new XPathLexer(".");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOTS, token.getTokenType());
        assertEquals(".", token.getText());
    }

    @Test
    public void testNextToken_LeftBracket() {
        XPathLexer lexer = new XPathLexer("[");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
        assertEquals("[", token.getText());
    }

    @Test
    public void testNextToken_RightBracket() {
        XPathLexer lexer = new XPathLexer("]");
        Token token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
        assertEquals("]", token.getText());
    }

    @Test
    public void testNextToken_LeftParen() {
        XPathLexer lexer = new XPathLexer("(");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LEFT_PAREN, token.getTokenType());
        assertEquals("(", token.getText());
    }

    @Test
    public void testNextToken_RightParen() {
        XPathLexer lexer = new XPathLexer(")");
        Token token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_PAREN, token.getTokenType());
        assertEquals(")", token.getText());
    }

    @Test
    public void testNextToken_Star() {
        XPathLexer lexer = new XPathLexer("*");
        Token token = lexer.nextToken();
        assertEquals(TokenType.STAR, token.getTokenType());
        assertEquals("*", token.getText());
    }
}