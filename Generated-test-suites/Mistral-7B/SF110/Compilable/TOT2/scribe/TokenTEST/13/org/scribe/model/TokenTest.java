package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenTest {
    @Test
    public void testGetToken() {
        String token = "abc123";
        Token tokenObj = new Token(token, null);
        assertEquals(token, tokenObj.getToken());
    }
}