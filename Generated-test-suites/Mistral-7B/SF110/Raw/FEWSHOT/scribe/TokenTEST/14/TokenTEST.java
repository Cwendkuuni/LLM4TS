package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenConstructorOneArg() {
        String token = "access_token";
        Token tokenObject = new Token(token);
        assertEquals(token, tokenObject.getToken());
        assertNull(tokenObject.getSecret());
        assertNull(tokenObject.getRawResponse());
    }

    @Test
    public void testTokenConstructorTwoArgs() {
        String token = "access_token";
        String secret = "access_token_secret";
        Token tokenObject = new Token(token, secret);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertNull(tokenObject.getRawResponse());
    }

    @Test
    public void testTokenConstructorThreeArgs() {
        String token = "access_token";
        String secret = "access_token_secret";
        String rawResponse = "{\"access_token\":\"access_token\",\"token_type\":\"Bearer\",\"expires_in\":3600,\"refresh_token\":\"refresh_token\"}";
        Token tokenObject = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponse() {
        String token = "access_token";
        String secret = "access_token_secret";
        Token tokenObject = new Token(token, secret);
        tokenObject.getRawResponse();
    }

    @Test
    public void testToString() {
        String token = "access_token";
        String secret = "access_token_secret";
        String rawResponse = "{\"access_token\":\"access_token\",\"token_type\":\"Bearer\",\"expires_in\":3600,\"refresh_token\":\"refresh_token\"}";
        Token tokenObject = new Token(token, secret, rawResponse);
        String expected = String.format("Token[%s , %s]", token, secret);
        assertEquals(expected, tokenObject.toString());
    }
}