package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenConstructionWithTokenAndSecret() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObj = new Token(token, secret);

        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
        assertNull(tokenObj.getRawResponse());
    }

    @Test
    public void testTokenConstructionWithTokenSecretAndRawResponse() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "{\"access_token\":\"xyz789\", \"token_type\":\"Bearer\"}";
        Token tokenObj = new Token(token, secret, rawResponse);

        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
        assertEquals(rawResponse, tokenObj.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionWhenTokenObjectDoesNotHaveRawResponse() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObj = new Token(token, secret);

        tokenObj.getRawResponse();
    }

    @Test
    public void testToStringMethod() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObj = new Token(token, secret);

        String expectedOutput = "Token[abc123 , def456]";
        assertEquals(expectedOutput, tokenObj.toString());
    }
}