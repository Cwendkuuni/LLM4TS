package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Token;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenObj = new Token(token, secret);
        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
        assertNull(tokenObj.getRawResponse());
    }

    @Test
    public void testTokenConstructorWithTokenSecretAndRawResponse() {
        String token = "testToken";
        String secret = "testSecret";
        String rawResponse = "testRawResponse";
        Token tokenObj = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
        assertEquals(rawResponse, tokenObj.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenNotConstructedByScribe() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenObj = new Token(token, secret);
        tokenObj.getRawResponse();
    }

    @Test
    public void testToStringMethod() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenObj = new Token(token, secret);
        String expectedOutput = "Token[testToken , testSecret]";
        assertEquals(expectedOutput, tokenObj.toString());
    }

    @Test
    public void testGetTokenWithNullToken() {
        String token = null;
        String secret = "testSecret";
        Token tokenObj = new Token(token, secret);
        assertNull(tokenObj.getToken());
    }

    @Test
    public void testGetSecretWithNullSecret() {
        String token = "testToken";
        String secret = null;
        Token tokenObj = new Token(token, secret);
        assertNull(tokenObj.getSecret());
    }

    @Test
    public void testGetTokenWithEmptyString() {
        String token = "";
        String secret = "testSecret";
        Token tokenObj = new Token(token, secret);
        assertEquals("", tokenObj.getToken());
    }

    @Test
    public void testGetSecretWithEmptyString() {
        String token = "testToken";
        String secret = "";
        Token tokenObj = new Token(token, secret);
        assertNull(tokenObj.getSecret());
    }

    @Test
    public void testGetTokenWithWhitespaceString() {
        String token = " ";
        String secret = "testSecret";
        Token tokenObj = new Token(token, secret);
        assertEquals("", tokenObj.getToken());
    }

    @Test
    public void testGetSecretWithWhitespaceString() {
        String token = "testToken";
        String secret = " ";
        Token tokenObj = new Token(token, secret);
        assertNull(tokenObj.getSecret());
    }
}