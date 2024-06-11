package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Token;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "sample_token";
        String secret = "sample_secret";
        Token tokenObject = new Token(token, secret);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertNull(tokenObject.getRawResponse());
    }

    @Test
    public void testTokenConstructorWithTokenSecretAndRawResponse() {
        String token = "sample_token";
        String secret = "sample_secret";
        String rawResponse = "sample_raw_response";
        Token tokenObject = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenTokenNotConstructedWithRawResponse() {
        String token = "sample_token";
        String secret = "sample_secret";
        Token tokenObject = new Token(token, secret);
        tokenObject.getRawResponse();
    }

    @Test
    public void testToStringMethod() {
        String token = "sample_token";
        String secret = "sample_secret";
        Token tokenObject = new Token(token, secret);
        String expectedString = "Token[sample_token , sample_secret]";
        assertEquals(expectedString, tokenObject.toString());
    }

    @Test
    public void testGetTokenWithNullToken() {
        String token = null;
        String secret = "sample_secret";
        Token tokenObject = new Token(token, secret);
        assertNull(tokenObject.getToken());
    }

    @Test
    public void testGetSecretWithNullSecret() {
        String token = "sample_token";
        String secret = null;
        Token tokenObject = new Token(token, secret);
        assertNull(tokenObject.getSecret());
    }

    @Test
    public void testGetRawResponseWithEmptyString() {
        String token = "sample_token";
        String secret = "sample_secret";
        String rawResponse = "";
        Token tokenObject = new Token(token, secret, rawResponse);
        assertNotNull(tokenObject.getRawResponse());
        assertEquals("", tokenObject.getRawResponse());
    }

    @Test
    public void testGetRawResponseWithLongString() {
        String token = "sample_token";
        String secret = "sample_secret";
        String rawResponse = "sample_raw_response_of_length_1000";
        Token tokenObject = new Token(token, secret, rawResponse);
        assertNotNull(tokenObject.getRawResponse());
        assertTrue(tokenObject.getRawResponse().length() > 50);
    }
}