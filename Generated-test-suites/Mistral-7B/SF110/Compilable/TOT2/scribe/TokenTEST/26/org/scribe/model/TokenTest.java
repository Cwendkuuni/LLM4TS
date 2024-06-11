package org.scribe.model;
import org.junit.Test;
import org.scribe.model.Token;
import static org.junit.Assert.*;

public class TokenTest {

    @Test public void testTokenConstructionWithTokenAndSecret() {
        String token = "abc123";
        String secret = "xyz456";
        Token tokenObject = new Token(token, secret);
        assertNotNull(tokenObject);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
    }

    @Test public void testTokenConstructionWithTokenSecretAndRawResponse() {
        String token = "abcd1234";
        String secret = "efgh5678";
        String rawResponse = "{\"access_token\":\"abcd1234\",\"token_type\":\"Bearer\"}";
        Token tokenObject = new Token(token, secret, rawResponse);
        assertNotNull(tokenObject);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test public void testTokenGetToken() {
        Token tokenObject = new Token("token", "secret");
        String token = tokenObject.getToken();
        assertEquals("token", token);
    }

    @Test public void testTokenGetSecret() {
        Token tokenObject = new Token("token", "secret");
        String secret = tokenObject.getSecret();
        assertEquals("secret", secret);
    }

    @Test public void testTokenToString() {
        Token tokenObject = new Token("token", "secret");
        String expectedTokenToString = "Token[token , secret]";
        String tokenToString = tokenObject.toString();
        assertEquals(expectedTokenToString, tokenToString);
    }

    @Test public void testTokenGetRawResponseNull() {
        Token tokenObject = new Token("token", "secret");
        try {
            tokenObject.getRawResponse();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test public void testTokenConstructorWithEmptyStringToken() {
        String token = "";
        String secret = "secret";
        try {
            new Token(token, secret);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test public void testTokenConstructorWithNullSecret() {
        String token = "token";
        String secret = null;
        try {
            new Token(token, secret);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test public void testTokenConstructorWithEmptyStringRawResponse() {
        String token = "token";
        String secret = "secret";
        String rawResponse = "";
        Token tokenObject = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertNull(tokenObject.getRawResponse());
    }

    @Test public void testTokenConstructorWithNullRawResponse() {
        String token = "token";
        String secret = "secret";
        String rawResponse = null;
        Token tokenObject = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertNull(tokenObject.getRawResponse());
    }
}