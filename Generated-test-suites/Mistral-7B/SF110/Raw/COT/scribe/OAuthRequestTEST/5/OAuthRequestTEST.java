package org.scribe.model;

import static org.junit.Assert.*;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Request;
import org.scribe.model.Verb;

import java.util.*;

public class OAuthRequestTest {

 private OAuthRequest oauthRequest;

 @Before
 public void setup() {
 oauthRequest = new OAuthRequest(Verb.GET, "http://test.com");
 }

 // Test constructor
 @Test
 public void testOAuthRequestConstruction() {
 OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://test.com");
 assertEquals("GET", oauthRequest.getVerb().name());
 assertEquals("http://test.com", oauthRequest.getUrl());
 assertNull(oauthRequest.getOauthParameters());
 }

 // Test addOAuthParameter method
 @Test
 public void testAddOAuthParameter() {
 oauthRequest.addOAuthParameter("oauth_consumer_key", "testKey");
 oauthRequest.addOAuthParameter("scope", "testScope");
 Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
 assertEquals("testKey", oauthParameters.get("oauth_consumer_key"));
 assertEquals("testScope", oauthParameters.get("scope"));
 }

 // Test checkKey method
 @Test(expected = IllegalArgumentException.class)
 public void testCheckKeyInvalidParameterName() {
 oauthRequest.checkKey("testParameter");
 }

 // Test getOauthParameters method
 @Test
 public void testGetOauthParameters() {
 Map<String, String> oauthParameters = new HashMap<String, String>();
 oauthParameters.put("oauth_consumer_key", "testKey");
 oauthRequest.setOauthParameters(oauthParameters);
 Map<String, String> actualOauthParameters = oauthRequest.getOauthParameters();
 assertEquals(oauthParameters, actualOauthParameters);
 }

 // Test toString method
 @Test
 public void testToString() {
 assertEquals("@OAuthRequest(GET, http://test.com)", oauthRequest.toString());
 }

 // Test addOAuthParameter with existing key
 @Test(expected = IllegalArgumentException.class)
 public void testAddOAuthParameterWithExistingKey() {
 oauthRequest.addOAuthParameter("oauth_consumer_key", "testKey1");
 oauthRequest.addOAuthParameter("oauth_consumer_key", "testKey2");
 }

 // Test getOauthParameters when no parameters are added
 @Test
 public void testGetOauthParametersWhenNoParametersAdded() {
 Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
 assertNull(oauthParameters);
 }

 // Test checkKey with invalid key starting with "oauth_" but not "oauth_"
 @Test(expected = IllegalArgumentException.class)
 public void testCheckKeyInvalidKeyStartingWithOauthButNotOauth_() {
 oauthRequest.checkKey("test_key");
 }

 // Test checkKey with invalid key not starting with "oauth_"
 @Test(expected = IllegalArgumentException.class)
 public void testCheckKeyInvalidKeyNotStartingWithOauth() {
 oauthRequest.checkKey("testParameter");
 }

 // Test checkKey with "scope" parameter
 @Test
 public void testCheckKeyScopeParameter() {
 oauthRequest.checkKey("scope");
 }

}