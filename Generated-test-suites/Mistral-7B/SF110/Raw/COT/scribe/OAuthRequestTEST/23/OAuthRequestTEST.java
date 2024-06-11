package org.scribe.model.test;

import static org.junit.Assert.*;

import java.util.*;
import org.scribe.model.*;
import org.scribe.model.OAuthRequest;

public class OAuthRequestTest {

 private OAuthRequest oAuthRequest;

 @Before
 public void setUp() {
 oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 }

 // Test case for constructor
 @Test
 public void testOAuthRequestConstruction() {
 OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 assertEquals(Verb.GET, oauthRequest.getVerb());
 assertEquals("http://example.com", oauthRequest.getUrl());
 assertNull(oauthRequest.getOauthParameters());
 }

 // Test case for addOAuthParameter method
 @Test
 public void testAddOAuthParameter() {
 oAuthRequest.addOAuthParameter("oauth_token", "testToken");
 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertEquals("testToken", oauthParameters.get("oauth_token"));
 }

 // Test case for checkKey method
 @Test(expected = IllegalArgumentException.class)
 public void testCheckKeyInvalidParameter() {
 oAuthRequest.addOAuthParameter("invalidKey", "testValue");
 oAuthRequest.checkKey("invalidKey");
 }

 @Test
 public void testCheckKeyValidParameter() {
 oAuthRequest.addOAuthParameter("oauth_token", "testToken");
 oAuthRequest.checkKey("oauth_token");
 }

 @Test
 @ExpectedException(IllegalArgumentException.class)
 public void testCheckKeyScopeParameter() {
 oAuthRequest.addOAuthParameter("scope", "testScope");
 oAuthRequest.checkKey("scope");
 }

 // Test case for getOauthParameters method
 @Test
 public void testGetOauthParametersEmpty() {
 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertNull(oauthParameters);
 }

 @Test
 public void testGetOauthParametersWithParameters() {
 oAuthRequest.addOAuthParameter("oauth_token", "testToken");
 oAuthRequest.addOAuthParameter("oauth_consumer_key", "testConsumerKey");
 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertEquals(2, oauthParameters.size());
 assertEquals("testToken", oauthParameters.get("oauth_token"));
 assertEquals("testConsumerKey", oauthParameters.get("oauth_consumer_key"));
 }

}