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
        String rawResponse = "this is raw response";
        Token tokenObject = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenInstanceWasNotConstructedWithRawResponse() {
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
        String expectedOutput = "Token[abc123 , def456]";
        assertEquals(expectedOutput, tokenObject.toString());
    }

    @Test
    public void testGetTokenNullValue() {
        Token tokenObject = new Token(null, "secret");
        assertNull(tokenObject.getToken());
    }

    @Test
    public void testGetSecretNullValue() {
        Token tokenObject = new Token("token", null);
        assertNull(tokenObject.getSecret());
    }

    @Test
    public void testGetTokenEmptyString() {
        Token tokenObject = new Token("", "secret");
        assertEquals("", tokenObject.getToken());
    }

    @Test
    public void testGetSecretEmptyString() {
        Token tokenObject = new Token("token", "");
        assertNull(tokenObject.getSecret());
    }

    @Test
    public void testGetTokenWithSpecialCharacters() {
        String token = "abc@123!";
        String secret = "def#456%";
        Token tokenObject = new Token(token, secret);
        assertEquals(token, tokenObject.getToken());
    }

    @Test
    public void testGetSecretWithSpecialCharacters() {
        String token = "abc123";
        String secret = "def#456%";
        Token tokenObject = new Token(token, secret);
        assertEquals(secret, tokenObject.getSecret());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTokenConstructorWithNullToken() {
        String secret = "def456";
        Token tokenObject = new Token(null, secret);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTokenConstructorWithNullSecret() {
        String token = "abc123";
        Token tokenObject = new Token(token, null);
    }
}