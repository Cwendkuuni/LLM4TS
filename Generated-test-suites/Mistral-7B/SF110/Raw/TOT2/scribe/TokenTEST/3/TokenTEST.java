import org.junit.Test;
import org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "example_token";
        String secret = "example_secret";
        Token tokenObj = new Token(token, secret);
        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
    }

    @Test
    public void testTokenConstructorWithTokenSecretAndRawResponse() {
        String token = "example_token";
        String secret = "example_secret";
        String rawResponse = "example_raw_response";
        Token tokenObj = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
        assertEquals(rawResponse, tokenObj.getRawResponse());
    }

    @Test
    public void testGetToken() {
        String token = "example_token";
        String secret = "example_secret";
        Token tokenObj = new Token(token, secret);
        assertEquals(token, tokenObj.getToken());
    }

    @Test
    public void testGetSecret() {
        String token = "example_token";
        String secret = "example_secret";
        Token tokenObj = new Token(token, secret);
        assertEquals(secret, tokenObj.getSecret());
    }

    @Test
    public void testGetRawResponse_WhenRawResponseIsPresent() {
        String token = "example_token";
        String secret = "example_secret";
        String rawResponse = "example_raw_response";
        Token tokenObj = new Token(token, secret, rawResponse);
        assertEquals(rawResponse, tokenObj.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponse_WhenRawResponseIsNotPresent() {
        String token = "example_token";
        String secret = "example_secret";
        Token tokenObj = new Token(token, secret);
        tokenObj.getRawResponse();
    }

    @Test
    public void testToString() {
        String token = "example_token";
        String secret = "example_secret";
        Token tokenObj = new Token(token, secret);
        String expected = "Token[example_token , example_secret]";
        assertEquals(expected, tokenObj.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTokenConstructor_WhenTokenIsNull() {
        String secret = "example_secret";
        String rawResponse = "example_raw_response";
        Token tokenObj = new Token(null, secret, rawResponse);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTokenConstructor_WhenSecretIsNull() {
        String token = "example_token";
        String rawResponse = "example_raw_response";
        Token tokenObj = new Token(token, null, rawResponse);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTokenConstructor_WhenRawResponseIsNull() {
        String token = "example_token";
        String secret = "example_secret";
        Token tokenObj = new Token(token, secret, null);
    }

    @Test
    public void testTokenConstructor_WhenBothTokenAndSecretAreEmptyStrings() {
        String token = "";
        String secret = "";
        Token tokenObj = new Token(token, secret);
        assertEquals("", tokenObj.getToken());
        assertEquals("", tokenObj.getSecret());
    }
}