import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Token;

public class TokenTest {
    @Test
    public void testTokenConstructor() {
        Token token = new Token("sampleToken", "sampleSecret");
        assertEquals("sampleToken", token.getToken());
        assertEquals("sampleSecret", token.getSecret());
        assertNull(token.getRawResponse());
    }
    
    @Test
    public void testConstructorWithNullArguments() {
        try {
            new Token(null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testConstructorWithEmptyStrings() {
        try {
            new Token("", "");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testGetToken() {
        Token token = new Token("sampleToken", "sampleSecret");
        assertEquals("sampleToken", token.getToken());
    }
    
    @Test
    public void testGetSecret() {
        Token token = new Token("sampleToken", "sampleSecret");
        assertEquals("sampleSecret", token.getSecret());
    }
    
    @Test
    public void testGetRawResponse() {
        Token token = new Token("sampleToken", "sampleSecret", "sampleRawResponse");
        assertEquals("sampleRawResponse", token.getRawResponse());

        Token tokenWithoutRawResponse = new Token("sampleToken", "sampleSecret");
        try {
            tokenWithoutRawResponse.getRawResponse();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testToString() {
        Token token = new Token("sampleToken", "sampleSecret");
        assertEquals("Token[sampleToken , sampleSecret]", token.toString());

        Token tokenWithEmptySecret = new Token("sampleToken", "", "sampleRawResponse");
        assertEquals("Token[sampleToken , ]", tokenWithEmptySecret.toString());
    }
}