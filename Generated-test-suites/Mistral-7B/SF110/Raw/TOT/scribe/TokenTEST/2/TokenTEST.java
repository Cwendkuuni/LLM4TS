import org.junit.Test;
import org.scribe.model.Token;
import static org.junit.Assert.*; // for assertions

public class TokenTest {
    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        String expectedToken = token;
        String expectedSecret = secret;

        // Test getters for token and secret
        String actualToken = tokenObject.getToken();
        String actualSecret = tokenObject.getSecret();

        assertEquals(expectedToken, actualToken);
        assertEquals(expectedSecret, actualSecret);
    }

    
    // Add more test cases here
    
}