package org.junit.tests;

import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.model.Token;

public class TokenTest {
    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "abcdef123456";
        String secret = "qwertyuiop123456";
        Token tokenInstance = new Token(token, secret);
        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
        assertNull(tokenInstance.getRawResponse());
    }

    @Test
    public void testTokenConstructorWithTokenAndSecretAndRawResponse() {
        String token = "abcdef123456";
        String secret = "qwertyuiop123456";
        String rawResponse = "this_is_the_raw_response";
        Token tokenInstance = new Token(token, secret, rawResponse);
        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
        assertEquals(rawResponse, tokenInstance.getRawResponse());
    }

    @Test
    public void testGetToken() {
        String token = "abcdef123456";
        String secret = "qwertyuiop123456";
        Token tokenInstance = new Token(token, secret);
        assertEquals(token, tokenInstance.getToken());
    }

    @Test
    public void testGetSecret() {
        String token = "abcdef123456";
        String secret = "qwertyuiop123456";
        Token tokenInstance = new Token(token, secret);
        assertEquals(secret, tokenInstance.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        String token = "abcdef123456";
        String secret = "qwertyuiop123456";
        String rawResponse = "this_is_the_raw_response";
        Token tokenInstance = new Token(token, secret, rawResponse);
        assertEquals(rawResponse, tokenInstance.getRawResponse());
    }

    @Test
    public void testToString() {
        String token = "abcdef123456";
        String secret = "qwertyuiop123456";
        Token tokenInstance = new Token(token, secret);
        String expectedOutput = "Token[abcdef123456 , qwertyuiop123456]";
        assertEquals(expectedOutput, tokenInstance.toString());
    }
}