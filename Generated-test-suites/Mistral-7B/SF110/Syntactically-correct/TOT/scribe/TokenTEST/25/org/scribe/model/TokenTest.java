import org.junit.Test;
import org.scribe.model.Token;

public class TokenTest {
    
    
    @Test
    public void testTokenConstructorWithValidArguments() {
        String token = "abc123";
        String secret = "def456";
        Token tokenInstance = new Token(token, secret);
        assertNotNull(tokenInstance);
        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
    }

    @Test(expected = NullPointerException.class)
    public void testTokenConstructorWithNullToken() {
        String secret = "def456";
        Token tokenInstance = new Token(null, secret);
        fail("Expected NullPointerException but got " + tokenInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testTokenConstructorWithNullSecret() {
        String token = "abc123";
        Token tokenInstance = new Token(token, null);
        fail("Expected NullPointerException but got " + tokenInstance);
    }

    
}