package org.scribe.model.test;

import static org.junit.Assert.*;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

import java.util.*;
import java.util.concurrent.*;

public class OAuthRequestTest {

 private OAuthRequest oAuthRequest;

 @Before
 public void setUp() {
 oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 }

 // Test constructor
 @Test
 public void testOAuthRequestConstruction() {
 OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");
 assertNotNull(request);
 }

 // Test addOAuthParameter method
 @Test
 public void testAddOAuthParameter() {
 oAuthRequest.addOAuthParameter("oauth_token", "token123");
 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertEquals("token123", oauthParameters.get("oauth_token"));
 }

 // Test checkKey method
 @Test(expected = IllegalArgumentException.class)
 public void testCheckKeyMethodWithInvalidKey() {
 oAuthRequest.checkKey("invalid_key");
 fail("IllegalArgumentException should be thrown");
 }

 @Test
 public void testCheckKeyMethodWithValidKey() {
 String key = "oauth_token";
 assertEquals(key, oAuthRequest.checkKey(key));
 }

 // Test getOauthParameters method
 @Test
 public void testGetOauthParameters() {
 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertNotNull(oauthParameters);
 assertFalse(oauthParameters.isEmpty());
 }

 // Test toString method
 @Test
 public void testToString() {
 String expected = "@OAuthRequest(GET, http://example.com)";
 String actual = oAuthRequest.toString();
 assertEquals(expected, actual);
 }

 // Test add multiple OAuthParameters
 @Test
 public void testAddMultipleOAuthParameters() {
 oAuthRequest.addOAuthParameter("oauth_token", "token123");
 oAuthRequest.addOAuthParameter("oauth_token_secret", "secret123");
 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertEquals(2, oauthParameters.size());
 }

 // Test add null value
 @Test(expected = NullPointerException.class)
 public void testAddNullValue() {
 oAuthRequest.addOAuthParameter("oauth_token", null);
 fail("NullPointerException should be thrown");
 }

 // Test add empty value
 @Test
 public void testAddEmptyValue() {
 oAuthRequest.addOAuthParameter("oauth_token", "");
 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertEquals("", oauthParameters.get("oauth_token"));
 }

 // Test add OAuthParameter with invalid key
 @Test(expected = IllegalArgumentException.class)
 public void testAddOAuthParameterWithInvalidKey() {
 oAuthRequest.checkKey("invalid_key");
 oAuthRequest.addOAuthParameter("invalid_key", "value");
 fail("IllegalArgumentException should be thrown");
 }

 // Test getOAuthParameter with non-existent key
 @Test
 public void testGetOAuthParameterWithNonExistentKey() {
 assertNull(oAuthRequest.getOauthParameters().get("non_existent_key"));
 }

 // Test getOAuthParameter with existing key
 @Test
 public void testGetOAuthParameterWithExistingKey() {
 oAuthRequest.addOAuthParameter("oauth_token", "token123");
 assertEquals("token123", oAuthRequest.getOauthParameters().get("oauth_token"));
 }

 // Test remove OAuthParameter
 @Test
 public void testRemoveOAuthParameter() {
 oAuthRequest.addOAuthParameter("oauth_token", "token123");
 oAuthRequest.removeOAuthParameter("oauth_token");
 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertNull(oauthParameters.get("oauth_token"));
 }

 // Test clear OAuthParameters
 @Test
 public void testClearOAuthParameters() {
 oAuthRequest.addOAuthParameter("oauth_token", "token123");
 oAuthRequest.clearOAuthParameters();
 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertFalse(oauthParameters.isEmpty());
 }

}