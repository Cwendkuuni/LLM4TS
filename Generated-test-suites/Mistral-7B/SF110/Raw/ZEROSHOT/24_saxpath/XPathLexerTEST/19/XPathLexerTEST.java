package com.werken.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XPathLexerTest {

    @Test
    public void testNextToken_EmptyString() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testNextToken_WhiteSpace() {
        XPathLexer lexer = new XPathLexer("   abc  ");
        Token token = lexer.nextToken();
        assertEquals(TokenType.WHITESPACE, token.getTokenType());
    }

    @Test
    public void testNextToken_Identifier() {
        XPathLexer lexer = new XPathLexer("abc");
        Token token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("abc", token.getValue());
    }

    @Test
    public void testNextToken_Operator() {
        XPathLexer lexer = new XPathLexer("+");
        Token token = lexer.nextToken();
        assertEquals(TokenType.PLUS, token.getTokenType());
    }

    @Test
    public void testNextToken_Number() {
        XPathLexer lexer = new XPathLexer("123");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("123", token.getValue());
    }

    @Test
    public void testNextToken_Literal() {
        XPathLexer lexer = new XPathLexer("\"abc\"");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LITERAL, token.getTokenType());
        assertEquals("abc", token.getValue());
    }

    @Test
    public void testNextToken_Dollar() {
        XPathLexer lexer = new XPathLexer("$");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOLLAR, token.getTokenType());
    }

    @Test
    public void testNextToken_Comma() {
        XPathLexer lexer = new XPathLexer(",");
        Token token = lexer.nextToken();
        assertEquals(TokenType.COMMA, token.getTokenType());
    }

    @Test
    public void testNextToken_LeftParen() {
        XPathLexer lexer = new XPathLexer("(");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LEFT_PAREN, token.getTokenType());
    }

    @Test
    public void testNextToken_RightParen() {
        XPathLexer lexer = new XPathLexer(")");
        Token token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_PAREN, token.getTokenType());
    }

    @Test
    public void testNextToken_LeftBracket() {
        XPathLexer lexer = new XPathLexer("[");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
    }

    @Test
    public void testNextToken_RightBracket() {
        XPathLexer lexer = new XPathLexer("]");
        Token token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
    }

    @Test
    public void testNextToken_Slashes() {
        XPathLexer lexer = new XPathLexer("/");
        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASHES, token.getTokenType());
    }

    @Test
    public void testNextToken_At() {
        XPathLexer lexer = new XPathLexer("@");
        Token token = lexer.nextToken();
        assertEquals(TokenType.AT, token.getTokenType());
    }

    @Test
    public void testNextToken_Colon() {
        XPathLexer lexer = new XPathLexer(":");
        Token token = lexer.nextToken();
        assertEquals(TokenType.COLON, token.getTokenType());
    }

    @Test
    public void testNextToken_DoubleColon() {
        XPathLexer lexer = new XPathLexer("::");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOUBLE_COLON, token.getTokenType());
    }

    @Test
    public void testNextToken_Not() {
        XPathLexer lexer = new XPathLexer("!");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NOT, token.getTokenType());
    }

    @Test
    public void testNextToken_NotEquals() {
        XPathLexer lexer = new XPathLexer("!=");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NOT_EQUALS, token.getTokenType());
    }

    @Test
    public void testNextToken_Equals() {
        XPathLexer lexer = new XPathLexer("=");
        Token token = lexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());
    }

    @Test
    public void testNextToken_LessThan() {
        XPathLexer lexer = new XPathLexer("<");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LESS_THAN, token.getTokenType());
    }

    @Test
    public void testNextToken_LessThanOrEqual() {
        XPathLexer lexer = new XPathLexer("<=");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LESS_THAN_OR_EQUAL, token.getTokenType());
    }

    @Test
    public void testNextToken_GreaterThan() {
        XPathLexer lexer = new XPathLexer(">");
        Token token = lexer.nextToken();
        assertEquals(TokenType.GREATER_THAN, token.getTokenType());
    }

    @Test
    public void testNextToken_GreaterThanOrEqual() {
        XPathLexer lexer = new XPathLexer(">=");
        Token token = lexer.nextToken();
        assertEquals(TokenType.GREATER_THAN_OR_EQUAL, token.getTokenType());
    }

    @Test
    public void testNextToken_And() {
        XPathLexer lexer = new XPathLexer("and");
        Token token = lexer.nextToken();
        assertEquals(TokenType.AND, token.getTokenType());
    }

    @Test
    public void testNextToken_Or() {
        XPathLexer lexer = new XPathLexer("or");
        Token token = lexer.nextToken();
        assertEquals(TokenType.OR, token.getTokenType());
    }

    @Test
    public void testNextToken_Mod() {
        XPathLexer lexer = new XPathLexer("mod");
        Token token = lexer.nextToken();
        assertEquals(TokenType.MOD, token.getTokenType());
    }

    @Test
    public void testNextToken_Div() {
        XPathLexer lexer = new XPathLexer("div");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DIV, token.getTokenType());
    }

    @Test
    public void testNextToken_Star() {
        XPathLexer lexer = new XPathLexer("*");
        Token token = lexer.nextToken();
        assertEquals(TokenType.STAR, token.getTokenType());
    }

    @Test
    public void testNextToken_Period() {
        XPathLexer lexer = new XPathLexer(".");
        Token token = lexer.nextToken();
        assertEquals(TokenType.PERIOD, token.getTokenType());
    }

    @Test
    public void testNextToken_IdentifierOrOperatorName() {
        XPathLexer lexer = new XPathLexer("myIdentifier");
        Token token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("myIdentifier", token.getValue());

        lexer.setPreviousToken(null);
        lexer = new XPathLexer("myOperatorName");
        token = lexer.nextToken();
        assertEquals(TokenType.OPERATOR_NAME, token.getTokenType());
        assertEquals("myOperatorName", token.getValue());
    }

    @Test
    public void testNextToken_StringLiteral() {
        XPathLexer lexer = new XPathLexer("\"abcdef\"");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LITERAL, token.getTokenType());
        assertEquals("abcdef", token.getValue());
    }

    @Test
    public void testNextToken_EmptyStringLiteral() {
        XPathLexer lexer = new XPathLexer("\"\"");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LITERAL, token.getTokenType());
        assertEquals("", token.getValue());
    }

    @Test
    public void testNextToken_Comment() {
        XPathLexer lexer = new XPathLexer("--comment");
        Token token = lexer.nextToken();
        assertNull(token);
    }
}