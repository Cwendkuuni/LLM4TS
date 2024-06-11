package com.werken.saxpath;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XPathLexerTest {

    @Test
    public void testEmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testSingleCharacterXPath() {
        XPathLexer lexer = new XPathLexer("a");
        Token token = lexer.nextToken();
        assertEquals(TokenType.AND, token.getTokenType());
    }

    @Test
    public void testSingleCharacterXPathWithPrefix() {
        XPathLexer lexer = new XPathLexer(":a");
        Token token = lexer.nextToken();
        assertEquals(TokenType.COLON, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(TokenType.AND, token.getTokenType());
    }

    @Test
    public void testXYZ() {
        XPathLexer lexer = new XPathLexer("XYZ");
        Token token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("XYZ", token.getValue());
    }

    @Test
    public void testX() {
        XPathLexer lexer = new XPathLexer("X");
        Token token = lexer.nextToken();
        assertEquals(TokenType.AND, token.getTokenType());
    }

    @Test
    public void testXPathWithSlashes() {
        XPathLexer lexer = new XPathLexer("/one/two");
        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("one", token.getValue());
        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("two", token.getValue());
        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testXPathWithSlashesAndAtSymbol() {
        XPathLexer lexer = new XPathLexer("/one/@two");
        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("one", token.getValue());
        token = lexer.nextToken();
        assertEquals(TokenType.AT_SIGN, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("two", token.getValue());
        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testXPathWithComma() {
        XPathLexer lexer = new XPathLexer("a,b");
        Token tokenA = lexer.nextToken();
        assertEquals(TokenType.AND, tokenA.getTokenType());
        Token tokenB = lexer.nextToken();
        assertEquals(TokenType.COMMA, tokenB.getTokenType());
        tokenB = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, tokenB.getTokenType());
        assertEquals("b", tokenB.getValue());
        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testXPathWithRelationalOperator() {
        XPathLexer lexer = new XPathLexer("a < b");
        Token tokenA = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, tokenA.getTokenType());
        assertEquals("a", tokenA.getValue());
        Token tokenRelationalOperator = lexer.nextToken();
        assertEquals(TokenType.RELATIONAL_OPERATOR, tokenRelationalOperator.getTokenType());
        Token tokenB = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, tokenB.getTokenType());
        assertEquals("b", tokenB.getValue());
        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testXPathWithEquals() {
        XPathLexer lexer = new XPathLexer("a = b");
        Token tokenA = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, tokenA.getTokenType());
        assertEquals("a", tokenA.getValue());
        Token tokenEquals = lexer.nextToken();
        assertEquals(TokenType.EQUALS, tokenEquals.getTokenType());
        Token tokenB = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, tokenB.getTokenType());
        assertEquals("b", tokenB.getValue());
        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testXPathWithNotEquals() {
        XPathLexer lexer = new XPathLexer("a != b");
        Token tokenA = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, tokenA.getTokenType());
        assertEquals("a", tokenA.getValue());
        Token tokenNotEquals = lexer.nextToken();
        assertEquals(TokenType.NOT_EQUALS, tokenNotEquals.getTokenType());
        Token tokenB = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, tokenB.getTokenType());
        assertEquals("b", tokenB.getValue());
        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testXPathWithNot() {
        XPathLexer lexer = new XPathLexer("not(a)");
        Token tokenNot = lexer.nextToken();
        assertEquals(TokenType.NOT, tokenNot.getTokenType());
        Token tokenA = lexer.nextToken();
        assertEquals(TokenType.LEFT_PAREN, tokenA.getTokenType());
        tokenA = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, tokenA.getTokenType());
        assertEquals("a", tokenA.getValue());
        tokenA = lexer.nextToken();
        assertEquals(TokenType.RIGHT_PAREN, tokenA.getTokenType());
        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testXPathWithAnd() {
        XPathLexer lexer = new XPathLexer("a and b");
        Token tokenA = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, tokenA.getTokenType());
        assertEquals("a", tokenA.getValue());
        Token tokenAnd = lexer.nextToken();
        assertEquals(TokenType.AND, tokenAnd.getTokenType());
        Token tokenB = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, tokenB.getTokenType());
        assertEquals("b", tokenB.getValue());
        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testXPathWithOr() {
        XPathLexer lexer = new XPathLexer("a or b");
        Token tokenA = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, tokenA.getTokenType());
        assertEquals("a", tokenA.getValue());
        Token tokenOr = lexer.nextToken();
        assertEquals(TokenType.OR, tokenOr.getTokenType());
        Token tokenB = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, tokenB.getTokenType());
        assertEquals("b", tokenB.getValue());
        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testXPathWithStar() {
        XPathLexer lexer = new XPathLexer("*");
        Token token = lexer.nextToken();
        assertEquals(TokenType.STAR, token.getTokenType());
        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testXPathWithNumber() {
        XPathLexer lexer = new XPathLexer("1");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("1", token.getValue());
        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testXPathWithNumberWithPeriod() {
        XPathLexer lexer = new XPathLexer("1.2");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("1.2", token.getValue());
        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testXPathWithDollar() {
        XPathLexer lexer = new XPathLexer("$");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOLLAR_SIGN, token.getTokenType());
        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testXPathWithDoubleColon() {
        XPathLexer lexer = new XPathLexer("::");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOUBLE_COLON, token.getTokenType());
        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testXPathWithSingleQuotedString() {
        XPathLexer lexer = new XPathLexer("'abc'");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LITERAL, token.getTokenType());
        assertEquals("abc", token.getValue());
        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testXPathWithDoubleQuotedString() {
        XPathLexer lexer = new XPathLexer("\"abc\"");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LITERAL, token.getTokenType());
        assertEquals("abc", token.getValue());
        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testXPathWithWhitespace() {
        XPathLexer lexer = new XPathLexer("   a  ");
        Token token = lexer.nextToken();
        assertEquals(TokenType.AND, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getValue());
        token = lexer.nextToken();
        assertNull(token);
    }
}