
import static org.junit.Assert.*;
import com.werken.saxpath.XPathLexer;
import com.werken.saxpath.Token;

public class XPathLexerTest {
    @Test
    public void testNextToken() {
        XPathLexer lexer = new XPathLexer("//a[b=1]");
        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());

        token = lexer.nextToken();
        assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());

        // Test edge case: no more tokens
        token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testIdentifierOrOperatorName() {
        XPathLexer lexer = new XPathLexer("a[b=1]");
        Token previousToken = new Token(15, "", 0, 0); // An identifier token
        Token token = lexer.identifierOrOperatorName(previousToken);
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());

        previousToken = new Token(11, "", 0, 0); // A slash token
        token = lexer.identifierOrOperatorName(previousToken);
        assertEquals(TokenType.AND, token.getTokenType()); // Assuming 'and' operator is the next token in the input

        // Test error case: invalid character after operator
        previousToken = new Token(11, "", 0, 0);
        token = lexer.identifierOrOperatorName(previousToken);
        assertNull(token);
    }

    @Test
    public void testIdentifier() {
        XPathLexer lexer = new XPathLexer("a[b=1]");
        Token token = lexer.identifier();
        assertEquals("a", token.getValue());

        lexer = new XPathLexer("myCustomXPath");
        token = lexer.identifier();
        assertEquals("myCustomXPath", token.getValue());
    }
}