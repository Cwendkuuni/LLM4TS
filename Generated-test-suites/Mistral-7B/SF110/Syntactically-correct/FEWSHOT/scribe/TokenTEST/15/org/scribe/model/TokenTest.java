package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "access_token";
        String secret = "access_token_secret";
        Token tokenInstance = new Token(token, secret);

        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
        assertNull(tokenInstance.getRawResponse());
    }

    @Test
    public void testTokenConstructorWithTokenSecretAndRawResponse() {
        String token = "access_token";
        String secret = "access_token_secret";
        String rawResponse = "raw_response";
        Token tokenInstance = new Token(token, secret, rawResponse);

        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
        assertEquals(rawResponse, tokenInstance.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionWhenGettingRawResponseOfUnconstructedToken() {
        String token = "access_token";
        String secret = "access_token_secret";
        Token tokenInstance = new Token(token, secret);
        tokenInstance.getRawResponse(); // This will throw an IllegalStateException
    }

    @Test
    public void testToStringMethod() {
        String token = "access_token";
        String secret = "access_token_secret";
        Token tokenInstance = new Token(token, secret);

        String expected = String.format("Token[%s , %s]", token, secret);
        String actual = tokenInstance.toString();

        assertEquals(expected, actual);
    }
}