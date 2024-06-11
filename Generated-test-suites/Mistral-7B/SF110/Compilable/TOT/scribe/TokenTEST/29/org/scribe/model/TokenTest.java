package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.*; // for assertions

public class TokenTest {
    @Test
    public void testTokenConstructionWithTokenAndSecret() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenObject = new Token(token, secret);

        // Test token and secret are set correctly
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());

        // Test toString method
        assertEquals("Token[testToken , testSecret]", tokenObject.toString());
    }
}