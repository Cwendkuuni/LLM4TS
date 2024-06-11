import org.junit.Test;
import static org.junit.Assert.*;
import com.werken.saxpath.Token;
import com.werken.saxpath.XPathLexer;

public class XPathLexerTest {

    @Test
    public void testNextToken() {
        String xpath = "/a[1]/b/c";
        XPathLexer lexer = new XPathLexer(xpath);
        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        assertEquals("/", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("a", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
        assertEquals("[", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("1", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
        assertEquals("]", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        assertEquals("/", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("b", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        assertEquals("/", token.getText());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("c", token.getText());

        token = lexer.nextToken();
        assertNull(token);
    }
}