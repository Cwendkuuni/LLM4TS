package com.werken.saxpath;

import static org.junit.Assert.*;

import org.junit.Test;

public class XPathLexerTest {
    

    @Test
    public void testConstructor() {
        XPathLexer lexer = new XPathLexer("");
        assertNotNull(lexer);

        XPathLexer lexerWithXPath = new XPathLexer("sampleXPath");
        assertEquals("sampleXPath", lexerWithXPath.getXPath());
    }

    @Test
    public void testNextToken_EmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testNextToken_Whitespace() {
        XPathLexer lexer = new XPathLexer("   \tsampleXPath");
        Token token = lexer.nextToken();
        assertEquals(TokenType.WHITESPACE, token.getTokenType());
    }

    @Test
    public void testNextToken_Identifier() {
        XPathLexer lexer = new XPathLexer("sampleIdentifier");
        Token token = lexer.nextToken();
        assertEquals(15, token.getTokenType());
        assertEquals("sampleIdentifier", token.getValue());
    }

    @Test
    public void testNextToken_OperatorName() {
        XPathLexer lexer = new XPathLexer("sampleOperatorName");
        Token token = lexer.nextToken();
        assertEquals(17, token.getTokenType()); // 'or'
    }

    @Test
    public void testNextToken_Number() {
        XPathLexer lexer = new XPathLexer("123");
        Token token = lexer.nextToken();
        assertEquals(31, token.getTokenType());
        assertEquals("123", token.getValue());

        XPathLexer lexerWithDecimal = new XPathLexer("123.456");
        token = lexer.nextToken();
        assertEquals(30, token.getTokenType());
        assertEquals("123.456", token.getValue());
    }

    @Test
    public void testNextToken_RelationalOperator() {
        XPathLexer lexer = new XPathLexer("sampleXPath < 10");
        Token token = lexer.nextToken();
        assertEquals(7, token.getTokenType());

        XPathLexer lexerWithEquals = new XPathLexer("sampleXPath = 10");
        token = lexer.nextToken();
        assertEquals(21, token.getTokenType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextToken_InvalidCharacter() {
        XPathLexer lexer = new XPathLexer("sampleXPath#");
        Token token = lexer.nextToken();
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testNextToken_Dollar() {
        XPathLexer lexer = new XPathLexer("$sampleXPath");
        Token token = lexer.nextToken();
        assertEquals(26, token.getTokenType());

        XPathLexer lexerWithDollarBeforeIdentifier = new XPathLexer("$identifier");
        token = lexer.nextToken();
        assertEquals(15, token.getTokenType());
    }

    @Test
    public void testNextToken_Literal() {
        XPathLexer lexer = new XPathLexer("'sample'" + "'");
        Token token = lexer.nextToken();
        assertEquals(27, token.getTokenType());
        assertEquals("sample", token.getValue());

        XPathLexer lexerWithSingleQuotedString = new XPathLexer("'single quoted string'");
        token = lexer.nextToken();
        assertEquals(27, token.getTokenType());
        assertEquals("single quoted string", token.getValue());

        XPathLexer lexerWithDoubleQuotedString = new XPathLexer("\"double quoted string\"");
        token = lexer.nextToken();
        assertEquals(27, token.getTokenType());
        assertEquals("double quoted string", token.getValue());
    }

    @Test
    public void testNextToken_Slashes() {
        XPathLexer lexer = new XPathLexer("/sampleXPath");
        Token token = lexer.nextToken();
        assertEquals(11, token.getTokenType());

        XPathLexer lexerWithDoubleSlashes = new XPathLexer("/sampleXPath//anotherIdentifier");
        token = lexer.nextToken();
        assertEquals(12, token.getTokenType());
    }

    @Test
    public void testNextToken_Comma() {
        XPathLexer lexer = new XPathLexer("sampleXPath, anotherIdentifier");
        Token token = lexer.nextToken();
        assertEquals(32, token.getTokenType());

        XPathLexer lexerWithMultipleCommas = new XPathLexer("sampleXPath, anotherIdentifier, thirdIdentifier");
        token = lexer.nextToken();
        assertEquals(32, token.getTokenType());
    }

    @Test
    public void testNextToken_LeftParen() {
        XPathLexer lexer = new XPathLexer("(sampleXPath)");
        Token token = lexer.nextToken();
        assertEquals(1, token.getTokenType());

        XPathLexer lexerWithComplexExpression = new XPathLexer("(sampleXPath/child::node()[3])");
        token = lexer.nextToken();
        assertEquals(1, token.getTokenType());
    }

    @Test
    public void testNextToken_RightParen() {
        XPathLexer lexer = new XPathLexer("(sampleXPath)");
        Token token = lexer.nextToken();
        assertEquals(1, token.getTokenType());

        XPathLexer lexerWithRightParenAfterIdentifier = new XPathLexer("sampleIdentifier)");
        token = lexer.nextToken();
        assertEquals(4, token.getTokenType());
    }

    @Test
    public void testNextToken_LeftBracket() {
        XPathLexer lexer = new XPathLexer("[sampleXPath]");
        Token token = lexer.nextToken();
        assertEquals(3, token.getTokenType());

        XPathLexer lexerWithComplexExpression = new XPathLexer("[sampleXPath[3]]");
        token = lexer.nextToken();
        assertEquals(3, token.getTokenType());
    }

    @Test
    public void testNextToken_RightBracket() {
        XPathLexer lexer = new XPathLexer("[sampleXPath]");
        Token token = lexer.nextToken();
        assertEquals(3, token.getTokenType());

        XPathLexer lexerWithRightBracketAfterIdentifier = new XPathLexer("sampleIdentifier]");
        token = lexer.nextToken();
        assertEquals(4, token.getTokenType());
    }

    @Test
    public void testNextToken_Plus() {
        XPathLexer lexer = new XPathLexer("sampleXPath + 5");
        Token token = lexer.nextToken();
        assertEquals(5, token.getTokenType());
    }

    @Test
    public void testNextToken_Minus() {
        XPathLexer lexer = new XPathLexer("sampleXPath - 5");
        Token token = lexer.nextToken();
        assertEquals(6, token.getTokenType());
    }

    @Test
    public void testNextToken_Star() {
        XPathLexer lexer = new XPathLexer("sampleXPath * 5");
        Token token = lexer.nextToken();
        assertEquals(20, token.getTokenType());
    }

    @Test
    public void testNextToken_At() {
        XPathLexer lexer = new XPathLexer("@sampleXPath");
        Token token = lexer.nextToken();
        assertEquals(16, token.getTokenType());
    }

    @Test
    public void testNextToken_Colon() {
        XPathLexer lexer = new XPathLexer(":sampleXPath");
        Token token = lexer.nextToken();
        assertEquals(18, token.getTokenType());

        XPathLexer lexerWithDoubleColon = new XPathLexer(":sampleXPath::anotherIdentifier");
        token = lexer.nextToken();
        assertEquals(19, token.getTokenType());
    }

    @Test
    public void testNextToken_Not() {
        XPathLexer lexer = new XPathLexer("not(sampleXPath)");
        Token token = lexer.nextToken();
        assertEquals(23, token.getTokenType());
    }

    @Test
    public void testNextToken_NotEquals() {
        XPathLexer lexer = new XPathLexer("not(sampleXPath = 5)");
        Token token = lexer.nextToken();
        assertEquals(22, token.getTokenType());
    }

    
}