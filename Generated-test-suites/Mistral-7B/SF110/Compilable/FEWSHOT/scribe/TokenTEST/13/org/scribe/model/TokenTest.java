package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {
    @Test
    public void testTokenConstructionWithTokenAndSecret() {
        String token = "abc123";
        String secret = "def456";

        Token tokenObject = new Token(token, secret);

        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertNull(tokenObject.getRawResponse());
    }

    @Test
    public void testTokenConstructionWithTokenSecretAndRawResponse() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "response from API";

        Token tokenObject = new Token(token, secret, rawResponse);

        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionWhenGettingRawResponseForTokenWithoutRawResponse() {
        String token = "abc123";
        String secret = "def456";

        Token tokenObject = new Token(token, secret);

        tokenObject.getRawResponse();
    }

    @Test
    public void testToStringMethod() {
        String token = "abc123";
        String secret = "def456";

        Token tokenObject = new Token(token, secret);

        String expectedString = "Token[abc123 , def456]";
        String actualString = tokenObject.toString();

        assertEquals(expectedString, actualString);
    }
}