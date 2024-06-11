package com.werken.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XPathLexerTest {

    @Test
    public void testEmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testIdentifier() {
        XPathLexer lexer = new XPathLexer("foo");
        Token token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());
    }

    @Test
    public void testIdentifierWithDots() {
        XPathLexer lexer = new XPathLexer("foo.bar.baz");
        Token token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());
        token = lexer.nextToken();
        assertEquals(14, token.getType());
        assertEquals("bar", token.getValue());
        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("baz", token.getValue());
    }

    @Test
    public void testNumber() {
        XPathLexer lexer = new XPathLexer("123");
        Token token = lexer.nextToken();
        assertEquals(31, token.getType());
        assertEquals("123", token.getValue());
    }

    @Test
    public void testNumberWithDot() {
        XPathLexer lexer = new XPathLexer("123.456");
        Token token = lexer.nextToken();
        assertEquals(30, token.getType());
        assertEquals("123", token.getValue());
        token = lexer.nextToken();
        assertEquals(13, token.getType());
        assertEquals(".", token.getValue());
        token = lexer.nextToken();
        assertEquals(31, token.getType());
        assertEquals("456", token.getValue());
    }

    @Test
    public void testWhitespace() {
        XPathLexer lexer = new XPathLexer(" foo \n bar \r baz ");
        Token token = lexer.nextToken();
        assertEquals(-2, token.getType());
        assertEquals("", token.getValue());
        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());
        token = lexer.nextToken();
        assertEquals(-2, token.getType());
        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("bar", token.getValue());
        token = lexer.nextToken();
        assertEquals(-2, token.getType());
        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("baz", token.getValue());
    }

    @Test
    public void testOperatorName() {
        XPathLexer lexer = new XPathLexer("and()");
        Token token = lexer.nextToken();
        assertEquals(28, token.getType());
        assertEquals("and", token.getValue());
        token = lexer.nextToken();
        assertEquals(-2, token.getType());
    }

    @Test
    public void testMod() {
        XPathLexer lexer = new XPathLexer("mod(foo, 1)");
        Token token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("mod", token.getValue());
        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());
        token = lexer.nextToken();
        assertEquals(31, token.getType());
        assertEquals("1", token.getValue());
    }

    @Test
    public void testDiv() {
        XPathLexer lexer = new XPathLexer("div(foo, 1)");
        Token token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("div", token.getValue());
        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());
        token = lexer.nextToken();
        assertEquals(31, token.getType());
        assertEquals("1", token.getValue());
    }

    @Test
    public void testAnd() {
        XPathLexer lexer = new XPathLexer("and");
        Token token = lexer.nextToken();
        assertEquals(28, token.getType());
        assertEquals("and", token.getValue());
    }

    @Test
    public void testOr() {
        XPathLexer lexer = new XPathLexer("or");
        Token token = lexer.nextToken();
        assertEquals(29, token.getType());
        assertEquals("or", token.getValue());
    }

    @Test
    public void testEqual() {
        XPathLexer lexer = new XPathLexer("=foo");
        Token token = lexer.nextToken();
        assertEquals(21, token.getType());
        assertEquals("=", token.getValue());
        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());
    }

    @Test
    public void testNotEquals() {
        XPathLexer lexer = new XPathLexer("!=foo");
        Token token = lexer.nextToken();
        assertEquals(22, token.getType());
        assertEquals("!=", token.getValue());
        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());
    }

    @Test
    public void testGreaterThan() {
        XPathLexer lexer = new XPathLexer(">foo");
        Token token = lexer.nextToken();
        assertEquals(9, token.getType());
        assertEquals(">", token.getValue());
        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());
    }

    @Test
    public void testLessThan() {
        XPathLexer lexer = new XPathLexer("<foo");
        Token token = lexer.nextToken();
        assertEquals(7, token.getType());
        assertEquals("<", token.getValue());
        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());
    }

    @Test
    public void testPlus() {
        XPathLexer lexer = new XPathLexer("+foo");
        Token token = lexer.nextToken();
        assertEquals(5, token.getType());
        assertEquals("+", token.getValue());
        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());
    }

    @Test
    public void testMinus() {
        XPathLexer lexer = new XPathLexer("-foo");
        Token token = lexer.nextToken();
        assertEquals(6, token.getType());
        assertEquals("-", token.getValue());
        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());
    }

    @Test
    public void testDollar() {
        XPathLexer lexer = new XPathLexer("$foo");
        Token token = lexer.nextToken();
        assertEquals(26, token.getType());
        assertEquals("$", token.getValue());
        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());
    }

    @Test
    public void testPipe() {
        XPathLexer lexer = new XPathLexer("|foo");
        Token token = lexer.nextToken();
        assertEquals(17, token.getType());
        assertEquals("|", token.getValue());
        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());
    }

    @Test
    public void testAt() {
        XPathLexer lexer = new XPathLexer("@foo");
        Token token = lexer.nextToken();
        assertEquals(16, token.getType());
        assertEquals("@", token.getValue());
        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());
    }

    @Test
    public void testColon() {
        XPathLexer lexer = new XPathLexer(":foo");
        Token token = lexer.nextToken();
        assertEquals(18, token.getType());
        assertEquals(":", token.getValue());
        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());
    }

    @Test
    public void testDoubleColon() {
        XPathLexer lexer = new XPathLexer("::foo");
        Token token = lexer.nextToken();
        assertEquals(19, token.getType());
        assertEquals("::", token.getValue());
        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());
    }

    @Test
    public void testLeftParen() {
        XPathLexer lexer = new XPathLexer("(");
        Token token = lexer.nextToken();
        assertEquals(1, token.getType());
        assertEquals("(", token.getValue());
    }

    @Test
    public void testRightParen() {
        XPathLexer lexer = new XPathLexer(")");
        Token token = lexer.nextToken();
        assertEquals(2, token.getType());
        assertEquals(")", token.getValue());
    }

    @Test
    public void testLeftBracket() {
        XPathLexer lexer = new XPathLexer("[");
        Token token = lexer.nextToken();
        assertEquals(3, token.getType());
        assertEquals("[", token.getValue());
    }

    @Test
    public void testRightBracket() {
        XPathLexer lexer = new XPathLexer("]");
        Token token = lexer.nextToken();
        assertEquals(4, token.getType());
        assertEquals("]", token.getValue());
    }

    @Test
    public void testSlashes() {
        XPathLexer lexer = new XPathLexer("/foo");
        Token token = lexer.nextToken();
        assertEquals(11, token.getType());
        assertEquals("/", token.getValue());
        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());
    }

    @Test
    public void testSlashesWithDoubleSlash() {
        XPathLexer lexer = new XPathLexer("//foo");
        Token token = lexer.nextToken();
        assertEquals(12, token.getType());
        assertEquals("//", token.getValue());
        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("foo", token.getValue());
    }

    @Test
    public void testComma() {
        XPathLexer lexer = new XPathLexer(",");
        Token token = lexer.nextToken();
        assertEquals(32, token.getType());
        assertEquals(",", token.getValue());
    }
}