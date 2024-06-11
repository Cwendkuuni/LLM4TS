package com.werken.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XPathLexerTest {

    @Test
    public void testXPathLexerEmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testXPathLexerSimpleXPath() {
        String xpath = "/a/b";
        XPathLexer lexer = new XPathLexer(xpath);

        Token token = null;
        while (token == null) {
            token = lexer.nextToken();
        }

        assertEquals(TokenType.SLASHES, token.getTokenType());
        assertEquals("/", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASHES, token.getTokenType());
        assertEquals("/", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("b", token.getText());

        assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
    }

    @Test
    public void testXPathLexerXPathWithNumber() {
        String xpath = "/a[1]";
        XPathLexer lexer = new XPathLexer(xpath);

        Token token = null;
        while (token == null) {
            token = lexer.nextToken();
        }

        assertEquals(TokenType.SLASHES, token.getTokenType());
        assertEquals("/", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.OPEN_BRACKET, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("1", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.CLOSE_BRACKET, token.getTokenType());

        assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
    }

    @Test
    public void testXPathLexerXPathWithOperator() {
        String xpath = "a/b=c";
        XPathLexer lexer = new XPathLexer(xpath);

        Token token = null;
        while (token == null) {
            token = lexer.nextToken();
        }

        assertEquals(TokenType.SLASHES, token.getTokenType());
        assertEquals("/", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASHES, token.getTokenType());
        assertEquals("/", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("b", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());
        assertEquals("=", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("c", token.getText());

        assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
    }

    @Test
    public void testXPathLexerXPathWithQuotes() {
        String xpath = "/a[contains(.,'xyz')]";
        XPathLexer lexer = new XPathLexer(xpath);

        Token token = null;
        while (token == null) {
            token = lexer.nextToken();
        }

        assertEquals(TokenType.SLASHES, token.getTokenType());
        assertEquals("/", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.OPEN_BRACKET, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.LEFT_PAREN, token.getTokenType());
        assertEquals(".", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.COMMA, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.QUOTE, token.getTokenType());
        assertEquals("'", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("xyz", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.QUOTE, token.getTokenType());
        assertEquals("'", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_PAREN, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.CLOSE_BRACKET, token.getTokenType());

        assertEquals(TokenType.EOF, lexer.nextToken().getTokenType());
    }
}