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
    public void testSimpleIdentifiers() {
        XPathLexer lexer = new XPathLexer("a b c d e f g h i j k l m n o p q r s t u v w x y z");
        Token token = null;
        for (int i = 0; i < 26; i++) {
            token = lexer.nextToken();
            assertEquals(String.valueOf((char) ('a' + i)), token.getText());
        }
        assertNull(lexer.nextToken());
    }

    @Test
    public void testOperators() {
        XPathLexer lexer = new XPathLexer("and or mod div mod div and or");
        Token token = null;
        token = lexer.nextToken();
        assertEquals("and", token.getText());
        token = lexer.nextToken();
        assertEquals("or", token.getText());
        token = lexer.nextToken();
        assertEquals("mod", token.getText());
        token = lexer.nextToken();
        assertEquals("div", token.getText());
        token = lexer.nextToken();
        assertEquals("mod", token.getText());
        token = lexer.nextToken();
        assertEquals("div", token.getText());
        token = lexer.nextToken();
        assertEquals("and", token.getText());
        token = lexer.nextToken();
        assertEquals("or", token.getText());
        assertNull(lexer.nextToken());
    }

    @Test
    public void testWhitespace() {
        XPathLexer lexer = new XPathLexer(" a b c \t\n  d\r e");
        Token token = null;
        token = lexer.nextToken();
        assertEquals("a", token.getText());
        token = lexer.nextToken();
        assertEquals("b", token.getText());
        token = lexer.nextToken();
        assertNull(token);
        token = lexer.nextToken();
        assertEquals("d", token.getText());
        token = lexer.nextToken();
        assertEquals("e", token.getText());
        assertNull(lexer.nextToken());
    }

    @Test
    public void testSlashes() {
        XPathLexer lexer = new XPathLexer("/a/b/c");
        Token token = null;
        token = lexer.nextToken();
        assertEquals("/", token.getText());
        token = lexer.nextToken();
        assertEquals("a", token.getText());
        token = lexer.nextToken();
        assertEquals("/", token.getText());
        token = lexer.nextToken();
        assertEquals("b", token.getText());
        token = lexer.nextToken();
        assertEquals("/", token.getText());
        token = lexer.nextToken();
        assertEquals("c", token.getText());
        assertNull(lexer.nextToken());
    }

    @Test
    public void testNumber() {
        XPathLexer lexer = new XPathLexer("1 2 2.1 100 1a2b");
        Token token = null;
        token = lexer.nextToken();
        assertEquals("1", token.getText());
        token = lexer.nextToken();
        assertEquals("2", token.getText());
        token = lexer.nextToken();
        assertEquals("2.1", token.getText());
        token = lexer.nextToken();
        assertEquals("100", token.getText());
        token = lexer.nextToken();
        assertEquals("1a2b", token.getText());
        assertNull(lexer.nextToken());
    }

    @Test
    public void testQuotedString() {
        XPathLexer lexer = new XPathLexer("'abc' \"def\" 'hij\"kl'");
        Token token = null;
        token = lexer.nextToken();
        assertEquals("'abc'", token.getText());
        token = lexer.nextToken();
        assertEquals("\"def\"", token.getText());
        token = lexer.nextToken();
        assertEquals("hij\"kl", token.getText());
        assertNull(lexer.nextToken());
    }

    @Test
    public void testBrackets() {
        XPathLexer lexer = new XPathLexer("[1][2][3-5]");
        Token token = null;
        token = lexer.nextToken();
        assertEquals("[", token.getText());
        token = lexer.nextToken();
        assertEquals("1", token.getText());
        token = lexer.nextToken();
        assertEquals("]", token.getText());
        token = lexer.nextToken();
        assertEquals("[", token.getText());
        token = lexer.nextToken();
        assertEquals("2", token.getText());
        token = lexer.nextToken();
        assertEquals("]", token.getText());
        token = lexer.nextToken();
        assertEquals("[", token.getText());
        token = lexer.nextToken();
        assertEquals("3-5", token.getText());
        token = lexer.nextToken();
        assertEquals("]", token.getText());
        assertNull(lexer.nextToken());
    }

    @Test
    public void testParentheses() {
        XPathLexer lexer = new XPathLexer("(a)(b) (c d) e");
        Token token = null;
        token = lexer.nextToken();
        assertEquals("(", token.getText());
        token = lexer.nextToken();
        assertEquals("a", token.getText());
        token = lexer.nextToken();
        assertEquals(")", token.getText());
        token = lexer.nextToken();
        assertEquals("(", token.getText());
        token = lexer.nextToken();
        assertEquals("b", token.getText());
        token = lexer.nextToken();
        assertEquals(")", token.getText());
        token = lexer.nextToken();
        assertEquals("(", token.getText());
        token = lexer.nextToken();
        assertEquals("c", token.getText());
        token = lexer.nextToken();
        assertEquals(" ", token.getText());
        token = lexer.nextToken();
        assertEquals("d", token.getText());
        token = lexer.nextToken();
        assertEquals(")", token.getText());
        token = lexer.nextToken();
        assertEquals("e", token.getText());
        assertNull(lexer.nextToken());
    }

    @Test
    public void testComplexXPath() {
        XPathLexer lexer = new XPathLexer("//a[@id='ID1'][@class='class1']/b/c[1]/@data-test");
        Token token = null;
        token = lexer.nextToken();
        assertEquals("//", token.getText());
        token = lexer.nextToken();
        assertEquals("a", token.getText());
        token = lexer.nextToken();
        assertEquals("[@", token.getText());
        token = lexer.nextToken();
        assertEquals("id", token.getText());
        token = lexer.nextToken();
        assertEquals("='", token.getText());
        token = lexer.nextToken();
        assertEquals("ID1", token.getText());
        token = lexer.nextToken();
        assertEquals("']", token.getText());
        token = lexer.nextToken();
        assertEquals("/", token.getText());
        token = lexer.nextToken();
        assertEquals("b", token.getText());
        token = lexer.nextToken();
        assertEquals("/", token.getText());
        token = lexer.nextToken();
        assertEquals("c", token.getText());
        token = lexer.nextToken();
        assertEquals("[1]", token.getText());
        token = lexer.nextToken();
        assertEquals("/", token.getText());
        token = lexer.nextToken();
        assertEquals("@", token.getText());
        token = lexer.nextToken();
        assertEquals("data-test", token.getText());
        assertNull(lexer.nextToken());
    }
}