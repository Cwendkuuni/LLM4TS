import static org.junit.Assert.*;
import static com.werken.saxpath.XPathLexer.*;

public class XPathLexerTest {

    @Test
    public void testNextToken_ValidInput() {
        XPathLexer lexer = new XPathLexer("$a/b[1]");
        Token token;

        token = lexer.nextToken();
        assertEquals(TokenType.DOLLAR, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("1", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());

        // Test for end of XPath
        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
    }
}