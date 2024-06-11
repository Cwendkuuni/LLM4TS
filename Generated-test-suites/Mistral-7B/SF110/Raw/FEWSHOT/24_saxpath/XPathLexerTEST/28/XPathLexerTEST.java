package com.werken.saxpath;

import static org.junit.Assert.*;

import org.junit.Test;

public class XPathLexerTest {

    @Test
    public void testEmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        assertEquals(null, lexer.nextToken());
    }

    @Test
    public void testSimpleIdentifier() {
        XPathLexer lexer = new XPathLexer("foo");
        assertEquals(new Token(15, "", 0, 3), lexer.nextToken());
    }

    @Test
    public void testSimpleOperator() {
        XPathLexer lexer = new XPathLexer(">");
        assertEquals(new Token(9, "", 0, 1), lexer.nextToken());
    }

    @Test
    public void testSimpleOperatorWithIdentifier() {
        XPathLexer lexer = new XPathLexer("foo >");
        assertEquals(new Token(15, "", 0, 3), lexer.nextToken());
        assertEquals(new Token(9, "", 3, 4), lexer.nextToken());
    }

    @Test
    public void testSimpleNumber() {
        XPathLexer lexer = new XPathLexer("1");
        assertEquals(new Token(31, "", 0, 1), lexer.nextToken());
    }

    @Test
    public void testSimpleNumberWithIdentifier() {
        XPathLexer lexer = new XPathLexer("foo 1");
        assertEquals(new Token(15, "", 0, 3), lexer.nextToken());
        assertEquals(new Token(31, "", 3, 4), lexer.nextToken());
    }

    @Test
    public void testSimpleString() {
        XPathLexer lexer = new XPathLexer("'foo'");
        assertEquals(new Token(27, "", 0, 6), lexer.nextToken());
    }

    @Test
    public void testComplexXPath() {
        XPathLexer lexer = new XPathLexer("/foo/bar[1]/baz@attr='value'");
        Token token;

        token = new Token(11, "", 0, 1);
        assertEquals(token, lexer.nextToken());

        token = new Token(15, "", 1, 5);
        assertEquals(token, lexer.nextToken());

        token = new Token(11, "", 5, 6);
        assertEquals(token, lexer.nextToken());

        token = new Token(15, "", 6, 10);
        assertEquals(token, lexer.nextToken());

        token = new Token(12, "", 10, 13);
        assertEquals(token, lexer.nextToken());

        token = new Token(15, "", 13, 16);
        assertEquals(token, lexer.nextToken());

        token = new Token(6, "", 16, 17);
        assertEquals(token, lexer.nextToken());

        token = new Token(18, "", 17, 19);
        assertEquals(token, lexer.nextToken());

        token = new Token(15, "", 19, 22);
        assertEquals(token, lexer.nextToken());

        token = new Token(16, "", 22, 24);
        assertEquals(token, lexer.nextToken());

        token = new Token(21, "", 24, 25);
        assertEquals(token, lexer.nextToken());

        assertEquals(null, lexer.nextToken());
    }

    @Test
    public void testComplexXPathWithWhitespace() {
        XPathLexer lexer = new XPathLexer("/foo /bar [1]/baz @ attr = 'value'");
        Token token;

        token = new Token(11, "", 0, 1);
        assertEquals(token, lexer.nextToken());

        token = new Token(15, "", 1, 5);
        assertEquals(token, lexer.nextToken());

        token = new Token(32, "", 5, 6);
        assertEquals(token, lexer.nextToken());

        token = new Token(11, "", 6, 7);
        assertEquals(token, lexer.nextToken());

        token = new Token(15, "", 7, 11);
        assertEquals(token, lexer.nextToken());

        token = new Token(12, "", 11, 13);
        assertEquals(token, lexer.nextToken());

        token = new Token(63, "", 13, 14);
        assertEquals(token, lexer.nextToken());

        token = new Token(32, "", 14, 15);
        assertEquals(token, lexer.nextToken());

        token = new Token(15, "", 15, 18);
        assertEquals(token, lexer.nextToken());

        token = new Token(16, "", 18, 20);
        assertEquals(token, lexer.nextToken());

        token = new Token(6, "", 20, 21);
        assertEquals(token, lexer.nextToken());

        token = new Token(18, "", 21, 23);
        assertEquals(token, lexer.nextToken());

        token = new Token(15, "", 23, 26);
        assertEquals(token, lexer.nextToken());

        token = new Token(16, "", 26, 28);
        assertEquals(token, lexer.nextToken());

        token = new Token(21, "", 28, 29);
        assertEquals(token, lexer.nextToken());

        token = new Token(27, "", 29, 33);
        assertEquals(token, lexer.nextToken());

        assertEquals(null, lexer.nextToken());
    }
}