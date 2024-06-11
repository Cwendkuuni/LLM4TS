package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.*; // For assertions

public class TokenTest {

    private final String testToken = "testToken";
    private final String testSecret = "testSecret";
    private final String testRawResponse = "testRawResponse";

    @Test
    public void testTokenAndSecretConstruction() {
        String token = "testToken";
        String secret = "testSecret";

        Token tokenObject = new Token(token, secret);

        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertNull(tokenObject.getRawResponse());
    }

    @Test
    public void testTokenAndSecretAndRawResponseConstruction() {
        String token = "testToken";
        String secret = "testSecret";
        String rawResponse = "testRawResponse";

        Token tokenObject = new Token(token, secret, rawResponse);

        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test
    public void testGetToken() {
        Token tokenObject = new Token(testToken, testSecret);
        assertEquals(testToken, tokenObject.getToken());
    }

    @Test
    public void testGetSecret() {
        Token tokenObject = new Token(testToken, testSecret);
        assertEquals(testSecret, tokenObject.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        String testRawResponse = "testRawResponse";
        Token tokenObject = new Token(testToken, testSecret, testRawResponse);
        assertEquals(testRawResponse, tokenObject.getRawResponse());
    }

    @Test
    public void testIllegalStateExceptionForNoRawResponse() {
        Token tokenObject = new Token(testToken, testSecret);
        try {
            tokenObject.getRawResponse();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            //expected
        }
    }
}