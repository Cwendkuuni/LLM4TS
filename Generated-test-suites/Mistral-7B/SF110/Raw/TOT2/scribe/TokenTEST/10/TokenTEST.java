package org.junit.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Token;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "token123";
        String secret = "secret456";
        Token tokenInstance = new Token(token, secret);
        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
        assertNull(tokenInstance.getRawResponse());
    }

    @Test
    public void testTokenConstructorWithTokenAndSecretAndRawResponse() {
        String token = "token123";
        String secret = "secret456";
        String rawResponse = "rawResponse123";
        Token tokenInstance = new Token(token, secret, rawResponse);
        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
        assertEquals(rawResponse, tokenInstance.getRawResponse());
    }

    @Test
    public void testTokenConstructorWithEmptyToken() {
        String token = "";
        String secret = "secret456";
        Token tokenInstance = new Token(token, secret);
        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
        assertNull(tokenInstance.getRawResponse());
    }

    @Test
    public void testTokenConstructorWithEmptySecret() {
        String token = "token123";
        String secret = "";
        Token tokenInstance = new Token(token, secret);
        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
        assertNull(tokenInstance.getRawResponse());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTokenConstructorWithNullToken() {
        String secret = "secret456";
        String token = null;
        Token tokenInstance = new Token(token, secret);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTokenConstructorWithNullSecret() {
        String token = "token123";
        String secret = null;
        Token tokenInstance = new Token(token, secret);
    }

    @Test
    public void testTokenConstructorWithLongTokenAndSecret() {
        String token = new String(new char[1024 * 1024]).replace("\0", "a");
        String secret = new String(new char[1024 * 1024]).replace("\0", "a");
        Token tokenInstance = new Token(token, secret);
        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
        assertNull(tokenInstance.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithNullObject() {
        Token token = null;
        token.getRawResponse();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithObjectNotConstructedByScribe() {
        String token = "token123";
        Token tokenInstance = new Token(token);
        tokenInstance.getRawResponse();
    }
}