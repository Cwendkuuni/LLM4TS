package com.werken.saxpath;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class XPathLexerTest {

    private XPathLexer lexer;

    @Before
    public void setUp() {
        lexer = new XPathLexer("");
    }

    @Test
    public void testNextToken_EmptyXPath() {
        assertNull(lexer.nextToken());
    }

    @Test
    public void testNextToken_SimpleXPath() {
        lexer.setXPath("/a[1]");
        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
    }

    @Test
    public void testIdentifierOrOperatorName() {
        lexer.setXPath("a b c d e f g h i j k l m n o p q r s t u v w x y z a and or mod div");
        Token token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.AND, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.OR, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.MOD, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.DIV, token.getTokenType());
    }

    @Test
    public void testNumber() {
        lexer.setXPath("123.456");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("123.456", token.getText());

        lexer.setXPath("123");
        token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("123", token.getText());
    }

    @Test
    public void testWhitespace() {
        lexer.setXPath("   /a[1]   ");
        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
    }

    @Test(expected = AssertionError.class)
    public void testUnexpectedCharacter() {
        lexer.setXPath("x%");
        lexer.nextToken(); // should be SLASH or IDENTIFIER or OPERATOR_NAME
        fail("Unexpected character % found");
    }

    // Add more test cases for other methods and edge cases here.
}