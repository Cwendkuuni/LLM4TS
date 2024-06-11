package com.werken.saxpath;

import org.junit.Test;
import static org.junit.Assert.*;

public class XPathLexerTest {

    @Test
    public void testNextToken_EmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertEquals(-1, token.getTokenType());
    }

    @Test
    public void testNextToken_SingleCharacterXPath() {
        XPathLexer lexer = new XPathLexer("a");
        Token token = lexer.nextToken();
        assertEquals(15, token.getTokenType());
    }

    @Test
    public void testNextToken_MultipleCharactersXPath() {
        XPathLexer lexer = new XPathLexer("abc");
        Token token = lexer.nextToken();
        assertEquals(15, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(-1, token.getTokenType());
    }

    @Test
    public void testNextToken_SpecialCharacterXPath() {
        XPathLexer lexer = new XPathLexer("/");
        Token token = lexer.nextToken();
        assertEquals(11, token.getTokenType());

        lexer.setXPath("//");
        token = lexer.nextToken();
        assertEquals(11, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType());

        lexer.setXPath("//a");
        token = lexer.nextToken();
        assertEquals(11, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType());

        lexer.setXPath("//a[1]");
        token = lexer.nextToken();
        assertEquals(11, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(5, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType());
    }

    @Test(expected = AssertionError.class)
    public void testNextToken_InvalidXPath() {
        XPathLexer lexer = new XPathLexer("//a[1]x");
        lexer.nextToken();
    }

    @Test
    public void testIdentifierOrOperatorName_NoPreviousToken() {
        XPathLexer lexer = new XPathLexer("a");
        Token token = lexer.identifierOrOperatorName();
        assertEquals(15, token.getTokenType());
    }

    @Test
    public void testIdentifierOrOperatorName_PreviousTokenIsIdentifier() {
        XPathLexer lexer = new XPathLexer("a b");
        Token previousToken = new Token(15, "", 0, 0);
        lexer.setPreviousToken(previousToken);
        Token token = lexer.identifierOrOperatorName();
        assertEquals(15, token.getTokenType());
    }

    @Test
    public void testIdentifierOrOperatorName_PreviousTokenIsOperator() {
        XPathLexer lexer = new XPathLexer("+ a");
        Token previousToken = new Token(5, "", 0, 0);
        lexer.setPreviousToken(previousToken);
        Token token = lexer.identifierOrOperatorName();
        assertEquals(15, token.getTokenType());
    }

    // Add more test cases for other methods in the XPathLexer class
}