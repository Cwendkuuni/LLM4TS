import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testGetToken() {
        Token token = new Token("testToken", "testSecret");
        String expectedToken = "testToken";
        String actualToken = token.getToken();
        assertEquals(expectedToken, actualToken);
    }

    @Test
    public void testGetSecret() {
        Token token = new Token("testToken", "testSecret");
        String expectedSecret = "testSecret";
        String actualSecret = token.getSecret();
        assertEquals(expectedSecret, actualSecret);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSecretWithNullRawResponse() {
        Token token = new Token("testToken", "testSecret");
        token.setRawResponse(null);
        token.getSecret();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSecretWithEmptyRawResponse() {
        Token token = new Token("testToken", "testSecret");
        token.setRawResponse("");
        token.getSecret();
    }

    @Test
    public void testGetRawResponse() {
        Token token = new Token("testToken", "testSecret", "testRawResponse");
        String expectedRawResponse = "testRawResponse";
        String actualRawResponse = token.getRawResponse();
        assertEquals(expectedRawResponse, actualRawResponse);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithEmptyRawResponse() {
        Token token = new Token("testToken", "testSecret");
        token.getRawResponse();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithNullRawResponse() {
        Token token = new Token("testToken", "testSecret");
        token.setRawResponse(null);
        token.getRawResponse();
    }

    @Test
    public void testToString() {
        Token token = new Token("testToken", "testSecret");
        String expectedString = "Token[testToken , testSecret]";
        String actualString = token.toString();
        assertEquals(expectedString, actualString);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNullToken() {
        Token token = null;
        try {
            token.getToken();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNullSecret() {
        Token token = new Token("testToken", null);
        token.getSecret();
    }
}