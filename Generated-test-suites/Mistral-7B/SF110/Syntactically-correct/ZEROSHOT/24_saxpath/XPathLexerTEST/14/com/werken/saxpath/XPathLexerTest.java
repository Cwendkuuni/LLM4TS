package com.werken.saxpath;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class XPathLexerTest {

    XPathLexer lexer;

    @Before
    public void setUp() {
        lexer = new XPathLexer("");
    }

    @After
    public void tearDown() {
        lexer = null;
    }

    @Test
    public void testEmptyXPath() {
        assertEquals(new Token(-2, "", 0, 0), lexer.nextToken());
    }

    @Test
    public void testIdentifier() {
        lexer.setXPath("testIdentifier");
        Token token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("testIdentifier", token.getValue());
    }

    @Test
    public void testOperatorName() {
        lexer.setXPath("testOperatorName");
        lexer.nextToken(); // consume identifier
        Token token = lexer.nextToken();
        assertEquals(28, token.getType()); // and
    }

    @Test
    public void testMod() {
        lexer.setXPath("testMod");
        lexer.nextToken(); // consume identifier
        lexer.nextToken(); // consume "mod"
        Token token = lexer.nextToken();
        assertEquals(25, token.getType());
    }

    @Test
    public void testDiv() {
        lexer.setXPath("testDiv");
        lexer.nextToken(); // consume identifier
        lexer.nextToken(); // consume "div"
        Token token = lexer.nextToken();
        assertEquals(24, token.getType());
    }

    @Test
    public void testAnd() {
        lexer.setXPath("testAnd");
        lexer.nextToken(); // consume identifier
        lexer.nextToken(); // consume "and"
        Token token = lexer.nextToken();
        assertEquals(28, token.getType());
    }

    @Test
    public void testOr() {
        lexer.setXPath("testOr");
        lexer.nextToken(); // consume identifier
        lexer.nextToken(); // consume "or"
        Token token = lexer.nextToken();
        assertEquals(29, token.getType());
    }

    @Test
    public void testNumber() {
        lexer.setXPath("123");
        Token token = lexer.nextToken();
        assertEquals(31, token.getType());
        assertEquals("123", token.getValue());

        lexer.setXPath("123.45");
        token = lexer.nextToken();
        assertEquals(30, token.getType());
        assertEquals("123.45", token.getValue());
    }

    @Test
    public void testWhitespace() {
        lexer.setXPath(" test      XPath ");
        Token token = lexer.nextToken();
        assertEquals(-2, token.getType());

        for (int i = 0; i < 5; i++) {
            lexer.nextToken();
        }

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("XPath", token.getValue());
    }

    @Test
    public void testComma() {
        lexer.setXPath("test, test");
        Token token1 = lexer.nextToken();
        assertEquals(15, token1.getType());
        assertEquals("test", token1.getValue());

        Token token2 = lexer.nextToken();
        assertEquals(32, token2.getType());

        Token token3 = lexer.nextToken();
        assertEquals(15, token3.getType());
        assertEquals("test", token3.getValue());
    }

    @Test
    public void testEquals() {
        lexer.setXPath("test = test");
        Token token1 = lexer.nextToken();
        assertEquals(15, token1.getType());
        assertEquals("test", token1.getValue());

        Token token2 = lexer.nextToken();
        assertEquals(21, token2.getType());

        Token token3 = lexer.nextToken();
        assertEquals(15, token3.getType());
        assertEquals("test", token3.getValue());
    }

    @Test
    public void testMinus() {
        lexer.setXPath("test - test");
        Token token1 = lexer.nextToken();
        assertEquals(15, token1.getType());
        assertEquals("test", token1.getValue());

        Token token2 = lexer.nextToken();
        assertEquals(6, token2.getType());

        Token token3 = lexer.nextToken();
        assertEquals(15, token3.getType());
        assertEquals("test", token3.getValue());
    }

    @Test
    public void testPlus() {
        lexer.setXPath("test + test");
        Token token1 = lexer.nextToken();
        assertEquals(15, token1.getType());
        assertEquals("test", token1.getValue());

        Token token2 = lexer.nextToken();
        assertEquals(5, token2.getType());

        Token token3 = lexer.nextToken();
        assertEquals(15, token3.getType());
        assertEquals("test", token3.getValue());
    }

    @Test
    public void testDollar() {
        lexer.setXPath("$test");
        Token token = lexer.nextToken();
        assertEquals(26, token.getType());

        Token token2 = lexer.nextToken();
        assertEquals(15, token2.getType());
        assertEquals("test", token2.getValue());
    }

    @Test
    public void testPipe() {
        lexer.setXPath("test | test");
        Token token1 = lexer.nextToken();
        assertEquals(15, token1.getType());
        assertEquals("test", token1.getValue());

        Token token2 = lexer.nextToken();
        assertEquals(17, token2.getType());

        Token token3 = lexer.nextToken();
        assertEquals(15, token3.getType());
        assertEquals("test", token3.getValue());
    }

    @Test
    public void testAt() {
        lexer.setXPath("@test");
        Token token = lexer.nextToken();
        assertEquals(16, token.getType());

        Token token2 = lexer.nextToken();
        assertEquals(15, token2.getType());
        assertEquals("test", token2.getValue());
    }

    @Test
    public void testColon() {
        lexer.setXPath("test:test");
        Token token1 = lexer.nextToken();
        assertEquals(15, token1.getType());
        assertEquals("test", token1.getValue());

        Token token2 = lexer.nextToken();
        assertEquals(18, token2.getType());

        Token token3 = lexer.nextToken();
        assertEquals(15, token3.getType());
        assertEquals("test", token3.getValue());
    }

    @Test
    public void testDoubleColon() {
        lexer.setXPath("test::test");
        Token token1 = lexer.nextToken();
        assertEquals(15, token1.getType());
        assertEquals("test", token1.getValue());

        Token token2 = lexer.nextToken();
        assertEquals(19, token2.getType());

        Token token3 = lexer.nextToken();
        assertEquals(15, token3.getType());
        assertEquals("test", token3.getValue());
    }

    @Test
    public void testNotEquals() {
        lexer.setXPath("test != test");
        Token token1 = lexer.nextToken();
        assertEquals(15, token1.getType());
        assertEquals("test", token1.getValue());

        Token token2 = lexer.nextToken();
        assertEquals(22, token2.getType());

        Token token3 = lexer.nextToken();
        assertEquals(15, token3.getType());
        assertEquals("test", token3.getValue());
    }

    @Test
    public void testNot() {
        lexer.setXPath("!test");
        Token token = lexer.nextToken();
        assertEquals(23, token.getType());

        Token token2 = lexer.nextToken();
        assertEquals(15, token2.getType());
        assertEquals("test", token2.getValue());
    }

    @Test
    public void testRelationalOperator() {
        lexer.setXPath("test < test");
        Token token1 = lexer.nextToken();
        assertEquals(15, token1.getType());
        assertEquals("test", token1.getValue());

        Token token2 = lexer.nextToken();
        assertEquals(7, token2.getType());

        Token token3 = lexer.nextToken();
        assertEquals(15, token3.getType());
        assertEquals("test", token3.getValue());
    }

    @Test
    public void testStar() {
        lexer.setXPath("test*");
        Token token = lexer.nextToken();
        assertEquals(20, token.getType());

        Token token2 = lexer.nextToken();
        assertEquals(15, token2.getType());
        assertEquals("test", token2.getValue());
    }

    @Test
    public void testSlashes() {
        lexer.setXPath("/test");
        Token token1 = lexer.nextToken();
        assertEquals(11, token1.getType());

        Token token2 = lexer.nextToken();
        assertEquals(15, token2.getType());
        assertEquals("test", token2.getValue());

        lexer.setXPath("/test/test");
        token1 = lexer.nextToken();
        assertEquals(11, token1.getType());

        token2 = lexer.nextToken();
        assertEquals(15, token2.getType());
        assertEquals("test", token2.getValue());

        token = lexer.nextToken();
        assertEquals(12, token.getType());

        token2 = lexer.nextToken();
        assertEquals(15, token2.getType());
        assertEquals("test", token2.getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidXPath() {
        lexer.setXPath("test");
        lexer.nextToken();
        lexer.nextToken(); // consume "/"
        lexer.nextToken(); // consume "test"
        lexer.nextToken(); // consume "/"
        lexer.nextToken(); // consume invalid character
    }
}