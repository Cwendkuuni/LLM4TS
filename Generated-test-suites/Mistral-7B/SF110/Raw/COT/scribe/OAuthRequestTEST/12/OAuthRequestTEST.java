import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.OAuthRequest;
import java.util.*;

public class OAuthRequestTest {

 private OAuthRequest oauthRequest;

 @Test
 public void testOAuthRequestConstructor() {
 oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 Assert.assertNotNull(oauthRequest);
 Assert.assertEquals(Verb.GET, oauthRequest.getVerb());
 Assert.assertNotNull(oauthRequest.getUrl());
 }

 @Test
 public void testAddOAuthParameter() {
 oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 oauthRequest.addOAuthParameter("oauth_nonce", "12345");
 Map<String, String> expectedMap = new HashMap<>();
 expectedMap.put("oauth_nonce", "12345");
 Assert.assertEquals(expectedMap, oauthRequest.getOauthParameters());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOAuthParameterWithInvalidKey() {
 oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 oauthRequest.addOAuthParameter("invalidKey", "value");
 fail("IllegalArgumentException should be thrown");
 }

 @Test
 public void testGetOauthParametersEmpty() {
 oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 Assert.assertEquals(Collections.emptyMap(), oauthRequest.getOauthParameters());
 }

 @Test
 public void testToString() {
 oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 oauthRequest.addOAuthParameter("oauth_nonce", "12345");
 oauthRequest.addOAuthParameter("scope", "read write");
 String expectedString = String.format("@OAuthRequest(GET, %s)", "http://example.com");
 Assert.assertEquals(expectedString, oauthRequest.toString());
 }

 @Test
 public void testCheckKeyWithValidOAuthParameter() {
 oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 String validKey = "oauth_nonce";
 String validValue = "12345";
 oauthRequest.addOAuthParameter(validKey, validValue);
 String result = oauthRequest.checkKey(validKey);
 Assert.assertEquals(validKey, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckKeyWithInvalidOAuthParameter() {
 oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 String invalidKey = "invalidKey";
 oauthRequest.addOAuthParameter(invalidKey, "value");
 String result = oauthRequest.checkKey(invalidKey);
 fail("IllegalArgumentException should be thrown");
 }

 @Test
 public void testCheckKeyWithScopeParameter() {
 oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 String scopeKey = "scope";
 String scopeValue = "read write";
 oauthRequest.addOAuthParameter(scopeKey, scopeValue);
 String result = oauthRequest.checkKey(scopeKey);
 Assert.assertEquals(scopeKey, result);
 }
}