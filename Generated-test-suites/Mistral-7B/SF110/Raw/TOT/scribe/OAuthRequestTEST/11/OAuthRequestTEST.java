import org.junit.Test;
import org.scribe.model.OAuthRequest;
import static org.junit.Assert.*;
import java.util.*;

public class OAuthRequestTest {

 @Test
 public void testOAuthRequestConstruction() {
 OAuthRequest request = new OAuthRequest(OAuthVerb.GET, "http://example.com");
 assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
 assertNotNull(request.getOauthParameters());
 }

 @Test
 public void testAddOAuthParameter() {
 OAuthRequest request = new OAuthRequest(OAuthVerb.POST, "http://example.com");
 request.addOAuthParameter("oauth_consumer_key", "ABC123");
 request.addOAuthParameter("oauth_nonce", "DEF456");
 request.addOAuthParameter("oauth_signature", "GHI789");
 request.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
 request.addOAuthParameter("oauth_timestamp", "1234567890");
 request.addOAuthParameter("oauth_token", "JKL0987654321");
 request.addOAuthParameter("oauth_version", "1.0");
 Map<String, String> parameters = request.getOauthParameters();
 assertEquals(7, parameters.size());
 assertEquals("ABC123", parameters.get("oauth_consumer_key"));
 assertEquals("DEF456", parameters.get("oauth_nonce"));
 assertEquals("GHI789", parameters.get("oauth_signature"));
 // ... other assertions for other keys
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddInvalidOAuthParameterKey() {
 OAuthRequest request = new OAuthRequest(OAuthVerb.POST, "http://example.com");
 request.addOAuthParameter("invalid_key", "value");
 // The following line will cause an IllegalArgumentException to be thrown
 request.addOAuthParameter("oauth_version", "1.0"); // Already added in previous test
 }

 @Test
 public void testToStringWithOAuthParameters() {
 OAuthRequest request = new OAuthRequest(OAuthVerb.GET, "http://example.com");
 request.addOAuthParameter("oauth_consumer_key", "ABC123");
 request.addOAuthParameter("oauth_nonce", "DEF456");
 request.addOAuthParameter("oauth_signature", "GHI789");
 request.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
 request.addOAuthParameter("oauth_timestamp", "1234567890");
 request.addOAuthParameter("oauth_token", "JKL0987654321");
 request.addOAuthParameter("oauth_version", "1.0");
 assertEquals("@OAuthRequest(GET, http://example.com, {oauth_consumer_key=ABC123, oauth_nonce=DEF456, oauth_signature=GHI789, oauth_signature_method=HMAC-SHA1, oauth_timestamp=1234567890, oauth_token=JKL0987654321, oauth_version=1.0})", request.toString());
 }

 @Test
 public void testGetOAuthParameter() {
 OAuthRequest request = new OAuthRequest(OAuthVerb.GET, "http://example.com");
 request.addOAuthParameter("oauth_consumer_key", "ABC123");
 request.addOAuthParameter("oauth_nonce", "DEF456");
 request.addOAuthParameter("oauth_signature", "GHI789");
 request.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
 request.addOAuthParameter("oauth_timestamp", "1234567890");
 request.addOAuthParameter("oauth_token", "JKL0987654321");
 request.addOAuthParameter("oauth_version", "1.0");
 Map<String, String> parameters = request.getOauthParameters();
 assertEquals("ABC123", parameters.get("oauth_consumer_key"));
 assertEquals("DEF456", parameters.get("oauth_nonce"));
 assertEquals("GHI789", parameters.get("oauth_signature"));
 // ... other assertions for other keys
 }

 @Test
 public void testGetOAuthParameterNull() {
 OAuthRequest request = new OAuthRequest(OAuthVerb.GET, "http://example.com");
 Map<String, String> parameters = request.getOauthParameters();
 assertNull(parameters.get("invalid_key"));
 }

}