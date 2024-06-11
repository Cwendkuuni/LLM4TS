import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XPathLexerTest {
    @Test
    public void testNextToken() {
        XPathLexer lexer = new XPathLexer("//div[@class='test']");
        Token token;

        // Test normal cases
        token = lexer.nextToken();
        assertEquals(XPathTokens.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(XPathTokens.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(XPathTokens.AT, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(XPathTokens.COLON, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(XPathTokens.LEFT_BRACKET, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(XPathTokens.AT, token.getTokenType());

        // Test edge cases and exception handling
        lexer = new XPathLexer("");
        token = lexer.nextToken();
        assertNull(token);

        lexer = new XPathLexer(null);
        token = lexer.nextToken();
        assertNull(token);
    }
}