package com.werken.saxpath;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XPathLexerTest {

    private XPathLexer lexer;
    private String xpath;
    private Token expectedToken;

    @Before
    public void setUp() {
        xpath = "//a[1] and b=c or d<e and f>=g";
        lexer = new XPathLexer(xpath);
    }

    @After
    public void tearDown() {
        lexer = null;
    }

    @Test
    public void testNextToken_initialState() {
        Token actualToken = lexer.nextToken();
        assertNull(actualToken);
    }

    @Test
    public void testNextToken_advanceToFirstToken() {
        Token actualToken = lexer.nextToken();
        assertEquals(TokenType.AND, actualToken.getTokenType());
    }

    @Test
    public void testIdentifierOrOperatorName() {
        Token actualToken = lexer.identifierOrOperatorName();
        assertEquals(TokenType.AND, actualToken.getTokenType());
    }

    @Test
    public void testIdentifier() {
        Token actualToken = lexer.identifier();
        assertEquals(TokenType.IDENTIFIER, actualToken.getTokenType());
        assertEquals("a", actualToken.getValue());
    }

    @Test
    public void testOperatorName() {
        Token actualToken = lexer.operatorName();
        assertEquals(TokenType.AND, actualToken.getTokenType());
    }

    @Test
    public void testMod() {
        lexer.setXPath("mod(x, y)");
        Token actualToken = lexer.nextToken();
        assertEquals(TokenType.MOD, actualToken.getTokenType());
    }

    @Test
    public void testDiv() {
        lexer.setXPath("div(x, y)");
        Token actualToken = lexer.nextToken();
        assertEquals(TokenType.DIV, actualToken.getTokenType());
    }

    @Test
    public void testAnd() {
        Token actualToken = lexer.nextToken();
        assertEquals(TokenType.AND, actualToken.getTokenType());
    }

    @Test
    public void testOr() {
        lexer.setXPath("or(x, y)");
        Token actualToken = lexer.nextToken();
        assertEquals(TokenType.OR, actualToken.getTokenType());
    }

    @Test
    public void testNumber() {
        Token actualToken = lexer.number();
        assertEquals(TokenType.NUMBER, actualToken.getTokenType());
        assertEquals("1", actualToken.getValue());
    }

    @Test
    public void testWhitespace() {
        lexer.setXPath(" //a[1] and b = c or d < e and f >= g ");
        Token actualToken = lexer.nextToken();
        assertEquals(TokenType.WHITESPACE, actualToken.getTokenType());
    }

    @Test
    public void testComma() {
        Token actualToken = lexer.nextToken();
        assertEquals(TokenType.COMMA, actualToken.getTokenType());
    }

    @Test
    public void testEquals() {
        Token actualToken = lexer.nextToken();
        assertEquals(TokenType.EQUALS, actualToken.getTokenType());
    }

    @Test
    public void testMinus() {
        Token actualToken = lexer.nextToken();
        assertEquals(TokenType.MINUS, actualToken.getTokenType());
    }

    @Test
    public void testPlus() {
        Token actualToken = lexer.nextToken();
        assertEquals(TokenType.PLUS, actualToken.getTokenType());
    }

    @Test
    public void testDollar() {
        lexer.setXPath("$x");
        Token actualToken = lexer.nextToken();
        assertEquals(TokenType.DOLLAR, actualToken.getTokenType());
    }

    @Test
    public void testPipe() {
        Token actualToken = lexer.nextToken();
        assertEquals(TokenType.PIPE, actualToken.getTokenType());
    }

    @Test
    public void testAt() {
        lexer.setXPath("@x");
        Token actualToken = lexer.nextToken();
        assertEquals(TokenType.AT, actualToken.getTokenType());
    }

    @Test
    public void testColon() {
        Token actualToken = lexer.nextToken();
        assertEquals(TokenType.COLON, actualToken.getTokenType());
    }

    @Test
    public void testDoubleColon() {
        lexer.setXPath("::x");
        Token actualToken = lexer.nextToken();
        assertEquals(TokenType.DOUBLE_COLON, actualToken.getTokenType());
    }

    @Test
    public void testNot() {
        Token actualToken = lexer.nextToken();
        assertEquals(TokenType.NOT, actualToken.getTokenType());
    }

    @Test
    public void testNotEquals() {
        lexer.setXPath("not(x) and not(y)");
        Token actualToken1 = lexer.nextToken();
        assertEquals(TokenType.NOT, actualToken1.getTokenType());
        Token actualToken2 = lexer.nextToken();
        assertEquals(TokenType.NOT, actualToken2.getTokenType());
        Token actualToken3 = lexer.nextToken();
        assertEquals(TokenType.AND, actualToken3.getTokenType());
    }

    @Test
    public void testRelationalOperator() {
        Token actualToken = lexer.nextToken();
        assertEquals(TokenType.LESS_THAN, actualToken.getTokenType());
    }

    @Test
    public void testStar() {
        Token actualToken = lexer.nextToken();
        assertEquals(TokenType.ASTERISK, actualToken.getTokenType());
    }

    @Test
    public void testLiteral() {
        lexer.setXPath("'a'");
        Token actualToken = lexer.nextToken();
        assertEquals(TokenType.LITERAL, actualToken.getTokenType());
        assertEquals("a", actualToken.getValue());
    }

    @Test
    public void testDots() {
        Token actualToken = lexer.nextToken();
        assertEquals(TokenType.DOTS, actualToken.getTokenType());
    }

    @Test
    public void testLeftBracket() {
        Token actualToken = lexer.nextToken();
        assertEquals(TokenType.LEFT_BRACKET, actualToken.getTokenType());
    }

    @Test
    public void testRightBracket() {
        Token actualToken = lexer.nextToken();
        assertEquals(TokenType.RIGHT_BRACKET, actualToken.getTokenType());
    }

    @Test
    public void testLeftParen() {
        Token actualToken = lexer.nextToken();
        assertEquals(TokenType.LEFT_PAREN, actualToken.getTokenType());
    }

    @Test
    public void testRightParen() {
        Token actualToken = lexer.nextToken();
        assertEquals(TokenType.RIGHT_PAREN, actualToken.getTokenType());
    }

    @Test
    public void testSlashes() {
        Token actualToken = lexer.nextToken();
        assertEquals(TokenType.SLASHES, actualToken.getTokenType());
    }

    @Test
    public void testLA() {
        assertEquals('a', lexer.LA(1));
        assertEquals('a', lexer.LA(2));
        assertEquals('\uffff', lexer.LA(-1));
    }
}