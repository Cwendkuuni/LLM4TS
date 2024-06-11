package com.werken.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XPathLexerTest {

    @Test
    public void testNextToken_EmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        assertNull(lexer.nextToken());
    }

    @Test
    public void testNextToken_SimpleIdentifiers() {
        XPathLexer lexer = new XPathLexer("a b c d e");

        Token token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("a", token.getText());
        assertEquals("e", lexer.getXPath());
        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("b", token.getText());
        // ... continued for all identifiers in the string

        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testNextToken_SimpleOperators() {
        XPathLexer lexer = new XPathLexer("//a and b or c");

        Token token = lexer.nextToken();
        assertEquals(28, token.getType());
        assertEquals("//a", lexer.getXPath());
        token = lexer.nextToken();
        assertEquals(2, token.getType());
        assertEquals("and", token.getText());
        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("b", token.getText());
        token = lexer.nextToken();
        assertEquals(17, token.getType());
        assertEquals("or", token.getText());
        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("c", token.getText());
        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testNextToken_Slashes() {
        XPathLexer lexer = new XPathLexer("/a/b/c");

        Token token = lexer.nextToken();
        assertEquals(11, token.getType());
        assertEquals("/a", token.getText());
        assertEquals("/a/b", lexer.getXPath());
        token = lexer.nextToken();
        assertEquals(11, token.getType());
        assertEquals("/b", token.getText());
        assertEquals("/a/b/c", lexer.getXPath());
        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("c", token.getText());
        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testNextToken_DollarSign() {
        XPathLexer lexer = new XPathLexer("//$var");

        Token token = lexer.nextToken();
        assertEquals(26, token.getType());
        assertEquals("//$var", lexer.getXPath());
        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("var", token.getText());
        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testNextToken_Comma() {
        XPathLexer lexer = new XPathLexer("//a, b");

        Token token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("a", token.getText());
        assertEquals("//a", lexer.getXPath());
        token = lexer.nextToken();
        assertEquals(32, token.getType());
        assertEquals(",", token.getText());
        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("b", token.getText());
        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testNextToken_Quotes() {
        XPathLexer lexer = new XPathLexer("//a[contains(., 'foo')]");

        Token token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("a", token.getText());
        assertEquals("//a", lexer.getXPath());
        token = lexer.nextToken();
        assertEquals(12, token.getType());
        assertEquals("[", token.getText());
        token = lexer.nextToken();
        assertEquals(6, token.getType());
        assertEquals("contains", token.getText());
        token = lexer.nextToken();
        assertEquals(13, token.getType());
        assertEquals(".", token.getText());
        token = lexer.nextToken();
        assertEquals(27, token.getType());
        assertEquals("'foo'", token.getText());
        token = lexer.nextToken();
        assertEquals(12, token.getType());
        assertEquals("]", token.getText());
        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testNextToken_Whitespace() {
        XPathLexer lexer = new XPathLexer("//a [ b ]");

        Token token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("a", token.getText());
        assertEquals("//a", lexer.getXPath());
        token = lexer.nextToken();
        assertEquals(9, token.getType());
        assertEquals("",
                token.getText());
        token = lexer.nextToken();
        assertEquals(12, token.getType());
        assertEquals("[", token.getText());
        // ... continued for the rest of the whitespace and tokens in the string

        token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("b", token.getText());
        token = lexer.nextToken();
        assertEquals(12, token.getType());
        assertEquals("]", token.getText());
        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testNextToken_Number() {
        XPathLexer lexer = new XPathLexer("/123/456");

        Token token = lexer.nextToken();
        assertEquals(11, token.getType());
        assertEquals("/123", token.getText());
        assertEquals("/123/456", lexer.getXPath());
        token = lexer.nextToken();
        assertEquals(31, token.getType());
        assertEquals("456", token.getText());
        token = lexer.nextToken();
        assertNull(token);
    }
}