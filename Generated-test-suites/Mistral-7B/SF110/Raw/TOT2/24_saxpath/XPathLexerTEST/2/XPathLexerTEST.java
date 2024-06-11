import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.werken.saxpath.XPathLexer;
import com.werken.saxpath.Token;

import org.junit.Test;

public class XPathLexerTest {

    @Test
    public void testNextToken() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertNull(token);

        lexer = new XPathLexer("$abc123");
        token = lexer.nextToken();
        assertEquals(TokenType.DOLLAR, token.getTokenType());

        lexer = new XPathLexer("/test[1]");
        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        lexer = new XPathLexer("a[1] = b[1]");
        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        lexer = new XPathLexer("(1 + 1) * 2");
        token = lexer.nextToken();
        assertEquals(TokenType.LEFT_PAREN, token.getTokenType());

        lexer = new XPathLexer("not(a[1])");
        token = lexer.nextToken();
        assertEquals(TokenType.NOT, token.getTokenType());

        lexer = new XPathLexer(".");
        token = lexer.nextToken();
        assertEquals(TokenType.DOTS, token.getTokenType());

        lexer = new XPathLexer("not(a[1]) = b[1]");
        token = lexer.nextToken();
        assertEquals(TokenType.NOT_EQUALS, token.getTokenType());

        lexer = new XPathLexer("not(a[1]) = b[1]");
        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        lexer = new XPathLexer("not(a[1]) = b[1]");
        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        lexer = new XPathLexer("not(a[1]) = b[1]");
        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        lexer = new XPathLexer("not(a[1]) = b[1]");
        token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_SQUARE_BRACKET, token.getTokenType());

        lexer = new XPathLexer("not(a[1]) = b[1]");
        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        lexer = new XPathLexer("not(a[1]) = b[1]");
        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        lexer = new XPathLexer("not(a[1]) = b[1]");
        token = lexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());

        lexer = new XPathLexer("not(a[1]) = b[1]");
        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        lexer = new XPathLexer("not(a[1]) = b[1]");
        token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_SQUARE_BRACKET, token.getTokenType());

        lexer = new XPathLexer("not(a[1]) = b[1]");
        token = lexer.nextToken();
        assertEquals(TokenType.END_OF_INPUT, token.getTokenType());
    }
}