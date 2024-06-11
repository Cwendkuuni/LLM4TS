package org.scribe.model.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Token;

public class TokenTest {

    @Test
    public void testTokenConstructionWithTokenAndSecret() {
        String token = "example_token";
        String secret = "example_secret";
        Token tokenObject = new Token(token, secret);
        Assert.assertEquals(token, tokenObject.getToken());
        Assert.assertEquals(secret, tokenObject.getSecret());
        Assert.assertNull(tokenObject.getRawResponse());
    }

    @Test
    public void testTokenConstructionWithTokenSecretAndRawResponse() {
        String token = "example_token";
        String secret = "example_secret";
        String rawResponse = "example_raw_response";
        Token tokenObject = new Token(token, secret, rawResponse);
        Assert.assertEquals(token, tokenObject.getToken());
        Assert.assertEquals(secret, tokenObject.getSecret());
        Assert.assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenTokenIsNotConstructedWithRawResponse() {
        String token = "example_token";
        String secret = "example_secret";
        Token tokenObject = new Token(token, secret);
        tokenObject.getRawResponse(); // This should throw an exception
    }

    @Test
    public void testToStringMethod() {
        String token = "example_token";
        String secret = "example_secret";
        Token tokenObject = new Token(token, secret);
        String expected = String.format("Token[%s , %s]", token, secret);
        String actual = tokenObject.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetTokenWithNullToken() {
        String token = null;
        String secret = "example_secret";
        Token tokenObject = new Token(token, secret);
        Assert.assertNull(tokenObject.getToken());
    }

    @Test
    public void testGetSecretWithNullSecret() {
        String token = "example_token";
        String secret = null;
        Token tokenObject = new Token(token, secret);
        Assert.assertNull(tokenObject.getSecret());
    }

    @Test
    public void testGetTokenWithEmptyString() {
        String token = "";
        String secret = "example_secret";
        Token tokenObject = new Token(token, secret);
        Assert.assertEquals("", tokenObject.getToken());
    }

    @Test
    public void testGetSecretWithEmptyString() {
        String token = "example_token";
        String secret = "";
        Token tokenObject = new Token(token, secret);
        Assert.assertNull(tokenObject.getSecret());
    }
}