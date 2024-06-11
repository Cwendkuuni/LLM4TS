import org.junit.Test;
import org.scribe.model.Token;

public class TokenTest {
    
    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "abcdefg1234567890";
        String secret = "hijklmnopqrstuvwxyz1234567890";
        Token tokenInstance = new Token(token, secret);
        String expectedToken = token;
        String expectedSecret = secret;

        assertNotNull(tokenInstance);
        assertEquals(expectedToken, tokenInstance.getToken());
        assertEquals(expectedSecret, tokenInstance.getSecret());
    }
    
}