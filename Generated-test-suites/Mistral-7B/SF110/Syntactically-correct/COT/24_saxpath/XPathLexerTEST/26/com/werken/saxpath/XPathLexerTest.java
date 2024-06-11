package com.werken.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class XPathLexerTest {
    private XPathLexer lexer;
    private String xpath;

    @Before
    public void setup() {
        this.xpath = "//a[1] and b=2 or c[2]/d=3";
        this.lexer = new XPathLexer(this.xpath);
    }

    

    @Test
    public void testNextToken_WhenCalledInitially_ShouldReturnNull() {
        assertNull(this.lexer.nextToken());
    }

    @Test
    public void testNextToken_ShouldReturnCorrespondingTokenBasedOnXPathCharacter() {
        Token token = null;
        for (int i = 0; i < this.xpath.length(); i++) {
            token = this.lexer.nextToken();
            switch (this.xpath.charAt(i)) {
                case '/': {
                    assertEquals(TokenType.SLASHES, token.getTokenType());
                    break;
                }
                case '[': {
                    assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
                    break;
                }
                case ']': {
                    assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
                    break;
                }
                case '>': {
                    assertEquals(TokenType.RELATIONAL_OPERATOR, token.getTokenType());
                    break;
                }
                case '<': {
                    assertEquals(TokenType.RELATIONAL_OPERATOR, token.getTokenType());
                    break;
                }
                case '=': {
                    assertEquals(TokenType.EQUALS, token.getTokenType());
                    break;
                }
                case '!': {
                    assertEquals(TokenType.NOT, token.getTokenType());
                    break;
                }
                case '.': {
                    assertEquals(TokenType.DOTS, token.getTokenType());
                    break;
                }
                case ' ': {
                    assertEquals(TokenType.WHITESPACE, token.getTokenType());
                    break;
                }
                case 'a':
                case 'b':
                case 'c': {
                    assertEquals(TokenType.IDENTIFIER, token.getTokenType());
                    assertEquals("a", token.getValue());
                    assertEquals(this.xpath, token.getXPath());
                    break;
                }
                case '1':
                case '2': {
                    assertEquals(TokenType.NUMBER, token.getTokenType());
                    break;
                }
                case '(': {
                    assertEquals(TokenType.LEFT_PAREN, token.getTokenType());
                    break;
                }
                case ')': {
                    assertEquals(TokenType.RIGHT_PAREN, token.getTokenType());
                    break;
                }
                case 'd': {
                    assertEquals(TokenType.DIV, token.getTokenType());
                    break;
                }
                case 'm': {
                    assertEquals(TokenType.MOD, token.getTokenType());
                    break;
                }
                case 'o': {
                    assertEquals(TokenType.OR, token.getTokenType());
                    break;
                }
                case 'a': {
                    assertEquals(TokenType.AND, token.getTokenType());
                    break;
                }
                default:
                    break;
            }
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testNextToken_ShouldThrowIllegalStateExceptionWhenEndOfXPathIsReached() {
        Token token = null;
        while ((token = this.lexer.nextToken()) != null) ;
    }

    
}