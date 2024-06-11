package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Token;

public class TokenTest {

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
        String rawResponse = "responseData";
        Token tokenInstance = new Token(token, secret, rawResponse);
        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
        assertEquals(rawResponse, tokenInstance.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenTokenObjectDoesNotHaveRawResponse() {
        String token = "abc123";
        String secret = "def456";
        Token tokenInstance = new Token(token, secret);
        tokenInstance.getRawResponse();
    }

    @Test
    public void testToStringMethod() {
        String token = "abc123";
        String secret = "def456";
        Token tokenInstance = new Token(token, secret);
        String expected = "Token[abc123 , def456]";
        assertEquals(expected, tokenInstance.toString());
    }

    @Test
    public void testGetTokenWhenTokenIsNull() {
        String token = null;
        String secret = "def456";
        try {
            new Token(token, secret);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetSecretWhenSecretIsNull() {
        String token = "abc123";
        String secret = null;
        try {
            new Token(token, secret);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}