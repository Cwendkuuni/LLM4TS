package com.werken.saxpath;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

public class XPathLexerTest {

    private XPathLexer lexer;
    private String xpath;
    private Token token;

    @Before
    public void setUp() {
        xpath = "/a[1]/b/c[1]/@d + e[1]/f[1] = g[1]";
        lexer = new XPathLexer(xpath);
    }

    @After
    public void tearDown() {
        lexer = null;
    }

    @Test
    public void testNextToken_EmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        assertNull(lexer.nextToken());
    }

    @Test
    public void testNextToken_SingleCharacter() {
        lexer.setXPath("/a");
        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
    }

    @Test
    public void testNextToken_Identifier() {
        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getText());
    }

    @Test
    public void testNextToken_Number() {
        lexer.setXPath("/1");
        token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("1", token.getText());
    }

    @Test
    public void testNextToken_Whitespace() {
        lexer.setXPath("/a[1]\t /b[1]");
        token = lexer.nextToken();
        assertEquals(TokenType.WHITESPACE, token.getTokenType());
    }

    @Test
    public void testNextToken_Comma() {
        lexer.setXPath("/a[1],/b[1]");
        token = lexer.nextToken();
        assertEquals(TokenType.COMMA, token.getTokenType());
    }

    @Test
    public void testNextToken_Dollar() {
        lexer.setXPath("/$foo");
        token = lexer.nextToken();
        assertEquals(TokenType.DOLLAR, token.getTokenType());
    }

    @Test
    public void testNextToken_DoubleColon() {
        lexer.setXPath("/a::b");
        token = lexer.nextToken();
        assertEquals(TokenType.DOUBLE_COLON, token.getTokenType());
    }

    @Test
    public void testNextToken_LeftParen() {
        lexer.setXPath("/a[1]/b(c)[]");
        token = lexer.nextToken();
        assertEquals(TokenType.LEFT_PARENTHESIS, token.getTokenType());
    }

    @Test
    public void testNextToken_RightParen() {
        lexer.setXPath("/a[1]/b(c)[1]");
        token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_PARENTHESIS, token.getTokenType());
    }

    @Test
    public void testNextToken_LeftBracket() {
        lexer.setXPath("/a[1]/b[1]");
        token = lexer.nextToken();
        assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
    }

    @Test
    public void testNextToken_RightBracket() {
        lexer.setXPath("/a[1]/b[1]");
        token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
    }

    @Test
    public void testNextToken_Slashes() {
        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        lexer.setXPath("/a/b/");
        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        lexer.setXPath("/a//b");
        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
    }

    @Test
    public void testNextToken_At() {
        lexer.setXPath("/a[@foo]");
        token = lexer.nextToken();
        assertEquals(TokenType.AT, token.getTokenType());
    }

    @Test
    public void testNextToken_Operator() {
        List<String> operators = List.of("+", "-", "*", "/", "<", ">", "<=", ">=", "=", "!=", "and", "or");
        for (String operator : operators) {
            lexer.setXPath("/a" + operator + "/b");
            token = lexer.nextToken();
            assertEquals( operator.charAt(0), token.getText().charAt(0));
        }
    }

    @Test
    public void testNextToken_Not() {
        lexer.setXPath("not(/a[1])");
        token = lexer.nextToken();
        assertEquals(TokenType.NOT, token.getTokenType());
    }

    @Test
    public void testNextToken_NotEquals() {
        lexer.setXPath("/a[1] != /b[1]");
        token = lexer.nextToken();
        assertEquals(TokenType.NOT_EQUALS, token.getTokenType());
    }

    @Test
    public void testNextToken_Equals() {
        lexer.setXPath("/a[1] = /b[1]");
        token = lexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());
    }

    @Test
    public void testNextToken_RelationalOperator() {
        List<String> relationalOperators = List.of("<=", ">=", "<>");
        for (String relationalOperator : relationalOperators) {
            lexer.setXPath("/a[1] " + relationalOperator + "/b[1]");
            token = lexer.nextToken();
            assertEquals(relationalOperator, token.getText());
        }
    }

    @Test
    public void testNextToken_Star() {
        lexer.setXPath("/a/b*/c");
        token = lexer.nextToken();
        assertEquals(TokenType.ASTERISK, token.getTokenType());
    }

    @Test
    public void testNextToken_IdentifierOrOperatorName() {
        List<String> identifiers = List.of("and", "or", "foo", "bar", "baz");
        for (String identifier : identifiers) {
            lexer.setXPath("/" + identifier);
            token = lexer.nextToken();
            assertEquals(TokenType.IDENTIFIER, token.getTokenType());
            assertEquals(identifier, token.getText());
        }

        lexer.setXPath("/a/or/b");
        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("or", token.getText());
    }

    @Test
    public void testNextToken_NumberOrIdentifier_Middle() {
        lexer.setXPath("/a[1]1b[1]");
        token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("1", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("b", token.getText());
    }

    @Test(expected = AssertionError.class)
    public void testNextToken_UnexpectedCharacter() {
        lexer.setXPath("/a/@x%");
        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("x", token.getText());
        token = lexer.nextToken();
        assertEquals(TokenType.AT, token.getTokenType());
        assertEquals("@", token.getText());
        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        assertEquals("/", token.getText());
        token = lexer.nextToken();
        assertEquals(TokenType.PERCENT_SIGN, token.getTokenType());
        assertEquals("%", token.getText());

        token = lexer.nextToken();
        // AssertionError should be thrown here
    }

    @Test(expected = AssertionError.class)
    public void testNextToken_UnexpectedEndOfString() {
        lexer.setXPath("/a/");
        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        assertEquals("/", token.getText());

        token = lexer.nextToken();
        // AssertionError should be thrown here
    }
}