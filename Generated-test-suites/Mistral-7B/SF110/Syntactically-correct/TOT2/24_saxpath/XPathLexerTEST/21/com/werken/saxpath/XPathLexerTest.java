// Test file for XPathLexer class

package com.werken.saxpath;

import static org.junit.Assert.*;
import org.junit.Test;

public class XPathLexerTest {

    @Test
    public void testNextToken_ValidXPath() {
        XPathLexer lexer = new XPathLexer("/a[1]/b");
        Token token = lexer.nextToken();

        assertNotNull(token);
        assertEquals(TokenType.SLASHES, token.getTokenType());
    }

    @Test
    public void testNextToken_EmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();

        assertNotNull(token);
        assertEquals(TokenType.WHITESPACE, token.getTokenType());
    }

    @Test
    public void testNextToken_InvalidXPath() {
        XPathLexer lexer = new XPathLexer("//a[1]%#/b[1]");
        Token token = lexer.nextToken();

        assertNotNull(token);
        assertEquals(TokenType.ERROR, token.getTokenType());
    }

    @Test
    public void testNextToken_EndOfString() {
        XPathLexer lexer = new XPathLexer("/a[1]/b");
        Token token = lexer.nextToken();

        assertNotNull(token);
        assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test
    public void testNextToken_UnexpectedCharacters() {
        XPathLexer lexer = new XPathLexer("/a[1]/b");
        lexer.consume(); // consume the '/' character
        Token token = lexer.nextToken();

        assertNotNull(token);
        assertEquals(TokenType.ERROR, token.getTokenType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextToken_NullXPath() {
        XPathLexer lexer = new XPathLexer(null);
        lexer.nextToken();
    }
}

// Test file for XPathLexer class ends here