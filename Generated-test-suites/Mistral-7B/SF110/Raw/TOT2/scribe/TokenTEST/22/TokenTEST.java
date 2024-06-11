package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    @Test
    public void testTokenConstructionWithTokenAndSecret() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        assertNotNull(tokenObject);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
    }
    
    @Test
    public void testTokenConstructionWithTokenAndSecretAndRawResponse() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "raw_response_string";
        Token tokenObject = new Token(token, secret, rawResponse);
        assertNotNull(tokenObject);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertEquals(rawResponse, tokenObject.getRawResponse());
    }
    
    @Test
    public void testIllegalStateExceptionWhenGetRawResponseIsCalledOnObjectWithoutRawResponse() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        try {
            tokenObject.getRawResponse();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }
}