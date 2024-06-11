package org.example;

import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.model.Token;

public class TokenTest {
    @Test
    public void testGetToken() {
        Token token = new Token("testToken", "testSecret");
        String expected = "testToken";
        String actual = token.getToken();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetTokenException() {
        Token token = new Token(null, "testSecret");
    }

    @Test
    public void testGetTokenEmptyToken() {
        Token token = new Token("", "testSecret");
        String actual = token.getToken();
        assertNotEmpty(actual);
    }

    @Test
    public void testGetSecret() {
        Token token = new Token("testToken", "testSecret");
        String expected = "testSecret";
        String actual = token.getSecret();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponse() {
        Token token = new Token("testToken", "testSecret");
        token.getRawResponse(); // Throws IllegalStateException since rawResponse is null
    }

    @Test
    public void testToString() {
        Token token = new Token("testToken", "testSecret");
        String expected = "Token[testToken , testSecret]";
        String actual = token.toString();
        assertEquals(expected, actual);
    }
}