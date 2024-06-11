import static org.junit.Assert.*;
import com.werken.saxpath.XPathLexer;
import com.werken.saxpath.Token;

public class XPathLexerTest {

    @Test
    public void testNextToken() {
        String xpath = "/a/b[1]";
        XPathLexer lexer = new XPathLexer(xpath);
        Token token = lexer.nextToken();
        assertEquals(TokenType.SLASH, token.getTokenType());
        assertEquals("/", token.getText());
        // Test other tokens here
    }
}