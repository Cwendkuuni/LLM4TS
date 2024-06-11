package com.werken.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.werken.saxpath.XPathLexer;
import com.werken.saxpath.Token;

public class XPathLexerTest {

    @Test
    public void testEmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testSimpleIdentifier() {
        XPathLexer lexer = new XPathLexer("abc");
        Token token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("abc", token.getValue());
    }

    @Test
    public void testSimpleOperator() {
        XPathLexer lexer = new XPathLexer("+");
        Token token = lexer.nextToken();
        assertEquals(TokenType.PLUS, token.getTokenType());
    }

    @Test
    public void testSimpleSlash() {
        XPathLexer lexer = new XPathLexer("/");
        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
    }

    @Test
    public void testSimpleDot() {
        XPathLexer lexer = new XPathLexer(".");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOT, token.getTokenType());
    }

    @Test
    public void testSimpleNumber() {
        XPathLexer lexer = new XPathLexer("123");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("123", token.getValue());
    }

    @Test
    public void testSimpleString() {
        XPathLexer lexer = new XPathLexer("'abc'");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LITERAL, token.getTokenType());
        assertEquals("abc", token.getValue());
    }

    @Test
    public void testSimpleQuotedString() {
        XPathLexer lexer = new XPathLexer("\"abc\"");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LITERAL, token.getTokenType());
        assertEquals("abc", token.getValue());
    }

    @Test
    public void testMultipleTokens() {
        XPathLexer lexer = new XPathLexer("a + b / c.d");
        Token token;
        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.PLUS, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("b", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("c", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.DOT, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("d", token.getValue());

        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testIdentifierFollowedByOperator() {
        XPathLexer lexer = new XPathLexer("a or b");
        Token token;

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.OR, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("b", token.getValue());

        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testOperatorFollowedByIdentifier() {
        XPathLexer lexer = new XPathLexer("or a");
        Token token;

        token = lexer.nextToken();
        assertEquals(TokenType.OR, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getValue());

        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testIdentifiersWithDifferentNames() {
        XPathLexer lexer = new XPathLexer("a b c");
        Token token;

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("b", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("c", token.getValue());

        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testIdentifiersWithSameName() {
        XPathLexer lexer = new XPathLexer("a a");
        Token token;

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getValue());

        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testSingleCharacterOperator() {
        XPathLexer lexer = new XPathLexer("= +");
        Token token;

        token = lexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.PLUS, token.getTokenType());

        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testStringWithQuotes() {
        XPathLexer lexer = new XPathLexer("'abc'def");
        Token token;

        token = lexer.nextToken();
        assertEquals(TokenType.LITERAL, token.getTokenType());
        assertEquals("abc", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("def", token.getValue());

        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testStringWithQuotesAndEscapedQuotes() {
        XPathLexer lexer = new XPathLexer("'abc\"def'");
        Token token;

        token = lexer.nextToken();
        assertEquals(TokenType.LITERAL, token.getTokenType());
        assertEquals("abc\"def", token.getValue());

        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testMultiCharacterOperator() {
        XPathLexer lexer = new XPathLexer("==");
        Token token;

        token = lexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());

        token = lexer.nextToken();
        assertNull(token);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidXPath() {
        XPathLexer lexer = new XPathLexer("a/b=c");
        Token token = lexer.nextToken();
    }
}