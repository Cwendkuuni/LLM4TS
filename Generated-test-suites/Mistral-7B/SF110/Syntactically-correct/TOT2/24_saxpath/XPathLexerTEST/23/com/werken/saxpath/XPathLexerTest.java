package com.example.junit4;

import org.junit.Test;
import static org.junit.Assert.*;
import com.werken.saxpath.XPathLexer;
import com.werken.saxpath.Token;

public class XPathLexerTest {

    @Test
    public void testNextToken_ValidXPathExpression() {
        XPathLexer lexer = new XPathLexer("/a/b[1]/c");
        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASHES, token.getTokenType());
        // Add more assertions for other expected tokens in the XPath expression
    }

    @Test
    public void testIdentifierOrOperatorName_ValidIdentifierOrOperatorName() {
        XPathLexer lexer = new XPathLexer("a:b and c=d");
        Token token = lexer.nextToken();
        if (token.getTokenType() == TokenType.IDENTIFIER) {
            assertEquals("a", token.getText());
        } else {
            assertEquals(TokenType.AND, token.getTokenType());
        }
        // Add more assertions for other expected tokens in the XPath expression
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextToken_EmptyXPathExpression() {
        XPathLexer lexer = new XPathLexer("");
        lexer.nextToken();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextToken_InvalidXPathExpression() {
        XPathLexer lexer = new XPathLexer("//a[1]%");
        lexer.nextToken();
    }

}