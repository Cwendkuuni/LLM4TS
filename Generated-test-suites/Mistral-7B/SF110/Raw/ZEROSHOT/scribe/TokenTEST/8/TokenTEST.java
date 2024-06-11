package org.scribe.model; // make sure your test class is in the same package as the Token class

import org.junit.Test;
import org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenConstructionWithTokenAndSecret() {
        String token = "1aB2c3D4";
        String secret = "eFgH5iJ6";
        Token tokenInstance = new Token(token, secret);

        String expectedToken = token;
        String expectedSecret = secret;

        String actualToken = tokenInstance.getToken();
        String actualSecret = tokenInstance.getSecret();

        assertEquals(expectedToken, actualToken);
        assertEquals(expectedSecret, actualSecret);
    }

    @Test
    public void testTokenConstructionWithTokenSecretAndRawResponse() {
        String token = "1aB2c3D4";
        String secret = "eFgH5iJ6";
        String rawResponse = "{\"access_token\":\"1aB2c3D4\",\"token_type\":\"Bearer\",\"expires_in\":3600}";

        Token tokenInstance = new Token(token, secret, rawResponse);

        String expectedToken = token;
        String expectedSecret = secret;
        String expectedRawResponse = rawResponse;

        String actualToken = tokenInstance.getToken();
        String actualSecret = tokenInstance.getSecret();
        String actualRawResponse = tokenInstance.getRawResponse();

        assertEquals(expectedToken, actualToken);
        assertEquals(expectedSecret, actualSecret);
        assertEquals(expectedRawResponse, actualRawResponse);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenTokenIsNotConstructedWithRawResponse() {
        String token = "1aB2c3D4";
        String secret = "eFgH5iJ6";
        Token tokenInstance = new Token(token, secret);

        tokenInstance.getRawResponse();
    }

    @Test
    public void testToStringMethod() {
        String token = "1aB2c3D4";
        String secret = "eFgH5iJ6";

        Token tokenInstance = new Token(token, secret);

        String expected = String.format("Token[%s , %s]", token, secret);
        String actual = tokenInstance.toString();

        assertEquals(expected, actual);
    }
}