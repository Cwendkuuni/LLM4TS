package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenConstructionWithTokenAndSecret() {
        String token = "myToken";
        String secret = "mySecret";
        Token tokenInstance = new Token(token, secret);

        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
        assertNull(tokenInstance.getRawResponse());
    }

    @Test
    public void testTokenConstructionWithTokenSecretAndRawResponse() {
        String token = "myToken";
        String secret = "mySecret";
        String rawResponse = "myRawResponse";
        Token tokenInstance = new Token(token, secret, rawResponse);

        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
        assertEquals(rawResponse, tokenInstance.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenNotConstructedWithRawResponse() {
        String token = "myToken";
        String secret = "mySecret";
        Token tokenInstance = new Token(token, secret);

        tokenInstance.getRawResponse();
    }

    @Test
    public void testToStringMethod() {
        String token = "myToken";
        String secret = "mySecret";
        Token tokenInstance = new Token(token, secret);

        assertEquals("Token[myToken , mySecret]", tokenInstance.toString());
    }
}