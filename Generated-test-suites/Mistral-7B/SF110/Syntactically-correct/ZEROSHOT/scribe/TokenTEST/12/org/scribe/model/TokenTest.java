import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.Token;

public class TokenTest {

    @Test
    public void testTokenConstructionWithTokenAndSecret() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        Assert.assertEquals(token, tokenObject.getToken());
        Assert.assertEquals(secret, tokenObject.getSecret());
        Assert.assertNull(tokenObject.getRawResponse());
    }

    @Test
    public void testTokenConstructionWithTokenSecretAndRawResponse() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "rawResponseData";
        Token tokenObject = new Token(token, secret, rawResponse);
        Assert.assertEquals(token, tokenObject.getToken());
        Assert.assertEquals(secret, tokenObject.getSecret());
        Assert.assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenObjectNotConstructedWithRawResponse() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        tokenObject.getRawResponse();
    }

    @Test
    public void testToStringMethod() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        String expectedString = "Token[abc123 , def456]";
        String actualString = tokenObject.toString();
        Assert.assertEquals(expectedString, actualString);
    }
}