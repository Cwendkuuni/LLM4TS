package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Token;

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
    public void testGetRawResponseWhenNoRawResponse() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObj = new Token(token, secret);
        tokenObj.getRawResponse();
    }

    @Test
    public void testToStringMethod() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObj = new Token(token, secret);
        String expected = "Token[abc123 , def456]";
        assertEquals(expected, tokenObj.toString());
    }

    @Test
    public void testGetTokenNull() {
        String secret = "def456";
        Token tokenObj = new Token(null, secret);
        assertNull(tokenObj.getToken());
        assertTrue(tokenObj.getSecret() != null);
    }

    @Test
    public void testGetSecretNull() {
        String token = "abc123";
        Token tokenObj = new Token(token, null);
        assertNull(tokenObj.getSecret());
        assertTrue(tokenObj.getToken() != null);
    }

    @Test
    public void testEmptyToken() {
        String token = "";
        String secret = "def456";
        Token tokenObj = new Token(token, secret);
        assertEquals("", tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
        assertNull(tokenObj.getRawResponse());
    }

    @Test
    public void testEmptySecret() {
        String token = "abc123";
        Token tokenObj = new Token(token, "");
        assertEquals(token, tokenObj.getToken());
        assertNull(tokenObj.getSecret());
        assertNull(tokenObj.getRawResponse());
    }
}