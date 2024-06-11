package com.werken.saxpath;

import static org.junit.Assert.*;

import org.junit.Test;

public class XPathLexerTest {

    @Test
    public void testXPathLexer_validXPath() {
        XPathLexer lexer = new XPathLexer("/a/b[c=1][@d='e']/../..//f");

        Token token = null;
        while ((token = lexer.nextToken()) != null) {
            System.out.println("Token type: " + token.getTokenType() + ", Value: " + token.getValue());
            assertTrue(token.getTokenType() >= 0 && token.getTokenType() <= 32);
        }
    }

    @Test
    public void testXPathLexer_emptyXPath() {
        XPathLexer lexer = new XPathLexer("");

        Token token = null;
        while ((token = lexer.nextToken()) != null) {
            fail("Expected end of input, but got: " + token.getValue());
        }
    }

    @Test
    public void testXPathLexer_whitespaceXPath() {
        XPathLexer lexer = new XPathLexer(" //a/b[ c = 1 ] /.. // f ");

        Token token = null;
        while ((token = lexer.nextToken()) != null) {
            if (token.getTokenType() == Token.WHITESPACE) {
                continue;
            }
            System.out.println("Token type: " + token.getTokenType() + ", Value: " + token.getValue());
            assertTrue(token.getTokenType() >= 0 && token.getTokenType() <= 32);
        }
    }

    @Test
    public void testXPathLexer_numberXPath() {
        XPathLexer lexer = new XPathLexer("123");

        Token token = null;
        while ((token = lexer.nextToken()) != null) {
            if (token.getTokenType() == Token.NUMBER) {
                assertEquals(token.getValue(), "123");
            } else {
                fail("Expected number, but got: " + token.getValue());
            }
        }
    }

    @Test
    public void testXPathLexer_identifierXPath() {
        XPathLexer lexer = new XPathLexer("myIdentifier");

        Token token = null;
        while ((token = lexer.nextToken()) != null) {
            if (token.getTokenType() == Token.IDENTIFIER) {
                assertEquals(token.getValue(), "myIdentifier");
            } else {
                fail("Expected identifier, but got: " + token.getValue());
            }
        }
    }

    @Test
    public void testXPathLexer_operatorXPath() {
        XPathLexer lexer = new XPathLexer("a/b=c");

        Token token = null;
        while ((token = lexer.nextToken()) != null) {
            switch (token.getTokenType()) {
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "a");
                    break;
                case Token.RELATIONAL_OPERATOR:
                    assertEquals(token.getValue(), "=");
                    break;
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "b");
                    break;
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "c");
                    break;
                default:
                    fail("Unexpected token type: " + token.getTokenType());
            }
        }
    }

    @Test
    public void testXPathLexer_andOperatorXPath() {
        XPathLexer lexer = new XPathLexer("a/b[c=1] and d/e[f=2]");

        Token token = null;
        while ((token = lexer.nextToken()) != null) {
            switch (token.getTokenType()) {
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "a");
                    break;
                case Token.SLASHES:
                    break;
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "b");
                    break;
                case Token.RELATIONAL_OPERATOR:
                    assertEquals(token.getValue(), "=");
                    break;
                case Token.NUMBER:
                    assertEquals(token.getValue(), "1");
                    break;
                case Token.AND:
                    break;
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "d");
                    break;
                case Token.SLASHES:
                    break;
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "e");
                    break;
                case Token.RELATIONAL_OPERATOR:
                    assertEquals(token.getValue(), "=");
                    break;
                case Token.NUMBER:
                    assertEquals(token.getValue(), "2");
                    break;
                default:
                    fail("Unexpected token type: " + token.getTokenType());
            }
        }
    }

    @Test
    public void testXPathLexer_orOperatorXPath() {
        XPathLexer lexer = new XPathLexer("a/b[c=1] or d/e[f=2]");

        Token token = null;
        while ((token = lexer.nextToken()) != null) {
            switch (token.getTokenType()) {
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "a");
                    break;
                case Token.SLASHES:
                    break;
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "b");
                    break;
                case Token.RELATIONAL_OPERATOR:
                    assertEquals(token.getValue(), "=");
                    break;
                case Token.NUMBER:
                    assertEquals(token.getValue(), "1");
                    break;
                case Token.OR:
                    break;
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "d");
                    break;
                case Token.SLASHES:
                    break;
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "e");
                    break;
                case Token.RELATIONAL_OPERATOR:
                    assertEquals(token.getValue(), "=");
                    break;
                case Token.NUMBER:
                    assertEquals(token.getValue(), "2");
                    break;
                default:
                    fail("Unexpected token type: " + token.getTokenType());
            }
        }
    }

    @Test
    public void testXPathLexer_notOperatorXPath() {
        XPathLexer lexer = new XPathLexer("not(a/b[c=1])");

        Token token = null;
        while ((token = lexer.nextToken()) != null) {
            switch (token.getTokenType()) {
                case Token.NOT:
                    break;
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "a");
                    break;
                case Token.SLASHES:
                    break;
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "b");
                    break;
                case Token.RELATIONAL_OPERATOR:
                    assertEquals(token.getValue(), "=");
                    break;
                case Token.NUMBER:
                    assertEquals(token.getValue(), "1");
                    break;
                default:
                    fail("Unexpected token type: " + token.getTokenType());
            }
        }
    }

    @Test
    public void testXPathLexer_notEqualsOperatorXPath() {
        XPathLexer lexer = new XPathLexer("not(a/b[c=1])[d!='e']");

        Token token = null;
        while ((token = lexer.nextToken()) != null) {
            switch (token.getTokenType()) {
                case Token.NOT:
                    break;
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "a");
                    break;
                case Token.SLASHES:
                    break;
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "b");
                    break;
                case Token.RELATIONAL_OPERATOR:
                    assertEquals(token.getValue(), "=");
                    break;
                case Token.NUMBER:
                    assertEquals(token.getValue(), "1");
                    break;
                case Token.NOT_EQUALS:
                    break;
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "d");
                    break;
                case Token.STRING_LITERAL:
                    assertEquals(token.getValue(), "'e'");
                    break;
                default:
                    fail("Unexpected token type: " + token.getTokenType());
            }
        }
    }

    @Test
    public void testXPathLexer_pipeOperatorXPath() {
        XPathLexer lexer = new XPathLexer("a/(b|c)[d]");

        Token token = null;
        while ((token = lexer.nextToken()) != null) {
            switch (token.getTokenType()) {
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "a");
                    break;
                case Token.SLASHES:
                    break;
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "b");
                    break;
                case Token.PIPE:
                    break;
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "c");
                    break;
                case Token.SLASHES:
                    break;
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "d");
                    break;
                default:
                    fail("Unexpected token type: " + token.getTokenType());
            }
        }
    }

    @Test
    public void testXPathLexer_slashOperatorXPath() {
        XPathLexer lexer = new XPathLexer("a/../b");

        Token token = null;
        while ((token = lexer.nextToken()) != null) {
            switch (token.getTokenType()) {
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "a");
                    break;
                case Token.SLASHES:
                    break;
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "b");
                    break;
                case Token.DOTS:
                    break;
                default:
                    fail("Unexpected token type: " + token.getTokenType());
            }
        }
    }

    @Test
    public void testXPathLexer_starOperatorXPath() {
        XPathLexer lexer = new XPathLexer("a/*");

        Token token = null;
        while ((token = lexer.nextToken()) != null) {
            switch (token.getTokenType()) {
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "a");
                    break;
                case Token.ASTERISK:
                    break;
                default:
                    fail("Unexpected token type: " + token.getTokenType());
            }
        }
    }

    @Test
    public void testXPathLexer_colonOperatorXPath() {
        XPathLexer lexer = new XPathLexer("a:b");

        Token token = null;
        while ((token = lexer.nextToken()) != null) {
            switch (token.getTokenType()) {
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "a");
                    break;
                case Token.COLON:
                    break;
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "b");
                    break;
                default:
                    fail("Unexpected token type: " + token.getTokenType());
            }
        }
    }

    @Test
    public void testXPathLexer_doubleColonOperatorXPath() {
        XPathLexer lexer = new XPathLexer("a::b");

        Token token = null;
        while ((token = lexer.nextToken()) != null) {
            switch (token.getTokenType()) {
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "a");
                    break;
                case Token.DOUBLE_COLON:
                    break;
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "b");
                    break;
                default:
                    fail("Unexpected token type: " + token.getTokenType());
            }
        }
    }

    @Test
    public void testXPathLexer_dollarVariableXPath() {
        XPathLexer lexer = new XPathLexer("$myVariable");

        Token token = null;
        while ((token = lexer.nextToken()) != null) {
            switch (token.getTokenType()) {
                case Token.DOLLAR:
                    break;
                case Token.IDENTIFIER:
                    assertEquals(token.getValue(), "myVariable");
                    break;
                default:
                    fail("Unexpected token type: " + token.getTokenType());
            }
        }
    }

    @Test
    public void testXPathLexer_stringLiteralXPath() {
        XPathLexer lexer = new XPathLexer("'single string'");

        Token token = null;
        while ((token = lexer.nextToken()) != null) {
            switch (token.getTokenType()) {
                case Token.STRING_LITERAL:
                    assertEquals(token.getValue(), "'single string'");
                    break;
                default:
                    fail("Unexpected token type: " + token.getTokenType());
            }
        }
    }

    @Test
    public void testXPathLexer_stringLiteralWithQuotesXPath() {
        XPathLexer lexer = new XPathLexer("'string with 'quotes''");

        Token token = null;
        while ((token = lexer.nextToken()) != null) {
            switch (token.getTokenType()) {
                case Token.STRING_LITERAL:
                    assertEquals(token.getValue(), "'string with 'quotes''");
                    break;
                default:
                    fail("Unexpected token type: " + token.getTokenType());
            }
        }
    }

    @Test
    public void testXPathLexer_stringLiteralWithBackslashXPath() {
        XPathLexer lexer = new XPathLexer("'string\\'with\\ backslashes'");

        Token token = null;
        while ((token = lexer.nextToken()) != null) {
            switch (token.getTokenType()) {
                case Token.STRING_LITERAL:
                    assertEquals(token.getValue(), "'string\\with\\ backslashes'");
                    break;
                default:
                    fail("Unexpected token type: " + token.getTokenType());
            }
        }
    }
}