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
    public void testSimpleXPath() {
        XPathLexer lexer = new XPathLexer("/foo");

        Token token = null;
        while (token.getTokenType() != 11) { // Slash token
            token = lexer.nextToken();
        }
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testXPathWithIdentifiersAndOperators() {
        XPathLexer lexer = new XPathLexer("//element1[@attr1='value1']/element2[@attr2='value2']");

        Token token = null;
        while (token.getTokenType() != 11) { // Slash token
            token = lexer.nextToken();
        }
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals("//", token.getValue());

        token = lexer.nextToken();
        assertEquals("element1", token.getValue());

        token = lexer.nextToken();
        assertEquals("[@", token.getValue());

        token = lexer.nextToken();
        assertEquals("attr1", token.getValue());

        token = lexer.nextToken();
        assertEquals("=", token.getValue());

        token = lexer.nextToken();
        assertEquals("'", token.getValue());

        token = lexer.nextToken();
        assertEquals("value1", token.getValue());

        token = lexer.nextToken();
        assertEquals("]", token.getValue());

        token = lexer.nextToken();
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals("element2", token.getValue());

        token = lexer.nextToken();
        assertEquals("[@", token.getValue());

        token = lexer.nextToken();
        assertEquals("attr2", token.getValue());

        token = lexer.nextToken();
        assertEquals("=", token.getValue());

        token = lexer.nextToken();
        assertEquals("'", token.getValue());

        token = lexer.nextToken();
        assertEquals("value2", token.getValue());

        token = lexer.nextToken();
        assertEquals("]", token.getValue());

        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testXPathWithNumbers() {
        XPathLexer lexer = new XPathLexer("//[1]/following::node()[2]");

        Token token = null;
        while (token.getTokenType() != 11) { // Slash token
            token = lexer.nextToken();
        }
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals("//", token.getValue());

        token = lexer.nextToken();
        assertEquals("[", token.getValue());

        token = lexer.nextToken();
        assertEquals("1", token.getValue());

        token = lexer.nextToken();
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals("following", token.getValue());

        token = lexer.nextToken();
        assertEquals("::", token.getValue());

        token = lexer.nextToken();
        assertEquals("node", token.getValue());

        token = lexer.nextToken();
        assertEquals("[", token.getValue());

        token = lexer.nextToken();
        assertEquals("2", token.getValue());

        token = lexer.nextToken();
        assertEquals("]", token.getValue());

        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testXPathWithQuotes() {
        XPathLexer lexer = new XPathLexer("//[@name='foo']");

        Token token = null;
        while (token.getTokenType() != 11) { // Slash token
            token = lexer.nextToken();
        }
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals("//", token.getValue());

        token = lexer.nextToken();
        assertEquals("[@", token.getValue());

        token = lexer.nextToken();
        assertEquals("name", token.getValue());

        token = lexer.nextToken();
        assertEquals("=", token.getValue());

        token = lexer.nextToken();
        assertEquals("'", token.getValue());

        token = lexer.nextToken();
        assertEquals("foo", token.getValue());

        token = lexer.nextToken();
        assertEquals("]", token.getValue());

        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testXPathWithAndOperator() {
        XPathLexer lexer = new XPathLexer("//element1[@attr1='value1'] and //element2[@attr2='value2']");

        Token token = null;
        while (token.getTokenType() != 11) { // Slash token
            token = lexer.nextToken();
        }
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals("//", token.getValue());

        token = lexer.nextToken();
        assertEquals("element1", token.getValue());

        token = lexer.nextToken();
        assertEquals("[@", token.getValue());

        token = lexer.nextToken();
        assertEquals("attr1", token.getValue());

        token = lexer.nextToken();
        assertEquals("=", token.getValue());

        token = lexer.nextToken();
        assertEquals("'", token.getValue());

        token = lexer.nextToken();
        assertEquals("value1", token.getValue());

        token = lexer.nextToken();
        assertEquals("]", token.getValue());

        token = lexer.nextToken();
        assertEquals(" and ", token.getValue());

        token = lexer.nextToken();
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals("element2", token.getValue());

        token = lexer.nextToken();
        assertEquals("[@", token.getValue());

        token = lexer.nextToken();
        assertEquals("attr2", token.getValue());

        token = lexer.nextToken();
        assertEquals("=", token.getValue());

        token = lexer.nextToken();
        assertEquals("'", token.getValue());

        token = lexer.nextToken();
        assertEquals("value2", token.getValue());

        token = lexer.nextToken();
        assertEquals("]", token.getValue());

        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testXPathWithOrOperator() {
        XPathLexer lexer = new XPathLexer("//element1[@attr1='value1'] or //element2[@attr2='value2']");

        Token token = null;
        while (token.getTokenType() != 11) { // Slash token
            token = lexer.nextToken();
        }
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals("//", token.getValue());

        token = lexer.nextToken();
        assertEquals("element1", token.getValue());

        token = lexer.nextToken();
        assertEquals("[@", token.getValue());

        token = lexer.nextToken();
        assertEquals("attr1", token.getValue());

        token = lexer.nextToken();
        assertEquals("=", token.getValue());

        token = lexer.nextToken();
        assertEquals("'", token.getValue());

        token = lexer.nextToken();
        assertEquals("value1", token.getValue());

        token = lexer.nextToken();
        assertEquals("]", token.getValue());

        token = lexer.nextToken();
        assertEquals(" or ", token.getValue());

        token = lexer.nextToken();
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals("element2", token.getValue());

        token = lexer.nextToken();
        assertEquals("[@", token.getValue());

        token = lexer.nextToken();
        assertEquals("attr2", token.getValue());

        token = lexer.nextToken();
        assertEquals("=", token.getValue());

        token = lexer.nextToken();
        assertEquals("'", token.getValue());

        token = lexer.nextToken();
        assertEquals("value2", token.getValue());

        token = lexer.nextToken();
        assertEquals("]", token.getValue());

        token = lexer.nextToken();
        assertNull(token);
    }
}