import org.junit.Test;
import org.scribe.model.Token;

public class TokenTest {
    
    @Test
    public void testTokenConstructionWithTwoArguments() {
        String token = "abc123";
        String secret = "xyz456";
        Token tokenObj = new Token(token, secret);
        assertNotNull(tokenObj);
        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
        assertNull(tokenObj.getRawResponse());
    }
    
}