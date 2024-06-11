package com.werken.saxpath;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XPathLexerTest {

    private XPathLexer lexer;
    private String xpath;

    @Before
    public void setUp() {
        xpath = "//a[1]";
        lexer = new XPathLexer(xpath);
    }

    @Test
    public void testNextToken_EmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertEquals(-2, token.getTokenType());
    }

    @Test
    public void testNextToken_SimpleXPath() {
        Token token;

        token = lexer.nextToken();
        assertEquals(15, token.getTokenType());
        assertEquals("//", token.getValue());

        token = lexer.nextToken();
        assertEquals(2, token.getTokenType());
        assertEquals("a", token.getValue());

        token = lexer.nextToken();
        assertEquals(1, token.getTokenType());
        assertEquals("[1]", token.getValue());

        token = lexer.nextToken();
        assertEquals(-2, token.getTokenType());
    }

    @Test
    public void testNextToken_ComplexXPath() {
        xpath = "/a[b='c' and c>5]/d[e>3 and f='g']/h[@i='j']";

        Token token;

        token = lexer.nextToken();
        assertEquals(15, token.getTokenType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(2, token.getTokenType());
        assertEquals("a", token.getValue());

        token = lexer.nextToken();
        assertEquals(9, token.getTokenType());
        assertEquals("b", token.getValue());

        token = lexer.nextToken();
        assertEquals(16, token.getTokenType());
        assertEquals("'", token.getValue());

        token = lexer.nextToken();
        assertEquals(3, token.getTokenType());
        assertEquals("c", token.getValue());

        token = lexer.nextToken();
        assertEquals(16, token.getTokenType());
        assertEquals("'", token.getValue());

        token = lexer.nextToken();
        assertEquals(2, token.getTokenType());
        assertEquals("=", token.getValue());

        token = lexer.nextToken();
        assertEquals(3, token.getTokenType());
        assertEquals("c", token.getValue());

        token = lexer.nextToken();
        assertEquals(6, token.getTokenType());
        assertEquals(">", token.getValue());

        token = lexer.nextToken();
        assertEquals(5, token.getTokenType());
        assertEquals("d", token.getValue());

        token = lexer.nextToken();
        assertEquals(9, token.getTokenType());
        assertEquals("e", token.getValue());

        token = lexer.nextToken();
        assertEquals(6, token.getTokenType());
        assertEquals(">", token.getValue());

        token = lexer.nextToken();
        assertEquals(3, token.getTokenType());
        assertEquals("f", token.getValue());

        token = lexer.nextToken();
        assertEquals(16, token.getTokenType());
        assertEquals("'", token.getValue());

        token = lexer.nextToken();
        assertEquals(3, token.getTokenType());
        assertEquals("g", token.getValue());

        token = lexer.nextToken();
        assertEquals(16, token.getTokenType());
        assertEquals("'", token.getValue());

        token = lexer.nextToken();
        assertEquals(2, token.getTokenType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(2, token.getTokenType());
        assertEquals("h", token.getValue());

        token = lexer.nextToken();
        assertEquals(9, token.getTokenType());
        assertEquals("i", token.getValue());

        token = lexer.nextToken();
        assertEquals(16, token.getTokenType());
        assertEquals("'", token.getValue());

        token = lexer.nextToken();
        assertEquals(3, token.getTokenType());
        assertEquals("j", token.getValue());

        token = lexer.nextToken();
        assertEquals(-2, token.getTokenType());
    }

    @Test
    public void testIdentifierOrOperatorName() {
        lexer.setXPath("a:b");

        Token token = lexer.nextToken();
        assertEquals(19, token.getTokenType());
        assertEquals(":b", token.getValue());
    }

    @After
    public void tearDown() {
        lexer = null;
        xpath = null;
    }
}