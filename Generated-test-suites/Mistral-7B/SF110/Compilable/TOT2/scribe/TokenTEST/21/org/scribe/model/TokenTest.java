package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenWithSecret() {
        Token token = new Token("token123", "secret123");
        String tokenValue = token.getToken();
        String secretValue = token.getSecret();
        assertNotNull(tokenValue);
        assertNotNull(secretValue);
        assertEquals("token123", tokenValue);
        assertEquals("secret123", secretValue);
    }

    @Test
    public void testTokenWithSecretAndRawResponse() {
        Token token = new Token("token123", "secret123", "rawResponse123");
        String tokenValue = token.getToken();
        String secretValue = token.getSecret();
        String rawResponseValue = token.getRawResponse();
        assertNotNull(tokenValue);
        assertNotNull(secretValue);
        assertEquals("token123", tokenValue);
        assertNotNull(secretValue);
        assertEquals("secret123", secretValue);
        assertNotNull(rawResponseValue);
        assertEquals("rawResponse123", rawResponseValue);
    }

    @Test
    public void testGetToken() {
        Token token = new Token("token123", "secret123");
        String tokenValue = token.getToken();
        assertNotNull(tokenValue);
        assertEquals("token123", tokenValue);
    }

    @Test
    public void testGetSecret() {
        Token token = new Token("token123", "secret123");
        String secretValue = token.getSecret();
        assertNotNull(secretValue);
        assertEquals("secret123", secretValue);
    }

    @Test
    public void testGetRawResponse() {
        Token token = new Token("token123", "secret123", "rawResponse123");
        String rawResponseValue = token.getRawResponse();
        assertNotNull(rawResponseValue);
        assertEquals("rawResponse123", rawResponseValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutValidTokenObject() {
        Token token = new Token("token123", "secret123");
        token.getRawResponse(); // This line should throw an IllegalStateException
    }
}