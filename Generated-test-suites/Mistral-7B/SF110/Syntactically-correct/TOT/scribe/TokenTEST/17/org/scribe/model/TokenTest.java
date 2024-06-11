import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenTest {
    @Test
    public void testTokenConstructor() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenObject = new Token(token, secret);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
    }
}
