import org.junit.Test;
import static org.junit.Assert.*; // for assert statements

public class TokenTest {
    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "token123";
        String secret = "secret123";
        Token tokenInstance = new Token(token, secret);
        String expectedToken = token;
        String expectedSecret = secret;
        assertNotNull(tokenInstance);
        assertEquals(expectedToken, tokenInstance.getToken());
        assertEquals(expectedSecret, tokenInstance.getSecret());
    }

    @Test
    public void testTokenConstructorWithTokenSecretAndRawResponse() {
        String token = "token123";
        String secret = "secret123";
        String rawResponse = "rawResponse123";
        Token tokenInstance = new Token(token, secret, rawResponse);
        String expectedToken = token;
        String expectedSecret = secret;
        String expectedRawResponse = rawResponse;
        assertNotNull(tokenInstance);
        assertEquals(expectedToken, tokenInstance.getToken());
        assertEquals(expectedSecret, tokenInstance.getSecret());
        assertEquals(expectedRawResponse, tokenInstance.getRawResponse());
    }

    @Test
    public void testGetTokenMethod() {
        String token = "token123";
        String secret = "secret123";
        Token tokenInstance = new Token(token, secret);
        String expectedToken = token;
        assertEquals(expectedToken, tokenInstance.getToken());
    }

    @Test
    public void testGetSecretMethod() {
        String token = "token123";
        String secret = "secret123";
        Token tokenInstance = new Token(token, secret);
        String expectedSecret = secret;
        assertEquals(expectedSecret, tokenInstance.getSecret());
    }

    @Test
    public void testGetRawResponseMethod() {
        String token = "token123";
        String secret = "secret123";
        String rawResponse = "rawResponse123";
        Token tokenInstance = new Token(token, secret, rawResponse);
        String expectedRawResponse = rawResponse;
        assertEquals(expectedRawResponse, tokenInstance.getRawResponse());
    }

    @Test
    public void testToStringMethod() {
        String token = "token123";
        String secret = "secret123";
        Token tokenInstance = new Token(token, secret);
        String expectedString = "Token[token123 , secret123]";
        assertEquals(expectedString, tokenInstance.toString());
    }
}