package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenConstructionWithTokenAndSecret() {
        Token token = new Token("access_token", "secret_key");
        assertEquals("access_token", token.getToken());
        assertEquals("secret_key", token.getSecret());
        assertNull(token.getRawResponse());
    }

    @Test
    public void testTokenConstructionWithTokenSecretAndRawResponse() {
        String rawResponse = "{\"access_token\":\"access_token\",\"token_type\":\"Bearer\",\"expires_in\":3600}";
        Token token = new Token("access_token", "secret_key", rawResponse);
        assertEquals("access_token", token.getToken());
        assertEquals("secret_key", token.getSecret());
        assertEquals(rawResponse, token.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionWhenGettingRawResponseWithoutIt() {
        Token token = new Token("access_token", "secret_key");
        token.getRawResponse();
    }

    @Test
    public void testToStringMethod() {
        Token token = new Token("access_token", "secret_key");
        String expected = "Token[access_token , secret_key]";
        assertEquals(expected, token.toString());
    }
}