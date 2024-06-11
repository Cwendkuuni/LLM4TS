package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;;

public class TokenTest {

    @Test
    public void testGetToken() {
        String token = "abc123";
        Token tokenInstance = new Token(token, "def456");
        assertEquals(token, tokenInstance.getToken());
    }

    @Test
    public void testGetSecret() {
        String secret = "def456";
        Token tokenInstance = new Token("abc123", secret);
        assertEquals(secret, tokenInstance.getSecret());
    }

    @Test
    public void testGetRawResponse_null() {
        Token tokenInstance = new Token("abc123", "def456");
        try {
            tokenInstance.getRawResponse();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testGetRawResponse_nonNull() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "{\"access_token\":\"xyz789\",\"expires_in\":3600}";
        Token tokenInstance = new Token(token, secret, rawResponse);
        assertEquals(rawResponse, tokenInstance.getRawResponse());
    }

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "abc123";
        String secret = "def456";
        Token tokenInstance = new Token(token, secret);
        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
        assertNull(tokenInstance.getRawResponse());
    }

    @Test
    public void testTokenConstructorWithTokenSecretAndRawResponse() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "{\"access_token\":\"xyz789\",\"expires_in\":3600}";
        Token tokenInstance = new Token(token, secret, rawResponse);
        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
        assertEquals(rawResponse, tokenInstance.getRawResponse());
    }
}