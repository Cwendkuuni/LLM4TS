package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Token;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "sampleToken";
        String secret = "sampleSecret";
        Token tokenObj = new Token(token, secret);
        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
    }

    @Test
    public void testTokenConstructorWithTokenAndSecretAndRawResponse() {
        String token = "sampleToken";
        String secret = "sampleSecret";
        String rawResponse = "sampleRawResponse";
        Token tokenObj = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
        assertEquals(rawResponse, tokenObj.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseIllegalStateException() {
        String token = "sampleToken";
        String secret = "sampleSecret";
        Token tokenObj = new Token(token, secret);
        tokenObj.getRawResponse();
    }

    @Test
    public void testToString() {
        String token = "sampleToken";
        String secret = "sampleSecret";
        Token tokenObj = new Token(token, secret);
        String expected = "Token[sampleToken , sampleSecret]";
        String actual = tokenObj.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTokenNull() {
        String token = null;
        String secret = "sampleSecret";
        Token tokenObj = new Token(token, secret);
        assertNull(tokenObj.getToken());
    }

    @Test
    public void testGetSecretNull() {
        String token = "sampleToken";
        String secret = null;
        Token tokenObj = new Token(token, secret);
        assertNull(tokenObj.getSecret());
    }

    @Test
    public void testGetRawResponseEmptyString() {
        String token = "sampleToken";
        String secret = "sampleSecret";
        String rawResponse = "";
        Token tokenObj = new Token(token, secret, rawResponse);
        assertEquals("", tokenObj.getRawResponse());
    }

    @Test
    public void testGetRawResponseWhiteSpaceString() {
        String token = "sampleToken";
        String secret = "sampleSecret";
        String rawResponse = " ";
        Token tokenObj = new Token(token, secret, rawResponse);
        assertEquals("", tokenObj.getRawResponse());
    }

    @Test
    public void testGetRawResponseNullCharacterString() {
        String token = "sampleToken";
        String secret = "sampleSecret";
        String rawResponse = "∅";
        Token tokenObj = new Token(token, secret, rawResponse);
        assertNull(tokenObj.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseNull() {
        String token = "sampleToken";
        String secret = "sampleSecret";
        Token tokenObj = new Token(token, secret);
        tokenObj.setRawResponse(null);
        tokenObj.getRawResponse();
    }
}