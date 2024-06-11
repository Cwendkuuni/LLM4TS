import org.junit.Before;
import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OAuthRequestTest {

 private OAuthRequest oAuthRequest;

 @Before
 public void setup() {
 oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 }

 @Test
 public void testOAuthRequestConstruction() {
 OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");
 assertNotNull(request);
 }

 @Test
 public void testAddOAuthParameter() {
 oAuthRequest.addOAuthParameter("consumerKey", "testConsumerKey");
 oAuthRequest.addOAuthParameter("consumerSecret", "testConsumerSecret");
 Map<String, String> oauthParameters = oAuthRequest.getOAuthParameters();
 assertEquals("testConsumerKey", oauthParameters.get("oauth_consumerKey"));
 assertEquals("testConsumerSecret", oauthParameters.get("oauth_consumerSecret"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNonOAuthParameter() {
 oAuthRequest.addOAuthParameter("nonOauthParameter", "testValue");
 assertThrows(IllegalArgumentException.class, () -> oAuthRequest.getOauthParameters());
 }

 @Test
 public void testAddDuplicateOAuthParameter() {
 oAuthRequest.addOAuthParameter("oauth_signature", "testSignature");
 oAuthRequest.addOAuthParameter("oauth_signature", "testSignature2");
 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertEquals("testSignature", oauthParameters.get("oauth_signature"));
 }

 @Test
 public void testCheckKey() {
 assertEquals("scope", oAuthRequest.checkKey("scope"));
 assertEquals("oauth_token", oAuthRequest.checkKey("oauth_token"));
 assertThrows(IllegalArgumentException.class, () -> oAuthRequest.checkKey("nonOauthKey"));
 }

 @Test
 public void testGetOauthParameters() {
 Map<String, String> oauthParameters = new HashMap<String, String>();
 oauthParameters.put("oauth_consumerKey", "testConsumerKey");
 oauthParameters.put("oauth_consumerSecret", "testConsumerSecret");
 oAuthRequest.setOauthParameters(oauthParameters);
 Map<String, String> result = oAuthRequest.getOauthParameters();
 assertEquals(oauthParameters, result);
 }

 @Test
 public void testToString() {
 assertEquals("@OAuthRequest(GET, http://example.com)", oAuthRequest.toString());
 }
}