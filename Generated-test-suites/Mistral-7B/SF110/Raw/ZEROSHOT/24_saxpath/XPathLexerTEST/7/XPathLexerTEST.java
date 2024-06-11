import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import com.werken.saxpath.XPathLexer;
import com.werken.saxpath.Token;

public class XPathLexerTest {

    @Test
    public void testXPathLexer_emptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testXPathLexer_simpleXPath() {
        XPathLexer lexer = new XPathLexer("/a/b");

        Token token = null;
        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("b", token.getValue());

        assertNull(lexer.nextToken());
    }

    @Test
    public void testXPathLexer_XPathWithOperator() {
        XPathLexer lexer = new XPathLexer("/a/b=c");

        Token token = null;
        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("b", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EQUAL, token.getTokenType());
        assertEquals("=", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("c", token.getValue());

        assertNull(lexer.nextToken());
    }

    @Test
    public void testXPathLexer_XPathWithNumber() {
        XPathLexer lexer = new XPathLexer("/123");

        Token token = null;
        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("123", token.getValue());

        assertNull(lexer.nextToken());
    }

    @Test
    public void testXPathLexer_XPathWithDots() {
        XPathLexer lexer = new XPathLexer("/a/..");

        Token token = null;
        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.DOTS, token.getTokenType());
        assertEquals("..", token.getValue());

        assertNull(lexer.nextToken());
    }

    @Test
    public void testXPathLexer_XPathWithWhiteSpace() {
        XPathLexer lexer = new XPathLexer("/a / b");

        Token token = null;
        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.WHITESPACE, token.getTokenType());
        assertNull(token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("b", token.getValue());

        assertNull(lexer.nextToken());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testXPathLexer_XPathWithInvalidChar() {
        XPathLexer lexer = new XPathLexer("/a/b%");

        Token token = null;
        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("b", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.PERCENT, token.getTokenType());
        assertEquals("%", token.getValue());

        lexer.nextToken();
    }
}