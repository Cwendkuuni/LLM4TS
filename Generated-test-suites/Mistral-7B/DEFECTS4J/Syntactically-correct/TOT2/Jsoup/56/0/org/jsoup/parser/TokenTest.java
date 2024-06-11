import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Token.Doctype;

public class TokenTest {
    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        String expected = "Doctype";
        String actual = token.tokenType();
        assertEquals(expected, actual);
    }

    // Add test cases for other methods here...
}