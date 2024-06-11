package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Token;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenObject = new Token(token, secret);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertNull(tokenObject.getRawResponse());
    }

    @Test
    public void testTokenConstructorWithTokenSecretAndRawResponse() {
        String token = "testToken";
        String secret = "testSecret";
        String rawResponse = "testRawResponse";
        Token tokenObject = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenRawResponseIsNull() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenObject = new Token(token, secret);
        tokenObject.getRawResponse();
    }

    @Test
    public void testToStringMethod() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenObject = new Token(token, secret);
        String expectedString = "Token[testToken , testSecret]";
        assertEquals(expectedString, tokenObject.toString());
    }

    @Test
    public void testGetTokenMethod() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenObject = new Token(token, secret);
        assertEquals(token, tokenObject.getToken());
    }

    @Test
    public void testGetSecretMethod() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenObject = new Token(token, secret);
        assertEquals(secret, tokenObject.getSecret());
    }

    @Test
    public void testGetRawResponseWithRawResponse() {
        String token = "testToken";
        String secret = "testSecret";
        String rawResponse = "testRawResponse";
        Token tokenObject = new Token(token, secret, rawResponse);
        assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponse() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenObject = new Token(token, secret);
        tokenObject.getRawResponse();
    }
}