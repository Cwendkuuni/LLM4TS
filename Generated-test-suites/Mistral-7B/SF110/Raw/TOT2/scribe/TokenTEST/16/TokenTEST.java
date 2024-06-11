import org.junit.Test;
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

    @Test(expected = NullPointerException.class)
    public void testGetSecretWithNullSecret() {
        Token token = new Token("testToken", null);
        token.getSecret();
    }

    @Test
    public void testGetRawResponse() {
        Token token = new Token("testToken", "testSecret");
        String expectedRawResponse = "testRawResponse";
        Token tokenWithRawResponse = new Token("testToken", "testSecret", expectedRawResponse);
        String actualRawResponse = tokenWithRawResponse.getRawResponse();

        assertEquals(expectedRawResponse, actualRawResponse);
    }

    @Test
    public void testGetRawResponseWithNullRawResponse() {
        Token token = new Token("testToken", "testSecret");
        try {
            token.getRawResponse();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testGetTokenWithLongToken() {
        String longToken = "1234567890123456789012345678901234567890";
        Token token = new Token(longToken, "testSecret");
        String actualToken = token.getToken();

        assertEquals(longToken, actualToken);
    }

    @Test
    public void testGetSecretWithLongSecret() {
        String longSecret = "1234567890123456789012345678901234567890";
        Token token = new Token("testToken", longSecret);
        String actualSecret = token.getSecret();

        assertEquals(longSecret, actualSecret);
    }

    @Test
    public void testGetTokenWithNullToken() {
        String token = null;
        try {
            new Token(token, "testSecret");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testGetSecretWithNullSecret() {
        String secret = null;
        try {
            new Token("testToken", secret);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testGetSecretWithEmptySecret() {
        Token token = new Token("testToken", "");
        String actualSecret = token.getSecret();

        assertEquals("", actualSecret);
    }

    @Test
    public void testGetTokenWithEmptyToken() {
        Token token = new Token("", "testSecret");
        String actualToken = token.getToken();

        assertEquals("", actualToken);
    }

    @Test
    public void testToString() {
        Token token = new Token("testToken", "testSecret");
        String expected = "Token[testToken , testSecret]";
        String actual = token.toString();

        assertEquals(expected, actual);
    }
}