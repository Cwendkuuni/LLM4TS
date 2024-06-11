import org.junit.Before;
import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Request;
import org.scribe.model.Verb;

import java.util.*;
import static org.junit.Assert.*;

public class OAuthRequestTest {

 private OAuthRequest oAuthRequest;

 @Before
 public void setUp() {
 oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 }

 @Test
 public void testAddOAuthParameter() {
 // Add valid OAuth parameter
 oAuthRequest.addOAuthParameter("oauth_consumer_key", "test_key");
 oAuthRequest.addOAuthParameter("oauth_nonce", "test_nonce");
 oAuthRequest.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
 oAuthRequest.addOAuthParameter("oauth_timestamp", "1234567890");
 oAuthRequest.addOAuthParameter("oauth_token", "test_token");
 oAuthRequest.addOAuthParameter("oauth_version", "1.0");

 // Check if OAuth parameters are added correctly
 Map<String, String> oauthParameters = oAuthRequest.getOAuthParameters();
 assertEquals("test_key", oauthParameters.get("oauth_consumer_key"));
 assertEquals("test_nonce", oauthParameters.get("oauth_nonce"));
 assertEquals("HMAC-SHA1", oauthParameters.get("oauth_signature_method"));
 // ... add assertions for other OAuth parameters as well

 // Test adding duplicate OAuth parameter
 oAuthRequest.addOAuthParameter("oauth_consumer_key", "new_key");
 // Assertion for duplicate key would be added here
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddInvalidOAuthParameter() {
 // Test adding invalid OAuth parameter (key doesn't start with "oauth_" or is "scope")
 oAuthRequest.addOAuthParameter("test_key", "test_value");
 }

 @Test
 public void testGetOAuthParameters() {
 // Test getting OAuth parameters
 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 // Perform assertions on the returned map
 }
}