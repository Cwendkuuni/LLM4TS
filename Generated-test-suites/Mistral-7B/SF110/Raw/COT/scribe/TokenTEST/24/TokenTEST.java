package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Token;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
    }

    @Test(expected = IllegalStateException.class)
    public void testTokenConstructorWithoutRawResponse() {
        String token = "abc123";
        String secret = "def456";
        new Token(token, secret); // No rawResponse provided, expect an IllegalStateException
    }

    @Test
    public void testTokenConstructorWithAllParameters() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "response data";
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
        tokenObject.getRawResponse(); // No rawResponse provided, expect an IllegalStateException
    }

    @Test
    public void testToString() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        String expected = String.format("Token[%s , %s]", token, secret);
        assertEquals(expected, tokenObject.toString());
    }

}