package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDefaultConstructor() {
        Token token = new Token();
        assertNull(token.getToken());
        assertNull(token.getSecret());
        assertNull(token.getRawResponse());
    }

    @Test
    public void testConstructorWithTokenAndSecret() {
        String token = "abcd1234";
        String secret = "efgh4567";
        Token newToken = new Token(token, secret);
        assertEquals(token, newToken.getToken());
        assertEquals(secret, newToken.getSecret());
        assertNull(newToken.getRawResponse());
    }

    @Test
    public void testConstructorWithTokenSecretAndRawResponse() {
        String token = "abcd1234";
        String secret = "efgh4567";
        String rawResponse = "responseData";
        Token newToken = new Token(token, secret, rawResponse);
        assertEquals(token, newToken.getToken());
        assertEquals(secret, newToken.getSecret());
        assertEquals(rawResponse, newToken.getRawResponse());
    }

    @Test
    public void testGetToken() {
        String token = "abcd1234";
        Token newToken = new Token(token, "secret");
        assertEquals(token, newToken.getToken());
    }

    @Test
    public void testGetSecret() {
        String secret = "efgh4567";
        Token newToken = new Token("token", secret);
        assertEquals(secret, newToken.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        String token = "token";
        String secret = "secret";
        String rawResponse = "rawData";
        Token newToken = new Token(token, secret, rawResponse);
        assertEquals(rawResponse, newToken.getRawResponse());
    }

    @Test
    public void testToString() {
        String token = "token";
        String secret = "secret";
        Token newToken = new Token(token, secret);
        assertEquals("Token[token , secret]", newToken.toString());
    }

    @Test
    public void testGetRawResponseWithNullRawResponse() {
        String token = "token";
        String secret = "secret";
        Token newToken = new Token(token, secret, null);
        assertNull(newToken.getRawResponse());
    }

    @Test
    public void testGetRawResponseWithEmptyRawResponse() {
        String token = "token";
        String secret = "secret";
        Token newToken = new Token(token, secret, "");
        assertNull(newToken.getRawResponse());
    }

    @Test
    public void testGetRawResponseWithUnconstructedToken() {
        String token = "token";
        String secret = "secret";
        Token newToken = new Token(token, secret);
        try {
            newToken.getRawResponse();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test
    public void testConstructorWithNullToken() {
        String secret = "secret";
        String rawResponse = "responseData";
        try {
            new Token(null, secret, rawResponse);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testConstructorWithNullSecret() {
        String token = "token";
        String rawResponse = "responseData";
        try {
            new Token(token, null, rawResponse);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testConstructorWithEmptyToken() {
        String secret = "secret";
        String rawResponse = "responseData";
        try {
            new Token("", secret, rawResponse);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testConstructorWithEmptySecret() {
        String token = "token";
        String rawResponse = "responseData";
        try {
            new Token(token, "", rawResponse);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}