package org.scribe.model.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Token;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "abcdefg123456";
        String secret = "hijklmnopqrstuvwxyz123456";
        Token tokenObject = new Token(token, secret);
        String actualToken = tokenObject.getToken();
        String actualSecret = tokenObject.getSecret();
        assertTrue("Token constructor with token and secret should create an instance with the given token and secret",
                actualToken.equals(token) && actualSecret.equals(secret));
    }

    @Test
    public void testTokenConstructorWithTokenSecretAndRawResponse() {
        String token = "abcdefg123456";
        String secret = "hijklmnopqrstuvwxyz123456";
        String rawResponse = "{\"access_token\":\"abcdefg123456\",\"expires_in\":3600,\"token_type\":\"bearer\"}";
        Token tokenObject = new Token(token, secret, rawResponse);
        String actualToken = tokenObject.getToken();
        String actualSecret = tokenObject.getSecret();
        String actualRawResponse = tokenObject.getRawResponse();
        assertTrue("Token constructor with token, secret and rawResponse should create an instance with the given token, secret and rawResponse",
                actualToken.equals(token) && actualSecret.equals(secret) && actualRawResponse.equals(rawResponse));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenNoRawResponse() {
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
        String expectedResult = "Token[abcdefg123456 , hijklmnopqrstuvwxyz123456]";
        String actualResult = tokenObject.toString();
        assertEquals("toString method should return the expected string representation", expectedResult, actualResult);
    }

    @Test
    public void testGetTokenMethodWithNullToken() {
        String secret = "hijklmnopqrstuvwxyz123456";
        Token tokenObject = new Token(null, secret);
        assertNull("getToken method should return null if token is null", tokenObject.getToken());
    }

    @Test
    public void testGetSecretMethodWithNullSecret() {
        String token = "abcdefg123456";
        Token tokenObject = new Token(token, null);
        assertNull("getSecret method should return null if secret is null", tokenObject.getSecret());
    }
}