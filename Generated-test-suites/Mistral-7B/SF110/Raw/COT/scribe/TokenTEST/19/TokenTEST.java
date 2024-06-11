import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Token;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "abc123";
        String secret = "def456";
        Token tokenInstance = new Token(token, secret);
        String expectedToken = token;
        String expectedSecret = secret;
        String actualToken = tokenInstance.getToken();
        String actualSecret = tokenInstance.getSecret();
        assertEquals(expectedToken, actualToken);
        assertEquals(expectedSecret, actualSecret);
    }

    @Test
    public void testTokenConstructorWithTokenSecretAndRawResponse() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "responseText";
        Token tokenInstance = new Token(token, secret, rawResponse);
        String expectedToken = token;
        String expectedSecret = secret;
        String expectedRawResponse = rawResponse;
        String actualToken = tokenInstance.getToken();
        String actualSecret = tokenInstance.getSecret();
        String actualRawResponse = tokenInstance.getRawResponse();
        assertEquals(expectedToken, actualToken);
        assertEquals(expectedSecret, actualSecret);
        assertEquals(expectedRawResponse, actualRawResponse);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponse() {
        String token = "abc123";
        String secret = "def456";
        Token tokenInstance = new Token(token, secret);
        tokenInstance.getRawResponse();
    }

    @Test
    public void testToStringMethod() {
        String token = "abc123";
        String secret = "def456";
        Token tokenInstance = new Token(token, secret);
        String expectedString = String.format("Token[%s , %s]", token, secret);
        String actualString = tokenInstance.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testGetTokenNullToken() {
        String secret = "def456";
        String rawResponse = "responseText";
        Token tokenInstance = new Token(null, secret, rawResponse);
        assertNull(tokenInstance.getToken());
    }

    @Test
    public void testGetSecretNullSecret() {
        String token = "abc123";
        String rawResponse = "responseText";
        Token tokenInstance = new Token(token, null, rawResponse);
        assertNull(tokenInstance.getSecret());
    }

    @Test
    public void testGetTokenEmptyString() {
        String secret = "def456";
        String rawResponse = "responseText";
        Token tokenInstance = new Token("", secret, rawResponse);
        assertEquals("", tokenInstance.getToken());
    }

    @Test
    public void testGetSecretEmptyString() {
        String token = "abc123";
        String rawResponse = "responseText";
        Token tokenInstance = new Token(token, "", rawResponse);
        assertNull(tokenInstance.getSecret());
    }
}