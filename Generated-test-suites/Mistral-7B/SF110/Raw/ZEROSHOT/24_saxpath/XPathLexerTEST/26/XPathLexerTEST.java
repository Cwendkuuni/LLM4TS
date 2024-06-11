package com.werken.saxpath;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class XPathLexerTest {

    @Test
    public void testNextToken_EmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertEquals(-2, token.getType());
    }

    @Test
    public void testNextToken_Whitespace() {
        XPathLexer lexer = new XPathLexer("   /a   ");
        Token token = lexer.nextToken();
        assertEquals(-2, token.getType());
    }

    @Test
    public void testNextToken_SingleCharacterIdentifier() {
        XPathLexer lexer = new XPathLexer("a");
        Token token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("a", token.getValue());
    }

    @Test
    public void testNextToken_MultiCharacterIdentifier() {
        XPathLexer lexer = new XPathLexer("identifier");
        Token token = lexer.nextToken();
        assertEquals(15, token.getType());
        assertEquals("identifier", token.getValue());
    }

    @Test
    public void testNextToken_Number() {
        XPathLexer lexer = new XPathLexer("123");
        Token token = lexer.nextToken();
        assertEquals(31, token.getType());
        assertEquals("123", token.getValue());
    }

    @Test
    public void testNextToken_NumberWithDecimalPoint() {
        XPathLexer lexer = new XPathLexer("123.45");
        Token token = lexer.nextToken();
        assertEquals(30, token.getType());
        assertEquals("123.45", token.getValue());
    }

    @Test
    public void testNextToken_SingleCharacterOperator() {
        XPathLexer lexer = new XPathLexer("+");
        Token token = lexer.nextToken();
        assertEquals(5, token.getType());
    }

    @Test
    public void testNextToken_MultiCharacterOperator() {
        XPathLexer lexer = new XPathLexer("and");
        Token token = lexer.nextToken();
        assertEquals(28, token.getType());
    }

    @Test
    public void testNextToken_Dollar() {
        XPathLexer lexer = new XPathLexer("$");
        Token token = lexer.nextToken();
        assertEquals(26, token.getType());
    }

    @Test
    public void testNextToken_LeftParenthesis() {
        XPathLexer lexer = new XPathLexer("(");
        Token token = lexer.nextToken();
        assertEquals(1, token.getType());
    }

    @Test
    public void testNextToken_RightParenthesis() {
        XPathLexer lexer = new XPathLexer(")");
        Token token = lexer.nextToken();
        assertEquals(2, token.getType());
    }

    @Test
    public void testNextToken_LeftBracket() {
        XPathLexer lexer = new XPathLexer("[");
        Token token = lexer.nextToken();
        assertEquals(3, token.getType());
    }

    @Test
    public void testNextToken_RightBracket() {
        XPathLexer lexer = new XPathLexer("]");
        Token token = lexer.nextToken();
        assertEquals(4, token.getType());
    }

    @Test
    public void testNextToken_Comma() {
        XPathLexer lexer = new XPathLexer(",");
        Token token = lexer.nextToken();
        assertEquals(32, token.getType());
    }

    @Test
    public void testNextToken_Equals() {
        XPathLexer lexer = new XPathLexer("=");
        Token token = lexer.nextToken();
        assertEquals(21, token.getType());
    }

    @Test
    public void testNextToken_Minus() {
        XPathLexer lexer = new XPathLexer("-");
        Token token = lexer.nextToken();
        assertEquals(6, token.getType());
    }

    @Test
    public void testNextToken_Plus() {
        XPathLexer lexer = new XPathLexer("+");
        Token token = lexer.nextToken();
        assertEquals(5, token.getType());
    }

    @Test
    public void testNextToken_Div() {
        XPathLexer lexer = new XPathLexer("div");
        Token token = lexer.nextToken();
        assertEquals(24, token.getType());
    }

    @Test
    public void testNextToken_Mod() {
        XPathLexer lexer = new XPathLexer("mod");
        Token token = lexer.nextToken();
        assertEquals(25, token.getType());
    }

    @Test
    public void testNextToken_NotEquals() {
        XPathLexer lexer = new XPathLexer("!=");
        Token token = lexer.nextToken();
        assertEquals(22, token.getType());
    }

    @Test
    public void testNextToken_Not() {
        XPathLexer lexer = new XPathLexer("!");
        Token token = lexer.nextToken();
        assertEquals(23, token.getType());
    }

    @Test
    public void testNextToken_GreaterThan() {
        XPathLexer lexer = new XPathLexer(">");
        Token token = lexer.nextToken();
        assertEquals(9, token.getType());
    }

    @Test
    public void testNextToken_GreaterThanOrEqual() {
        XPathLexer lexer = new XPathLexer(">=");
        Token token = lexer.nextToken();
        assertEquals(10, token.getType());
    }

    @Test
    public void testNextToken_LessThan() {
        XPathLexer lexer = new XPathLexer("<");
        Token token = lexer.nextToken();
        assertEquals(7, token.getType());
    }

    @Test
    public void testNextToken_LessThanOrEqual() {
        XPathLexer lexer = new XPathLexer("<=");
        Token token = lexer.nextToken();
        assertEquals(8, token.getType());
    }

    @Test
    public void testNextToken_Star() {
        XPathLexer lexer = new XPathLexer("*");
        Token token = lexer.nextToken();
        assertEquals(20, token.getType());
    }

    @Test
    public void testNextToken_Slashes() {
        XPathLexer lexer = new XPathLexer("/");
        Token token = lexer.nextToken();
        assertEquals(11, token.getType());
    }

    @Test
    public void testNextToken_DoubleSlashes() {
        XPathLexer lexer = new XPathLexer("//");
        Token token = lexer.nextToken();
        assertEquals(12, token.getType());
    }

    @Test
    public void testNextToken_DoubleColon() {
        XPathLexer lexer = new XPathLexer("::");
        Token token = lexer.nextToken();
        assertEquals(19, token.getType());
    }

    @Test
    public void testNextToken_At() {
        XPathLexer lexer = new XPathLexer("@");
        Token token = lexer.nextToken();
        assertEquals(16, token.getType());
    }

    @Test
    public void testNextToken_PIpe() {
        XPathLexer lexer = new XPathLexer("|");
        Token token = lexer.nextToken();
        assertEquals(17, token.getType());
    }

    @Test
    public void testNextToken_Dot() {
        XPathLexer lexer = new XPathLexer(".");
        Token token = lexer.nextToken();
        assertEquals(13, token.getType());
    }

    @Test
    public void testNextToken_DoubleDot() {
        XPathLexer lexer = new XPathLexer("..");
        Token token = lexer.nextToken();
        assertEquals(14, token.getType());
    }

    @Test
    public void testNextToken_Literal() {
        XPathLexer lexer = new XPathLexer("'single character literal'");
        Token token = lexer.nextToken();
        assertEquals(27, token.getType());
        assertEquals("single character literal", token.getValue());
    }

    @Test
    public void testNextToken_LiteralWithQuotes() {
        XPathLexer lexer = new XPathLexer("\"double character literal\"");
        Token token = lexer.nextToken();
        assertEquals(27, token.getType());
        assertEquals("double character literal", token.getValue());
    }

    @Test
    public void testNextToken_MultipleTokens() {
        XPathLexer lexer = new XPathLexer("and/or(a)//b@c=1 and not(c=2)");
        List<Token> tokens = new java.util.ArrayList<>();
        Token token = null;
        while ((token = lexer.nextToken()) != null) {
            tokens.add(token);
        }
        assertEquals(28, tokens.get(0).getType());
        assertEquals(11, tokens.get(1).getType());
        assertEquals(29, tokens.get(2).getType());
        assertEquals(15, tokens.get(3).getType());
        assertEquals(3, tokens.get(4).getType());
        assertEquals(13, tokens.get(5).getType());
        assertEquals(1, tokens.get(6).getType());
        assertEquals(11, tokens.get(7).getType());
        assertEquals(13, tokens.get(8).getType());
        assertEquals(16, tokens.get(9).getType());
        assertEquals(21, tokens.get(10).getType());
        assertEquals(23, tokens.get(11).getType());
        assertEquals(11, tokens.get(12).getType());
        assertEquals(13, tokens.get(13).getType());
        assertEquals(15, tokens.get(14).getType());
        assertEquals(21, tokens.get(15).getType());
        assertEquals(11, tokens.get(16).getType());
        assertEquals(13, tokens.get(17).getType());
        assertEquals(22, tokens.get(18).getType());
        assertEquals(5, tokens.get(19).getType());
        assertEquals(32, tokens.get(20).getType());
    }
}