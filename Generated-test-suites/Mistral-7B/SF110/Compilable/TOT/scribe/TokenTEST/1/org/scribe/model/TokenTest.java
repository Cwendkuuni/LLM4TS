package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    @Test
    public void testTokenConstructor() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        assertEquals(token, tokenObject.getToken());
        assertEquals(secret, tokenObject.getSecret());
        assertNull(tokenObject.getRawResponse());
    }
}