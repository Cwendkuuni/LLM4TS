import static org.junit.Assert.*;
import com.werken.saxpath.Token;
import com.werken.saxpath.XPathLexer;

public class XPathLexerTest {

    @Test
    public void testNextToken() {
        XPathLexer lexer = new XPathLexer("");
        Token token1 = lexer.nextToken();
        Token token2 = lexer.nextToken();

        assertNotNull(token1);
        assertNotNull(token2);
        assertNotSame(token1, token2);
        assertEquals(token1.getTokenType(), token2.getTokenType());
        assertEquals(token1.getXPath(), token2.getXPath());
        assertEquals(token1.getStartPosition(), token1.getStartPosition());
        assertNotEquals(token1.getStartPosition(), token2.getStartPosition());
    }
}