package com.werken.saxpath;

import org.junit.Test;
import static org.junit.Assert.*;

public class XPathLexerTest {
    // Given an instance of XPathLexer
    XPathLexer xpathLexer;

    @Test
    public void testEmptyXPath() throws Exception {
        xpathLexer = new XPathLexer("");
        assertEquals(new Token(-1, "", 0, 0), xpathLexer.nextToken());
    }

    @Test
    public void testBasicIdentifier() throws Exception {
        xpathLexer = new XPathLexer("myIdentifier");
        Token token = xpathLexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("myIdentifier", token.getText());
    }

    @Test
    public void testBasicOperatorName() throws Exception {
        xpathLexer = new XPathLexer("myOperator");
        Token token = xpathLexer.nextToken();
        assertEquals(16, token.getType());
        assertEquals("myOperator", token.getText());
    }

    @Test
    public void testBasicKeyword() throws Exception {
        xpathLexer = new XPathLexer("and");
        Token token = xpathLexer.nextToken();
        assertEquals(28, token.getType());

        xpathLexer = new XPathLexer("or");
        token = xpathLexer.nextToken();
        assertEquals(29, token.getType());

        xpathLexer = new XPathLexer("div");
        token = xpathLexer.nextToken();
        assertEquals(24, token.getType());

        xpathLexer = new XPathLexer("mod");
        token = xpathLexer.nextToken();
        assertEquals(25, token.getType());
    }

    @Test
    public void testBasicNumber() throws Exception {
        xpathLexer = new XPathLexer("1234");
        Token token = xpathLexer.nextToken();
        assertEquals(31, token.getType());
        assertEquals("1234", token.getText());

        xpathLexer = new XPathLexer("123.");
        token = xpathLexer.nextToken();
        assertEquals(30, token.getType());
        assertEquals("123.", token.getText());

        xpathLexer = new XPathLexer("123.45");
        token = xpathLexer.nextToken();
        assertEquals(30, token.getType());
        assertEquals("123.45", token.getText());
    }

    @Test
    public void testBasicWhitespace() throws Exception {
        xpathLexer = new XPathLexer("   myIdentifier");
        Token token = xpathLexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("myIdentifier", token.getText());
    }

    @Test
    public void testBasicComma() throws Exception {
        xpathLexer = new XPathLexer("myIdentifier, myOperator");
        Token token1 = xpathLexer.nextToken();
        assertEquals(15, token1.getType());
        assertEquals("myIdentifier", token1.getText());

        Token token2 = xpathLexer.nextToken();
        assertEquals(16, token2.getType());
        assertEquals("myOperator", token2.getText());
    }

    @Test
    public void testBasicEquals() throws Exception {
        xpathLexer = new XPathLexer("myIdentifier = myOperator");
        Token token1 = xpathLexer.nextToken();
        assertEquals(15, token1.getType());
        assertEquals("myIdentifier", token1.getText());

        Token token2 = xpathLexer.nextToken();
        assertEquals(21, token2.getType());

        Token token3 = xpathLexer.nextToken();
        assertEquals(16, token3.getType());
        assertEquals("myOperator", token3.getText());
    }

    @Test
    public void testBasicMinus() throws Exception {
        xpathLexer = new XPathLexer("myIdentifier - myOperator");
        Token token1 = xpathLexer.nextToken();
        assertEquals(15, token1.getType());
        assertEquals("myIdentifier", token1.getText());

        Token token2 = xpathLexer.nextToken();
        assertEquals(6, token2.getType());

        Token token3 = xpathLexer.nextToken();
        assertEquals(16, token3.getType());
        assertEquals("myOperator", token3.getText());
    }

    @Test
    public void testBasicPlus() throws Exception {
        xpathLexer = new XPathLexer("myIdentifier + myOperator");
        Token token1 = xpathLexer.nextToken();
        assertEquals(15, token1.getType());
        assertEquals("myIdentifier", token1.getText());

        Token token2 = xpathLexer.nextToken();
        assertEquals(5, token2.getType());

        Token token3 = xpathLexer.nextToken();
        assertEquals(16, token3.getType());
        assertEquals("myOperator", token3.getText());
    }

    @Test
    public void testBasicDollar() throws Exception {
        xpathLexer = new XPathLexer("$myIdentifier");
        Token token = xpathLexer.nextToken();
        assertEquals(26, token.getType());
        assertEquals("$myIdentifier", token.getText());
    }

    @Test
    public void testBasicPipe() throws Exception {
        xpathLexer = new XPathLexer("myIdentifier | myOperator");
        Token token1 = xpathLexer.nextToken();
        assertEquals(15, token1.getType());
        assertEquals("myIdentifier", token1.getText());

        Token token2 = xpathLexer.nextToken();
        assertEquals(17, token2.getType());

        Token token3 = xpathLexer.nextToken();
        assertEquals(16, token3.getType());
        assertEquals("myOperator", token3.getText());
    }

    @Test
    public void testBasicAt() throws Exception {
        xpathLexer = new XPathLexer("@myIdentifier");
        Token token = xpathLexer.nextToken();
        assertEquals(16, token.getType());
        assertEquals("@myIdentifier", token.getText());
    }

    @Test
    public void testBasicColon() throws Exception {
        xpathLexer = new XPathLexer(":myIdentifier");
        Token token = xpathLexer.nextToken();
        assertEquals(18, token.getType());
        assertEquals(":myIdentifier", token.getText());
    }

    @Test
    public void testBasicDoubleColon() throws Exception {
        xpathLexer = new XPathLexer("::myIdentifier");
        Token token = xpathLexer.nextToken();
        assertEquals(19, token.getType());
        assertEquals("::myIdentifier", token.getText());
    }

    @Test
    public void testBasicNot() throws Exception {
        xpathLexer = new XPathLexer("not(myIdentifier)");
        Token token = xpathLexer.nextToken();
        assertEquals(23, token.getType());

        Token token2 = xpathLexer.nextToken();
        assertEquals(15, token2.getType());
        assertEquals("myIdentifier", token2.getText());
    }

    @Test
    public void testBasicNotEquals() throws Exception {
        xpathLexer = new XPathLexer("not(myIdentifier = myOperator)");
        Token token1 = xpathLexer.nextToken();
        assertEquals(23, token1.getType());

        Token token2 = xpathLexer.nextToken();
        assertEquals(15, token2.getType());
        assertEquals("myIdentifier", token2.getText());

        Token token3 = xpathLexer.nextToken();
        assertEquals(21, token3.getType());

        Token token4 = xpathLexer.nextToken();
        assertEquals(16, token4.getType());
        assertEquals("myOperator", token4.getText());
    }

    @Test
    public void testBasicRelationalOperator() throws Exception {
        xpathLexer = new XPathLexer("myIdentifier < myOperator");
        Token token1 = xpathLexer.nextToken();
        assertEquals(15, token1.getType());
        assertEquals("myIdentifier", token1.getText());

        Token token2 = xpathLexer.nextToken();
        Token token3 = xpathLexer.nextToken();
        assertEquals(7, token2.getType());
        assertEquals("myOperator", token3.getText());
    }

    @Test
    public void testBasicRelationalOperatorWithEquals() throws Exception {
        xpathLexer = new XPathLexer("myIdentifier <= myOperator");
        Token token1 = xpathLexer.nextToken();
        assertEquals(15, token1.getType());
        assertEquals("myIdentifier", token1.getText());

        Token token2 = xpathLexer.nextToken();
        Token token3 = xpathLexer.nextToken();
        assertEquals(8, token2.getType());
        assertEquals("myOperator", token3.getText());
    }

    @Test
    public void testBasicRelationalOperatorWithNotEquals() throws Exception {
        xpathLexer = new XPathLexer("myIdentifier <> myOperator");
        Token token1 = xpathLexer.nextToken();
        assertEquals(15, token1.getType());
        assertEquals("myIdentifier", token1.getText());

        Token token2 = xpathLexer.nextToken();
        Token token3 = xpathLexer.nextToken();
        assertEquals(22, token2.getType());
        assertEquals("myOperator", token3.getText());
    }

    @Test
    public void testSlashes() throws Exception {
        xpathLexer = new XPathLexer("/myIdentifier");
        Token token = xpathLexer.nextToken();
        assertEquals(11, token.getType());
        assertEquals("/myIdentifier", token.getText());

        token = xpathLexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("myIdentifier", token.getText());
    }

    @Test
    public void testSlashesWithTwoSlashes() throws Exception {
        xpathLexer = new XPathLexer("/myIdentifier/");
        Token token = xpathLexer.nextToken();
        assertEquals(11, token.getType());
        assertEquals("/myIdentifier/", token.getText());

        token = xpathLexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("myIdentifier", token.getText());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testEndOfXPath() throws Exception {
        xpathLexer = new XPathLexer("myIdentifier");
        Token token1 = xpathLexer.nextToken();
        assertEquals(15, token1.getType());
        assertEquals("myIdentifier", token1.getText());

        Token token2 = xpathLexer.nextToken();
        assertEquals(Token.TYPE_EOF, token2.getType());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testEmptyXPath() throws Exception {
        xpathLexer = new XPathLexer("");
        Token token = xpathLexer.nextToken();
        assertEquals(Token.TYPE_EOF, token.getType());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testWhitespaceOnlyXPath() throws Exception {
        xpathLexer = new XPathLexer("   ");
        Token token = xpathLexer.nextToken();
        assertEquals(Token.TYPE_EOF, token.getType());
    }
}