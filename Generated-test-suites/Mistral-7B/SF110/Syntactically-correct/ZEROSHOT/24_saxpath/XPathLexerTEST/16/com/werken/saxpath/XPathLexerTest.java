package com.werken.saxpath;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XPathLexerTest {

    @Test
    public void testNextToken_EmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertEquals(-2, token.getTokenType());
    }

    @Test
    public void testNextToken_Whitespace() {
        XPathLexer lexer = new XPathLexer("   a = b");
        Token token = lexer.nextToken();
        assertEquals(13, token.getTokenType()); // DOTS
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // IDENTIFIER
        token = lexer.nextToken();
        assertEquals(18, token.getTokenType()); // COLON
    }

    @Test
    public void testNextToken_SingleCharacter() {
        XPathLexer lexer = new XPathLexer("a");
        Token token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // IDENTIFIER
        assertEquals("a", token.getValue());

        lexer = new XPathLexer("/");
        token = lexer.nextToken();
        assertEquals(11, token.getTokenType()); // SLASHES
        assertEquals("/", token.getValue());

        lexer = new XPathLexer(".");
        token = lexer.nextToken();
        assertEquals(13, token.getTokenType()); // DOTS
        assertEquals(".", token.getValue());

        lexer = new XPathLexer("+");
        token = lexer.nextToken();
        assertEquals(5, token.getTokenType()); // PLUS
        assertEquals("+", token.getValue());

        lexer = new XPathLexer("-");
        token = lexer.nextToken();
        assertEquals(6, token.getTokenType()); // MINUS
        assertEquals("-", token.getValue());

        lexer = new XPathLexer("=");
        token = lexer.nextToken();
        assertEquals(21, token.getTokenType()); // EQUALS
        assertEquals("=", token.getValue());

        lexer = new XPathLexer("!");
        token = lexer.nextToken();
        assertEquals(23, token.getTokenType()); // NOT
        assertEquals("!", token.getValue());

        lexer = new XPathLexer("|");
        token = lexer.nextToken();
        assertEquals(17, token.getTokenType()); // PIPE
        assertEquals("|", token.getValue());

        lexer = new XPathLexer("(");
        token = lexer.nextToken();
        assertEquals(1, token.getTokenType()); // LEFT_PAREN
        assertEquals("(", token.getValue());

        lexer = new XPathLexer(")");
        token = lexer.nextToken();
        assertEquals(2, token.getTokenType()); // RIGHT_PAREN
        assertEquals(")", token.getValue());

        lexer = new XPathLexer("[");
        token = lexer.nextToken();
        assertEquals(3, token.getTokenType()); // LEFT_BRACKET
        assertEquals("[", token.getValue());

        lexer = new XPathLexer("]");
        token = lexer.nextToken();
        assertEquals(4, token.getTokenType()); // RIGHT_BRACKET
        assertEquals("]", token.getValue());

        lexer = new XPathLexer("$");
        token = lexer.nextToken();
        assertEquals(26, token.getTokenType()); // DOLLAR
        assertEquals("$", token.getValue());
    }

    @Test
    public void testNextToken_Identifier() {
        XPathLexer lexer = new XPathLexer("ident123");
        Token token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // IDENTIFIER
        assertEquals("ident123", token.getValue());
    }

    @Test
    public void testNextToken_OperatorName() {
        XPathLexer lexer = new XPathLexer("and");
        Token token = lexer.nextToken();
        assertEquals(28, token.getTokenType()); // AND
        assertEquals("and", token.getValue());

        lexer = new XPathLexer("or");
        token = lexer.nextToken();
        assertEquals(29, token.getTokenType()); // OR
        assertEquals("or", token.getValue());

        lexer = new XPathLexer("mod");
        token = lexer.nextToken();
        assertEquals(25, token.getTokenType()); // MOD
        assertEquals("mod", token.getValue());

        lexer = new XPathLexer("div");
        token = lexer.nextToken();
        assertEquals(24, token.getTokenType()); // DIV
        assertEquals("div", token.getValue());
    }

    @Test
    public void testNextToken_Number() {
        XPathLexer lexer = new XPathLexer("123");
        Token token = lexer.nextToken();
        assertEquals(31, token.getTokenType()); // NUMBER
        assertEquals("123", token.getValue());

        lexer = new XPathLexer("123.45");
        token = lexer.nextToken();
        assertEquals(30, token.getTokenType()); // NUMBER
        assertEquals("123.45", token.getValue());
    }

    @Test
    public void testNextToken_RelationalOperator() {
        XPathLexer lexer = new XPathLexer(">");
        Token token = lexer.nextToken();
        assertEquals(9, token.getTokenType()); // GREATER_THAN
        assertEquals(">", token.getValue());

        lexer = new XPathLexer(">=");
        token = lexer.nextToken();
        assertEquals(10, token.getTokenType()); // GREATER_THAN_OR_EQUALS
        assertEquals(">=", token.getValue());

        lexer = new XPathLexer("<");
        token = lexer.nextToken();
        assertEquals(7, token.getTokenType()); // LESS_THAN
        assertEquals("<", token.getValue());

        lexer = new XPathLexer("<=");
        token = lexer.nextToken();
        assertEquals(8, token.getTokenType()); // LESS_THAN_OR_EQUALS
        assertEquals("<=", token.getValue());

        lexer = new XPathLexer("==");
        token = lexer.nextToken();
        assertEquals(21, token.getTokenType()); // EQUALS
        assertEquals("==", token.getValue());

        lexer = new XPathLexer("!=");
        token = lexer.nextToken();
        assertEquals(22, token.getTokenType()); // NOT_EQUALS
        assertEquals("!=", token.getValue());
    }

    @Test
    public void testNextToken_Literal() {
        XPathLexer lexer = new XPathLexer("\"abc\"");
        Token token = lexer.nextToken();
        assertEquals(27, token.getTokenType()); // LITERAL
        assertEquals("abc", token.getValue());

        lexer = new XPathLexer("'abc'");
        token = lexer.nextToken();
        assertEquals(27, token.getTokenType()); // LITERAL
        assertEquals("abc", token.getValue());
    }

    @Test
    public void testNextToken_Slashes() {
        XPathLexer lexer = new XPathLexer("/a");
        Token token = lexer.nextToken();
        assertEquals(11, token.getTokenType()); // SLASHES
        assertEquals("/a", token.getValue());

        lexer = new XPathLexer("/a/b");
        token = lexer.nextToken();
        assertEquals(11, token.getTokenType()); // SLASHES
        assertEquals("/a", token.getValue());

        token = lexer.nextToken();
        assertEquals(11, token.getTokenType()); // SLASHES
        assertEquals("/b", token.getValue());
    }

    @Test
    public void testNextToken_Comma() {
        XPathLexer lexer = new XPathLexer("a, b");
        Token token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // IDENTIFIER
        assertEquals("a", token.getValue());

        token = lexer.nextToken();
        assertEquals(32, token.getTokenType()); // COMMA
        assertEquals(",", token.getValue());

        token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // IDENTIFIER
        assertEquals("b", token.getValue());
    }

    @Test
    public void testNextToken_Dollar() {
        XPathLexer lexer = new XPathLexer("$a");
        Token token = lexer.nextToken();
        assertEquals(26, token.getTokenType()); // DOLLAR
        assertEquals("$a", token.getValue());
    }

    @Test
    public void testNextToken_At() {
        XPathLexer lexer = new XPathLexer("@a");
        Token token = lexer.nextToken();
        assertEquals(16, token.getTokenType()); // AT
        assertEquals("@a", token.getValue());
    }

    @Test
    public void testNextToken_Colon() {
        XPathLexer lexer = new XPathLexer(":a");
        Token token = lexer.nextToken();
        assertEquals(18, token.getTokenType()); // COLON
        assertEquals(":a", token.getValue());

        lexer = new XPathLexer("::a");
        token = lexer.nextToken();
        assertEquals(19, token.getTokenType()); // DOUBLE_COLON
        assertEquals("::a", token.getValue());
    }

    @Test
    public void testNextToken_Star() {
        XPathLexer lexer = new XPathLexer("*a");
        Token token = lexer.nextToken();
        assertEquals(20, token.getTokenType()); // STAR
        assertEquals("*a", token.getValue());
    }

    @Test
    public void testNextToken_ConsumeAll() {
        XPathLexer lexer = new XPathLexer("a/b/c[d]/@e");
        Token token = lexer.nextToken();
        assertEquals(11, token.getTokenType()); // SLASHES
        assertEquals("/a", token.getValue());

        token = lexer.nextToken();
        assertEquals(11, token.getTokenType()); // SLASHES
        assertEquals("/b", token.getValue());

        token = lexer.nextToken();
        assertEquals(3, token.getTokenType()); // LEFT_BRACKET

        token = lexer.nextToken();
        assertEquals(15, token.getTokenType()); // IDENTIFIER
        assertEquals("d", token.getValue());

        token = lexer.nextToken();
        assertEquals(4, token.getTokenType()); // RIGHT_BRACKET

        token = lexer.nextToken();
        assertEquals(16, token.getTokenType()); // AT
        assertEquals("@e", token.getValue());

        assertNull(lexer.nextToken());
    }
}