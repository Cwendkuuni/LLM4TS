package org.junit.tests;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenDefaultConstructor() {
        // Test default constructor
        Token token = new Token();
        assertNull(token.getToken());
        assertNull(token.getSecret());
        String tokenString = token.toString();
        assertEquals("Token[null , null]", tokenString);
    }

    @Test
    public void testTokenTwoParameterConstructor() {
        // Test constructor with two parameters
        Token token = new Token("testToken", "testSecret");
        String tokenValue = token.getToken();
        assertEquals("testToken", tokenValue);
        String secretValue = token.getSecret();
        assertEquals("testSecret", secretValue);
        try {
            token.getRawResponse();
            fail("getRawResponse() should have thrown an exception");
        } catch (IllegalStateException e) {
            // Expected exception
        }
        String tokenString = token.toString();
        assertEquals("Token[testToken , testSecret]", tokenString);
    }

    @Test
    public void testTokenTwoParameterConstructorWithNullAndEmptyStrings() {
        // Test constructor with null token
        try {
            new Token(null, "testSecret");
            fail("Constructor should have thrown an exception");
        } catch (NullPointerException e) {
            // Expected exception
        }

        // Test constructor with empty token
        Token emptyToken = new Token("", "testSecret");
        String tokenValue = emptyToken.getToken();
        assertEquals("", tokenValue);
        String secretValue = emptyToken.getSecret();
        assertEquals("testSecret", secretValue);
        String rawResponse = emptyToken.getRawResponse();
        assertEquals("", rawResponse);
        String tokenString = emptyToken.toString();
        assertEquals("Token[ , testSecret]", tokenString);
    }

    @Test
    public void testTokenThreeParameterConstructor() {
        // Test constructor with three parameters
        Token token = new Token("testToken", "testSecret", "testRawResponse");
        String tokenValue = token.getToken();
        assertEquals("testToken", tokenValue);
        String secretValue = token.getSecret();
        assertEquals("testSecret", secretValue);
        String rawResponseValue = token.getRawResponse();
        assertEquals("testRawResponse", rawResponseValue);
        String tokenString = token.toString();
        assertEquals("Token[testToken , testSecret]", tokenString);
    }

    @Test
    public void testGetStringTokenWithNullToken() {
        // Test getToken() method with null token
        Token token = new Token(null, "testSecret");
        try {
            token.getToken();
            fail("getToken() should have thrown an exception");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetStringTokenWithEmptyToken() {
        // Test getToken() method with empty token
        Token token = new Token("", "testSecret");
        String tokenValue = token.getToken();
        assertEquals("", tokenValue);
    }

    @Test
    public void testGetStringToken() {
        // Test getToken() method with valid token
        Token token = new Token("testToken", "testSecret");
        String tokenValue = token.getToken();
        assertEquals("testToken", tokenValue);
    }

    @Test
    public void testGetStringSecretWithNullSecret() {
        // Test getSecret() method with null secret
        Token token = new Token("testToken", null);
        try {
            token.getSecret();
            fail("getSecret() should have thrown an exception");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetStringSecretWithEmptySecret() {
        // Test getSecret() method with empty secret
        Token token = new Token("testToken", "");
        String secretValue = token.getSecret();
        assertEquals("", secretValue);
    }

    @Test
    public void testGetStringSecret() {
        // Test getSecret() method with valid secret
        Token token = new Token("testToken", "testSecret");
        String secretValue = token.getSecret();
        assertEquals("testSecret", secretValue);
    }

    @Test
    public void testGetRawResponseWithNullToken() {
        // Test getRawResponse() method with null token
        Token token = new Token(null, "testSecret");
        try {
            token.getRawResponse();
            fail("getRawResponse() should have thrown an exception");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetRawResponseWithEmptyToken() {
        // Test getRawResponse() method with empty token
        Token token = new Token("", "testSecret");
        String rawResponseValue = token.getRawResponse();
        assertEquals("", rawResponseValue);
    }

    @Test
    public void testGetRawResponse() {
        // Test getRawResponse() method with valid rawResponse
        Token token = new Token("testToken", "testSecret", "testRawResponse");
        String rawResponseValue = token.getRawResponse();
        assertEquals("testRawResponse", rawResponseValue);
    }

    @Test
    public void testToStringWithNullOrEmptyValues() {
        // Test toString() method with null token
        Token token = new Token(null, "testSecret");
        String tokenString = token.toString();
        assertEquals("Token[null , testSecret]", tokenString);

        // Test toString() method with empty token
        Token emptyToken = new Token("", "testSecret");
        tokenString = emptyToken.toString();
        assertEquals("Token[ , testSecret]", tokenString);

        // Test toString() method with null secret
        Token tokenWithNullSecret = new Token("testToken", null);
        tokenString = tokenWithNullSecret.toString();
        assertEquals("Token[testToken , null]", tokenString);

        // Test toString() method with empty secret
        Token emptySecretToken = new Token("testToken", "");
        tokenString = emptySecretToken.toString();
        assertEquals("Token[testToken , ]", tokenString);
    }
}