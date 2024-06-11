package com.werken.saxpath;

import org.junit.Test;
import org.junit.Assert.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XPathLexerTest {

    @Test
    public void testNextToken_EmptyString() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testNextToken_SimpleTokens() {
        XPathLexer lexer = new XPathLexer("/a/b[1]");

        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("b", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("1", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());

        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testNextToken_ComplexTokens() {
        XPathLexer lexer = new XPathLexer("/a[b=1 and c='test']/d[@e='foo'][f]/g[h='bar']/*[i]");

        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("b", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("1", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.AND, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("c", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.SINGLE_QUOTE, token.getTokenType());
        assertEquals("'test'", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("d", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.AT_SIGN, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("e", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.SINGLE_QUOTE, token.getTokenType());
        assertEquals("'foo'", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("f", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("g", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("h", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.SINGLE_QUOTE, token.getTokenType());
        assertEquals("'bar'", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.ASTERISK, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("i", token.getText());

        token = lexer.nextToken();
        assertNull(token);
    }
}