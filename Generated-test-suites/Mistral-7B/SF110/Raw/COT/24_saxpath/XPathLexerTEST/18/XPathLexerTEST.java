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
    public void testNextToken_SingleIdentifier() {
        XPathLexer lexer = new XPathLexer("myIdentifier");
        Token token = lexer.nextToken();
        Assert.assertEquals(15, token.getTokenType());
        Assert.assertEquals("myIdentifier", token.getText());
    }

    @Test
    public void testNextToken_SingleOperator() {
        XPathLexer lexer = new XPathLexer("op");
        Token token = lexer.nextToken();
        Assert.assertEquals((char) 11, token.getText().charAt(0));
    }

    @Test
    public void testNextToken_SingleDigitNumber() {
        XPathLexer lexer = new XPathLexer("1");
        Token token = lexer.nextToken();
        Assert.assertEquals(31, token.getTokenType());
        Assert.assertEquals("1", token.getText());
    }

    @Test
    public void testNextToken_MultiDigitNumber() {
        XPathLexer lexer = new XPathLexer("123");
        Token token = lexer.nextToken();
        Assert.assertEquals(30, token.getTokenType());
        Assert.assertEquals("123", token.getText());
    }

    @Test
    public void testNextToken_SingleCharacterLiteral() {
        XPathLexer lexer = new XPathLexer("'a'");
        Token token = lexer.nextToken();
        Assert.assertEquals(27, token.getTokenType());
        Assert.assertEquals("a", token.getText());
    }

    @Test
    public void testNextToken_MultiCharacterLiteral() {
        XPathLexer lexer = new XPathLexer("'abc'");
        Token token = lexer.nextToken();
        Assert.assertEquals(27, token.getTokenType());
        Assert.assertEquals("abc", token.getText());
    }

    @Test
    public void testNextToken_Whitespace() {
        XPathLexer lexer = new XPathLexer("   myXPath   ");
        Token token = lexer.nextToken();
        Assert.assertEquals(-2, token.getTokenType());
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
        XPathLexer lexer = new XPathLexer("not");
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
    public void testNextToken_LessThan() {
        XPathLexer lexer = new XPathLexer("<");
        Token token = lexer.nextToken();
        Assert.assertEquals(7, token.getTokenType());
    }

    @Test
    public void testNextToken_LessThanOrEqual() {
        XPathLexer lexer = new XPathLexer("<=");
        Token token = lexer.nextToken();
        Assert.assertEquals(8, token.getTokenType());
    }

    @Test
    public void testNextToken_GreaterThan() {
        XPathLexer lexer = new XPathLexer(">");
        Token token = lexer.nextToken();
        Assert.assertEquals(9, token.getTokenType());
    }

    @Test
    public void testNextToken_GreaterThanOrEqual() {
        XPathLexer lexer = new XPathLexer(">=");
        Token token = lexer.nextToken();
        Assert.assertEquals(10, token.getTokenType());
    }

    @Test
    public void testNextToken_Star() {
        XPathLexer lexer = new XPathLexer("*");
        Token token = lexer.nextToken();
        Assert.assertEquals(20, token.getTokenType());
    }

    @Test
    public void testNextToken_Slashes() {
        XPathLexer lexer = new XPathLexer("/");
        Token token = lexer.nextToken();
        Assert.assertEquals(11, token.getTokenType());
    }

    @Test(expected = RuntimeException.class)
    public void testNextToken_UnexpectedCharacter_1() {
        XPathLexer lexer = new XPathLexer("x@th");
        lexer.nextToken(); // Consume 'x'
        lexer.nextToken(); // Consume '@'
        lexer.nextToken(); // Expected identifier or operator name, but got 't'
    }

    @Test(expected = RuntimeException.class)
    public void testNextToken_UnexpectedCharacter_2() {
        XPathLexer lexer = new XPathLexer("1+");
        lexer.nextToken(); // Consume '1'
        lexer.nextToken(); // Consume '+'
        lexer.nextToken(); // Expected digit, but got unknown character
    }

    @Test(expected = RuntimeException.class)
    public void testNextToken_UnexpectedCharacter_3() {
        XPathLexer lexer = new XPathLexer("myIdentifier/");
        lexer.nextToken(); // Consume 'myIdentifier'
        lexer.nextToken(); // Consume '/'
        lexer.nextToken(); // Expected identifier or operator name, but got unknown character
    }

    @Test(expected = RuntimeException.class)
    public void testNextToken_UnexpectedEndOfString() {
        XPathLexer lexer = new XPathLexer("myIdentifier");
        lexer.nextToken(); // Consume 'myIdentifier'
        lexer.nextToken(); // Expected end of string, but got unknown character
    }

    
}