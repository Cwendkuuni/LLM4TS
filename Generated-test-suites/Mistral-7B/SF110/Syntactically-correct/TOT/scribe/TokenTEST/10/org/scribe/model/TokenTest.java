import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Token;

public class TokenTest {

 // Test case for the constructor with token and secret arguments
 @Test
 public void testTokenConstructorWithTokenAndSecret() {
 String token = "abc123";
 String secret = "def456";
 Token tokenObject = new Token(token, secret);
 assertEquals(token, tokenObject.getToken());
 assertEquals(secret, tokenObject.getSecret());
 assertNull(tokenObject.getRawResponse());
 }

 // Test case for the constructor with token, secret, and rawResponse arguments
 @Test
 public void testTokenConstructorWithTokenSecretAndRawResponse() {
 String token = "abc123";
 String secret = "def456";
 String rawResponse = "{\"access_token\":\"abc123\",\"expires_in\":3600}";
 Token tokenObject = new Token(token, secret, rawResponse);
 assertEquals(token, tokenObject.getToken());
 assertEquals(secret, tokenObject.getSecret());
 assertEquals(rawResponse, tokenObject.getRawResponse());
 }

 // Test case for getToken method
 @Test
 public void testGetToken() {
 String token = "abc123";
 String secret = "def456";
 String rawResponse = "{\"access_token\":\"abc123\",\"expires_in\":3600}";
 Token tokenObject = new Token(token, secret, rawResponse);
 Token anotherTokenObject = new Token(token, secret);
 assertEquals(token, tokenObject.getToken());
 assertEquals(token, anotherTokenObject.getToken());
 }

 // Test case for getSecret method
 @Test
 public void testGetSecret() {
 String token = "abc123";
 String secret = "def456";
 String rawResponse = "{\"access_token\":\"abc123\",\"expires_in\":3600}";
 Token tokenObject = new Token(token, secret, rawResponse);
 Token anotherTokenObject = new Token(token, secret);
 assertEquals(secret, tokenObject.getSecret());
 assertEquals(secret, anotherTokenObject.getSecret());
 }

 // Test case for getRawResponse method with valid token object
 @Test
 public void testGetRawResponseWithValidTokenObject() {
 String token = "abc123";
 String secret = "def456";
 String rawResponse = "{\"access_token\":\"abc123\",\"expires_in\":3600}";
 Token tokenObject = new Token(token, secret, rawResponse);
 assertEquals(rawResponse, tokenObject.getRawResponse());
 }

 // Test case for getRawResponse method with invalid token object (no rawResponse)
 @Test(expected = IllegalStateException.class)
 public void testGetRawResponseWithInvalidTokenObject() {
 String token = "abc123";
 String secret = "def456";
 Token tokenObject = new Token(token, secret);
 tokenObject.getRawResponse();
 }
}