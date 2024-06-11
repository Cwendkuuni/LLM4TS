package org.scribe.model; // import necessary packages here if needed

import org.junit.Test;
import static org.junit.Assert.*;

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
        String rawResponse = "responseData";
        new Token(token, secret, rawResponse); // Should throw an exception
    }

    @Test
    public void testTokenToStringMethod() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        String expectedTokenString = "Token[abc123 , def456]";
        assertEquals(expectedTokenString, tokenObject.toString());
    }

    @Test
    public void testGetRawResponseMethodWithValidTokenObject() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "responseData";
        Token tokenObject = new Token(token, secret, rawResponse);
        assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseMethodWithInvalidTokenObject() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        tokenObject.getRawResponse(); // Should throw an exception
    }
}