import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Token;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenInstance = new Token(token, secret);
        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
        assertNull(tokenInstance.getRawResponse());
    }

    @Test
    public void testTokenConstructorWithTokenSecretAndRawResponse() {
        String token = "testToken";
        String secret = "testSecret";
        String rawResponse = "testRawResponse";
        Token tokenInstance = new Token(token, secret, rawResponse);
        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
        assertEquals(rawResponse, tokenInstance.getRawResponse());
    }

    @Test
    public void testGetToken() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenInstance = new Token(token, secret);
        assertEquals(token, tokenInstance.getToken());
    }

    @Test
    public void testGetSecret() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenInstance = new Token(token, secret);
        assertEquals(secret, tokenInstance.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        String token = "testToken";
        String secret = "testSecret";
        String rawResponse = "testRawResponse";
        Token tokenInstance = new Token(token, secret, rawResponse);
        assertEquals(rawResponse, tokenInstance.getRawResponse());
    }

    @Test
    public void testToString() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenInstance = new Token(token, secret);
        String expected = "Token[testToken , testSecret]";
        assertEquals(expected, tokenInstance.toString());
    }
}