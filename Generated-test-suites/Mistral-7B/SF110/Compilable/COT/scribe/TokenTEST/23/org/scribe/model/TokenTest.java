package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Token;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "abcdefg123456";
        String secret = "hijklmnopqrstuvwxyz123456";
        Token tokenObject = new Token(token, secret);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertNull(tokenObject.getRawResponse());
    }

    @Test
    public void testTokenConstructorWithTokenSecretAndRawResponse() {
        String token = "abcdefg123456";
        String secret = "hijklmnopqrstuvwxyz123456";
        String rawResponse = "rawResponseData";
        Token tokenObject = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenTokenObjectWasNotConstructedByScribe() {
        String token = "abcdefg123456";
        String secret = "hijklmnopqrstuvwxyz123456";
        Token tokenObject = new Token(token, secret);
        tokenObject.getRawResponse();
    }

    @Test
    public void testToStringMethod() {
        String token = "abcdefg123456";
        String secret = "hijklmnopqrstuvwxyz123456";
        Token tokenObject = new Token(token, secret);
        String expected = "Token[abcdefg123456 , hijklmnopqrstuvwxyz123456]";
        assertEquals(expected, tokenObject.toString());
    }

    @Test
    public void testGetTokenWithNullToken() {
        String token = null;
        Token tokenObject = new Token(token, "secret");
        assertNull(tokenObject.getToken());
        assertNotNull(tokenObject.getSecret());
    }

    @Test
    public void testGetSecretWithNullSecret() {
        String secret = null;
        Token tokenObject = new Token("token", secret);
        assertNotNull(tokenObject.getToken());
        assertNull(tokenObject.getSecret());
    }

    @Test
    public void testGetTokenWithEmptyString() {
        String token = "";
        Token tokenObject = new Token(token, "secret");
        assertEquals("", tokenObject.getToken());
        assertNotNull(tokenObject.getSecret());
    }

    @Test
    public void testGetSecretWithEmptyStringAndRawResponse() {
        String token = "";
        String rawResponse = "rawResponseData";
        Token tokenObject = new Token(token, "", rawResponse);
        assertEquals("", tokenObject.getToken());
        assertNotNull(tokenObject.getSecret());
        assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test
    public void testGetTokenWithNullTokenAndRawResponse() {
        String token = null;
        String rawResponse = "rawResponseData";
        Token tokenObject = new Token(token, "", rawResponse);
        assertNull(tokenObject.getToken());
        assertNotNull(tokenObject.getSecret());
        assertNull(tokenObject.getRawResponse());
    }

    @Test
    public void testGetSecretWithEmptyStringAndNullRawResponse() {
        String token = "";
        String rawResponse = null;
        Token tokenObject = new Token(token, "", rawResponse);
        assertEquals("", tokenObject.getToken());
        assertNull(tokenObject.getSecret());
        assertNull(tokenObject.getRawResponse());
    }
}