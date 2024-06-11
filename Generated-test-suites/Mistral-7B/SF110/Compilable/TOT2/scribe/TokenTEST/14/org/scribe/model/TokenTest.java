package org.scribe.model;
import org.junit.Test;
import org.scribe.model.Token;

public class TokenTest {

    @Test
    public void testTokenConstructor() {
        String token = "abcdefg123456";
        String secret = "hijklmnopqrstuvwxyz123456";
        Token tokenInstance = new Token(token, secret);
        assert (tokenInstance.getToken().equals(token));
        assert (tokenInstance.getSecret().equals(secret));
    }

    @Test
    public void testTokenConstructorWithRawResponse() {
        String token = "abcdefg123456";
        String secret = "hijklmnopqrstuvwxyz123456";
        String rawResponse = "abcdefg123456hijklmnopqrstuvwxyz123456";
        Token tokenInstance = new Token(token, secret, rawResponse);
        assert (tokenInstance.getToken().equals(token));
        assert (tokenInstance.getSecret().equals(secret));
        assert (tokenInstance.getRawResponse().equals(rawResponse));
    }
}