package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenConstructionWithTokenAndSecret() {
        String token = "token123";
        String secret = "secret123";
        Token tokenInstance = new Token(token, secret);
        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
        assertNull(tokenInstance.getRawResponse());
    }

    @Test
    public void testTokenConstructionWithTokenSecretAndRawResponse() {
        String token = "token123";
        String secret = "secret123";
        String rawResponse = "raw response";
        Token tokenInstance = new Token(token, secret, rawResponse);
        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
        assertEquals(rawResponse, tokenInstance.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionWhenGettingRawResponseWithoutIt() {
        String token = "token123";
        String secret = "secret123";
        Token tokenInstance = new Token(token, secret);
        tokenInstance.getRawResponse();
    }

    @Test
    public void testToStringMethod() {
        String token = "token123";
        String secret = "secret123";
        Token tokenInstance = new Token(token, secret);
        String expected = String.format("Token[%s , %s]", token, secret);
        assertEquals(expected, tokenInstance.toString());
    }
}