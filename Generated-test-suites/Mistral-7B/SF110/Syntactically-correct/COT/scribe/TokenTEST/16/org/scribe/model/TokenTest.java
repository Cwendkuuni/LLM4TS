import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Token;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertNull(tokenObject.getRawResponse());
    }

    @Test
    public void testTokenConstructorWithTokenSecretAndRawResponse() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "response data";
        Token tokenObject = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenNotConstructedWithRawResponse() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        tokenObject.getRawResponse();
    }

    @Test
    public void testToStringMethod() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        assertEquals("Token[abc123 , def456]", tokenObject.toString());
    }

    @Test
    public void testGetTokenWithNullToken() {
        String token = null;
        String secret = "def456";
        try {
            new Token(token, secret);
            fail("Expected an IllegalStateException");
        } catch (IllegalArgumentException e) {
            assertEquals("token is marked non-null but is null", e.getMessage());
        }
    }

    @Test
    public void testGetSecretWithNullSecret() {
        String token = "abc123";
        String secret = null;
        try {
            new Token(token, secret);
            fail("Expected an IllegalStateException");
        } catch (IllegalArgumentException e) {
            assertEquals("secret is marked non-null but is null", e.getMessage());
        }
    }

    @Test
    public void testGetTokenWithEmptyString() {
        String token = "";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        assertEquals(token, tokenObject.getToken());
        assertNotNull(tokenObject.getSecret());
        assertNull(tokenObject.getRawResponse());
    }

    @Test
    public void testGetSecretWithEmptyStringAndRawResponse() {
        String token = "";
        String secret = "";
        String rawResponse = "response data";
        Token tokenObject = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObject.getToken());
        assertNotNull(tokenObject.getSecret());
        assertEquals(rawResponse, tokenObject.getRawResponse());
    }
}