package com.werken.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.werken.saxpath.XPathLexer;
import com.werken.saxpath.Token;

public class XPathLexerTest {

    @Test
    public void testNextToken_emptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testNextToken_simpleIdentifier() {
        XPathLexer lexer = new XPathLexer("foo");
        Token token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getText());
    }

    @Test
    public void testNextToken_simpleOperator() {
        XPathLexer lexer = new XPathLexer("+");
        Token token = lexer.nextToken();
        assertEquals(TokenType.PLUS, token.getTokenType());
    }

    @Test
    public void testNextToken_simpleLiteral() {
        XPathLexer lexer = new XPathLexer("'foo'");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LITERAL, token.getTokenType());
        assertEquals("foo", token.getText());
    }

    @Test
    public void testNextToken_simpleWhiteSpace() {
        XPathLexer lexer = new XPathLexer("   ");
        Token token = lexer.nextToken();
        assertEquals(TokenType.WHITESPACE, token.getTokenType());
    }

    @Test
    public void testNextToken_simpleOperatorAndIdentifier() {
        XPathLexer lexer = new XPathLexer("foo +");
        Token previousToken = null;
        Token token = null;
        do {
            token = lexer.nextToken();
            if (token != null) {
                previousToken = token;
            }
        } while (token.getTokenType() != TokenType.PLUS);
        assertEquals(TokenType.PLUS, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getText());
        assertSame(previousToken, token);
    }

    @Test
    public void testNextToken_complexXPath() {
        XPathLexer lexer = new XPathLexer("//foo[@bar='baz']/qux[1]");
        Token previousToken = null;
        Token token = null;
        do {
            token = lexer.nextToken();
            if (token != null) {
                previousToken = token;
            }
        } while (token.getTokenType() != TokenType.SLASHES);
        assertEquals(TokenType.SLASHES, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getText());
        assertSame(token, previousToken);
        token = lexer.nextToken();
        assertEquals(TokenType.AT, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("bar", token.getText());
        assertSame(token, previousToken);
        token = lexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("baz", token.getText());
        assertSame(token, previousToken);
        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("qux", token.getText());
        assertSame(token, previousToken);
        token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("1", token.getText());
        assertSame(token, previousToken);
        assertNull(lexer.nextToken());
    }
}