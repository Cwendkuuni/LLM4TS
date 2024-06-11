package com.werken.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class XPathLexerTest {

    @Test
    public void testNextToken_EmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertEquals(-2, token.getTokenType());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_Whitespace() {
        XPathLexer lexer = new XPathLexer("   ");
        Token token = lexer.nextToken();
        assertEquals(-2, token.getTokenType());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_Identifier() {
        XPathLexer lexer = new XPathLexer("myIdentifier");
        Token token = lexer.nextToken();
        assertEquals(15, token.getTokenType());
        assertEquals("myIdentifier", token.getText());
        assertEquals("myIdentifier", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(10, lexer.endPosition());
        assertEquals(myIdentifier, lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_IdentifierWithWhitespace() {
        XPathLexer lexer = new XPathLexer("myIdentifier   ");
        Token token = lexer.nextToken();
        assertEquals(15, token.getTokenType());
        assertEquals("myIdentifier", token.getText());
        assertEquals("myIdentifier", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(12, lexer.endPosition());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_OperatorName() {
        XPathLexer lexer = new XPathLexer("and");
        Token token = lexer.nextToken();
        assertEquals(28, token.getTokenType());
        assertEquals("and", token.getText());
        assertEquals("and", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(3, lexer.endPosition());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_OperatorNameWithWhitespace() {
        XPathLexer lexer = new XPathLexer(" and ");
        Token token = lexer.nextToken();
        assertEquals(28, token.getTokenType());
        assertEquals("and", token.getText());
        assertEquals("and", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(3, lexer.endPosition());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_SingleCharacterIdentifier() {
        XPathLexer lexer = new XPathLexer("a");
        Token token = lexer.nextToken();
        assertEquals(15, token.getTokenType());
        assertEquals("a", token.getText());
        assertEquals("a", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(1, lexer.endPosition());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_SingleCharacterOperatorName() {
        XPathLexer lexer = new XPathLexer("a");
        lexer.nextToken(); // consume identifier
        Token token = lexer.nextToken();
        assertEquals(2, token.getTokenType());
        assertEquals("", token.getText());
        assertEquals("a", lexer.getXPath());
        assertEquals(1, lexer.currentPosition());
        assertEquals(1, lexer.endPosition());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_MultipleCharacterIdentifier() {
        XPathLexer lexer = new XPathLexer("multiCharacterIdentifier");
        Token token = lexer.nextToken();
        assertEquals(15, token.getTokenType());
        assertEquals("multiCharacterIdentifier", token.getText());
        assertEquals("multiCharacterIdentifier", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(26, lexer.endPosition());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_Dollar() {
        XPathLexer lexer = new XPathLexer("$");
        Token token = lexer.nextToken();
        assertEquals(26, token.getTokenType());
        assertEquals("", token.getText());
        assertEquals("$", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(1, lexer.endPosition());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_Slashes() {
        XPathLexer lexer = new XPathLexer("/");
        Token token = lexer.nextToken();
        assertEquals(11, token.getTokenType());
        assertEquals("/", token.getText());
        assertEquals("/", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(1, lexer.endPosition());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_SlashesWithIdentifier() {
        XPathLexer lexer = new XPathLexer("/myIdentifier");
        Token token = lexer.nextToken();
        assertEquals(12, token.getTokenType());
        assertEquals("/myIdentifier", token.getText());
        assertEquals("/myIdentifier", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(11, lexer.endPosition());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_Comma() {
        XPathLexer lexer = new XPathLexer(",");
        Token token = lexer.nextToken();
        assertEquals(32, token.getTokenType());
        assertEquals(",", token.getText());
        assertEquals(",", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(1, lexer.endPosition());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_Equals() {
        XPathLexer lexer = new XPathLexer("=");
        Token token = lexer.nextToken();
        assertEquals(21, token.getTokenType());
        assertEquals("", token.getText());
        assertEquals("=", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(1, lexer.endPosition());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_Minus() {
        XPathLexer lexer = new XPathLexer("-");
        Token token = lexer.nextToken();
        assertEquals(6, token.getTokenType());
        assertEquals("", token.getText());
        assertEquals("-", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(1, lexer.endPosition());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_Plus() {
        XPathLexer lexer = new XPathLexer("+");
        Token token = lexer.nextToken();
        assertEquals(5, token.getTokenType());
        assertEquals("", token.getText());
        assertEquals("+", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(1, lexer.endPosition());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_Not() {
        XPathLexer lexer = new XPathLexer("!");
        Token token = lexer.nextToken();
        assertEquals(23, token.getTokenType());
        assertEquals("", token.getText());
        assertEquals("!", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(1, lexer.endPosition());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_NotEquals() {
        XPathLexer lexer = new XPathLexer("!=");
        Token token = lexer.nextToken();
        assertEquals(22, token.getTokenType());
        assertEquals("!", token.getText());
        assertEquals("!", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(2, lexer.endPosition());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_RelationalOperator() {
        XPathLexer lexer = new XPathLexer("<");
        Token token = lexer.nextToken();
        assertEquals(7, token.getTokenType());
        assertEquals("<", token.getText());
        assertEquals("<", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(1, lexer.endPosition());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_RelationalOperatorWithEquals() {
        XPathLexer lexer = new XPathLexer("<=");
        Token token = lexer.nextToken();
        assertEquals(8, token.getTokenType());
        assertEquals("<=", token.getText());
        assertEquals("<=", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(2, lexer.endPosition());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_RelationalOperatorWithGreater() {
        XPathLexer lexer = new XPathLexer(">=");
        Token token = lexer.nextToken();
        assertEquals(10, token.getTokenType());
        assertEquals(">=", token.getText());
        assertEquals(">=", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(2, lexer.endPosition());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_Star() {
        XPathLexer lexer = new XPathLexer("*");
        Token token = lexer.nextToken();
        assertEquals(20, token.getTokenType());
        assertEquals("", token.getText());
        assertEquals("*", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(1, lexer.endPosition());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_Literal() {
        XPathLexer lexer = new XPathLexer("'single character literal'");
        Token token = lexer.nextToken();
        assertEquals(27, token.getTokenType());
        assertEquals("single character literal", token.getText());
        assertEquals("single character literal", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(31, lexer.endPosition());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_LiteralWithQuotes() {
        XPathLexer lexer = new XPathLexer("'multi character literal'");
        Token token = lexer.nextToken();
        assertEquals(27, token.getTokenType());
        assertEquals("multi character literal", token.getText());
        assertEquals("multi character literal", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(32, lexer.endPosition());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_LeftBracket() {
        XPathLexer lexer = new XPathLexer("[");
        Token token = lexer.nextToken();
        assertEquals(3, token.getTokenType());
        assertEquals("", token.getText());
        assertEquals("[", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(1, lexer.endPosition());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_RightBracket() {
        XPathLexer lexer = new XPathLexer("]");
        Token token = lexer.nextToken();
        assertEquals(4, token.getTokenType());
        assertEquals("", token.getText());
        assertEquals("]", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(1, lexer.endPosition());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_LeftParen() {
        XPathLexer lexer = new XPathLexer("(");
        Token token = lexer.nextToken();
        assertEquals(1, token.getTokenType());
        assertEquals("", token.getText());
        assertEquals("(", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(1, lexer.endPosition());
        assertNull(lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_RightParen() {
        XPathLexer lexer = new XPathLexer(")");
        Token token = lexer.nextToken();
        assertEquals(2, token.getTokenType());
        assertEquals("", token.getText());
        assertEquals(")", lexer.getXPath());
        assertEquals(0, lexer.currentPosition());
        assertEquals(1, lexer.endPosition());
        assertNull(lexer.getPreviousToken());
    }
}