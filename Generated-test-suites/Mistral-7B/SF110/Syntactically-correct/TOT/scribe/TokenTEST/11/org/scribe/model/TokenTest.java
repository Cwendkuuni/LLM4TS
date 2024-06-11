import org.junit.Test;
import org.scribe.model.Token;

public class TokenTest {
    
    
    @Test
    public void testTokenConstruction() {
        String token = "valid_token";
        String secret = "valid_secret";

        Token tokenObj = new Token(token, secret);

        assertNotNull(tokenObj);
        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
        assertNull(tokenObj.getRawResponse());
    }

    
}