import org.junit.Test;
import org.scribe.model.Token;

public class TokenTest {
    
    
    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "abcde12345";
        String secret = "secret123";
        Token tokenObject = new Token(token, secret);
        String expectedToken = "abcde12345";
        String expectedSecret = "secret123";
        assertNotNull(tokenObject);
        assertEquals(expectedToken, tokenObject.getToken());
        assertEquals(expectedSecret, tokenObject.getSecret());
    }

    
}