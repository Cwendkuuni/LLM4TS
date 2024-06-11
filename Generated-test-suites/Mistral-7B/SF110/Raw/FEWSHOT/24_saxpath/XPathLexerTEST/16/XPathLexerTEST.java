package com.werken.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XPathLexerTest {

    @Test
    public void testNextTokenEmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testNextTokenSingleCharacter() {
        XPathLexer lexer = new XPathLexer("a");
        Token token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getText());
    }

    @Test
    public void testNextTokenMultipleCharacters() {
        XPathLexer lexer = new XPathLexer("abcd");
        Token token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("abcd", token.getText());
    }

    @Test
    public void testNextTokenWhitespace() {
        XPathLexer lexer = new XPathLexer(" \t abc ");
        Token token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testNextTokenSingleCharacterIdentifiers() {
        XPathLexer lexer = new XPathLexer("_ a-b c_d e_f g_0123");
        Token token = null;
        int i = 0;
        while (token == null && i < 7) {
            token = lexer.nextToken();
            if (token != null) {
                assertEquals(TokenType.IDENTIFIER, token.getTokenType());
                switch (i) {
                    case 0:
                        assertEquals("_", token.getText());
                        break;
                    case 1:
                        assertEquals("a", token.getText());
                        break;
                    case 2:
                        assertEquals("b", token.getText());
                        break;
                    case 3:
                        assertEquals("c", token.getText());
                        break;
                    case 4:
                        assertEquals("d", token.getText());
                        break;
                    case 5:
                        assertEquals("e", token.getText());
                        break;
                    case 6:
                        assertEquals("f", token.getText());
                        break;
                    default:
                        assertEquals("g", token.getText());
                }
            }
            i++;
        }
    }

    @Test
    public void testNextTokenOperators() {
        XPathLexer lexer = new XPathLexer("and or mod div and or");
        Token token = null;
        int i = 0;
        while (token == null && i < 6) {
            token = lexer.nextToken();
            if (token != null) {
                switch (i) {
                    case 0:
                        assertEquals(TokenType.AND, token.getTokenType());
                        break;
                    case 1:
                        assertEquals(TokenType.OR, token.getTokenType());
                        break;
                    case 2:
                        assertEquals(TokenType.MOD, token.getTokenType());
                        break;
                    case 3:
                        assertEquals(TokenType.DIV, token.getTokenType());
                        break;
                    case 4:
                        assertEquals(TokenType.AND, token.getTokenType());
                        break;
                    case 5:
                        assertEquals(TokenType.OR, token.getTokenType());
                        break;
                }
            }
            i++;
        }
    }

    @Test
    public void testNextTokenNumber() {
        XPathLexer lexer = new XPathLexer("123.456");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("123.456", token.getText());
    }

    @Test
    public void testNextTokenDoubleColon() {
        XPathLexer lexer = new XPathLexer("::");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOUBLE_COLON, token.getTokenType());
    }

    @Test
    public void testNextTokenAtSymbol() {
        XPathLexer lexer = new XPathLexer("@");
        Token token = lexer.nextToken();
        assertEquals(TokenType.AT, token.getTokenType());
    }

    @Test
    public void testNextTokenColon() {
        XPathLexer lexer = new XPathLexer(":");
        Token token = lexer.nextToken();
        assertEquals(TokenType.COLON, token.getTokenType());
    }

    @Test
    public void testNextTokenPipe() {
        XPathLexer lexer = new XPathLexer("|");
        Token token = lexer.nextToken();
        assertEquals(TokenType.PIPE, token.getTokenType());
    }

    @Test
    public void testNextTokenDollar() {
        XPathLexer lexer = new XPathLexer("$");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOLLAR, token.getTokenType());
    }

    @Test
    public void testNextTokenSlash() {
        XPathLexer lexer = new XPathLexer("/");
        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
    }

    @Test
    public void testNextTokenComma() {
        XPathLexer lexer = new XPathLexer(",");
        Token token = lexer.nextToken();
        assertEquals(TokenType.COMMA, token.getTokenType());
    }

    @Test
    public void testNextTokenEquals() {
        XPathLexer lexer = new XPathLexer("=");
        Token token = lexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());
    }

    @Test
    public void testNextTokenMinus() {
        XPathLexer lexer = new XPathLexer("-");
        Token token = lexer.nextToken();
        assertEquals(TokenType.MINUS, token.getTokenType());
    }

    @Test
    public void testNextTokenPlus() {
        XPathLexer lexer = new XPathLexer("+");
        Token token = lexer.nextToken();
        assertEquals(TokenType.PLUS, token.getTokenType());
    }

    @Test
    public void testNextTokenLeftParenthesis() {
        XPathLexer lexer = new XPathLexer("(");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LEFT_PAREN, token.getTokenType());
    }

    @Test
    public void testNextTokenRightParenthesis() {
        XPathLexer lexer = new XPathLexer(")");
        Token token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_PAREN, token.getTokenType());
    }

    @Test
    public void testNextTokenLeftBracket() {
        XPathLexer lexer = new XPathLexer("[");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
    }

    @Test
    public void testNextTokenRightBracket() {
        XPathLexer lexer = new XPathLexer("]");
        Token token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
    }

    @Test
    public void testNextTokenNot() {
        XPathLexer lexer = new XPathLexer("!");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NOT, token.getTokenType());
    }

    @Test
    public void testNextTokenNotEquals() {
        XPathLexer lexer = new XPathLexer("!= ");
        Token token = lexer.nextToken();
        assertEquals(TokenType.NOT_EQUALS, token.getTokenType());
    }

    @Test
    public void testNextTokenLessThan() {
        XPathLexer lexer = new XPathLexer("<");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LESS_THAN, token.getTokenType());
    }

    @Test
    public void testNextTokenLessThanOrEqual() {
        XPathLexer lexer = new XPathLexer("<=");
        Token token = lexer.nextToken();
        assertEquals(TokenType.LESS_THAN_OR_EQUAL, token.getTokenType());
    }

    @Test
    public void testNextTokenGreaterThan() {
        XPathLexer lexer = new XPathLexer(">");
        Token token = lexer.nextToken();
        assertEquals(TokenType.GREATER_THAN, token.getTokenType());
    }

    @Test
    public void testNextTokenGreaterThanOrEqual() {
        XPathLexer lexer = new XPathLexer(">=");
        Token token = lexer.nextToken();
        assertEquals(TokenType.GREATER_THAN_OR_EQUAL, token.getTokenType());
    }

    @Test
    public void testNextTokenStringLiteral() {
        XPathLexer lexer = new XPathLexer("'single-quoted'");
        Token token = lexer.nextToken();
        assertEquals(TokenType.STRING_LITERAL, token.getTokenType());
        assertEquals("single-quoted", token.getText());
    }

    @Test
    public void testNextTokenDoubleQuotedStringLiteral() {
        XPathLexer lexer = new XPathLexer("\"double-quoted\"");
        Token token = lexer.nextToken();
        assertEquals(TokenType.STRING_LITERAL, token.getTokenType());
        assertEquals("double-quoted", token.getText());
    }

    @Test
    public void testNextTokenDoubleColonDoubleColon() {
        XPathLexer lexer = new XPathLexer("::::");
        Token token = lexer.nextToken();
        assertEquals(TokenType.DOUBLE_COLON_DOUBLE_COLON, token.getTokenType());
    }
}