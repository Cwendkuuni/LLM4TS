package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Token;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "1234567890";
        String secret = "abcdefghijklmnopqrstuvwxyz";
        Token tokenInstance = new Token(token, secret);
        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
    }

    @Test
    public void testTokenConstructorWithTokenSecretAndRawResponse() {
        String token = "1234567890";
        String secret = "abcdefghijklmnopqrstuvwxyz";
        String rawResponse = "{\"access_token\":\"1234567890\",\"token_type\":\"Bearer\"}";
        Token tokenInstance = new Token(token, secret, rawResponse);
        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
        assertEquals(rawResponse, tokenInstance.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenNoRawResponse() {
        String token = "1234567890";
        String secret = "abcdefghijklmnopqrstuvwxyz";
        Token tokenInstance = new Token(token, secret);
        tokenInstance.getRawResponse();
    }

    @Test
    public void testToString() {
        String token = "1234567890";
        String secret = "abcdefghijklmnopqrstuvwxyz";
        Token tokenInstance = new Token(token, secret);
        String expected = "Token[1234567890 , abcdefghijklmnopqrstuvwxyz]";
        assertEquals(expected, tokenInstance.toString());
    }

    @Test
    public void testGetTokenWhenNullToken() {
        String secret = "abcdefghijklmnopqrstuvwxyz";
        String token = null;
        Token tokenInstance = new Token(token, secret);
        assertNull(tokenInstance.getToken());
        assertSame(secret, tokenInstance.getSecret());
    }

    @Test
    public void testGetSecretWhenNullSecret() {
        String token = "1234567890";
        String secret = null;
        Token tokenInstance = new Token(token, secret);
        assertSame(token, tokenInstance.getToken());
        assertNull(tokenInstance.getSecret());
    }

    @Test
    public void testGetTokenWhenEmptyStringToken() {
        String secret = "abcdefghijklmnopqrstuvwxyz";
        String token = "";
        Token tokenInstance = new Token(token, secret);
        assertSame(token, tokenInstance.getToken());
        assertSame(secret, tokenInstance.getSecret());
    }

    @Test
    public void testGetSecretWhenEmptyStringSecret() {
        String token = "1234567890";
        String secret = "";
        Token tokenInstance = new Token(token, secret);
        assertSame(token, tokenInstance.getToken());
        assertSame(secret, tokenInstance.getSecret());
    }
}