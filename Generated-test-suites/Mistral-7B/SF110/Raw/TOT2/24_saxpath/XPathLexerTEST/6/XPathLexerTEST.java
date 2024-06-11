
import static org.junit.Assert.*;
import com.werken.saxpath.XPathLexer;
import com.werken.saxpath.Token;

public class XPathLexerTest {

    @Test
    public void testNextToken() {
        XPathLexer lexer = new XPathLexer("/test[1]");
        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        assertEquals("/", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("test", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getTokenType());
        assertEquals("1", token.getValue());
    }

    @Test
    public void testIdentifierOrOperatorName() {
        XPathLexer lexer = new XPathLexer("/test[1] and @class='red'");
        Token token = lexer.nextToken(); // "/"
        token = lexer.nextToken(); // "test"

        token = lexer.nextToken(); // "and"
        assertEquals(TokenType.AND, token.getTokenType());

        token = lexer.nextToken(); // "@"
        assertEquals(TokenType.AT, token.getTokenType());

        token = lexer.nextToken(); // "class"
        assertEquals(TokenType.IDENTIFIER, token.getTokenType());
        assertEquals("class", token.getValue());

        token = lexer.nextToken(); // "="
        assertEquals(TokenType.EQUALS, token.getTokenType());

        token = lexer.nextToken(); // "'"
        assertEquals(TokenType.SINGLE_QUOTE, token.getTokenType());
        assertEquals("red", token.getValue());
    }

    @Test
    public void testEmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertEquals(TokenType.WHITESPACE, token.getTokenType());
    }

    @Test
    public void testSpecialCharacters() {
        XPathLexer lexer = new XPathLexer("/test[1]//@class='red'#fragment[1]");
        Token token = lexer.nextToken(); // "/"
        token = lexer.nextToken(); // "test"
        token = lexer.nextToken(); // "[1]"
        token = lexer.nextToken(); // "//"
        token = lexer.nextToken(); // "@"
        token = lexer.nextToken(); // "class"
        token = lexer.nextToken(); // "'"
        token = lexer.nextToken(); // "r"
        token = lexer.nextToken(); // "e"
        token = lexer.nextToken(); // "d"
        token = lexer.nextToken(); // "#"
        token = lexer.nextToken(); // "fragment"
        token = lexer.nextToken(); // "[1]"

        // Test for special characters
        token = lexer.nextToken(); // "#"
        assertEquals(TokenType.NUMBER_SIGN, token.getTokenType());

        token = lexer.nextToken(); // "["
        assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());

        token = lexer.nextToken(); // "]"
        assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
    }

    // Add other test cases for edge cases and exception handling scenarios
}