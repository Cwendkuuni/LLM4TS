package org.scribe.model.test;

import static org.junit.Assert.*;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Request;
import org.scribe.model.Verb;

import java.util.*;
import java.util.concurrent.*;

public class OAuthRequestTest {

 private OAuthRequest oAuthRequest;

 @Before
 public void setUp() {
 oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 }

 // Test case for constructor
 @Test
 public void testOAuthRequestConstruction() {
 assertEquals("GET", oAuthRequest.getVerb());
 assertEquals("http://example.com", oAuthRequest.getUrl());
 assertEquals(0, oAuthRequest.getOauthParameters().size());
 }

 // Test case for addOAuthParameter method
 @Test
 public void testAddOAuthParameter() {
 oAuthRequest.addOAuthParameter("oauth_consumer_key", "test_key");
 oAuthRequest.addOAuthParameter("oauth_token", "test_token");
 oAuthRequest.addOAuthParameter("scope", "test_scope");

 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertEquals("test_key", oauthParameters.get("oauth_consumer_key"));
 assertEquals("test_token", oauthParameters.get("oauth_token"));
 assertEquals("test_scope", oauthParameters.get("scope"));
 }

 // Test case for checkKey method
 @Test(expected = IllegalArgumentException.class)
 public void testCheckKeyInvalidKey() {
 oAuthRequest.checkKey("invalid_key");
 }

 // Test case for checkKey method with valid keys
 @Test
 public void testCheckKeyValidKeys() {
 String key = "oauth_consumer_key";
 String expectedKey = "oauth_consumer_key";
 assertEquals(expectedKey, oAuthRequest.checkKey(key));
 }

 // Test case for getOauthParameters method
 @Test
 public void testGetOauthParameters() {
 Map<String, String> oauthParameters = new HashMap<>();
 oAuthRequest.addOAuthParameter("oauth_consumer_key", "test_key");
 oAuthRequest.addOAuthParameter("oauth_token", "test_token");
 oAuthRequest.addOAuthParameter("scope", "test_scope");

 assertEquals(oauthParameters, oAuthRequest.getOauthParameters());
 }

 // Test case for toString method
 @Test
 public void testToString() {
 assertEquals("@OAuthRequest(GET, http://example.com)", oAuthRequest.toString());
 }

 // Test case for addOAuthParameter method with existing key
 @Test(expected = IllegalStateException.class)
 public void testAddOAuthParameterExistingKey() {
 oAuthRequest.addOAuthParameter("oauth_consumer_key", "new_test_key");
 oAuthRequest.addOAuthParameter("oauth_consumer_key", "existing_test_key");
 }

 // Test case for addOAuthParameter method with null value
 @Test(expected = NullPointerException.class)
 public void testAddOAuthParameterNullValue() {
 oAuthRequest.addOAuthParameter("oauth_consumer_key", null);
 }

 // Test case for getOauthParameters method with empty map
 @Test
 public void testGetOauthParametersEmptyMap() {
 Map<String, String> emptyOauthParameters = new HashMap<>();
 assertEquals(emptyOauthParameters, new OAuthRequest(Verb.GET, "http://example.com").getOauthParameters());
 }

 // Test case for checkKey method with empty key
 @Test(expected = IllegalArgumentException.class)
 public void testCheckKeyEmptyKey() {
 oAuthRequest.checkKey("");
 }
}