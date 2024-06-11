package org.scribe.model.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.model.Token;

public class TokenTest {
    @Test
    public void testTokenConstructor() {
        String token = "1aB3C4dE5F6";
        String secret = "abcdefg123456";
        Token tokenObject = new Token(token, secret);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertNull(tokenObject.getRawResponse());
    }

    @Test
    public void testTokenConstructorWithRawResponse() {
        String token = "1aB3C4dE5F6";
        String secret = "abcdefg123456";
        String rawResponse = "{\"access_token\":\"ABC123def456\",\"token_type\":\"Bearer\"}";
        Token tokenObject = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test
    public void testGetToken() {
        String token = "1aB3C4dE5F6";
        String secret = "abcdefg123456";
        Token tokenObject = new Token(token, secret);
        String returnedToken = tokenObject.getToken();
        assertEquals(token, returnedToken);
    }

    @Test
    public void testGetSecret() {
        String token = "1aB3C4dE5F6";
        String secret = "abcdefg123456";
        Token tokenObject = new Token(token, secret);
        String returnedSecret = tokenObject.getSecret();
        assertEquals(secret, returnedSecret);
    }

    @Test
    public void testGetRawResponse() {
        String token = "1aB3C4dE5F6";
        String secret = "abcdefg123456";
        Token tokenObject = new Token(token, secret, "{\"access_token\":\"ABC123def456\",\"token_type\":\"Bearer\"}");
        String returnedRawResponse = tokenObject.getRawResponse();
        assertEquals("{\"access_token\":\"ABC123def456\",\"token_type\":\"Bearer\"}", returnedRawResponse);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseNoRawResponse() {
        String token = "1aB3C4dE5F6";
        String secret = "abcdefg123456";
        Token tokenObject = new Token(token, secret);
        tokenObject.getRawResponse();
    }
}