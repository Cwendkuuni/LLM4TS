import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.werken.saxpath.Token;
import com.werken.saxpath.XPathLexer;
import com.werken.saxpath.XPathLexer.TokenType;

public class XPathLexerTest {
    @Test
    public void testNextToken() {
        XPathLexer lexer = new XPathLexer("");
        Token token;

        // Test empty string
        token = lexer.nextToken();
        assertNull(token);

        // Test setting XPath
        lexer.setXPath("//a[b=1]");

        // Test basic tokens
        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        assertEquals("a", token.getText());
        token = lexer.nextToken();
        assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());

        // Test identifer
        token = lexer.nextToken();
        assertEquals("b", token.getText());
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        // Test operator
        token = lexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());

        // Test number
        token = lexer.nextToken();
        assertEquals("1", token.getText());
        assertEquals(TokenType.NUMBER, token.getTokenType());

        // Test right bracket
        token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());

        // Test end of XPath
        token = lexer.nextToken();
        assertNull(token);
        
        // Test consumption of whitespace
        lexer.setXPath(" //a[b = 1 ] ");
        lexer.nextToken();
        lexer.nextToken();
        token = lexer.nextToken();
        assertEquals("a", token.getText());
    }
}