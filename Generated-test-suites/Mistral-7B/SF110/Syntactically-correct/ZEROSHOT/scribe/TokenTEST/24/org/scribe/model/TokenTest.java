package org.scribe.model; // make sure to import necessary packages at the beginning of your test file

import org.junit.Test;
import org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenConstructionWithTokenAndSecret() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertNull(tokenObject.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testTokenConstructionWithTokenAndSecretAndRawResponse() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "rawResponseData";
        new Token(token, secret, rawResponse); // this line will throw an exception
    }

    @Test
    public void testTokenToString() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        String expectedTokenString = String.format("Token[%s , %s]", token, secret);
        assertEquals(expectedTokenString, tokenObject.toString());
    }

    @Test
    public void testGetRawResponse() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "rawResponseData";
        Token tokenObject = new Token(token, secret, rawResponse);
        assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseForTokenWithoutRawResponse() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        tokenObject.getRawResponse(); // this line will throw an exception
    }
}