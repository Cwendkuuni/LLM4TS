package org.scribe.model.test;

import static org.junit.Assert.*;

import org.scribe.model.Token;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "abcdefg123456";
        String secret = "hijklmnopqrstuv";
        Token tokenObject = new Token(token, secret);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
    }

    @Test(expected = IllegalStateException.class)
    public void testTokenConstructorWithoutRawResponse() {
        String token = "abcdefg123456";
        String secret = "hijklmnopqrstuv";
        Token tokenObject = new Token(token, secret);
        tokenObject.getRawResponse(); // this will throw an exception
    }

    @Test
    public void testTokenConstructorWithTokenSecretAndRawResponse() {
        String token = "abcdefg123456";
        String secret = "hijklmnopqrstuv";
        String rawResponse = "response from OAuth server";
        Token tokenObject = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test
    public void testGetTokens() {
        String token = "abcdefg123456";
        String secret = "hijklmnopqrstuv";
        Token tokenObject = new Token(token, secret);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertNull(tokenObject.getRawResponse());
    }

    @Test
    public void testGetSecret() {
        String token = "abcdefg123456";
        String secret = "hijklmnopqrstuv";
        Token tokenObject = new Token(token, secret);
        assertEquals(secret, tokenObject.getSecret());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponse() {
        String token = "abcdefg123456";
        String secret = "hijklmnopqrstuv";
        Token tokenObject = new Token(token, secret);
        tokenObject.getRawResponse(); // this will throw an exception
    }

    @Test
    public void testToString() {
        String token = "abcdefg123456";
        String secret = "hijklmnopqrstuv";
        Token tokenObject = new Token(token, secret);
        String expectedString = "Token[abcdefg123456 , hijklmnopqrstuv]";
        assertEquals(expectedString, tokenObject.toString());
    }

    @Test
    public void testNullToken() {
        String secret = "hijklmnopqrstuv";
        Token tokenObject = new Token(null, secret);
        assertNull(tokenObject.getToken());
    }

    @Test
    public void testNullSecret() {
        String token = "abcdefg123456";
        Token tokenObject = new Token(token, null);
        assertNull(tokenObject.getSecret());
    }
}