import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static com.werken.saxpath.XPathLexer.TokenType.*;

import org.junit.Test;

public class XPathLexerTest {
    @Test
    public void testNextToken() {
        String xpath = "/a[1]/b[2]/c";
        XPathLexer lexer = new XPathLexer(xpath);

        // Test empty string
        assertNull(lexer.nextToken());

        // Test initial state
        Token token = lexer.nextToken();
        assertEquals(SLASH, token.getTokenType());
        assertEquals("/", token.getValue());

        // Test identifier
        token = lexer.nextToken();
        assertEquals(IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getValue());

        // Test number
        token = lexer.nextToken();
        assertEquals(NUMBER, token.getTokenType());
        assertEquals("1", token.getValue());

        // Test square bracket
        token = lexer.nextToken();
        assertEquals(LEFT_BRACKET, token.getTokenType());
        assertEquals("[", token.getValue());

        // Test digit inside square bracket
        token = lexer.nextToken();
        assertEquals(DIGIT, token.getTokenType());
        assertEquals("2", token.getValue());

        // Test right square bracket
        token = lexer.nextToken();
        assertEquals(RIGHT_BRACKET, token.getTokenType());
        assertEquals("]", token.getValue());

        // Test slash
        token = lexer.nextToken();
        assertEquals(SLASH, token.getTokenType());
        assertEquals("/", token.getValue());

        // Test identifier
        token = lexer.nextToken();
        assertEquals(IDENTIFIER, token.getTokenType());
        assertEquals("b", token.getValue());

        // Test left square bracket
        token = lexer.nextToken();
        assertEquals(LEFT_BRACKET, token.getTokenType());
        assertEquals("[", token.getValue());

        // Test digit inside square bracket
        token = lexer.nextToken();
        assertEquals(DIGIT, token.getTokenType());
        assertEquals("2", token.getValue());

        // Test right square bracket
        token = lexer.nextToken();
        assertEquals(RIGHT_BRACKET, token.getTokenType());
        assertEquals("]", token.getValue());

        // Test slash
        token = lexer.nextToken();
        assertEquals(SLASH, token.getTokenType());
        assertEquals("/", token.getValue());

        // Test identifier
        token = lexer.nextToken();
        assertEquals(IDENTIFIER, token.getTokenType());
        assertEquals("c", token.getValue());

        // Test end of string
        token = lexer.nextToken();
        assertNull(token);
    }
}