
package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    @Test
    public void testTokenConstructorTwoArgs() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObj = new Token(token, secret);
        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
        assertNull(tokenObj.getRawResponse());
    }
    
    @Test
    public void testTokenConstructorThreeArgs() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "raw data response";
        Token tokenObj = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
        assertEquals(rawResponse, tokenObj.getRawResponse());
    }
    
    @Test
    public void testTokenNullToken() {
        String secret = "def456";
        try {
            new Token(null, secret);
            fail("Expected IllegalStateException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
    
    @Test
    public void testTokenNullSecret() {
        String token = "abc123";
        try {
            new Token(token, null);
            fail("Expected IllegalStateException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
    
    @Test
    public void testTokenEmptyToken() {
        String secret = "def456";
        String token = "";
        Token tokenObj = new Token(token, secret);
        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
        assertNull(tokenObj.getRawResponse());
    }
    
    @Test
    public void testTokenEmptySecret() {
        String token = "abc123";
        String secret = "";
        Token tokenObj = new Token(token, secret);
        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
        assertNull(tokenObj.getRawResponse());
    }
    
    @Test
    public void testTokenGetRawResponse() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "raw data response";
        Token tokenObj = new Token(token, secret, rawResponse);
        assertEquals(token, tokenObj.getToken());
        assertEquals(secret, tokenObj.getSecret());
        assertEquals(rawResponse, tokenObj.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testTokenNoRawResponse() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObj = new Token(token, secret);
        tokenObj.getRawResponse(); // this line should throw an exception
    }
}