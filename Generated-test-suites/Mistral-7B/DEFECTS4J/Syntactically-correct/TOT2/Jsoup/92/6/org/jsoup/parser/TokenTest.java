import org.junit.Test;
import org.jsoup.parser.Token;
import static org.junit.Assert.assertEquals;

public class TokenTest {
    @Test
    public void testTokenType() {
        Token token = new Token.StartTag();
        assertEquals("StartTag", token.tokenType());
    }
}