package com.werken.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
    public void testBasicIdentifiers() {
        XPathLexer lexer = new XPathLexer("foo bar baz");

        Token token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("bar", token.getValue());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("baz", token.getValue());

        assertNull(lexer.nextToken());
    }

    @Test
    public void testForwardSlashes() {
        XPathLexer lexer = new XPathLexer("/foo/bar");

        Token token = lexer.nextToken();
        assertEquals(11, token.getType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(11, token.getType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("bar", token.getValue());

        assertNull(lexer.nextToken());
    }

    @Test
    public void testBackwardSlashes() {
        XPathLexer lexer = new XPathLexer("//foo//bar");

        Token token = lexer.nextToken();
        assertEquals(12, token.getType());
        assertEquals("//", token.getValue());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(12, token.getType());
        assertEquals("//", token.getValue());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("bar", token.getValue());

        assertNull(lexer.nextToken());
    }

    @Test
    public void testWhitespace() {
        XPathLexer lexer = new XPathLexer("   /foo   /bar  ");

        Token token = lexer.nextToken();
        assertEquals(11, token.getType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(11, token.getType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("bar", token.getValue());

        assertNull(lexer.nextToken());
    }

    @Test
    public void testSingleCharacterOperators() {
        XPathLexer lexer = new XPathLexer("//foo/@id");

        Token token = lexer.nextToken();
        assertEquals(12, token.getType());
        assertEquals("//", token.getValue());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(18, token.getType());
        assertEquals(":", token.getValue());

        token = lexer.nextToken();
        assertEquals(16, token.getType());
        assertEquals("@", token.getValue());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("id", token.getValue());

        assertNull(lexer.nextToken());
    }

    @Test
    public void testMultiCharacterOperators() {
        XPathLexer lexer = new XPathLexer("//foo/and(bar, baz)");

        Token token = lexer.nextToken();
        assertEquals(12, token.getType());
        assertEquals("//", token.getValue());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(28, token.getType());
        assertEquals("and", token.getValue());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("bar", token.getValue());

        token = lexer.nextToken();
        assertEquals(13, token.getType());
        assertEquals(",", token.getValue());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("baz", token.getValue());

        assertNull(lexer.nextToken());
    }

    @Test
    public void testNumbers() {
        XPathLexer lexer = new XPathLexer("1+2*3/4.0");

        Token token = lexer.nextToken();
        assertEquals(31, token.getType());
        assertEquals("1", token.getValue());

        token = lexer.nextToken();
        assertEquals(5, token.getType());
        assertEquals("+", token.getValue());

        token = lexer.nextToken();
        assertEquals(30, token.getType());
        assertEquals("2", token.getValue());

        token = lexer.nextToken();
        assertEquals(5, token.getType());
        assertEquals("*", token.getValue());

        token = lexer.nextToken();
        assertEquals(30, token.getType());
        assertEquals("3", token.getValue());

        token = lexer.nextToken();
        assertEquals(11, token.getType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(31, token.getType());
        assertEquals("4", token.getValue());

        token = lexer.nextToken();
        assertEquals(10, token.getType());
        assertEquals(".", token.getValue());

        token = lexer.nextToken();
        assertEquals(31, token.getType());
        assertEquals("4.0", token.getValue());

        assertNull(lexer.nextToken());
    }

    @Test
    public void testDollarSign() {
        XPathLexer lexer = new XPathLexer("//$foo");

        Token token = lexer.nextToken();
        assertEquals(12, token.getType());
        assertEquals("//", token.getValue());

        token = lexer.nextToken();
        assertEquals(26, token.getType());
        assertEquals("$", token.getValue());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());

        assertNull(lexer.nextToken());
    }

    @Test
    public void testParentheses() {
        XPathLexer lexer = new XPathLexer("(//foo)[1]");

        Token token = lexer.nextToken();
        assertEquals(3, token.getType());
        assertEquals("(", token.getValue());

        token = lexer.nextToken();
        assertEquals(12, token.getType());
        assertEquals("//", token.getValue());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(3, token.getType());
        assertEquals(")", token.getValue());

        token = lexer.nextToken();
        assertEquals(11, token.getType());
        assertEquals("[", token.getValue());

        token = lexer.nextToken();
        assertEquals(1, token.getType());
        assertEquals("1", token.getValue());

        token = lexer.nextToken();
        assertEquals(11, token.getType());
        assertEquals("]", token.getValue());

        assertNull(lexer.nextToken());
    }

    @Test
    public void testQuotedStrings() {
        XPathLexer lexer = new XPathLexer("//'foo'/bar");

        Token token = lexer.nextToken();
        assertEquals(11, token.getType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("'foo'", token.getValue());

        token = lexer.nextToken();
        assertEquals(11, token.getType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("bar", token.getValue());

        assertNull(lexer.nextToken());
    }

    @Test
    public void testQuotedStringsWithEscapedQuotes() {
        XPathLexer lexer = new XPathLexer("//'\''foo'\''/bar");

        Token token = lexer.nextToken();
        assertEquals(11, token.getType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("'\'", token.getValue());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("'\'", token.getValue());

        token = lexer.nextToken();
        assertEquals(11, token.getType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("bar", token.getValue());

        assertNull(lexer.nextToken());
    }
}