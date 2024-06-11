package com.werken.saxpath;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XPathLexerTest {

    @Test
    public void testNextToken_EmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        assertEquals(new Token(-2, "", 0, 0), lexer.nextToken());
    }

    @Test
    public void testNextToken_Whitespace() {
        XPathLexer lexer = new XPathLexer("   /a[1]   ");
        assertEquals(new Token(11, "/a[1]", 8, 11), lexer.nextToken());
        assertEquals(new Token(-2, "", 12, 12), lexer.nextToken());
    }

    @Test
    public void testNextToken_SingleCharacter() {
        XPathLexer lexer = new XPathLexer("/a");
        assertEquals(new Token(15, "/a", 0, 1), lexer.nextToken());
    }

    @Test
    public void testNextToken_Identifier() {
        XPathLexer lexer = new XPathLexer("/my:identifier");
        assertEquals(new Token(15, "/my:identifier", 0, 12), lexer.nextToken());
    }

    @Test
    public void testNextToken_Operator() {
        XPathLexer lexer = new XPathLexer("/a/+b");
        assertEquals(new Token(5, "/a/+b", 5, 6), lexer.nextToken());
    }

    @Test
    public void testNextToken_Number() {
        XPathLexer lexer = new XPathLexer("/a[1]");
        assertEquals(new Token(31, "/a[1]", 5, 6), lexer.nextToken());
    }

    @Test
    public void testNextToken_RelationalOperator() {
        XPathLexer lexer = new XPathLexer("/a[1] > b[2]");
        assertEquals(new Token(9, "/a[1] > b[2]", 11, 15), lexer.nextToken());
    }

    @Test
    public void testNextToken_AndOperator() {
        XPathLexer lexer = new XPathLexer("/a[1] and b[2]");
        assertEquals(new Token(28, "/a[1] and b[2]", 13, 19), lexer.nextToken());
    }

    @Test
    public void testNextToken_OrOperator() {
        XPathLexer lexer = new XPathLexer("/a[1] or b[2]");
        assertEquals(new Token(29, "/a[1] or b[2]", 13, 17), lexer.nextToken());
    }

    @Test
    public void testNextToken_DivOperator() {
        XPathLexer lexer = new XPathLexer("/a[1] div b[2]");
        assertEquals(new Token(24, "/a[1] div b[2]", 13, 18), lexer.nextToken());
    }

    @Test
    public void testNextToken_ModOperator() {
        XPathLexer lexer = new XPathLexer("/a[1] mod b[2]");
        assertEquals(new Token(25, "/a[1] mod b[2]", 13, 16), lexer.nextToken());
    }

    @Test
    public void testNextToken_StarOperator() {
        XPathLexer lexer = new XPathLexer("/a[1]/*");
        assertEquals(new Token(20, "/a[1]/*", 11, 13), lexer.nextToken());
    }

    @Test
    public void testNextToken_Literal() {
        XPathLexer lexer = new XPathLexer("/a[1]='test'");
        assertEquals(new Token(16, "/a[1]='test'", 11, 14), lexer.nextToken());
    }

    @Test
    public void testNextToken_DollarSign() {
        XPathLexer lexer = new XPathLexer("/$myVariable");
        assertEquals(new Token(26, "/$myVariable", 0, 11), lexer.nextToken());
    }

    @Test
    public void testNextToken_Comma() {
        XPathLexer lexer = new XPathLexer("/a[1],b[2]");
        assertEquals(new Token(32, "/a[1],b[2]", 11, 13), lexer.nextToken());
    }

    @Test
    public void testNextToken_DoubleColon() {
        XPathLexer lexer = new XPathLexer("/a:my:identifier");
        assertEquals(new Token(19, "/a:my:identifier", 0, 14), lexer.nextToken());
    }

    @Test
    public void testNextToken_Brackets() {
        XPathLexer lexer = new XPathLexer("/[1]");
        assertEquals(new Token(3, "/[1]", 0, 3), lexer.nextToken());
        assertEquals(new Token(4, "", 4, 4), lexer.nextToken());
        assertEquals(new Token(-2, "", 5, 5), lexer.nextToken());
    }

    @Test
    public void testNextToken_Slashes() {
        XPathLexer lexer = new XPathLexer("/a//b");
        assertEquals(new Token(12, "/a//b", 0, 6), lexer.nextToken());
    }

    @Test
    public void testNextToken_Parentheses() {
        XPathLexer lexer = new XPathLexer("/(/a)//b");
        assertEquals(new Token(1, "/(/a)", 0, 3), lexer.nextToken());
        assertEquals(new Token(4, "", 4, 4), lexer.nextToken());
        assertEquals(new Token(12, "/b", 8, 11), lexer.nextToken());
    }

    @Test
    public void testNextToken_MultipleTokens() {
        XPathLexer lexer = new XPathLexer("/a[1]>b[2] and c[3]/d[@id='test']");
        assertEquals(new Token(9, "/a[1]>b[2]", 1, 6), lexer.nextToken());
        assertEquals(new Token(28, "and", 7, 9), lexer.nextToken());
        assertEquals(new Token(13, "/c[3]/d[@id='test']", 10, 27), lexer.nextToken());
    }

    @Test
    public void testNextToken_InvalidCharacter() {
        XPathLexer lexer = new XPathLexer("/invalidCharacter");
        assertNull(lexer.nextToken());
    }
}