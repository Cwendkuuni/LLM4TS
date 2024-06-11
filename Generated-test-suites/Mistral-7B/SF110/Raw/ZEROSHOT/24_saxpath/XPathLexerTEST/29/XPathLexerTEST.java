package com.werken.saxpath;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XPathLexerTest {

    private XPathLexer lexer;

    @Before
    public void setUp() {
        this.lexer = new XPathLexer("");
    }

    @Test
    public void testEmptyXPath() {
        assertNull(this.lexer.nextToken());
    }

    @Test
    public void testSimpleIdentifier() {
        this.lexer.setXPath("testIdentifier");
        Token token = this.lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("testIdentifier", token.getValue());
    }

    @Test
    public void testIdentifierWithNumber() {
        this.lexer.setXPath("testIdentifier123");
        Token token = this.lexxer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("testIdentifier", token.getValue());
    }

    @Test
    public void testOperatorName() {
        this.lexer.setXPath("testOperatorName");
        Token token = this.lexer.nextToken();
        assertEquals(18, token.getType());
        assertEquals("testOperatorName", token.getValue());
    }

    @Test
    public void testAndOperator() {
        this.lexer.setXPath("testAndOperator");
        Token token = this.lexer.nextToken();
        assertEquals(28, token.getType());
        assertEquals("testAndOperator", token.getValue());
    }

    @Test
    public void testOrOperator() {
        this.lexer.setXPath("testOrOperator");
        Token token = this.lexer.nextToken();
        assertEquals(29, token.getType());
        assertEquals("testOrOperator", token.getValue());
    }

    @Test
    public void testDivOperator() {
        this.lexer.setXPath("testDivOperator");
        Token token = this.lexer.nextToken();
        assertEquals(24, token.getType());
        assertEquals("testDivOperator", token.getValue());
    }

    @Test
    public void testModOperator() {
        this.lexer.setXPath("testModOperator");
        Token token = this.lexer.nextToken();
        assertEquals(25, token.getType());
        assertEquals("testModOperator", token.getValue());
    }

    @Test
    public void testNumber() {
        this.lexer.setXPath("123");
        Token token = this.lexer.nextToken();
        assertEquals(31, token.getType());
        assertEquals("123", token.getValue());
    }

    @Test
    public void testNumberWithPeriod() {
        this.lexer.setXPath("123.456");
        Token token = this.lexer.nextToken();
        assertEquals(30, token.getType());
        assertEquals("123.456", token.getValue());
    }

    @Test
    public void testWhitespace() {
        this.lexer.setXPath("   testWhitespace   ");
        Token token = this.lexer.nextToken();
        assertEquals(-2, token.getType());
    }

    @Test
    public void testComma() {
        this.lexer.setXPath(",");
        Token token = this.lexer.nextToken();
        assertEquals(32, token.getType());
        assertEquals(",", token.getValue());
    }

    @Test
    public void testEquals() {
        this.lexer.setXPath("testEquals");
        Token token = this.lexer.nextToken();
        assertEquals(21, token.getType());
        assertEquals("testEquals", token.getValue());
    }

    @Test
    public void testMinus() {
        this.lexer.setXPath("testMinus");
        Token token = this.lexer.nextToken();
        assertEquals(6, token.getType());
        assertEquals("testMinus", token.getValue());
    }

    @Test
    public void testPlus() {
        this.lexer.setXPath("testPlus");
        Token token = this.lexer.nextToken();
        assertEquals(5, token.getType());
        assertEquals("testPlus", token.getValue());
    }

    @Test
    public void testDollar() {
        this.lexer.setXPath("$testDollar");
        Token token = this.lexer.nextToken();
        assertEquals(26, token.getType());
        assertEquals("$testDollar", token.getValue());
    }

    @Test
    public void testPipe() {
        this.lexer.setXPath("testPipe");
        Token token = this.lexer.nextToken();
        assertEquals(17, token.getType());
        assertEquals("testPipe", token.getValue());
    }

    @Test
    public void testAtSymbol() {
        this.lexer.setXPath("@testAtSymbol");
        Token token = this.lexer.nextToken();
        assertEquals(16, token.getType());
        assertEquals("@testAtSymbol", token.getValue());
    }

    @Test
    public void testColon() {
        this.lexer.setXPath("testColon");
        Token token = this.lexer.nextToken();
        assertEquals(18, token.getType());
        assertEquals("testColon", token.getValue());
    }

    @Test
    public void testDoubleColon() {
        this.lexer.setXPath("testDoubleColon");
        Token token = this.lexer.nextToken();
        assertEquals(19, token.getType());
        assertEquals("testDoubleColon", token.getValue());
    }

    @Test
    public void testNotOperator() {
        this.lexer.setXPath("testNotOperator");
        Token token = this.lexer.nextToken();
        assertEquals(23, token.getType());
        assertEquals("testNotOperator", token.getValue());
    }

    @Test
    public void testNotEqualsOperator() {
        this.lexer.setXPath("testNotEqualsOperator");
        Token token = this.lexer.nextToken();
        assertEquals(22, token.getType());
        assertEquals("testNotEqualsOperator", token.getValue());
    }

    @Test
    public void testRelationalOperator() {
        this.lexer.setXPath("testRelationalOperator");
        Token token = this.lexer.nextToken();
        assertEquals(7, token.getType());
        assertEquals("testRelationalOperator", token.getValue());
    }

    @Test
    public void testStar() {
        this.lexer.setXPath("testStar");
        Token token = this.lexer.nextToken();
        assertEquals(20, token.getType());
        assertEquals("testStar", token.getValue());
    }

    @Test
    public void testLiteral() {
        this.lexer.setXPath("\"testLiteral\"");
        Token token = this.lexer.nextToken();
        assertEquals(27, token.getType());
        assertEquals("testLiteral", token.getValue());
    }

    @Test
    public void testSlashes() {
        this.lexer.setXPath("/testSlashes");
        Token token = this.lexer.nextToken();
        assertEquals(11, token.getType());
        assertEquals("/testSlashes", token.getValue());
    }

    @Test
    public void testSlashesWithDoubleSlash() {
        this.lexer.setXPath("/testSlashes//testSlashes");
        Token token = this.lexer.nextToken();
        assertEquals(12, token.getType());
        assertEquals("/testSlashes//testSlashes", token.getValue());
    }

    @Test
    public void testLeftBracket() {
        this.lexer.setXPath("[testLeftBracket]");
        Token token = this.lexer.nextToken();
        assertEquals(3, token.getType());
        assertEquals("[testLeftBracket]", token.getValue());
    }

    @Test
    public void testRightBracket() {
        this.lexer.setXPath("[testLeftBracket] testRightBracket");
        Token token = this.lexer.nextToken();
        assertEquals(4, token.getType());
        assertEquals(" testRightBracket", token.getValue());
    }

    @Test
    public void testLeftParenthesis() {
        this.lexer.setXPath("(testLeftParenthesis)");
        Token token = this.lexer.nextToken();
        assertEquals(1, token.getType());
        assertEquals("(testLeftParenthesis)", token.getValue());
    }

    @Test
    public void testRightParenthesis() {
        this.lexer.setXPath("(testLeftParenthesis) testRightParenthesis");
        Token token = this.lexer.nextToken();
        assertEquals(2, token.getType());
        assertEquals(" testRightParenthesis", token.getValue());
    }

    @Test
    public void testMultipleTokens() {
        this.lexer.setXPath("testAndOperator testOrOperator testIdentifier testNumber testEquals testMinus testPlus testPipe testAtSymbol testColon testDoubleColon testNotOperator testNotEquals testRelationalOperator testStar testLiteral testSlashes testLeftBracket testRightBracket testLeftParenthesis testRightParenthesis");
        for (int i = 0; i < 18; i++) {
            Token token = this.lexer.nextToken();
            assertNotNull(token);
        }
        assertNull(this.lexer.nextToken());
    }

    @After
    public void tearDown() {
        this.lexer = null;
    }
}