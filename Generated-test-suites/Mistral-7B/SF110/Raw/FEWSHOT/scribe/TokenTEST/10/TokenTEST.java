package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        Token token = new Token("token1", "secret1");
        assertEquals("token1", token.getToken());
        assertEquals("secret1", token.getSecret());
        assertNull(token.getRawResponse());
    }

    @Test
    public void testTokenConstructorWithTokenSecretAndRawResponse() {
        String rawResponse = "raw response";
        Token token = new Token("token1", "secret1", rawResponse);
        assertEquals("token1", token.getToken());
        assertEquals("secret1", token.getSecret());
        assertEquals(rawResponse, token.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testTokenWithoutRawResponse() {
        new Token("token1", "secret1");
    }

    @Test
    public void testGetTokenMethod() {
        Token token = new Token("token1", "secret1");
        assertEquals("token1", token.getToken());
    }

    @Test
    public void testGetSecretMethod() {
        Token token = new Token("token1", "secret1");
        assertEquals("secret1", token.getSecret());
    }

    @Test
    public void testGetRawResponseMethod() {
        String rawResponse = "raw response";
        Token token = new Token("token1", "secret1", rawResponse);
        assertEquals(rawResponse, token.getRawResponse());
    }

    @Test
    public void testToStringMethod() {
        Token token = new Token("token1", "secret1");
        assertEquals("Token[token1 , secret1]", token.toString());
    }

    @Test
    public void testTokenEquality() {
        Token token1 = new Token("token1", "secret1");
        Token token2 = new Token("token1", "secret1");
        assertEquals(token1, token2);
    }

    @Test
    public void testTokenInequality() {
        Token token1 = new Token("token1", "secret1");
        Token token2 = new Token("token2", "secret2");
        assertNotEquals(token1, token2);
    }

    @Test
    public void testTokenNull() {
        assertNull(new Token(null, "secret1"));
        assertNull(new Token("token1", null));
    }
}