package com.werken.saxpath.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.werken.saxpath.XPathLexer;
import com.werken.saxpath.Token;

public class XPathLexerTest {

    @Test
    public void testNextToken_EmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testNextToken_Whitespace() {
        XPathLexer lexer = new XPathLexer("   ");
        Token token = lexer.nextToken();
        assertEquals(Token.Type.WHITESPACE, token.getType());
    }

    @Test
    public void testNextToken_Identifier() {
        XPathLexer lexer = new XPathLexer("myIdentifier");
        Token token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("myIdentifier", token.getValue());
    }

    @Test
    public void testNextToken_Operator() {
        XPathLexer lexer = new XPathLexer("a and b");
        Token token = lexer.nextToken();
        assertEquals(Token.Type.AND, token.getType());
        assertEquals("a", lexer.getPreviousToken().getValue());
        assertEquals("and", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("b", token.getValue());
    }

    @Test
    public void testNextToken_Number() {
        XPathLexer lexer = new XPathLexer("1");
        Token token = lexer.nextToken();
        assertEquals(Token.Type.NUMBER, token.getType());
        assertEquals("1", token.getValue());
    }

    @Test
    public void testNextToken_NumberWithDecimalPoint() {
        XPathLexer lexer = new XPathLexer("1.25");
        Token token = lexer.nextToken();
        assertEquals(Token.Type.NUMBER, token.getType());
        assertEquals("1.25", token.getValue());
    }

    @Test
    public void testNextToken_RelationalOperator() {
        XPathLexer lexer = new XPathLexer("a < b");
        Token token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("a", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.RELATIONAL_OPERATOR, token.getType());
        assertEquals("<", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("b", token.getValue());
    }

    @Test
    public void testNextToken_Equals() {
        XPathLexer lexer = new XPathLexer("a = b");
        Token token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("a", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.EQUALS, token.getType());
        assertEquals("=", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("b", token.getValue());
    }

    @Test
    public void testNextToken_NotEquals() {
        XPathLexer lexer = new XPathLexer("a != b");
        Token token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("a", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.NOT_EQUALS, token.getType());
        assertEquals("!=", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("b", token.getValue());
    }

    @Test
    public void testNextToken_Not() {
        XPathLexer lexer = new XPathLexer("not(a)");
        Token token = lexer.nextToken();
        assertEquals(Token.Type.NOT, token.getType());
        assertEquals("not", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.OPEN_PAREN, token.getType());
        assertEquals("(", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("a", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.CLOSE_PAREN, token.getType());
        assertEquals(")", token.getValue());
    }

    @Test
    public void testNextToken_Div() {
        XPathLexer lexer = new XPathLexer("a div b");
        Token token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("a", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.DIV, token.getType());
        assertEquals("/", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("b", token.getValue());
    }

    @Test
    public void testNextToken_Mod() {
        XPathLexer lexer = new XPathLexer("a mod b");
        Token token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("a", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.MOD, token.getType());
        assertEquals("mod", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("b", token.getValue());
    }

    @Test
    public void testNextToken_Plus() {
        XPathLexer lexer = new XPathLexer("a + b");
        Token token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("a", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.PLUS, token.getType());
        assertEquals("+", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("b", token.getValue());
    }

    @Test
    public void testNextToken_Minus() {
        XPathLexer lexer = new XPathLexer("a - b");
        Token token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("a", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.MINUS, token.getType());
        assertEquals("-", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("b", token.getValue());
    }

    @Test
    public void testNextToken_Star() {
        XPathLexer lexer = new XPathLexer("a * b");
        Token token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("a", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.STAR, token.getType());
        assertEquals("*", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("b", token.getValue());
    }

    @Test
    public void testNextToken_Slashes() {
        XPathLexer lexer = new XPathLexer("/a/b");
        Token token = lexer.nextToken();
        assertEquals(Token.Type.SLASH, token.getType());
        assertEquals("/", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("a", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.SLASH, token.getType());
        assertEquals("/", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("b", token.getValue());
    }

    @Test
    public void testNextToken_LeftParen() {
        XPathLexer lexer = new XPathLexer("(a)");
        Token token = lexer.nextToken();
        assertEquals(Token.Type.LEFT_PAREN, token.getType());
        assertEquals("(", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("a", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.RIGHT_PAREN, token.getType());
        assertEquals(")", token.getValue());
    }

    @Test
    public void testNextToken_LeftBracket() {
        XPathLexer lexer = new XPathLexer("[a]");
        Token token = lexer.nextToken();
        assertEquals(Token.Type.LEFT_BRACKET, token.getType());
        assertEquals("[", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("a", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.RIGHT_BRACKET, token.getType());
        assertEquals("]", token.getValue());
    }

    @Test
    public void testNextToken_Literal() {
        XPathLexer lexer = new XPathLexer("'foo'");
        Token token = lexer.nextToken();
        assertEquals(Token.Type.LITERAL, token.getType());
        assertEquals("foo", token.getValue());
    }

    @Test
    public void testNextToken_Dots() {
        XPathLexer lexer = new XPathLexer("./a/./b");
        Token token = lexer.nextToken();
        assertEquals(Token.Type.DOTS, token.getType());
        assertEquals(".", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.SLASH, token.getType());
        assertEquals("/", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("a", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.DOTS, token.getType());
        assertEquals(".", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.SLASH, token.getType());
        assertEquals("/", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.Type.IDENTIFIER, token.getType());
        assertEquals("b", token.getValue());
    }
}