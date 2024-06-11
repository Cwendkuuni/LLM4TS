
package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenWithSecret() {
        String token = "abc123";
        String secret = "def456";

        Token tokenObject = new Token(token, secret);

        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertNull(tokenObject.getRawResponse());
    }

    @Test
    public void testTokenWithSecretAndRawResponse() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "{\"access_token\":\"token123\", \"token_type\":\"Bearer\", \"expires_in\":3600}";

        Token tokenObject = new Token(token, secret, rawResponse);

        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test
    public void testGetToken() {
        String token = "abc123";
        String secret = "def456";

        Token tokenObject = new Token(token, secret);

        assertEquals(token, tokenObject.getToken());
    }

    @Test
    public void testGetSecret() {
        String token = "abc123";
        String secret = "def456";

        Token tokenObject = new Token(token, secret);

        assertEquals(secret, tokenObject.getSecret());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponse() {
        String token = "abc123";
        String secret = "def456";

        Token tokenObject = new Token(token, secret);

        tokenObject.getRawResponse();
    }

    @Test
    public void testGetRawResponseWithRawResponse() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "{\"access_token\":\"token123\", \"token_type\":\"Bearer\", \"expires_in\":3600}";

        Token tokenObject = new Token(token, secret, rawResponse);

        assertEquals(rawResponse, tokenObject.getRawResponse());
    }
}