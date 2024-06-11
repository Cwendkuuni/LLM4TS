package org.scribe.model;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;
import org.scribe.model.OAuthRequest;

public class OAuthRequestTest {

 private OAuthRequest oauthRequest;

 @Test
 public void testOAuthRequestInstance() {
 oauthRequest = new OAuthRequest(Request.Verb.GET, "http://example.com");
 assertEquals("@OAuthRequest(GET, http://example.com)", oauthRequest.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOAuthParameterWithInvalidKey() {
 oauthRequest = new OAuthRequest(Request.Verb.GET, "http://example.com");
 oauthRequest.addOAuthParameter("invalidKey", "value");
 fail("IllegalArgumentException should be thrown when key starts with neither 'oauth_' nor equals 'scope'");
 }

 @Test
 public void testAddOAuthParameter() {
 oauthRequest = new OAuthRequest(Request.Verb.GET, "http://example.com");
 oauthRequest.addOAuthParameter("oauth_token", "tokenValue");
 Map<String, String> oauthParams = oauthRequest.getOauthParameters();
 assertEquals("tokenValue", oauthParams.get("oauth_token"));
 }

 @Test
 public void testAddScopeParameter() {
 oauthRequest = new OAuthRequest(Request.Verb.GET, "http://example.com");
 oauthRequest.addOAuthParameter("scope", "read write");
 Map<String, String> oauthParams = oauthRequest.getOauthParameters();
 assertEquals("read write", oauthParams.get("scope"));
 }

 @Test
 public void testGetOauthParameters() {
 oauthRequest = new OAuthRequest(Request.Verb.GET, "http://example.com");
 oauthRequest.addOAuthParameter("oauth_token", "tokenValue");
 oauthRequest.addOAuthParameter("scope", "read write");
 Map<String, String> oauthParams = oauthRequest.getOauthParameters();
 assertEquals(2, oauthParams.size());
 }

 @Test
 public void testToString() {
 oauthRequest = new OAuthRequest(Request.Verb.GET, "http://example.com");
 oauthRequest.addOAuthParameter("oauth_token", "tokenValue");
 oauthRequest.addOAuthParameter("scope", "read write");
 assertEquals("@OAuthRequest(GET, http://example.com)", oauthRequest.toString());
 }

}