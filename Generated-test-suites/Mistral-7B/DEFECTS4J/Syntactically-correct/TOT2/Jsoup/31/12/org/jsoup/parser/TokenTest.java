import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {
    @Test
    public void testDoctypeType() {
        Token token = new Token.Doctype();
        String expectedType = TokenType.Doctype.name();
        String actualType = token.tokenType();
        assertEquals(expectedType, actualType);
    }
}