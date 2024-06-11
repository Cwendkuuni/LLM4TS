import org.junit.Test;
import org.scribe.model.Token;

public class TokenTest {
    
    
    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "abc123";
        String secret = "def456";
        
        Token tokenInstance = new Token(token, secret);
        
        assertNotNull(tokenInstance);
        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
        
        //Verify that getRawResponse throws an IllegalStateException when not provided
        try {
            tokenInstance.getRawResponse();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            //Expected
        }
    }

    
}