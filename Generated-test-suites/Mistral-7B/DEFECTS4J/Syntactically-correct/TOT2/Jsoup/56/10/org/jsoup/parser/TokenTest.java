import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        // Basic functionality test
        Token doctypeToken = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctypeToken.tokenType());

        // Edge case: Test reset method
        Token doctypeTokenReset = doctypeToken.reset();
        assertNotSame(doctypeToken, doctypeTokenReset);
        assertEquals(TokenType.Doctype, doctypeTokenReset.tokenType());

        // Exception handling: Test invalid input for getName method
        Token doctypeTokenInvalidInput = new Token.Doctype();
        try {
            doctypeTokenInvalidInput.getName();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Add test cases for other methods here...
}