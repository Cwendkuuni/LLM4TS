package com.werken.saxpath;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class XPathLexerTest {

    @Test
    public void testEmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        assertNull(lexer.nextToken());
    }

    @Test
    public void testSimpleXPath() {
        XPathLexer lexer = new XPathLexer("/a");

        List<Token> tokens = expectedTokens("/a");

        Token token;
        while ((token = lexer.nextToken()) != null) {
            assertEquals(tokens.get(0), token);
            tokens = tokens.subList(1, tokens.size());
        }
        assertEquals(0, tokens.size());
    }

    @Test
    public void testXPathWithNumber() {
        XPathLexer lexer = new XPathLexer("/a[1]");

        List<Token> tokens = expectedTokens("/a[1]");

        Token token;
        while ((token = lexer.nextToken()) != null) {
            assertEquals(tokens.get(0), token);
            tokens = tokens.subList(1, tokens.size());
        }
        assertEquals(0, tokens.size());
    }

    @Test
    public void testXPathWithRelationalOperator() {
        XPathLexer lexer = new XPathLexer("/a[1 < b]");

        List<Token> tokens = expectedTokens("/a[1 < b]");

        Token token;
        while ((token = lexer.nextToken()) != null) {
            assertEquals(tokens.get(0), token);
            tokens = tokens.subList(1, tokens.size());
        }
        assertEquals(0, tokens.size());
    }

    @Test
    public void testXPathWithAndOperator() {
        XPathLexer lexer = new XPathLexer("/a[1 = b and 1 < c]");

        List<Token> tokens = expectedTokens("/a[1 = b and 1 < c]");

        Token token;
        while ((token = lexer.nextToken()) != null) {
            assertEquals(tokens.get(0), token);
            tokens = tokens.subList(1, tokens.size());
        }
        assertEquals(0, tokens.size());
    }

    @Test
    public void testXPathWithOrOperator() {
        XPathLexer lexer = new XPathLexer("/a[1 = b or 1 < c]");

        List<Token> tokens = expectedTokens("/a[1 = b or 1 < c]");

        Token token;
        while ((token = lexer.nextToken()) != null) {
            assertEquals(tokens.get(0), token);
            tokens = tokens.subList(1, tokens.size());
        }
        assertEquals(0, tokens.size());
    }

    @Test
    public void testXPathWithNotOperator() {
        XPathLexer lexer = new XPathLexer("/not(a[1 = b])");

        List<Token> tokens = expectedTokens("/not(a[1 = b])");

        Token token;
        while ((token = lexer.nextToken()) != null) {
            assertEquals(tokens.get(0), token);
            tokens = tokens.subList(1, tokens.size());
        }
        assertEquals(0, tokens.size());
    }

    @Test
    public void testXPathWithNotEqualsOperator() {
        XPathLexer lexer = new XPathLexer("/a[1 != b]");

        List<Token> tokens = expectedTokens("/a[1 != b]");

        Token token;
        while ((token = lexer.nextToken()) != null) {
            assertEquals(tokens.get(0), token);
            tokens = tokens.subList(1, tokens.size());
        }
        assertEquals(0, tokens.size());
    }

    @Test
    public void testXPathWithIdentifier() {
        XPathLexer lexer = new XPathLexer("/a/b");

        List<Token> tokens = expectedTokens("/a/b");

        Token token;
        while ((token = lexer.nextToken()) != null) {
            assertEquals(tokens.get(0), token);
            tokens = tokens.subList(1, tokens.size());
        }
        assertEquals(0, tokens.size());
    }

    @Test
    public void testXPathWithDollarSign() {
        XPathLexer lexer = new XPathLexer("/$foo");

        List<Token> tokens = expectedTokens("/$foo");

        Token token;
        while ((token = lexer.nextToken()) != null) {
            assertEquals(tokens.get(0), token);
            tokens = tokens.subList(1, tokens.size());
        }
        assertEquals(0, tokens.size());
    }

    @Test
    public void testXPathWithSlashes() {
        XPathLexer lexer = new XPathLexer("/a//b");

        List<Token> tokens = expectedTokens("/a//b");

        Token token;
        while ((token = lexer.nextToken()) != null) {
            assertEquals(tokens.get(0), token);
            tokens = tokens.subList(1, tokens.size());
        }
        assertEquals(0, tokens.size());
    }

    @Test
    public void testXPathWithComma() {
        XPathLexer lexer = new XPathLexer("/a, b");

        List<Token> tokens = expectedTokens("/a, b");

        Token token;
        while ((token = lexer.nextToken()) != null) {
            assertEquals(tokens.get(0), token);
            tokens = tokens.subList(1, tokens.size());
        }
        assertEquals(0, tokens.size());
    }

    @Test
    public void testXPathWithLeftBracket() {
        XPathLexer lexer = new XPathLexer("/[a]");

        List<Token> tokens = expectedTokens("/[a]");

        Token token;
        while ((token = lexer.nextToken()) != null) {
            assertEquals(tokens.get(0), token);
            tokens = tokens.subList(1, tokens.size());
        }
        assertEquals(0, tokens.size());
    }

    @Test
    public void testXPathWithRightBracket() {
        XPathLexer lexer = new XPathLexer("/[a]/b]");

        List<Token> tokens = expectedTokens("/[a]/b]");

        Token token;
        while ((token = lexer.nextToken()) != null) {
            assertEquals(tokens.get(0), token);
            tokens = tokens.subList(1, tokens.size());
        }
        assertEquals(0, tokens.size());
    }

    @Test
    public void testXPathWithLeftParen() {
        XPathLexer lexer = new XPathLexer("/(a)
        b");

        List<Token> tokens = expectedTokens("/(a)
        b");

        Token token;
        while ((token = lexer.nextToken()) != null) {
            assertEquals(tokens.get(0), token);
            tokens = tokens.subList(1, tokens.size());
        }
        assertEquals(0, tokens.size());
    }

    @Test
    public void testXPathWithRightParen() {
        XPathLexer lexer = new XPathLexer("/(a)
        b)
        c");

        List<Token> tokens = expectedTokens("/(a)
        b)
        c");

        Token token;
        while ((token = lexer.nextToken()) != null) {
            assertEquals(tokens.get(0), token);
            tokens = tokens.subList(1, tokens.size());
        }
        assertEquals(0, tokens.size());
    }

    @Test
    public void testXPathWithStar() {
        XPathLexer lexer = new XPathLexer("/a/*");

        List<Token> tokens = expectedTokens("/a/*");

        Token token;
        while ((token = lexer.nextToken()) != null) {
            assertEquals(tokens.get(0), token);
            tokens = tokens.subList(1, tokens.size());
        }
        assertEquals(0, tokens.size());
    }

    @Test
    public void testXPathWithDots() {
        XPathLexer lexer = new XPathLexer("/a/..");

        List<Token> tokens = expectedTokens("/a/..");

        Token token;
        while ((token = lexer.nextToken()) != null) {
            assertEquals(tokens.get(0), token);
            tokens = tokens.subList(1, tokens.size());
        }
        assertEquals(0, tokens.size());
    }

    private List<Token> expectedTokens(String xpath) {
        XPathLexer lexer = new XPathLexer(xpath);
        List<Token> tokens = new java.util.ArrayList<>();
        Token token;
        while ((token = lexer.nextToken()) != null) {
            tokens.add(token);
        }
        return tokens;
    }
}