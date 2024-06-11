package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObj = new Token(token, secret);
        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
        assertNotNull(tokenObj.getToken());
        assertNotNull(tokenObj.getSecret());
    }

    @Test
    public void testTokenConstructorWithTokenSecretAndRawResponse() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "response123";
        Token tokenObj = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
        assertEquals(rawResponse, tokenObj.getRawResponse());
        assertNotNull(tokenObj.getToken());
        assertNotNull(tokenObj.getSecret());
        assertNotNull(tokenObj.getRawResponse());
    }

    @Test
    public void testGetTokenMethod() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObj = new Token(token, secret);
        assertEquals(token, tokenObj.getToken());
    }

    @Test
    public void testGetSecretMethod() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObj = new Token(token, secret);
        assertEquals(secret, tokenObj.getSecret());
    }

    @Test
    public void testGetRawResponseMethod() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "response123";
        Token tokenObj = new Token(token, secret, rawResponse);
        assertEquals(rawResponse, tokenObj.getRawResponse());

        Token tokenObjWithoutRawResponse = new Token(token, secret);
        assertThrows(IllegalStateException.class, () -> tokenObjWithoutRawResponse.getRawResponse());
    }

    @Test
    public void testToStringMethod() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObj = new Token(token, secret);
        String expectedResult = "Token[abc123 , def456]";
        assertEquals(expectedResult, tokenObj.toString());
    }

    @Test
    public void testIllegalStateException() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObj = new Token(token, secret);
        assertThrows(IllegalStateException.class, () -> tokenObj.getRawResponse());

        Token tokenObjWithoutRawResponse = new Token(token, secret);
        Token tokenObjWithRawResponse = new Token(token, secret, "response123");
        assertEquals("response123", tokenObjWithRawResponse.getRawResponse());
    }
}