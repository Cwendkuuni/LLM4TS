import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Token;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenObject = new Token(token, secret);
        Assert.assertEquals(token, tokenObject.getToken());
        Assert.assertEquals(secret, tokenObject.getSecret());
        Assert.assertNull(tokenObject.getRawResponse());
    }

    @Test
    public void testTokenConstructorWithTokenSecretAndRawResponse() {
        String token = "testToken";
        String secret = "testSecret";
        String rawResponse = "testRawResponse";
        Token tokenObject = new Token(token, secret, rawResponse);
        Assert.assertEquals(token, tokenObject.getToken());
        Assert.assertEquals(secret, tokenObject.getSecret());
        Assert.assertEquals(rawResponse, tokenObject.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponse() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenObject = new Token(token, secret);
        tokenObject.getRawResponse();
    }

    @Test
    public void testToStringMethod() {
        String token = "testToken";
        String secret = "testSecret";
        Token tokenObject = new Token(token, secret);
        String expectedResult = "Token[testToken , testSecret]";
        String actualResult = tokenObject.toString();
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetTokenNull() {
        String secret = "testSecret";
        Token tokenObject = new Token(null, secret);
        Assert.assertNull(tokenObject.getToken());
        Assert.assertEquals(secret, tokenObject.getSecret());
        Assert.assertNull(tokenObject.getRawResponse());
    }

    @Test
    public void testGetSecretNull() {
        String token = "testToken";
        Token tokenObject = new Token(token, null);
        Assert.assertEquals(token, tokenObject.getToken());
        Assert.assertNull(tokenObject.getSecret());
        Assert.assertNull(tokenObject.getRawResponse());
    }
}