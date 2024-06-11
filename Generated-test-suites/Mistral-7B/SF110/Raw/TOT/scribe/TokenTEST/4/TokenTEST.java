package org.example;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    @Test
    public void testTokenConstructorWithTwoArgs() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObj = new Token(token, secret);
        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
        assertNull(tokenObj.getRawResponse());
    }

    @Test
    public void testTokenConstructorWithThreeArgs() {
        String token = "xyz789";
        String secret = "pqr012";
        String rawResponse = "{\"access_token\": \"abc123\", \"expires_in\": 3600}";
        Token tokenObj = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
        assertEquals(rawResponse, tokenObj.getRawResponse());
    }

    @Test
    public void testGetToken() {
        String token = "tokenValue";
        Token tokenObj = new Token(token, "secretValue");
        assertEquals(token, tokenObj.getToken());
    }

    @Test
    public void testGetSecret() {
        String secret = "secretValue";
        Token tokenObj = new Token("tokenValue", secret);
        assertEquals(secret, tokenObj.getSecret());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponse() {
        Token tokenObj = new Token("tokenValue", "secretValue");
        tokenObj.getRawResponse();
    }

    @Test
    public void testGetRawResponseWithRawResponse() {
        String rawResponse = "{\"access_token\": \"abc123\", \"expires_in\": 3600}";
        Token tokenObj = new Token("tokenValue", "secretValue", rawResponse);
        assertEquals(rawResponse, tokenObj.getRawResponse());
    }
}