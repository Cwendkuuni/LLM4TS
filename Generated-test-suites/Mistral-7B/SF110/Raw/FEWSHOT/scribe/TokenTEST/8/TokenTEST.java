package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenConstructionWithTokenAndSecret() {
        String token = "token123";
        String secret = "secret123";
        Token tokenObject = new Token(token, secret);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertNull(tokenObject.getRawResponse());
    }

    @Test
    public void testTokenConstructionWithTokenSecretAndRawResponse() {
        String token = "token123";
        String secret = "secret123";
        String rawResponse = "rawResponse123";
        Token tokenObject = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenNotConstructedWithRawResponse() {
        String token = "token123";
        String secret = "secret123";
        Token tokenObject = new Token(token, secret);
        tokenObject.getRawResponse();
    }

    @Test
    public void testToStringMethod() {
        String token = "token123";
        String secret = "secret123";
        Token tokenObject = new Token(token, secret);
        String expected = "Token[token123 , secret123]";
        assertEquals(expected, tokenObject.toString());
    }
}