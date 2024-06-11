package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenConstructionWithTokenAndSecret() {
        String token = "oauth_token_123";
        String secret = "oauth_token_secret_123";
        Token tokenObject = new Token(token, secret);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertNull(tokenObject.getRawResponse());
    }

    @Test
    public void testTokenConstructionWithTokenSecretAndRawResponse() {
        String token = "oauth_token_123";
        String secret = "oauth_token_secret_123";
        String rawResponse = "{\"access_token\":\"access_token_value\", \"token_type\":\"Bearer\"}";
        Token tokenObject = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGettingRawResponseWhenNotConstructedWithIt() {
        String token = "oauth_token_123";
        String secret = "oauth_token_secret_123";
        Token tokenObject = new Token(token, secret);
        tokenObject.getRawResponse();
    }

    @Test
    public void testToStringMethod() {
        String token = "oauth_token_123";
        String secret = "oauth_token_secret_123";
        Token tokenObject = new Token(token, secret);
        String expected = "Token[oauth_token_123 , oauth_token_secret_123]";
        assertEquals(expected, tokenObject.toString());
    }
}