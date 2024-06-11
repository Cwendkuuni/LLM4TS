package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testConstructorWithTokenAndSecret() {
        String token = "token123";
        String secret = "secret123";
        Token tokenObject = new Token(token, secret);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertNull(tokenObject.getRawResponse());
    }

    @Test
    public void testConstructorWithTokenSecretAndRawResponse() {
        String token = "token123";
        String secret = "secret123";
        String rawResponse = "rawResponse123";
        Token tokenObject = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponse() {
        String token = "token123";
        String secret = "secret123";
        new Token(token, secret);
    }

    @Test
    public void testToString() {
        String token = "token123";
        String secret = "secret123";
        String rawResponse = "rawResponse123";
        String expected = String.format("Token[%s , %s]", token, secret);
        Token tokenObject = new Token(token, secret, rawResponse);
        assertEquals(expected, tokenObject.toString());
    }
}