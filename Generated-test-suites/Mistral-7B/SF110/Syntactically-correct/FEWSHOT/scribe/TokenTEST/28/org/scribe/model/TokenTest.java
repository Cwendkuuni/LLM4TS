package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenConstructionWithTokenAndSecret() {
        String token = "abcd1234";
        String secret = "xyz123456";
        Token tokenObject = new Token(token, secret);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertNull(tokenObject.getRawResponse());
    }

    @Test
    public void testTokenConstructionWithTokenSecretAndRawResponse() {
        String token = "abcd1234";
        String secret = "xyz123456";
        String rawResponse = "{\"access_token\":\"abcd1234\", \"expires_in\":123}";
        Token tokenObject = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testTokenWithoutRawResponse() {
        String token = "abcd1234";
        String secret = "xyz123456";
        new Token(token, secret);
    }

    @Test
    public void testToString() {
        String token = "abcd1234";
        String secret = "xyz123456";
        Token tokenObject = new Token(token, secret);
        String expected = "Token[abcd1234 , xyz123456]";
        assertEquals(expected, tokenObject.toString());
    }
}