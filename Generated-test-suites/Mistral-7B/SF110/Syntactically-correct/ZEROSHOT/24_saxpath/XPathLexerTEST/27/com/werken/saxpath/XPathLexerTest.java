package com.werken.saxpath.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.werken.saxpath.XPathLexer;
import com.werken.saxpath.Token;

public class XPathLexerTest {

    private XPathLexer lexer;

    @Before
    public void setUp() {
        String xpath = "//a[b='c' and d=1]";
        lexer = new XPathLexer(xpath);
    }

    @Test
    public void testNextToken_EmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testNextToken_Slash() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
    }

    @Test
    public void testNextToken_Identifier() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
    }

    @Test
    public void testNextToken_KeywordAnd() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.AND, token.getTokenType());
    }

    @Test
    public void testNextToken_KeywordOr() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.OR, token.getTokenType());
    }

    @Test
    public void testNextToken_KeywordNot() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.NOT, token.getTokenType());
    }

    @Test
    public void testNextToken_KeywordDiv() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.DIV, token.getTokenType());
    }

    @Test
    public void testNextToken_KeywordMod() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.MOD, token.getTokenType());
    }

    @Test
    public void testNextToken_KeywordMultiply() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.MULTIPLY, token.getTokenType());
    }

    @Test
    public void testNextToken_KeywordAdd() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.ADD, token.getTokenType());
    }

    @Test
    public void testNextToken_KeywordSubtract() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.SUBTRACT, token.getTokenType());
    }

    @Test
    public void testNextToken_KeywordLessThan() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.LESS_THAN, token.getTokenType());
    }

    @Test
    public void testNextToken_KeywordGreaterThan() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.GREATER_THAN, token.getTokenType());
    }

    @Test
    public void testNextToken_KeywordLessThanOrEqual() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.LESS_THAN_OR_EQUAL, token.getTokenType());
    }

    @Test
    public void testNextToken_KeywordGreaterThanOrEqual() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.GREATER_THAN_OR_EQUAL, token.getTokenType());
    }

    @Test
    public void testNextToken_KeywordEquals() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());
    }

    @Test
    public void testNextToken_KeywordNotEquals() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.NOT_EQUALS, token.getTokenType());
    }

    @Test
    public void testNextToken_Dot() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOT, token.getTokenType());
    }

    @Test
    public void testNextToken_Colon() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.COLON, token.getTokenType());
    }

    @Test
    public void testNextToken_DoubleColon() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOUBLE_COLON, token.getTokenType());
    }

    @Test
    public void testNextToken_Dollar() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOLLAR, token.getTokenType());
    }

    @Test
    public void testNextToken_Comma() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.COMMA, token.getTokenType());
    }

    @Test
    public void testNextToken_LeftParenthesis() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.LEFT_PARENTHESIS, token.getTokenType());
    }

    @Test
    public void testNextToken_RightParenthesis() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_PARENTHESIS, token.getTokenType());
    }

    @Test
    public void testNextToken_LeftBracket() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
    }

    @Test
    public void testNextToken_RightBracket() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
    }

    @Test
    public void testNextToken_Number() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
    }

    @Test
    public void testNextToken_StringLiteral() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.STRING_LITERAL, token.getTokenType());
    }

    @Test
    public void testNextToken_Whitespace() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.WHITESPACE, token.getTokenType());
    }

    @Test
    public void testNextToken_EOF() {
        for (int i = 0; i < xpath.length(); i++) {
            Token token = lexer.nextToken();
            if (token.getTokenType() != TokenType.EOF) {
                i--;
                break;
            }
        }
    }

    @Test
    public void testNextToken_IdentifierOrOperatorName() {
        Token token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER_OR_OPERATOR_NAME, token.getTokenType());
    }

}