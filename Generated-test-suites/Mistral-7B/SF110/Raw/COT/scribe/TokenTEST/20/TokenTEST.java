package org.scribe.model.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Token;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "abc123";
        String secret = "def456";
        Token tokenInstance = new Token(token, secret);
        String actualToken = tokenInstance.getToken();
        String actualSecret = tokenInstance.getSecret();
        String expectedToken = token;
        String expectedSecret = secret;
        assertEquals(expectedToken, actualToken);
        assertEquals(expectedSecret, actualSecret);
    }

    @Test
    public void testTokenConstructorWithTokenSecretAndRawResponse() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "responseData";
        Token tokenInstance = new Token(token, secret, rawResponse);
        String actualToken = tokenInstance.getToken();
        String actualSecret = tokenInstance.getSecret();
        String actualRawResponse = tokenInstance.getRawResponse();
        String expectedToken = token;
        String expectedSecret = secret;
        String expectedRawResponse = rawResponse;
        assertEquals(expectedToken, actualToken);
        assertEquals(expectedSecret, actualSecret);
        assertEquals(expectedRawResponse, actualRawResponse);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenTokenIsNotConstructedByScribe() {
        String token = "abc123";
        String secret = "def456";
        Token tokenInstance = new Token(token, secret);
        tokenInstance.getRawResponse();
    }

    @Test
    public void testToStringMethod() {
        String token = "abc123";
        String secret = "def456";
        Token tokenInstance = new Token(token, secret);
        String expectedResult = "Token[abc123 , def456]";
        String actualResult = tokenInstance.toString();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetTokenMethodWithNullToken() {
        Token tokenInstance = new Token(null, "secret");
        assertNull(tokenInstance.getToken());
    }

    @Test
    public void testGetSecretMethodWithNullSecret() {
        Token tokenInstance = new Token("token", null);
        assertNull(tokenInstance.getSecret());
    }

    @Test
    public void testGetTokenMethodWithEmptyStringToken() {
        Token tokenInstance = new Token("", "secret");
        assertEquals("", tokenInstance.getToken());
    }

    @Test
    public void testGetSecretMethodWithEmptyStringSecret() {
        Token tokenInstance = new Token("token", "");
        assertEquals("", tokenInstance.getSecret());
    }
}