package org.scribe.model.test;

import static org.junit.Assert.*;

import org.scribe.model.Token;

import org.junit.Test;

public class TokenTest {

    // Test constructor with token and secret
    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
    }

    // Test constructor with token, secret and rawResponse
    @Test
    public void testTokenConstructorWithTokenAndSecretAndRawResponse() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "raw response data";
        Token tokenObject = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    // Test getToken method
    @Test
    public void testGetTokenMethod() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        assertEquals(token, tokenObject.getToken());
    }

    // Test getSecret method
    @Test
    public void testGetSecretMethod() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        assertEquals(secret, tokenObject.getSecret());
    }

    // Test getRawResponse method with valid rawResponse
    @Test
    public void testGetRawResponseMethodWithValidRawResponse() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "raw response data";
        Token tokenObject = new Token(token, secret, rawResponse);
        assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    // Test getRawResponse method with null rawResponse
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseMethodWithNullRawResponse() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        tokenObject.getRawResponse(); // should throw IllegalStateException
    }

    // Test toString method
    @Test
    public void testToStringMethod() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        String expected = "Token[abc123 , def456]";
        assertEquals(expected, tokenObject.toString());
    }
}