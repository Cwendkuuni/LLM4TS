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
    public void testSimpleIdentifier() {
        XPathLexer lexer = new XPathLexer("myIdentifier");
        Token token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("myIdentifier", token.getText());
    }

    @Test
    public void testOperatorName() {
        XPathLexer lexer = new XPathLexer("myOperator");
        Token token = lexer.nextToken();
        assertEquals(28, token.getType()); // AND
        assertEquals("myOperator", token.getText());
    }

    @Test
    public void testModOperator() {
        XPathLexer lexer = new XPathLexer("mymod");
        Token token = lexer.nextToken();
        assertEquals(25, token.getType());
        assertEquals("mymod", token.getText());
    }

    @Test
    public void testDivOperator() {
        XPathLexer lexer = new XPathLexer("mydiv");
        Token token = lexer.nextToken();
        assertEquals(24, token.getType());
        assertEquals("mydiv", token.getText());
    }

    @Test
    public void testAndOperator() {
        XPathLexer lexer = new XPathLexer("myAnd");
        Token token = lexer.nextToken();
        assertEquals(28, token.getType());
        assertEquals("myAnd", token.getText());
    }

    @Test
    public void testOrOperator() {
        XPathLexer lexer = new XPathLexer("myOr");
        Token token = lexer.nextToken();
        assertEquals(29, token.getType());
        assertEquals("myOr", token.getText());
    }

    @Test
    public void testNumber() {
        XPathLexer lexer = new XPathLexer("123");
        Token token = lexer.nextToken();
        assertEquals(31, token.getType());
        assertEquals("123", token.getText());

        lexer = new XPathLexer("123.45");
        token = lexer.nextToken();
        assertEquals(30, token.getType());
        assertEquals("123.45", token.getText());
    }

    @Test
    public void testWhitespace() {
        XPathLexer lexer = new XPathLexer(" \t myIdentifier \n");
        Token token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testComma() {
        XPathLexer lexer = new XPathLexer(",");
        Token token = lexer.nextToken();
        assertEquals(32, token.getType());
        assertEquals(",", token.getText());
    }

    @Test
    public void testEqualsOperator() {
        XPathLexer lexer = new XPathLexer("myIdentifier = 123");
        Token token = lexer.nextToken();
        assertEquals(21, token.getType());
        assertEquals("=", token.getText());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("myIdentifier", token.getText());

        token = lexer.nextToken();
        assertEquals(31, token.getType());
        assertEquals("123", token.getText());
    }

    @Test
    public void testMinusOperator() {
        XPathLexer lexer = new XPathLexer("myIdentifier - 123");
        Token token = lexer.nextToken();
        assertEquals(7, token.getType());
        assertEquals("-", token.getText());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("myIdentifier", token.getText());

        token = lexer.nextToken();
        assertEquals(31, token.getType());
        assertEquals("123", token.getText());
    }

    @Test
    public void testPlusOperator() {
        XPathLexer lexer = new XPathLexer("myIdentifier + 123");
        Token token = lexer.nextToken();
        assertEquals(5, token.getType());
        assertEquals("+", token.getText());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("myIdentifier", token.getText());

        token = lexer.nextToken();
        assertEquals(31, token.getType());
        assertEquals("123", token.getText());
    }

    @Test
    public void testDollarOperator() {
        XPathLexer lexer = new XPathLexer("$myIdentifier");
        Token token = lexer.nextToken();
        assertEquals(26, token.getType());
        assertEquals("$", token.getText());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("myIdentifier", token.getText());
    }

    @Test
    public void testDotOperator() {
        XPathLexer lexer = new XPathLexer("myIdentifier.myOtherIdentifier");
        Token token = lexer.nextToken();
        assertEquals(13, token.getType());
        assertEquals(".", token.getText());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("myIdentifier", token.getText());

        token = lexer.nextToken();
        assertEquals(13, token.getType());
        assertEquals(".", token.getText());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("myOtherIdentifier", token.getText());
    }

    @Test
    public void testSlashesOperator() {
        XPathLexer lexer = new XPathLexer("/myIdentifier");
        Token token = lexer.nextToken();
        assertEquals(11, token.getType());
        assertEquals("/", token.getText());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("myIdentifier", token.getText());
    }

    @Test
    public void testDoubleSlashesOperator() {
        XPathLexer lexer = new XPathLexer("//myIdentifier");
        Token token = lexer.nextToken();
        assertEquals(12, token.getType());
        assertEquals("//", token.getText());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("myIdentifier", token.getText());
    }

    @Test
    public void testLeftParenthesis() {
        XPathLexer lexer = new XPathLexer("(myIdentifier)");
        Token token = lexer.nextToken();
        assertEquals(1, token.getType());
        assertEquals("(", token.getText());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("myIdentifier", token.getText());

        token = lexer.nextToken();
        assertEquals(1, token.getType());
        assertEquals(")", token.getText());
    }

    @Test
    public void testLeftBracket() {
        XPathLexer lexer = new XPathLexer("[myIdentifier]");
        Token token = lexer.nextToken();
        assertEquals(3, token.getType());
        assertEquals("[", token.getText());

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("myIdentifier", token.getText());

        token = lexer.nextToken();
        assertEquals(3, token.getType());
        assertEquals("]", token.getText());
    }

    @Test
    public void testLiteral() {
        XPathLexer lexer = new XPathLexer("'single' or \"double\\\" or 'triple''");
        Token token = lexer.nextToken();
        assertEquals(27, token.getType());
        assertEquals("single", token.getText());

        token = lexer.nextToken();
        assertEquals(17, token.getType());
        assertEquals("or", token.getText());

        token = lexer.nextToken();
        assertEquals(27, token.getType());
        assertEquals("double\\", token.getText());

        token = lexer.nextToken();
        assertEquals(17, token.getType());
        assertEquals("or", token.getText());

        token = lexer.nextToken();
        assertEquals(27, token.getType());
        assertEquals("triple''", token.getText());

        token = lexer.nextToken();
        assertNull(token);
    }
}