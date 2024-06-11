package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class OAuthRequestTest {

 private OAuthRequest oAuthRequest;

 @Before
 public void setUp() {
 oAuthRequest = new OAuthRequest(Request.Verb.GET, "https://api.example.com/resource");
 }

 @Test
 public void testOAuthRequestInstance() {
 OAuthRequest oAuthRequestInstance = new OAuthRequest(Request.Verb.POST, "https://api.example.com/resource");
 assertEquals("@OAuthRequest(GET, https://api.example.com/resource)", oAuthRequest.toString());
 assertEquals("@OAuthRequest(POST, https://api.example.com/resource)", oAuthRequestInstance.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOAuthParameterWithInvalidKey() {
 oAuthRequest.addOAuthParameter("invalidKey", "value");
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testAddOAuthParameter() {
 oAuthRequest.addOAuthParameter("oauth_consumer_key", "ABC123");
 oAuthRequest.addOAuthParameter("oauth_token", "XYZ456");
 oAuthRequest.addOAuthParameter("scope", "read write");
 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertEquals("ABC123", oauthParameters.get("oauth_consumer_key"));
 assertEquals("XYZ456", oauthParameters.get("oauth_token"));
 assertEquals("read write", oauthParameters.get("scope"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOAuthParameterWithDuplicateKey() {
 oAuthRequest.addOAuthParameter("oauth_consumer_key", "ABC123");
 oAuthRequest.addOAuthParameter("oauth_consumer_key", "DEF456");
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testGetOauthParameters() {
 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertNotNull(oauthParameters);
 assertEquals(3, oauthParameters.size());
 }

}