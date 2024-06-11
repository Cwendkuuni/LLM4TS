import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Token;

public class TokenTest {

    @Test
    public void testTokenConstructorWithTokenAndSecret() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        String actualToken = tokenObject.getToken();
        String actualSecret = tokenObject.getSecret();
        assertEquals(token, actualToken);
        assertEquals(secret, actualSecret);
    }

    @Test
    public void testTokenConstructorWithTokenSecretAndRawResponse() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "responseData";
        Token tokenObject = new Token(token, secret, rawResponse);
        String actualToken = tokenObject.getToken();
        String actualSecret = tokenObject.getSecret();
        String actualRawResponse = tokenObject.getRawResponse();
        assertEquals(token, actualToken);
        assertEquals(secret, actualSecret);
        assertEquals(rawResponse, actualRawResponse);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWhenRawResponseIsNull() {
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
        String expectedTokenToString = "Token[abc123 , def456]";
        String actualTokenToString = tokenObject.toString();
        assertEquals(expectedTokenToString, actualTokenToString);
    }

    @Test
    public void testGetTokenWithSerializableTokenObject() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        String tokenAsString = new ObjectToStringStrategy().convertToJsonString(tokenObject);
        Token deserializedTokenObject = new Gson().fromJson(tokenAsString, Token.class);
        String actualToken = deserializedTokenObject.getToken();
        assertEquals(token, actualToken);
    }

    @Test
    public void testGetSecretWithSerializableTokenObject() {
        String token = "abc123";
        String secret = "def456";
        Token tokenObject = new Token(token, secret);
        String tokenAsString = new ObjectToStringStrategy().convertToJsonString(tokenObject);
        Token deserializedTokenObject = new Gson().fromJson(tokenAsString, Token.class);
        String actualSecret = deserializedTokenObject.getSecret();
        assertEquals(secret, actualSecret);
    }

    @Test
    public void testGetRawResponseWithSerializableTokenObject() {
        String token = "abc123";
        String secret = "def456";
        String rawResponse = "responseData";
        Token tokenObject = new Token(token, secret, rawResponse);
        String tokenAsString = new ObjectToStringStrategy().convertToJsonString(tokenObject);
        Token deserializedTokenObject = new Gson().fromJson(tokenAsString, Token.class);
        String actualRawResponse = deserializedTokenObject.getRawResponse();
        assertEquals(rawResponse, actualRawResponse);
    }
}