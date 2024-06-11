package com.werken.saxpath;

import static org.junit.Assert.*;

import org.junit.Test;

public class XPathLexerTest {

    @Test
    public void testEmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testSimpleIdentifier() {
        XPathLexer lexer = new XPathLexer("myIdentifier");
        Token token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("myIdentifier", token.getText());
    }

    @Test
    public void testIdentifierWithNumber() {
        XPathLexer lexer = new XPathLexer("myIdentifier123");
        Token token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("myIdentifier123", token.getText());
    }

    @Test
    public void testSimpleOperator() {
        XPathLexer lexer = new XPathLexer(">");
        Token token = lexer.nextToken();
        assertEquals(TokenType.GREATER_THAN, token.getTokenType());
        assertEquals(">", token.getText());
    }

    @Test
    public void testSimpleLiteral() {
        XPathLexer lexer = new XPathLexer("'foo'");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LITERAL, token.getTokenType());
        assertEquals("foo", token.getText());
    }

    @Test
    public void testLiteralWithQuotes() {
        XPathLexer lexer = new XPathLexer("''foo''");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LITERAL, token.getTokenType());
        assertEquals("foo", token.getText());
    }

    @Test
    public void testSlashOperator() {
        XPathLexer lexer = new XPathLexer("/");
        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        assertEquals("/", token.getText());
    }

    @Test
    public void testSlashWithIdentifier() {
        XPathLexer lexer = new XPathLexer("/myIdentifier");
        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        assertEquals("/", token.getText());
        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("myIdentifier", token.getText());
    }

    @Test
    public void testCommaOperator() {
        XPathLexer lexer = new XPathLexer(",");
        Token token = lexer.nextToken();
        assertEquals(TokenType.COMMA, token.getTokenType());
        assertEquals(",", token.getText());
    }

    @Test
    public void testDollarOperator() {
        XPathLexer lexer = new XPathLexer("$");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOLLAR, token.getTokenType());
        assertEquals("$", token.getText());
    }

    @Test
    public void testLeftParenthesis() {
        XPathLexer lexer = new XPathLexer("(");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LEFT_PAREN, token.getTokenType());
        assertEquals("(", token.getText());
    }

    @Test
    public void testRightParenthesis() {
        XPathLexer lexer = new XPathLexer(")");
        Token token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_PAREN, token.getTokenType());
        assertEquals(")", token.getText());
    }

    @Test
    public void testLeftBracket() {
        XPathLexer lexer = new XPathLexer("[");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
        assertEquals("[", token.getText());
    }

    @Test
    public void testRightBracket() {
        XPathLexer lexer = new XPathLexer("]");
        Token token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
        assertEquals("]", token.getText());
    }

    @Test
    public void testPlusOperator() {
        XPathLexer lexer = new XPathLexer("+");
        Token token = lexer.nextToken();
        assertEquals(TokenType.PLUS, token.getTokenType());
        assertEquals("+", token.getText());
    }

    @Test
    public void testMinusOperator() {
        XPathLexer lexer = new XPathLexer("-");
        Token token = lexer.nextToken();
        assertEquals(TokenType.MINUS, token.getTokenType());
        assertEquals("-", token.getText());
    }

    @Test
    public void testNumber() {
        XPathLexer lexer = new XPathLexer("123");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("123", token.getText());
    }

    @Test
    public void testNumberWithDecimalPoint() {
        XPathLexer lexer = new XPathLexer("123.456");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("123.456", token.getText());
    }

    @Test
    public void testWhitespace() {
        XPathLexer lexer = new XPathLexer("   \tfoo   ");
        Token token = lexer.nextToken();
        assertNull(token);
        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("foo", token.getText());
    }

    @Test
    public void testWhitespaceWithNumber() {
        XPathLexer lexer = new XPathLexer("   \t123");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("123", token.getText());
    }

    @Test
    public void testMultiTokenIdentifier() {
        XPathLexer lexer = new XPathLexer("myMultiTokenIdentifier");
        Token token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("myMultiTokenIdentifier", token.getText());
    }

    @Test
    public void testMultiTokenOperator() {
        XPathLexer lexer = new XPathLexer("myOperatorName");
        Token token = lexer.nextToken();
        assertEquals(TokenType.OPERATOR_NAME, token.getTokenType());
        assertEquals("myOperatorName", token.getText());
    }

    @Test
    public void testAndOperator() {
        XPathLexer lexer = new XPathLexer("and");
        Token token = lexer.nextToken();
        assertEquals(TokenType.AND, token.getTokenType());
        assertEquals("and", token.getText());
    }

    @Test
    public void testOrOperator() {
        XPathLexer lexer = new XPathLexer("or");
        Token token = lexer.nextToken();
        assertEquals(TokenType.OR, token.getTokenType());
        assertEquals("or", token.getText());
    }

    @Test
    public void testModOperator() {
        XPathLexer lexer = new XPathLexer("mod");
        Token token = lexer.nextToken();
        assertEquals(TokenType.MOD, token.getTokenType());
        assertEquals("mod", token.getText());
    }

    @Test
    public void testDivOperator() {
        XPathLexer lexer = new XPathLexer("div");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DIV, token.getTokenType());
        assertEquals("div", token.getText());
    }
}