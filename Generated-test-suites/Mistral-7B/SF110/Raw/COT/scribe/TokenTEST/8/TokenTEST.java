package org.scribe.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.scribe.model.Token;

public class TokenTest {

    // Test constructor with token and secret
    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObj = new Token(token, secret);
        assertNotNull(tokenObj);
        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
        assertNull(tokenObj.getRawResponse());
    }

    // Test constructor with token, secret and rawResponse
    @Test
    public void testTokenConstructorWithTokenAndSecretAndRawResponse() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "{\"access_token\":\"abc123\",\"token_type\":\"Bearer\"}";
        Token tokenObj = new Token(token, secret, rawResponse);
        assertNotNull(tokenObj);
        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
        assertEquals(rawResponse, tokenObj.getRawResponse());
    }

    // Test getToken method
    @Test
    public void testGetToken() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObj = new Token(token, secret);
        assertEquals(token, tokenObj.getToken());
    }

    // Test getSecret method
    @Test
    public void testGetSecret() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObj = new Token(token, secret);
        assertEquals(secret, tokenObj.getSecret());
    }

    // Test getRawResponse method with valid token
    @Test
    public void testGetRawResponseWithValidToken() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "{\"access_token\":\"abc123\",\"token_type\":\"Bearer\"}";
        Token tokenObj = new Token(token, secret, rawResponse);
        assertEquals(rawResponse, tokenObj.getRawResponse());
    }

    // Test getRawResponse method with invalid token
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithInvalidToken() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObj = new Token(token, secret);
        tokenObj.getRawResponse();
    }

    // Test toString method
    @Test
    public void testToString() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObj = new Token(token, secret);
        assertTrue(tokenObj.toString().contains("Token[") && tokenObj.toString().contains("]")
                && tokenObj.toString().contains(token) && tokenObj.toString().contains(secret));
    }
}