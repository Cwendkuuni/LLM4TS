import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.OAuthRequest;
import java.util.*;

public class OAuthRequestTest {

 private OAuthRequest oauthRequest;

 @Test
 public void testOAuthRequestConstructor() {
 oauthRequest = new OAuthRequest(Verb.GET, "https://example.com");
 Assert.assertEquals(Verb.GET, oauthRequest.getVerb());
 Assert.assertEquals("https://example.com", oauthRequest.getUrl());
 Assert.assertNotNull(oauthRequest.getOauthParameters());
 Assert.assertTrue(oauthRequest.getOauthParameters().isEmpty());
 }

 @Test
 public void testAddOAuthParameter() {
 oauthRequest = new OAuthRequest(Verb.GET, "https://example.com");
 oauthRequest.addOAuthParameter("oauth_token", "token123");
 Map<String, String> oauthParams = oauthRequest.getOauthParameters();
 Assert.assertEquals("token123", oauthParams.get("oauth_token"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOAuthParameterInvalidKey() {
 oauthRequest = new OAuthRequest(Verb.GET, "https://example.com");
 oauthRequest.addOAuthParameter("invalidKey", "value");
 fail("IllegalArgumentException should be thrown for invalid key");
 }

 @Test
 public void testAddScopeParameter() {
 oauthRequest = new OAuthRequest(Verb.GET, "https://example.com");
 oauthRequest.addOAuthParameter("scope", "read write");
 Map<String, String> oauthParams = oauthRequest.getOauthParameters();
 Assert.assertEquals("read write", oauthParams.get("scope"));
 }

 @Test
 public void testGetOAuthParametersEmpty() {
 oauthRequest = new OAuthRequest(Verb.GET, "https://example.com");
 Map<String, String> oauthParams = oauthRequest.getOauthParameters();
 Assert.assertTrue(oauthParams.isEmpty());
 }

 @Test
 public void testGetOAuthParametersWithMultipleParameters() {
 oauthRequest = new OAuthRequest(Verb.GET, "https://example.com");
 oauthRequest.addOAuthParameter("oauth_token", "token123");
 oauthRequest.addOAuthParameter("oauth_consumer_key", "consumerKey");
 Map<String, String> oauthParams = oauthRequest.getOauthParameters();
 Set<String> keys = oauthParams.keySet();
 assertTrue(keys.containsAll( Arrays.asList("oauth_token", "oauth_consumer_key") ));
 }

 @Test(expected = ConcurrentModificationException.class)
 public void testAddOAuthParameterConcurrentModification() {
 ConcurrentModificationException exception = null;
 oauthRequest = new OAuthRequest(Verb.GET, "https://example.com");
 Thread thread1 = new Thread(() -> oauthRequest.addOAuthParameter("oauth_token", "token123"));
 Thread thread2 = new Thread(() -> oauthRequest.addOAuthParameter("oauth_token", "token456"));
 thread1.start();
 thread2.start();
 try {
 thread1.join();
 thread2.join();
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 exception = oauthRequest.getOauthParameters().size() > 0 ? null : oauthRequest.getOauthParameters().getClass().cast(oauthRequest.getOauthParameters()).newDescription("Size should be greater than zero after concurrent modifications");
 fail("ConcurrentModificationException should be thrown for concurrent modifications");
 }

 @Test
 public void testToString() {
 oauthRequest = new OAuthRequest(Verb.GET, "https://example.com");
 oauthRequest.addOAuthParameter("oauth_token", "token123");
 oauthRequest.addOAuthParameter("oauth_consumer_key", "consumerKey");
 String expected = String.format("@OAuthRequest(GET, %s)", "https://example.com");
 String actual = oauthRequest.toString();
 Assert.assertEquals(expected, actual);
 }
}