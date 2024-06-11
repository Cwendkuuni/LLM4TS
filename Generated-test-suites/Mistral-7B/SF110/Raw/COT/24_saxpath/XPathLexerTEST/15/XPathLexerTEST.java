package com.werken.saxpath;

import org.junit.Assert;
import org.junit.Test;

public class XPathLexerTest {
    @Test
    public void testNextToken_EmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        Assert.assertEquals(-1, token.getTokenType());
    }

    @Test
    public void testNextToken_Identifier() {
        XPathLexer lexer = new XPathLexer("testIdentifier");
        Token token = lexer.nextToken();
        Assert.assertEquals(15, token.getTokenType());
        Assert.assertEquals("testIdentifier", token.getValue());
    }

    @Test
    public void testNextToken_OperatorName() {
        XPathLexer lexer = new XPathLexer("test operatorName");
        Token token = lexer.nextToken();
        Assert.assertEquals(3, token.getTokenType()); // assuming 'operatorName' is mapped to 3 in XPathLexer
    }

    @Test
    public void testNextToken_Whitespace() {
        XPathLexer lexer = new XPathLexer(" test ");
        Token token = lexer.nextToken();
        Assert.assertEquals(-2, token.getTokenType());
    }

    @Test
    public void testNextToken_Number() {
        XPathLexer lexer = new XPathLexer("123");
        Token token = lexer.nextToken();
        Assert.assertEquals(31, token.getTokenType());
        Assert.assertEquals("123", token.getValue());
    }

    @Test
    public void testNextToken_DoubleNumber() {
        XPathLexer lexer = new XPathLexer("123.456");
        Token token = lexer.nextToken();
        Assert.assertEquals(30, token.getTokenType());
        Assert.assertEquals("123.456", token.getValue());
    }

    @Test
    public void testNextToken_Comma() {
        XPathLexer lexer = new XPathLexer(",");
        Token token = lexer.nextToken();
        Assert.assertEquals(32, token.getTokenType());
    }

    @Test
    public void testNextToken_Equals() {
        XPathLexer lexer = new XPathLexer("=");
        Token token = lexer.nextToken();
        Assert.assertEquals(21, token.getTokenType());
    }

    @Test
    public void testNextToken_Minus() {
        XPathLexer lexer = new XPathLexer("-");
        Token token = lexer.nextToken();
        Assert.assertEquals(6, token.getTokenType());
    }

    @Test
    public void testNextToken_Plus() {
        XPathLexer lexer = new XPathLexer("+");
        Token token = lexer.nextToken();
        Assert.assertEquals(5, token.getTokenType());
    }

    @Test
    public void testNextToken_Dollar() {
        XPathLexer lexer = new XPathLexer("$");
        Token token = lexer.nextToken();
        Assert.assertEquals(26, token.getTokenType());
    }

    @Test
    public void testNextToken_Pipe() {
        XPathLexer lexer = new XPathLexer("|");
        Token token = lexer.nextToken();
        Assert.assertEquals(17, token.getTokenType());
    }

    @Test
    public void testNextToken_At() {
        XPathLexer lexer = new XPathLexer("@");
        Token token = lexer.nextToken();
        Assert.assertEquals(16, token.getTokenType());
    }

    @Test
    public void testNextToken_Colon() {
        XPathLexer lexer = new XPathLexer(":");
        Token token = lexer.nextToken();
        Assert.assertEquals(18, token.getTokenType());
    }

    @Test
    public void testNextToken_DoubleColon() {
        XPathLexer lexer = new XPathLexer("::");
        Token token = lexer.nextToken();
        Assert.assertEquals(19, token.getTokenType());
    }

    @Test
    public void testNextToken_Not() {
        XPathLexer lexer = new XPathLexer("!");
        Token token = lexer.nextToken();
        Assert.assertEquals(23, token.getTokenType());
    }

    @Test
    public void testNextToken_NotEquals() {
        XPathLexer lexer = new XPathLexer("!=");
        Token token = lexer.nextToken();
        Assert.assertEquals(22, token.getTokenType());
    }

    @Test
    public void testNextToken_RelationalOperator() {
        XPathLexer lexer = new XPathLexer("<=");
        Token token = lexer.nextToken();
        Assert.assertEquals(8, token.getTokenType());
    }

    @Test
    public void testNextToken_RelationalOperator_WithPlus() {
        XPathLexer lexer = new XPathLexer("<=+");
        Token token = lexer.nextToken();
        Assert.assertEquals(8, token.getTokenType());
    }

    @Test
    public void testNextToken_Slashes() {
        XPathLexer lexer = new XPathLexer("/");
        Token token = lexer.nextToken();
        Assert.assertEquals(11, token.getTokenType());
    }

    @Test
    public void testNextToken_Slashes_WithTwoSlashes() {
        XPathLexer lexer = new XPathLexer("//");
        Token token = lexer.nextToken();
        Assert.assertEquals(12, token.getTokenType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextToken_InvalidCharacter() {
        XPathLexer lexer = new XPathLexer("invalidCharacter");
        lexer.nextToken();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextToken_EmptyStringAfterConsumption() {
        XPathLexer lexer = new XPathLexer("test");
        lexer.consume(4);
        lexer.nextToken();
    }
}