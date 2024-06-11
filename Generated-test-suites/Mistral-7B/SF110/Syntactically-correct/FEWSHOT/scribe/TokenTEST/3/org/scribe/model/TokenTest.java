package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenConstructionWithTokenAndSecret() {
        Token token = new Token("tokenValue", "secretValue");
        assertEquals("tokenValue", token.getToken());
        assertEquals("secretValue", token.getSecret());
        assertNull(token.getRawResponse());
    }

    @Test
    public void testTokenConstructionWithTokenSecretAndRawResponse() {
        String rawResponse = "rawResponseValue";
        Token token = new Token("tokenValue", "secretValue", rawResponse);
        assertEquals("tokenValue", token.getToken());
        assertEquals("secretValue", token.getSecret());
        assertEquals(rawResponse, token.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionWhenGettingRawResponseWithoutIt() {
        Token token = new Token("tokenValue", "secretValue");
        token.getRawResponse(); // This will throw an IllegalStateException
    }

    @Test
    public void testToStringMethod() {
        Token token = new Token("tokenValue", "secretValue");
        String expected = String.format("Token[%s , %s]", "tokenValue", "secretValue");
        assertEquals(expected, token.toString());
    }
}