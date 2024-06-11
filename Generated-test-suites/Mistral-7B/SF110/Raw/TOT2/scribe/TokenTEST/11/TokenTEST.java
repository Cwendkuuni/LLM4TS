package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenObject = new Token(token, secret);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertNull(tokenObject.getRawResponse());
    }

    @Test
    public void testTokenConstructorWithTokenSecretAndRawResponse() {
        String token = "testToken";
        String secret = "testSecret";
        String rawResponse = "testRawResponse";
        Token tokenObject = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testTokenObjectWithoutRawResponse() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenObject = new Token(token, secret);
        tokenObject.getRawResponse();
    }

    @Test
    public void testNullToken() {
        String secret = "testSecret";
        assertNull(new Token(null, secret));
    }

    @Test
    public void testEmptyToken() {
        String secret = "testSecret";
        Token tokenObject = new Token("", secret);
        assertEquals("", tokenObject.getToken());
    }

    @Test
    public void testEmptySecret() {
        String token = "testToken";
        Token tokenObject = new Token(token, "");
        assertEquals(token, tokenObject.getToken());
        assertNull(tokenObject.getSecret());
    }

    @Test
    public void testLongToken() {
        int length = 100;
        String token = String.format("TestToken%1$" + length + "s", "Token");
        String secret = "testSecret";
        Token tokenObject = new Token(token, secret);
        assertEquals(token, tokenObject.getToken());
    }

    @Test
    public void testLongSecret() {
        int length = 100;
        String token = "testToken";
        String secret = String.format("TestSecret%1$" + length + "s", "Secret");
        Token tokenObject = new Token(token, secret);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
    }
}