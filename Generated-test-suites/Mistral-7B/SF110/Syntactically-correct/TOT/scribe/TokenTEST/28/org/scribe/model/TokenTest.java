import org.junit.Test;
import static org.junit.Assert.*;;

public class TokenTest {
    @Test
    public void testTokenConstructionWithTokenAndSecret() {
        String token = "abc123";
        String secret = "def456";
        Token tokenInstance = new Token(token, secret);
        String expectedToken = token;
        String expectedSecret = secret;
        String actualToken = tokenInstance.getToken();
        String actualSecret = tokenInstance.getSecret();
        assertEquals(expectedToken, actualToken);
        assertEquals(expectedSecret, actualSecret);
    }
}