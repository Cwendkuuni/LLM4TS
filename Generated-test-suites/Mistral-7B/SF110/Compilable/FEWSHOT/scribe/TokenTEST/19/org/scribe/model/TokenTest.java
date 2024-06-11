package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObj = new Token(token, secret);
        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
        assertNull(tokenObj.getRawResponse());
    }

    @Test
    public void testTokenConstructorWithTokenSecretAndRawResponse() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "response data";
        Token tokenObj = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
        assertEquals(rawResponse, tokenObj.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testTokenNoRawResponse() {
        String token = "abc123";
        String secret = "def456";
        new Token(token, secret);
    }

    @Test
    public void testToStringMethod() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "response data";
        Token tokenObj = new Token(token, secret, rawResponse);
        String expectedString = String.format("Token[%s , %s]", token, secret);
        assertEquals(expectedString, tokenObj.toString());
    }
}