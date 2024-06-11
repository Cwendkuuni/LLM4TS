import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "1234567890";
        String secret = "abcdefghijklmnopqrstuvwxyz";
        Token tokenObject = new Token(token, secret);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertNull(tokenObject.getRawResponse());
    }
}