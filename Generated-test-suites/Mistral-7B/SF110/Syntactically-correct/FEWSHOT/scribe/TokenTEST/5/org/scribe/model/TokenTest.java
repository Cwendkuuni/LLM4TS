package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenConstructionWithTokenAndSecret() {
        String token = "access_token";
        String secret = "oauth_token_secret";
        Token tokenInstance = new Token(token, secret);

        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
        assertNull(tokenInstance.getRawResponse());
    }

    @Test
    public void testTokenConstructionWithTokenSecretAndRawResponse() {
        String token = "access_token";
        String secret = "oauth_token_secret";
        String rawResponse = "raw_response_data";
        Token tokenInstance = new Token(token, secret, rawResponse);

        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
        assertEquals(rawResponse, tokenInstance.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionWhenGettingRawResponseWithoutIt() {
        String token = "access_token";
        String secret = "oauth_token_secret";
        Token tokenInstance = new Token(token, secret);

        tokenInstance.getRawResponse();
    }

    @Test
    public void testToStringMethod() {
        String token = "access_token";
        String secret = "oauth_token_secret";
        Token tokenInstance = new Token(token, secret);

        String expectedResult = "Token[access_token , oauth_token_secret]";
        assertEquals(expectedResult, tokenInstance.toString());
    }
}