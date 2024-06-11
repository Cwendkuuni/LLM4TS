import org.junit.Test;
import org.scribe.model.Token;

public class TokenTest {
    
    
    @Test
    public void testTokenConstructorWithTwoArguments() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        
        assertNotNull(tokenObject);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertNull(tokenObject.getRawResponse());
    }

    
}