package com.werken.saxpath;

import org.junit.Test;
import static org.junit.Assert.*;

public class XPathLexerTest {

    @Test
    public void testXPathLexerConstructor() {
        XPathLexer lexer = new XPathLexer("");
        assertNull(lexer.getXPath());
        assertEquals(0, lexer.getCurrentPosition());
        assertEquals(0, lexer.getEndPosition());
        assertEquals(null, lexer.getPreviousToken());

        lexer = new XPathLexer("testXPath");
        assertEquals("testXPath", lexer.getXPath());
        assertEquals(0, lexer.getCurrentPosition());
        assertEquals(11, lexer.getEndPosition());
        assertEquals(null, lexer.getPreviousToken());
    }

    @Test
    public void testNextToken_EmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertEquals(-2, token.getTokenType());
    }

    @Test
    public void testNextToken_SingleCharacter() {
        XPathLexer lexer = new XPathLexer("a");
        Token token = lexer.nextToken();
        assertEquals(1, token.getTokenType());
        assertEquals("a", token.getValue());
    }

    @Test
    public void testNextToken_MultipleCharacters() {
        XPathLexer lexer = new XPathLexer("testXPath");
        Token token = lexer.nextToken();
        assertEquals(1, token.getTokenType());
        assertEquals("test", token.getValue());

        token = lexer.nextToken();
        assertEquals(1, token.getTokenType());
        assertEquals("XPath", token.getValue());
    }

    @Test
    public void testNextToken_Whitespace() {
        XPathLexer lexer = new XPathLexer(" testXPath ");
        Token token = lexer.nextToken();
        assertEquals(1, token.getTokenType());
        assertEquals("testXPath", token.getValue());
    }

    @Test
    public void testNextToken_Identifiers() {
        XPathLexer lexer = new XPathLexer("myIdentifier myOtherIdentifier");
        Token token = lexer.nextToken();
        assertEquals(1, token.getTokenType());
        assertEquals("myIdentifier", token.getValue());

        token = lexer.nextToken();
        assertEquals(1, token.getTokenType());
        assertEquals("myOtherIdentifier", token.getValue());
    }

    @Test
    public void testNextToken_Operators() {
        XPathLexer lexer = new XPathLexer("a op b op c");
        Token token = lexer.nextToken();
        assertEquals(1, token.getTokenType());
        assertEquals("a", token.getValue());

        token = lexer.nextToken();
        assertEquals(7, token.getTokenType()); // '<'

        token = lexer.nextToken();
        assertEquals(1, token.getTokenType());
        assertEquals("op", token.getValue());

        token = lexer.nextToken();
        assertEquals(1, token.getTokenType());
        assertEquals("b", token.getValue());

        token = lexer.nextToken();
        assertEquals(7, token.getTokenType()); // '<'

        token = lexer.nextToken();
        assertEquals(1, token.getTokenType());
        assertEquals("op", token.getValue());

        token = lexer.nextToken();
        assertEquals(1, token.getTokenType());
        assertEquals("c", token.getValue());
    }

    @Test
    public void testNextToken_Numbers() {
        XPathLexer lexer = new XPathLexer("1 1.0 123 123.456");
        Token token = lexer.nextToken();
        assertEquals(31, token.getTokenType()); // number
        assertEquals("1", token.getValue());

        token = lexer.nextToken();
        assertEquals(30, token.getTokenType()); // number
        assertEquals("1.0", token.getValue());

        token = lexer.nextToken();
        assertEquals(31, token.getTokenType()); // number
        assertEquals("123", token.getValue());

        token = lexer.nextToken();
        assertEquals(30, token.getTokenType()); // number
        assertEquals("123.456", token.getValue());
    }

    @Test
    public void testNextToken_DollarSign() {
        XPathLexer lexer = new XPathLexer("$myVariable");
        Token token = lexer.nextToken();
        assertEquals(26, token.getTokenType()); // '$'
        assertEquals("myVariable", token.getValue());
    }

    @Test(expected = AssertionError.class)
    public void testNextToken_UnexpectedCharacter() {
        XPathLexer lexer = new XPathLexer("!invalidXPath");
        lexer.nextToken(); // consume '!'
        lexer.nextToken(); // should throw an exception
    }

    @Test
    public void testNextToken_Comment() {
        XPathLexer lexer = new XPathLexer("/* my comment */ testXPath");
        Token token = lexer.nextToken(); // consume '/*'
        assertEquals(1, token.getTokenType());
        assertEquals("testXPath", token.getValue());

        token = lexer.nextToken();
        assertEquals(1, token.getTokenType());
        assertEquals("", token.getValue());

        token = lexer.nextToken(); // consume '/'
        assertEquals(1, token.getTokenType());
        assertEquals("", token.getValue());

        token = lexer.nextToken(); // consume '*/'
        assertEquals(1, token.getTokenType());
        assertEquals("", token.getValue());
    }

    @Test
    public void testNextToken_StringLiteral() {
        XPathLexer lexer = new XPathLexer("'single quote string' testXPath");
        Token token = lexer.nextToken(); // consume "'"

        Token stringToken = lexer.nextToken();
        assertEquals(27, stringToken.getTokenType()); // string literal
        assertEquals("single quote string", stringToken.getValue());

        token = lexer.nextToken(); // consume 'testXPath'
        assertEquals(1, token.getTokenType());
        assertEquals("testXPath", token.getValue());
    }

    @Test
    public void testNextToken_DoubleQuotedStringLiteral() {
        XPathLexer lexer = new XPathLexer("\"double quote string\" testXPath");
        Token token = lexer.nextToken(); // consume "\"

        Token stringToken = lexer.nextToken();
        assertEquals(27, stringToken.getTokenType()); // string literal
        assertEquals("double quote string", stringToken.getValue());

        token = lexer.nextToken(); // consume "testXPath"
        assertEquals(1, token.getTokenType());
        assertEquals("testXPath", token.getValue());
    }

    @Test
    public void testNextToken_Brackets() {
        XPathLexer lexer = new XPathLexer("[1][2] testXPath");
        Token token = lexer.nextToken(); // consume '['

        Token leftBracketToken = lexer.nextToken();
        assertEquals(3, leftBracketToken.getTokenType()); // '['

        token = lexer.nextToken(); // consume '1'
        assertEquals(31, token.getTokenType()); // number
        assertEquals("1", token.getValue());

        token = lexer.nextToken(); // consume ']'
        assertEquals(4, token.getTokenType()); // ']'

        leftBracketToken = lexer.nextToken();
        assertEquals(3, leftBracketToken.getTokenType()); // '['

        token = lexer.nextToken(); // consume '2'
        assertEquals(31, token.getTokenType()); // number
        assertEquals("2", token.getValue());

        token = lexer.nextToken(); // consume ']'
        assertEquals(4, token.getTokenType()); // ']'

        token = lexer.nextToken(); // consume 'testXPath'
        assertEquals(1, token.getTokenType());
        assertEquals("testXPath", token.getValue());
    }
}