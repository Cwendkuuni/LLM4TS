import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {
    // ...

    @Test
    public void testDoctypeToken() {
        Token doctype = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctype.tokenType());
        // Test other Doctype methods here, if necessary
    }

    // ...
}