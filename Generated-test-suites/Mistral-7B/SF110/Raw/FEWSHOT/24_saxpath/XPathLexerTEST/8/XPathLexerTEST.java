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
        assertEquals(Token.TYPE_WHITESPACE, token.getTokenType());
        assertNull(token.getValue());
    }

    @Test
    public void testNextToken_SimpleIdentifier() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.nextToken();
        assertEquals(Token.TYPE_IDENTIFIER, token.getTokenType());
        assertEquals("test", token.getValue());
    }

    @Test
    public void testNextToken_SimpleOperator() {
        XPathLexer lexer = new XPathLexer("//a=b");
        Token token = lexer.nextToken();
        assertEquals(Token.TYPE_RELATIONAL_OPERATOR, token.getTokenType());
        assertEquals("=", token.getValue());
    }

    @Test
    public void testNextToken_SimpleOperatorWithSlash() {
        XPathLexer lexer = new XPathLexer("/a=b");
        Token token = lexer.nextToken();
        assertEquals(Token.TYPE_SLASHES, token.getTokenType());
        assertNull(token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_RELATIONAL_OPERATOR, token.getTokenType());
        assertEquals("=", token.getValue());
    }

    @Test
    public void testNextToken_IdentifierWithSlash() {
        XPathLexer lexer = new XPathLexer("a/b=c");
        Token token = lexer.nextToken();
        assertEquals(Token.TYPE_IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_SLASHES, token.getTokenType());
        assertNull(token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_IDENTIFIER, token.getTokenType());
        assertEquals("b", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_RELATIONAL_OPERATOR, token.getTokenType());
        assertEquals("=", token.getValue());
    }

    @Test
    public void testNextToken_SimpleNumber() {
        XPathLexer lexer = new XPathLexer("a[1]");
        Token token = lexer.nextToken();
        assertEquals(Token.TYPE_IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_LEFT_BRACKET, token.getTokenType());
        assertNull(token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_NUMBER, token.getTokenType());
        assertEquals("1", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_RIGHT_BRACKET, token.getTokenType());
        assertNull(token.getValue());
    }

    @Test
    public void testNextToken_ComplexNumberWithIdentifiers() {
        XPathLexer lexer = new XPathLexer("a[1+2][2+3]");
        Token token = lexer.nextToken();
        assertEquals(Token.TYPE_IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_LEFT_BRACKET, token.getTokenType());
        assertNull(token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_NUMBER, token.getTokenType());
        assertEquals("1", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_PLUS, token.getTokenType());
        assertNull(token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_NUMBER, token.getTokenType());
        assertEquals("2", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_LEFT_BRACKET, token.getTokenType());
        assertNull(token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_NUMBER, token.getTokenType());
        assertEquals("2", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_PLUS, token.getTokenType());
        assertNull(token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_NUMBER, token.getTokenType());
        assertEquals("3", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_RIGHT_BRACKET, token.getTokenType());
        assertNull(token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_RIGHT_BRACKET, token.getTokenType());
        assertNull(token.getValue());
    }

    @Test
    public void testNextToken_StringLiteral() {
        XPathLexer lexer = new XPathLexer("'test'");
        Token token = lexer.nextToken();
        assertEquals(Token.TYPE_LITERAL, token.getTokenType());
        assertEquals("test", token.getValue());
    }

    @Test
    public void testNextToken_StringLiteralWithSlash() {
        XPathLexer lexer = new XPathLexer("'test/data'");
        Token token = lexer.nextToken();
        assertEquals(Token.TYPE_LITERAL, token.getTokenType());
        assertEquals("test/data", token.getValue());
    }

    @Test
    public void testNextToken_ComplexExpression() {
        XPathLexer lexer = new XPathLexer("/abc[1+2][2+3]/def/@attrib");
        Token token = lexer.nextToken();
        assertEquals(Token.TYPE_SLASHES, token.getTokenType());
        assertNull(token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_IDENTIFIER, token.getTokenType());
        assertEquals("abc", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_LEFT_BRACKET, token.getTokenType());
        assertNull(token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_NUMBER, token.getTokenType());
        assertEquals("1", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_PLUS, token.getTokenType());
        assertNull(token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_NUMBER, token.getTokenType());
        assertEquals("2", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_LEFT_BRACKET, token.getTokenType());
        assertNull(token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_NUMBER, token.getTokenType());
        assertEquals("2", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_PLUS, token.getTokenType());
        assertNull(token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE.NUMBER, token.getTokenType());
        assertEquals("3", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_RIGHT_BRACKET, token.getTokenType());
        assertNull(token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_SLASHES, token.getTokenType());
        assertNull(token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_IDENTIFIER, token.getTokenType());
        assertEquals("def", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_LEFT_BRACKET, token.getTokenType());
        assertNull(token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_AT, token.getTokenType());
        assertNull(token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_IDENTIFIER, token.getTokenType());
        assertEquals("attrib", token.getValue());
        token = lexer.nextToken();
        assertEquals(Token.TYPE_RIGHT_BRACKET, token.getTokenType());
        assertNull(token.getValue());
    }
}