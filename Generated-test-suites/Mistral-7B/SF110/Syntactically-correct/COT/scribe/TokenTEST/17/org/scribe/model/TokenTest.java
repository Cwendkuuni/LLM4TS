package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Token;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "1234567890";
        String secret = "abcdefghijklmnopqrstuvwxyz";
        Token tokenObj = new Token(token, secret);
        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
    }

    @Test
    public void testTokenConstructorWithTokenSecretAndRawResponse() {
        String token = "1234567890";
        String secret = "abcdefghijklmnopqrstuvwxyz";
        String rawResponse = "Response from OAuth service";
        Token tokenObj = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
        assertEquals(rawResponse, tokenObj.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenTokenObjectIsNotConstructedWithRawResponse() {
        String token = "1234567890";
        String secret = "abcdefghijklmnopqrstuvwxyz";
        Token tokenObj = new Token(token, secret);
        tokenObj.getRawResponse();
    }

    @Test
    public void testToStringMethod() {
        String token = "1234567890";
        String secret = "abcdefghijklmnopqrstuvwxyz";
        Token tokenObj = new Token(token, secret);
        String expected = "Token[1234567890 , abcdefghijklmnopqrstuvwxyz]";
        String actual = tokenObj.toString();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSecretWhenTokenObjectIsNotConstructedWithSecret() {
        String token = "1234567890";
        String rawResponse = "Response from OAuth service";
        Token tokenObj = new Token(token, null, rawResponse);
        tokenObj.getSecret();
    }

    @Test
    public void testGetEmptyToken() {
        String token = "";
        String secret = "abcdefghijklmnopqrstuvwxyz";
        Token tokenObj = new Token(token, secret);
        assertEquals("", tokenObj.getToken());
    }

    @Test
    public void testGetEmptySecret() {
        String token = "1234567890";
        String secret = "";
        Token tokenObj = new Token(token, secret);
        assertEquals(secret, tokenObj.getSecret());
    }

    @Test
    public void testGetNullToken() {
        String secret = "abcdefghijklmnopqrstuvwxyz";
        Token tokenObj = new Token(null, secret);
        assertNull(tokenObj.getToken());
    }

    @Test
    public void testGetNullSecret() {
        String token = "1234567890";
        Token tokenObj = new Token(token, null);
        assertNull(tokenObj.getSecret());
    }
}