package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "token123";
        String secret = "secret123";
        Token tokenInstance = new Token(token, secret);

        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
        assertNull(tokenInstance.getRawResponse());
    }

    @Test
    public void testTokenConstructorWithTokenSecretAndRawResponse() {
        String token = "token123";
        String secret = "secret123";
        String rawResponse = "rawResponse123";
        Token tokenInstance = new Token(token, secret, rawResponse);

        assertEquals(token, tokenInstance.getToken());
        assertEquals(secret, tokenInstance.getSecret());
        assertEquals(rawResponse, tokenInstance.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenNotConstructedWithRawResponse() {
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

        assertEquals("Token[token123 , secret123]", tokenInstance.toString());
    }
}