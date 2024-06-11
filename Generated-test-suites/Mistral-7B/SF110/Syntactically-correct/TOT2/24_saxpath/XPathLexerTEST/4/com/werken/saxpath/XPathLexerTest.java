import org.junit.Test;
import static org.junit.Assert.*;
import com.werken.saxpath.XPathLexer;
import com.werken.saxpath.Token;

public class XPathLexerTest {

    @Test
    public void testNextToken_EmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertNull(token);
    }

    @Test
    public void testNextToken_WhitespaceXPath() {
        XPathLexer lexer = new XPathLexer("   ");
        Token token = lexer.nextToken();
        assertEquals(TokenType.WHITESPACE, token.getTokenType());
    }

    @Test
    public void testNextToken_SpecialCharacters() {
        XPathLexer lexer = new XPathLexer("$@");
        Token token = lexer.nextToken();
        assertEquals(TokenType.AT, token.getTokenType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextToken_InvalidCharacters() {
        XPathLexer lexer = new XPathLexer("!@#$%^&*()");
        lexer.nextToken(); // Consume first valid token: @
        Token token = lexer.nextToken();
        fail("Expected IllegalArgumentException, but got a valid token.");
    }

    @Test(expected = IllegalStateException.class)
    public void testNextToken_UnclosedBracket() {
        XPathLexer lexer = new XPathLexer("[");
        Token token = lexer.nextToken(); // Consume [
        Token previousToken = lexer.getPreviousToken();
        assertEquals(TokenType.LEFT_BRACKET, previousToken.getTokenType());
        Token token2 = lexer.nextToken();
        fail("Expected IllegalStateException, but got a valid token.");
    }

    @Test
    public void testNextToken_Basic() {
        XPathLexer lexer = new XPathLexer("//a[1]");
        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
    }

    @Test
    public void testNextToken_Identifier() {
        XPathLexer lexer = new XPathLexer("id1");
        Token token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
    }

    @Test
    public void testNextToken_Operator() {
        XPathLexer lexer = new XPathLexer("//a=b");
        Token token = lexer.nextToken();
        assertEquals(TokenType.EQUALS, token.getTokenType());
    }

    @Test
    public void testNextToken_Number() {
        XPathLexer lexer = new XPathLexer("//a[1]");
        Token token = lexer.nextToken(); // Consume /
        Token numberToken = lexer.nextToken();
        assertEquals(TokenType.NUMBER, numberToken.getTokenType());
        assertEquals("1", numberToken.getValue());
    }

    @Test
    public void testNextToken_Dot() {
        XPathLexer lexer = new XPathLexer("//a.b");
        Token token = lexer.nextToken(); // Consume /
        Token dotToken = lexer.nextToken();
        assertEquals(TokenType.DOTS, dotToken.getTokenType());
        Token identifierToken = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, identifierToken.getTokenType());
        assertEquals("b", identifierToken.getValue());
    }

    @Test
    public void testNextToken_Slash() {
        XPathLexer lexer = new XPathLexer("//a/b");
        Token token = lexer.nextToken(); // Consume /
        Token identifierToken = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, identifierToken.getTokenType());
        assertEquals("a", identifierToken.getValue());

        token = lexer.nextToken(); // Consume /
        Token identifierToken2 = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, identifierToken2.getTokenType());
        assertEquals("b", identifierToken2.getValue());
    }
}