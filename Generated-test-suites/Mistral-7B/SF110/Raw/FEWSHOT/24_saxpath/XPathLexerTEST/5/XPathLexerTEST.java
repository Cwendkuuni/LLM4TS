package com.werken.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class XPathLexerTest {

    @Test
    public void testNextToken_EmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertEquals(TokenType.WHITESPACE, token.getTokenType());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_SimpleXPath() {
        XPathLexer lexer = new XPathLexer("/foo");
        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASHES, token.getTokenType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getValue());

        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_XPathWithOperator() {
        XPathLexer lexer = new XPathLexer("/foo/bar[@class='class']");

        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASHES, token.getTokenType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASHES, token.getTokenType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.AT, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("class", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.STRING_LITERAL, token.getTokenType());
        assertEquals("'class'", token.getValue());

        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_XPathWithComments() {
        XPathLexer lexer = new XPathLexer("/foo/*[comment()='test']");

        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASHES, token.getTokenType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.ASTERISK, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.COMMENT, token.getTokenType());
        assertEquals("test", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());

        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_XPathWithNumbers() {
        XPathLexer lexer = new XPathLexer("/foo[1][2]");

        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASHES, token.getTokenType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("1", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("2", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());

        assertNull(lexer.getPreviousToken());
    }
}