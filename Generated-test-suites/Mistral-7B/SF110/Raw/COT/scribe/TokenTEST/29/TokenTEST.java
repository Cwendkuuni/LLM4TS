package org.scribe.model.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Token;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "abcdef123456";
        String secret = "1234567890abcdef";
        Token tokenObject = new Token(token, secret);
        Assert.assertEquals(token, tokenObject.getToken());
        Assert.assertEquals(secret, tokenObject.getSecret());
    }

    @Test
    public void testTokenConstructorWithTokenSecretAndRawResponse() {
        String token = "abcdef123456";
        String secret = "1234567890abcdef";
        String rawResponse = "{\"access_token\":\"abcdef123456\",\"token_type\":\"Bearer\",\"expires_in\":3600}";
        Token tokenObject = new Token(token, secret, rawResponse);
        Assert.assertEquals(token, tokenObject.getToken());
        Assert.assertEquals(secret, tokenObject.getSecret());
        Assert.assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenObjectNotConstructedWithRawResponse() {
        String token = "abcdef123456";
        String secret = "1234567890abcdef";
        Token tokenObject = new Token(token, secret);
        tokenObject.getRawResponse();
    }

    @Test
    public void testToStringMethod() {
        String token = "abcdef123456";
        String secret = "1234567890abcdef";
        Token tokenObject = new Token(token, secret);
        String expectedOutput = "Token[abcdef123456 , 1234567890abcdef]";
        Assert.assertEquals(expectedOutput, tokenObject.toString());
    }

    @Test
    public void testGetTokenMethod() {
        String token = "abcdef123456";
        String secret = "1234567890abcdef";
        Token tokenObject = new Token(token, secret);
        Assert.assertEquals(token, tokenObject.getToken());
    }

    @Test
    public void testGetSecretMethod() {
        String token = "abcdef123456";
        String secret = "1234567890abcdef";
        Token tokenObject = new Token(token, secret);
        Assert.assertEquals(secret, tokenObject.getSecret());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenNull() {
        String token = "abcdef123456";
        String secret = "1234567890abcdef";
        Token tokenObject = new Token(token, secret);
        tokenObject.rawResponse = null;
        tokenObject.getRawResponse();
    }

    @Test
    public void testGetRawResponseEmptyString() {
        String token = "abcdef123456";
        String secret = "1234567890abcdef";
        String rawResponse = "";
        Token tokenObject = new Token(token, secret, rawResponse);
        Assert.assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test
    public void testGetRawResponseWithSpecialCharacters() {
        String token = "abcdef123456";
        String secret = "1234567890abcdef";
        String rawResponse = "{\"access_token\":\"abcdef123456\",\"token_type\":\"Bearer\",\"expires_in\":3600,\"scope\":\"read write\"}";
        Token tokenObject = new Token(token, secret, rawResponse);
        Assert.assertEquals(rawResponse, tokenObject.getRawResponse());
    }
}