package org.scribe.model.test;

import org.junit.Before;
import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Request;
import org.scribe.model.Verb;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OAuthRequestTest {

 private OAuthRequest oAuthRequest;

 @Before
 public void setup() {
 oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 }

 // Test constructor
 @Test
 public void testOAuthRequestConstructor() {
 assertEquals(Verb.GET, oAuthRequest.getVerb());
 assertEquals("http://example.com", oAuthRequest.getUrl());
 assertNotNull(oAuthRequest.getOauthParameters());
 }

 // Test addOAuthParameter method
 @Test
 public void testAddOAuthParameter() {
 oAuthRequest.addOAuthParameter("oauth_token", "tokenValue");
 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertEquals("tokenValue", oauthParameters.get("oauth_token"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddInvalidOAuthParameterKey() {
 oAuthRequest.addOAuthParameter("invalidKey", "value");
 assertFalse(oAuthRequest.getOauthParameters().containsKey("invalidKey"));
 }

 // Test getOauthParameters method
 @Test
 public void testGetOauthParameters() {
 Map<String, String> expectedOauthParameters = new HashMap<>();
 expectedOauthParameters.put("oauth_token", "tokenValue");
 expectedOauthParameters.put("scope", "read write");
 oAuthRequest.addOAuthParameter("oauth_token", "tokenValue");
 oAuthRequest.addOAuthParameter("scope", "read write");
 Map<String, String> actualOauthParameters = oAuthRequest.getOauthParameters();
 assertEquals(expectedOauthParameters, actualOauthParameters);
 }

 // Test toString method
 @Test
 public void testToString() {
 assertEquals("@OAuthRequest(GET, http://example.com)", oAuthRequest.toString());
 }

 // Test checkKey method
 @Test(expected = IllegalArgumentException.class)
 public void testCheckKeyInvalidKey() {
 String invalidKey = "invalidKey";
 assertThrows(IllegalArgumentException.class, () -> oAuthRequest.checkKey(invalidKey));
 }

 @Test
 public void testCheckKeyValidKey() {
 String validKey = "oauth_token";
 assertEquals(validKey, oAuthRequest.checkKey(validKey));
 }

 // Test checkKeyWithScopeKey
 @Test
 public void testCheckKeyWithScopeKey() {
 String scopeKey = "scope";
 assertEquals(scopeKey, oAuthRequest.checkKey(scopeKey));
 }

 // Test edge cases - empty constructor
 @Test
 public void testEmptyConstructor() {
 OAuthRequest emptyOAuthRequest = new OAuthRequest(null, null);
 assertNull(emptyOAuthRequest.getVerb());
 assertNull(emptyOAuthRequest.getUrl());
 assertNull(emptyOAuthRequest.getOauthParameters());
 }

 // Test edge cases - null verb
 @Test
 public void testNullVerb() {
 OAuthRequest nullVerbOAuthRequest = new OAuthRequest(null, "http://example.com");
 assertNull(nullVerbOAuthRequest.getVerb());
 assertNotNull(nullVerbOAuthRequest.getUrl());
 assertNotNull(nullVerbOAuthRequest.getOauthParameters());
 }

 // Test edge cases - null url
 @Test
 public void testNullUrl() {
 OAuthRequest nullUrlOAuthRequest = new OAuthRequest(Verb.GET, null);
 assertNotNull(nullUrlOAuthRequest.getVerb());
 assertNull(nullUrlOAuthRequest.getUrl());
 assertNotNull(nullUrlOAuthRequest.getOauthParameters());
 }

 // Test edge cases - empty map
 @Test
 public void testEmptyMap() {
 OAuthRequest emptyMapOAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 assertEquals(0, emptyMapOAuthRequest.getOauthParameters().size());
 }

}