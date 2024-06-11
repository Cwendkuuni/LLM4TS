package org.scribe.model;

import static org.junit.Assert.*;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

import java.util.*;
import java.util.concurrent.*;

public class OAuthRequestTest {

 private OAuthRequest oauthRequest;

 @Before
 public void setUp() {
 oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 }

 // Test case for constructor
 @Test
 public void testOAuthRequestConstruction() {
 OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 assertEquals("GET", oauthRequest.getVerb().name());
 assertEquals("http://example.com", oauthRequest.getUrl());
 assertNull(oauthRequest.getOauthParameters());
 }

 // Test case for addOAuthParameter method
 @Test
 public void testAddOAuthParameter() {
 oauthRequest.addOAuthParameter("oauth_token", "tokenValue");
 Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
assertEquals("tokenValue", oauthParameters.get("oauth_token"));
 }

 // Test case for checkKey method
 @Test(expected = IllegalArgumentException.class)
 public void testCheckKeyInvalid() {
 oauthRequest.checkKey("invalidKey");
 }

 // Test case for checkKey valid oauth_ parameter
 @Test
 public void testCheckKeyValidOAuth() {
 oauthRequest.checkKey("oauth_token");
 }

 // Test case for checkKey valid scope parameter
 @Test
 public void testCheckKeyValidScope() {
 oauthRequest.checkKey("scope");
 }

 // Test case for getOauthParameters method
 @Test
 public void testGetOauthParameters() {
 oauthRequest.addOAuthParameter("oauth_token", "tokenValue");
 oauthRequest.addOAuthParameter("oauth_consumer_key", "consumerKey");
 Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
 assertEquals(2, oauthParameters.size());
 }

 // Test case for addOAuthParameter with existing key
 @Test(expected = IllegalStateException.class)
 public void testAddOAuthParameterExistingKey() {
 oauthRequest.addOAuthParameter("oauth_token", "tokenValue");
 oauthRequest.addOAuthParameter("oauth_token", "newTokenValue");
 }

 // Test case for toString method
 @Test
 public void testToString() {
 assertEquals("@OAuthRequest(GET, http://example.com)", oauthRequest.toString());
 }

 // Test case for addOAuthParameter with null key
 @Test(expected = IllegalArgumentException.class)
 public void testAddOAuthParameterNullKey() {
 oauthRequest.addOAuthParameter(null, "value");
 }

 // Test case for addOAuthParameter with null value
 @Test(expected = IllegalArgumentException.class)
 public void testAddOAuthParameterNullValue() {
 oauthRequest.addOAuthParameter("key", null);
 }

 // Test case for getOauthParameters with empty map
 @Test
 public void testGetOauthParametersEmpty() {
 assertEquals(0, oauthRequest.getOauthParameters().size());
 }
}