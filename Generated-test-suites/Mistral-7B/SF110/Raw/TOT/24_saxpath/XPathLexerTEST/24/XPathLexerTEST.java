package com.werken.saxpath;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XPathLexerTest {

    private XPathLexer lexer;

    @Before
    public void setUp() {
        this.lexer = new XPathLexer("");
    }

    @After
    public void tearDown() {
        this.lexer = null;
    }

    @Test
    public void testEmptyXPath() {
        assertEquals(new Token(-2, "", 0, 0), this.lexer.nextToken());
    }

    @Test
    public void testSingleCharacterXPath() {
        this.lexer.setXPath("a");
        Token token = this.lexer.nextToken();
        assertEquals(15, token.getTokenType());
        assertEquals("a", token.getValue());
    }

    @Test
    public void testIdentifierXPath() {
        this.lexer.setXPath("myIdentifier");
        Token token = this.lexer.nextToken();
        assertEquals(15, token.getTokenType());
        assertEquals("myIdentifier", token.getValue());
    }

    @Test
    public void testOperatorXPath() {
        this.lexer.setXPath("and");
        Token token = this.lexer.nextToken();
        assertEquals(28, token.getTokenType());
        assertEquals("and", token.getValue());
    }

    @Test
    public void testNumberXPath() {
        this.lexer.setXPath("123");
        Token token = this.lexer.nextToken();
        assertEquals(31, token.getTokenType());
        assertEquals("123", token.getValue());
    }

    @Test
    public void testNumberWithDecimalXPath() {
        this.lexer.setXPath("123.45");
        Token token = this.lexer.nextToken();
        assertEquals(30, token.getTokenType());
        assertEquals("123.45", token.getValue());
    }

    @Test
    public void testWhitespaceXPath() {
        this.lexer.setXPath("   \t xpath \r\n");
        Token token = this.lexer.nextToken();
        assertEquals(-2, token.getTokenType());
        assertEquals("", token.getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidXPath() {
        this.lexer.setXPath("invalidXPath");
        this.lexer.nextToken();
    }

    @Test
    public void testXPathWithMissingToken() {
        this.lexer.setXPath("a b");
        Token token = this.lexer.nextToken();
        assertEquals(15, token.getTokenType());
        assertEquals("a", token.getValue());
        Token nextToken = this.lexer.nextToken();
        assertEquals(15, nextToken.getTokenType());
        assertEquals("b", nextToken.getValue());
    }

    @Test
    public void testXPathWithComment() {
        this.lexer.setXPath("//comment()");
        Token token = this.lexer.nextToken();
        assertEquals(11, token.getTokenType());
        assertEquals("/", token.getValue());
        Token nextToken = this.lexer.nextToken();
        assertEquals(13, nextToken.getTokenType());
        assertEquals("comment()", nextToken.getValue());
    }

    @Test
    public void testXPathWithFunction() {
        this.lexer.setXPath("count(.)");
        Token token = this.lexer.nextToken();
        assertEquals(11, token.getTokenType());
        assertEquals("/", token.getValue());
        Token nextToken = this.lexer.nextToken();
        assertEquals(3, nextToken.getTokenType());
        Token nextNextToken = this.lexer.nextToken();
        assertEquals(19, nextNextToken.getTokenType());
        assertEquals("count", nextNextToken.getValue());
        Token nextNextNextToken = this.lexer.nextToken();
        assertEquals(15, nextNextNextToken.getTokenType());
        assertEquals(".", nextNextNextToken.getValue());
    }
}