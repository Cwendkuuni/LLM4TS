import org.junit.Before;
import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Request;
import org.scribe.model.Verb;

import java.util.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class OAuthRequestTest {

 private OAuthRequest oauthRequest;

 @Before
 public void setUp() {
 oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 }

 @Test
 public void testAddOAuthParameter() {
 oauthRequest.addOAuthParameter("oauth_consumer_key", "test_key");
 oauthRequest.addOAuthParameter("scope", "read write");

 Map<String, String> expectedOAuthParameters = new HashMap<>();
 expectedOauthParameters.put("oauth_consumer_key", "test_key");
 expectedOauthParameters.put("scope", "read write");

 Map<String, String> actualOAuthParameters = oauthRequest.getOauthParameters();
 assertEquals(expectedOAuthParameters, actualOAuthParameters);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddInvalidOAuthParameter() {
 oauthRequest.addOAuthParameter("invalid_key", "test_value");
 }

 @Test
 public void testGetOAuthParameters() {
 Map<String, String> oAuthParameters = oauthRequest.getOauthParameters();
 assertEquals(2, oAuthParameters.size());
 }

 @Test
 public void testToString() {
 assertEquals("@OAuthRequest(GET, http://example.com)", oauthRequest.toString());
 }
}