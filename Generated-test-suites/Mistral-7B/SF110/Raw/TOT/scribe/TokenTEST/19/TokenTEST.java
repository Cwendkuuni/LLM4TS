import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    @Test
    public void testTokenConstructionWithTokenAndSecret() {
        String token = "abc123";
        String secret = "def456";

        Token tokenInstance = new Token(token, secret);

        String expectedToken = token;
        String expectedSecret = secret;

        assertEquals(expectedToken, tokenInstance.getToken());
        assertEquals(expectedSecret, tokenInstance.getSecret());
    }
}