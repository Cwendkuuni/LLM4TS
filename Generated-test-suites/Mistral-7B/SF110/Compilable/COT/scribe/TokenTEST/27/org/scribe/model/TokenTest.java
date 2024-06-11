package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Token;

public class TokenTest {

    // Basic test cases for the constructors

    @Test
    public void testTokenConstructor() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenObj = new Token(token, secret);
        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
        assertNull(tokenObj.getRawResponse());
    }

    @Test
    public void testTokenConstructorWithRawResponse() {
        String token = "testToken";
        String secret = "testSecret";
        String rawResponse = "testRawResponse";
        Token tokenObj = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
        assertEquals(rawResponse, tokenObj.getRawResponse());
    }

    // Edge cases and exception handling test cases

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenNotConstructedWithRawResponse() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenObj = new Token(token, secret);
        tokenObj.getRawResponse();
    }

    @Test
    public void testToString() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenObj = new Token(token, secret);
        String expected = "Token[testToken , testSecret]";
        assertEquals(expected, tokenObj.toString());
    }

    @Test
    public void testGetTokenNull() {
        String secret = "testSecret";
        Token tokenObj = new Token(null, secret);
        assertNull(tokenObj.getToken());
    }

    @Test
    public void testGetSecretNull() {
        String token = "testToken";
        Token tokenObj = new Token(token, null);
        assertNull(tokenObj.getSecret());
    }
}