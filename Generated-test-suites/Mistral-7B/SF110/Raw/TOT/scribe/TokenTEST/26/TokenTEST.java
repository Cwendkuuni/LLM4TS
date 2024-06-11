package org.scribe.model.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.model.Token;

public class TokenTest {
    @Test
    public void testGetToken() {
        String token = "abc123";
        Token tokenObject = new Token(token, "secret");
        assertEquals(token, tokenObject.getToken());
    }

    @Test
    public void testGetSecret() {
        String secret = "secretKey";
        Token tokenObject = new Token("token", secret);
        assertEquals(secret, tokenObject.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        String rawResponse = "this is a raw response";
        Token tokenObject = new Token("token", "secret", rawResponse);
        assertEquals(rawResponse, tokenObject.getRawResponse());

        // Test the exception
        Token tokenObjectWithoutRawResponse = new Token("token", "secret");
        try {
            tokenObjectWithoutRawResponse.getRawResponse();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Pass
        }
    }
}