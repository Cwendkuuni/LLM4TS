package com.werken.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XPathLexerTest {

    @Test
    public void testNextTokenEmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        assertNull(lexer.nextToken());
    }

    @Test
    public void testNextTokenSingleCharacterXPath() {
        XPathLexer lexer = new XPathLexer("a");
        Token token = lexer.nextToken();
        assertEquals(1, token.getType());
        assertEquals("a", token.getValue());
    }

    @Test
    public void testNextTokenMultipleCharacterXPath() {
        XPathLexer lexer = new XPathLexer("abcd");
        Token token1 = lexer.nextToken();
        assertEquals(1, token1.getType());
        assertEquals("a", token1.getValue());
        Token token2 = lexer.nextToken();
        assertEquals(1, token2.getType());
        assertEquals("b", token2.getValue());
        Token token3 = lexer.nextToken();
        assertEquals(1, token3.getType());
        assertEquals("c", token3.getValue());
        Token token4 = lexer.nextToken();
        assertEquals(1, token4.getType());
        assertEquals("d", token4.getValue());
        assertNull(lexer.nextToken());
    }

    @Test
    public void testNextTokenWhitespaceXPath() {
        XPathLexer lexer = new XPathLexer("   a   ");
        Token token = lexer.nextToken();
        assertEquals(1, token.getType());
        assertEquals("a", token.getValue());
    }

    @Test
    public void testNextTokenSingleCharacterOperators() {
        XPathLexer lexer = new XPathLexer("a/b");
        Token token1 = lexer.nextToken();
        assertEquals(11, token1.getType());
        assertEquals("/", token1.getValue());
        Token token2 = lexer.nextToken();
        assertEquals(1, token2.getType());
        assertEquals("b", token2.getValue());
    }

    @Test
    public void testNextTokenMultipleCharacterOperators() {
        XPathLexer lexer = new XPathLexer("a//b/c");
        Token token1 = lexer.nextToken();
        assertEquals(11, token1.getType());
        assertEquals("/", token1.getValue());
        Token token2 = lexer.nextToken();
        assertEquals(1, token2.getType());
        assertEquals("a", token2.getValue());
        Token token3 = lexer.nextToken();
        assertEquals(3, token3.getType());
        assertEquals("//", token3.getValue());
        Token token4 = lexer.nextToken();
        assertEquals(1, token4.getType());
        assertEquals("b", token4.getValue());
        Token token5 = lexer.nextToken();
        assertEquals(11, token5.getType());
        assertEquals("/", token5.getValue());
        Token token6 = lexer.nextToken();
        assertEquals(1, token6.getType());
        assertEquals("c", token6.getValue());
        assertNull(lexer.nextToken());
    }

    @Test
    public void testNextTokenIdentifiers() {
        XPathLexer lexer = new XPathLexer("a:b:c");
        Token token1 = lexer.nextToken();
        assertEquals(18, token1.getType());
        assertEquals("a", token1.getValue());
        Token token2 = lexer.nextToken();
        assertEquals(19, token2.getType());
        assertEquals(":b:", token2.getValue());
        Token token3 = lexer.nextToken();
        assertEquals(1, token3.getType());
        assertEquals("c", token3.getValue());
        assertNull(lexer.nextToken());
    }

    @Test
    public void testNextTokenDollarSign() {
        XPathLexer lexer = new XPathLexer("$foo");
        Token token = lexer.nextToken();
        assertEquals(26, token.getType());
        assertEquals("$", token.getValue());
        Token nextToken = lexer.nextToken();
        assertEquals(1, nextToken.getType());
        assertEquals("foo", nextToken.getValue());
    }

    @Test
    public void testNextTokenQuotedString() {
        XPathLexer lexer = new XPathLexer("'foo'");
        Token token = lexer.nextToken();
        assertEquals(27, token.getType());
        assertEquals("'foo'", token.getValue());
    }

    @Test
    public void testNextTokenDoubleQuotedString() {
        XPathLexer lexer = new XPathLexer("\"foo\"");
        Token token = lexer.nextToken();
        assertEquals(27, token.getType());
        assertEquals("\"foo\"", token.getValue());
    }

    @Test
    public void testNextTokenNumber() {
        XPathLexer lexer = new XPathLexer("123");
        Token token = lexer.nextToken();
        assertEquals(31, token.getType());
        assertEquals("123", token.getValue());
    }

    @Test
    public void testNextTokenNumberWithDecimalPoint() {
        XPathLexer lexer = new XPathLexer("123.45");
        Token token = lexer.nextToken();
        assertEquals(30, token.getType());
        assertEquals("123.45", token.getValue());
    }

    @Test
    public void testNextTokenComma() {
        XPathLexer lexer = new XPathLexer(",");
        Token token = lexer.nextToken();
        assertEquals(32, token.getType());
        assertEquals(",", token.getValue());
    }

    @Test
    public void testNextTokenLeftParenthesis() {
        XPathLexer lexer = new XPathLexer("(");
        Token token = lexer.nextToken();
        assertEquals(1, token.getType());
        assertEquals("(", token.getValue());
    }

    @Test
    public void testNextTokenRightParenthesis() {
        XPathLexer lexer = new XPathLexer(")");
        Token token = lexer.nextToken();
        assertEquals(2, token.getType());
        assertEquals(")", token.getValue());
    }

    @Test
    public void testNextTokenLeftBracket() {
        XPathLexer lexer = new XPathLexer("[");
        Token token = lexer.nextToken();
        assertEquals(3, token.getType());
        assertEquals("[", token.getValue());
    }

    @Test
    public void testNextTokenRightBracket() {
        XPathLexer lexer = new XPathLexer("]");
        Token token = lexer.nextToken();
        assertEquals(4, token.getType());
        assertEquals("]", token.getValue());
    }

    @Test
    public void testNextTokenMinus() {
        XPathLexer lexer = new XPathLexer("-");
        Token token = lexer.nextToken();
        assertEquals(6, token.getType());
        assertEquals("-", token.getValue());
    }

    @Test
    public void testNextTokenPlus() {
        XPathLexer lexer = new XPathLexer("+");
        Token token = lexer.nextToken();
        assertEquals(5, token.getType());
        assertEquals("+", token.getValue());
    }

    @Test
    public void testNextTokenAtSign() {
        XPathLexer lexer = new XPathLexer("@");
        Token token = lexer.nextToken();
        assertEquals(16, token.getType());
        assertEquals("@", token.getValue());
    }

    @Test
    public void testNextTokenColon() {
        XPathLexer lexer = new XPathLexer(":");
        Token token = lexer.nextToken();
        assertEquals(18, token.getType());
        assertEquals(":", token.getValue());
    }

    @Test
    public void testNextTokenDoubleColon() {
        XPathLexer lexer = new XPathLexer("::");
        Token token = lexer.nextToken();
        assertEquals(19, token.getType());
        assertEquals("::", token.getValue());
    }

    @Test
    public void testNextTokenNotOperator() {
        XPathLexer lexer = new XPathLexer("!");
        Token token = lexer.nextToken();
        assertEquals(23, token.getType());
        assertEquals("!", token.getValue());
    }

    @Test
    public void testNextTokenNotEqualsOperator() {
        XPathLexer lexer = new XPathLexer("!=");
        Token token = lexer.nextToken();
        assertEquals(22, token.getType());
        assertEquals("!=", token.getValue());
    }

    @Test
    public void testNextTokenEqualsOperator() {
        XPathLexer lexer = new XPathLexer("=");
        Token token = lexer.nextToken();
        assertEquals(21, token.getType());
        assertEquals("=", token.getValue());
    }

    @Test
    public void testNextTokenLessThanOperator() {
        XPathLexer lexer = new XPathLexer("<");
        Token token = lexer.nextToken();
        assertEquals(7, token.getType());
        assertEquals("<", token.getValue());
    }

    @Test
    public void testNextTokenGreaterThanOperator() {
        XPathLexer lexer = new XPathLexer(">");
        Token token = lexer.nextToken();
        assertEquals(9, token.getType());
        assertEquals(">", token.getValue());
    }

    @Test
    public void testNextTokenAndOperator() {
        XPathLexer lexer = new XPathLexer("and");
        Token token = lexer.nextToken();
        assertEquals(28, token.getType());
        assertEquals("and", token.getValue());
    }

    @Test
    public void testNextTokenOrOperator() {
        XPathLexer lexer = new XPathLexer("or");
        Token token = lexer.nextToken();
        assertEquals(29, token.getType());
        assertEquals("or", token.getValue());
    }

    @Test
    public void testNextTokenModOperator() {
        XPathLexer lexer = new XPathLexer("mod");
        Token token = lexer.nextToken();
        assertEquals(25, token.getType());
        assertEquals("mod", token.getValue());
    }

    @Test
    public void testNextTokenDivOperator() {
        XPathLexer lexer = new XPathLexer("div");
        Token token = lexer.nextToken();
        assertEquals(24, token.getType());
        assertEquals("div", token.getValue());
    }
}