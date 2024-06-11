import org.junit.Test;
import static org.junit.Assert.*;

public class XPathLexerTest {

    @Test
    public void testNextToken_ValidXPath() {
        XPathLexer lexer = new XPathLexer("/a/b");
        Token token = lexer.nextToken();
        assertEquals("/", token.getValue());
    }

    @Test
    public void testNextToken_EmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertNull(token);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextToken_NullXPath() {
        XPathLexer lexer = new XPathLexer(null);
    }

    // Add more test cases for other token types and edge cases
}