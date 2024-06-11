package com.werken.saxpath;

import static org.junit.Assert.*;

import org.junit.Test;

public class XPathLexerTest {

    @Test
    public void testIdentifierOrOperatorName() {
        XPathLexer lexer = new XPathLexer("and(a) or (b and c) div d mod mod");
        Token token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // Identifier
        token = lexer.nextToken();
        assertEquals(28, token.getTokenType()); // And
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // Identifier 'a'
        token = lexer.nextToken();
        assertEquals(32, token.getTokenType()); // Or
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // Identifier 'b'
        token = lexer.nextToken();
        assertEquals(14, token.getTokenType()); // And
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // Identifier 'c'
        token = lexer.nextToken();
        assertEquals(24, token.getTokenType()); // Div
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // Identifier 'd'
        token = lexer.nextToken();
        assertEquals(25, token.getTokenType()); // Mod
        token = lexer.nextToken();
        assertEquals(25, token.getTokenType()); // Mod

        lexer = new XPathLexer("not (a = b) or (c > d and e < f)");
        token = lexer.nextToken();
        assertEquals(23, token.getTokenType()); // Not
        token = lexer.nextToken();
        assertEquals(12, token.getTokenType()); // Left paren
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // Identifier 'a'
        token = lexer.nextToken();
        assertEquals(18, token.getTokenType()); // Equals
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // Identifier 'b'
        token = lexer.nextToken();
        assertEquals(12, token.getTokenType()); // Right paren
        token = lexer.nextToken();
        assertEquals(17, token.getTokenType()); // Or
        token = lexer.nextToken();
        assertEquals(9, token.getTokenType()); // Greater than
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // Identifier 'c'
        token = lexer.nextToken();
        assertEquals(10, token.getTokenType()); // Less than
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // Identifier 'e'
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // Identifier 'f'
    }

    @Test
    public void testNumber() {
        XPathLexer lexer = new XPathLexer("123");
        Token token = lexer.nextToken();
        assertEquals(31, token.getTokenType()); // Number without decimal point
        token = lexer.nextToken();
        assertEquals(-2, token.getTokenType()); // End of string

        lexer = new XPathLexer("123.456");
        token = lexer.nextToken();
        assertEquals(30, token.getTokenType()); // Number with decimal point
        token = lexer.nextToken();
        assertEquals(-2, token.getTokenType()); // End of string
    }

    @Test
    public void testWhitespace() {
        XPathLexer lexer = new XPathLexer("  123   ");
        Token token = lexer.nextToken();
        assertEquals(31, token.getTokenType()); // Number '123'
        token = lexer.nextToken();
        assertEquals(-2, token.getTokenType()); // End of string

        lexer = new XPathLexer("\n123\r\t");
        token = lexer.nextToken();
        assertEquals(31, token.getTokenType()); // Number '123'
        token = lexer.nextToken();
        assertEquals(-2, token.getTokenType()); // End of string
    }

    @Test
    public void testOperator() {
        XPathLexer lexer = new XPathLexer("> = < != + -");
        Token token = lexer.nextToken();
        assertEquals(9, token.getTokenType()); // Greater than
        token = lexer.nextToken();
        assertEquals(18, token.getTokenType()); // Equals
        token = lexer.nextToken();
        assertEquals(7, token.getTokenType()); // Less than
        token = lexer.nextToken();
        assertEquals(22, token.getTokenType()); // Not equals
        token = lexer.nextToken();
        assertEquals(5, token.getTokenType()); // Plus
        token = lexer.nextToken();
        assertEquals(6, token.getTokenType()); // Minus
        token = lexer.nextToken();
        assertEquals(-2, token.getTokenType()); // End of string
    }

    @Test
    public void testLiteral() {
        XPathLexer lexer = new XPathLexer("'string with 'single quotes' and \"double quotes\"");
        Token token = lexer.nextToken();
        assertEquals(27, token.getTokenType()); // Literal
        assertEquals("string with 'single quotes' and \"double quotes\"", token.getValue());
        token = lexer.nextToken();
        assertEquals(-2, token.getTokenType()); // End of string
    }

    @Test
    public void testSlashes() {
        XPathLexer lexer = new XPathLexer("/abc/def");
        Token token = lexer.nextToken();
        assertEquals(11, token.getTokenType()); // Slash for absolute path
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // Identifier 'abc'
        token = lexer.nextToken();
        assertEquals(12, token.getTokenType()); // Slash for next level of elements
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // Identifier 'def'
        token = lexer.nextToken();
        assertEquals(-2, token.getTokenType()); // End of string

        lexer = new XPathLexer("//div[@class='test']");
        token = lexer.nextToken();
        assertEquals(11, token.getTokenType()); // Slash for all elements
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // Identifier 'div'
        token = lexer.nextToken();
        assertEquals(18, token.getTokenType()); // Attribute
        token = lexer.nextToken();
        assertEquals(16, token.getTokenType()); // At sign
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // Identifier 'class'
        token = lexer.nextToken();
        assertEquals(34, token.getTokenType()); // Equals sign
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // Identifier 'test'
        token = lexer.nextToken();
        assertEquals(-2, token.getTokenType()); // End of string
    }

    @Test
    public void testBrackets() {
        XPathLexer lexer = new XPathLexer("[1, 2, 3]");
        Token token = lexer.nextToken();
        assertEquals(3, token.getTokenType()); // Left bracket
        token = lexer.nextToken();
        assertEquals(30, token.getTokenType()); // Number '1'
        token = lexer.nextToken();
        assertEquals(44, token.getTokenType()); // Comma
        token = lexer.nextToken();
        assertEquals(30, token.getTokenType()); // Number '2'
        token = lexer.nextToken();
        assertEquals(44, token.getTokenType()); // Comma
        token = lexer.nextToken();
        assertEquals(30, token.getTokenType()); // Number '3'
        token = lexer.nextToken();
        assertEquals(4, token.getTokenType()); // Right bracket
        token = lexer.nextToken();
        assertEquals(-2, token.getTokenType()); // End of string

        lexer = new XPathLexer("[1 and 2 = 3]");
        token = lexer.nextToken();
        assertEquals(3, token.getTokenType()); // Left bracket
        token = lexer.nextToken();
        assertEquals(28, token.getTokenType()); // And
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // Identifier '1'
        token = lexer.nextToken();
        assertEquals(18, token.getTokenType()); // Equals
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // Identifier '3'
        token = lexer.nextToken();
        assertEquals(41, token.getTokenType()); // Right parenthesis
        token = lexer.nextToken();
        assertEquals(-2, token.getTokenType()); // End of string
    }

    @Test
    public void testParentheses() {
        XPathLexer lexer = new XPathLexer("(a + b) * c");
        Token token = lexer.nextToken();
        assertEquals(1, token.getTokenType()); // Left parenthesis
        token = lexer.nextToken();
        assertEquals(28, token.getTokenType()); // And
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // Identifier 'a'
        token = lexer.nextToken();
        assertEquals(5, token.getTokenType()); // Plus
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // Identifier 'b'
        token = lexer.nextToken();
        assertEquals(29, token.getTokenType()); // Right parenthesis
        token = lexer.nextToken();
        assertEquals(20, token.getTokenType()); // Star
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // Identifier 'c'
        token = lexer.nextToken();
        assertEquals(-2, token.getTokenType()); // End of string
    }

    @Test
    public void testDollarSign() {
        XPathLexer lexer = new XPathLexer("$x");
        Token token = lexer.nextToken();
        assertEquals(26, token.getTokenType()); // Dollar sign
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // Identifier 'x'
        token = lexer.nextToken();
        assertEquals(-2, token.getTokenType()); // End of string
    }
}