package com.werken.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XPathLexerTest {
    @Test
    public void testNextToken_EmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertEquals(new Token(-2, "", 0, 0), token);
    }

    @Test
    public void testNextToken_SingleCharacter() {
        XPathLexer lexer = new XPathLexer("a");
        Token token = lexer.nextToken();
        assertEquals(new Token(1, "", 0, 1), token);
    }

    @Test
    public void testNextToken_SingleCharacterWithWhitespace() {
        XPathLexer lexer = new XPathLexer(" a ");
        Token token = lexer.nextToken();
        assertEquals(new Token(1, "", 0, 1), token);
    }

    @Test
    public void testNextToken_Identifier() {
        XPathLexer lexer = new XPathLexer("myIdentifier");
        Token token = lexer.nextToken();
        assertEquals(new Token(15, "", 0, 12), token);
    }

    @Test
    public void testNextToken_OperatorName() {
        XPathLexer lexer = new XPathLexer(" and ");
        Token token = lexer.nextToken();
        assertEquals(new Token(28, "", 0, 3), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 4, 5), token);
    }

    @Test
    public void testNextToken_Number() {
        XPathLexer lexer = new XPathLexer("123");
        Token token = lexer.nextToken();
        assertEquals(new Token(31, "", 0, 3), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 4, 5), token);
    }

    @Test
    public void testNextToken_NumberWithDecimal() {
        XPathLexer lexer = new XPathLexer("123.45");
        Token token = lexer.nextToken();
        assertEquals(new Token(30, "", 0, 3), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 4, 6), token);
    }

    @Test
    public void testNextToken_Dot() {
        XPathLexer lexer = new XPathLexer(".");
        Token token = lexer.nextToken();
        assertEquals(new Token(13, "", 0, 1), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 2, 3), token);
    }

    @Test
    public void testNextToken_MultipleDots() {
        XPathLexer lexer = new XPathLexer("....");
        Token token = lexer.nextToken();
        assertEquals(new Token(14, "", 0, 2), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 3, 4), token);
    }

    @Test
    public void testNextToken_Whitespace() {
        XPathLexer lexer = new XPathLexer("   ");
        Token token = lexer.nextToken();
        assertEquals(new Token(-2, "", 0, 0), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 4, 5), token);
    }

    @Test
    public void testNextToken_Comment() {
        XPathLexer lexer = new XPathLexer("-- This is a comment");
        Token token = lexer.nextToken();
        assertEquals(new Token(-2, "", 0, 0), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 13, 18), token);
    }

    @Test
    public void testNextToken_SingleQuotedString() {
        XPathLexer lexer = new XPathLexer("'single'");
        Token token = lexer.nextToken();
        assertEquals(new Token(27, "", 0, 7), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 8, 11), token);
    }

    @Test
    public void testNextToken_DoubleQuotedString() {
        XPathLexer lexer = new XPathLexer("\"double\"");
        Token token = lexer.nextToken();
        assertEquals(new Token(27, "", 0, 7), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 8, 11), token);
    }

    @Test
    public void testNextToken_SingleCharacterWithQuotes() {
        XPathLexer lexer = new XPathLexer("'/'");
        Token token = lexer.nextToken();
        assertEquals(new Token(11, "", 0, 2), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 3, 4), token);
    }

    @Test
    public void testNextToken_Slashes() {
        XPathLexer lexer = new XPathLexer("/");
        Token token = lexer.nextToken();
        assertEquals(new Token(11, "", 0, 1), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 2, 3), token);
    }

    @Test
    public void testNextToken_Dollar() {
        XPathLexer lexer = new XPathLexer("$");
        Token token = lexer.nextToken();
        assertEquals(new Token(26, "", 0, 1), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 2, 3), token);
    }

    @Test
    public void testNextToken_Comma() {
        XPathLexer lexer = new XPathLexer(",");
        Token token = lexer.nextToken();
        assertEquals(new Token(32, "", 0, 1), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 2, 3), token);
    }

    @Test
    public void testNextToken_Equals() {
        XPathLexer lexer = new XPathLexer("=");
        Token token = lexer.nextToken();
        assertEquals(new Token(21, "", 0, 1), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 2, 3), token);
    }

    @Test
    public void testNextToken_Minus() {
        XPathLexer lexer = new XPathLexer("-");
        Token token = lexer.nextToken();
        assertEquals(new Token(6, "", 0, 1), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 2, 3), token);
    }

    @Test
    public void testNextToken_Plus() {
        XPathLexer lexer = new XPathLexer("+");
        Token token = lexer.nextToken();
        assertEquals(new Token(5, "", 0, 1), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 2, 3), token);
    }

    @Test
    public void testNextToken_Not() {
        XPathLexer lexer = new XPathLexer("!");
        Token token = lexer.nextToken();
        assertEquals(new Token(23, "", 0, 1), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 2, 3), token);
    }

    @Test
    public void testNextToken_NotEquals() {
        XPathLexer lexer = new XPathLexer("!=");
        Token token = lexer.nextToken();
        assertEquals(new Token(22, "", 0, 2), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 4, 5), token);
    }

    @Test
    public void testNextToken_LessThan() {
        XPathLexer lexer = new XPathLexer("<");
        Token token = lexer.nextToken();
        assertEquals(new Token(7, "", 0, 1), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 2, 3), token);
    }

    @Test
    public void testNextToken_LessThanOrEqual() {
        XPathLexer lexer = new XPathLexer("<=");
        Token token = lexer.nextToken();
        assertEquals(new Token(8, "", 0, 2), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 4, 5), token);
    }

    @Test
    public void testNextToken_GreaterThan() {
        XPathLexer lexer = new XPathLexer(">");
        Token token = lexer.nextToken();
        assertEquals(new Token(9, "", 0, 1), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 2, 3), token);
    }

    @Test
    public void testNextToken_GreaterThanOrEqual() {
        XPathLexer lexer = new XPathLexer(">=");
        Token token = lexer.nextToken();
        assertEquals(new Token(10, "", 0, 2), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 4, 5), token);
    }

    @Test
    public void testNextToken_Modulo() {
        XPathLexer lexer = new XPathLexer("mod");
        Token token = lexer.nextToken();
        assertEquals(new Token(25, "", 0, 4), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 8, 11), token);
    }

    @Test
    public void testNextToken_Divide() {
        XPathLexer lexer = new XPathLexer("div");
        Token token = lexer.nextToken();
        assertEquals(new Token(24, "", 0, 4), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 8, 11), token);
    }

    @Test
    public void testNextToken_And() {
        XPathLexer lexer = new XPathLexer("and");
        Token token = lexer.nextToken();
        assertEquals(new Token(28, "", 0, 4), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 8, 11), token);
    }

    @Test
    public void testNextToken_Or() {
        XPathLexer lexer = new XPathLexer("or");
        Token token = lexer.nextToken();
        assertEquals(new Token(29, "", 0, 3), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 6, 9), token);
    }

    @Test
    public void testNextToken_Parentheses_Open() {
        XPathLexer lexer = new XPathLexer("(");
        Token token = lexer.nextToken();
        assertEquals(new Token(1, "", 0, 1), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 2, 3), token);
    }

    @Test
    public void testNextToken_Parentheses_Close() {
        XPathLexer lexer = new XPathLexer(")");
        Token token = lexer.nextToken();
        assertEquals(new Token(2, "", 0, 1), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 2, 3), token);
    }

    @Test
    public void testNextToken_Brackets_Open() {
        XPathLexer lexer = new XPathLexer("[");
        Token token = lexer.nextToken();
        assertEquals(new Token(3, "", 0, 1), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 2, 3), token);
    }

    @Test
    public void testNextToken_Brackets_Close() {
        XPathLexer lexer = new XPathLexer("]");
        Token token = lexer.nextToken();
        assertEquals(new Token(4, "", 0, 1), token);

        token = lexer.nextToken();
        assertEquals(new Token(1, "", 2, 3), token);
    }
}