package org.scribe.model.test;

import static org.junit.Assert.*;

import java.util.*;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Request;

import org.junit.Test;

public class OAuthRequestTest {

 private OAuthRequest oauthRequest;

 @Test
 public void testOAuthRequestConstruction() {
 oauthRequest = new OAuthRequest(Request.Verb.GET, "http://example.com/api");
 assertEquals("@OAuthRequest(GET, http://example.com/api)", oauthRequest.toString());
 }

 @Test
 public void testAddOAuthParameter() {
 oauthRequest = new OAuthRequest(Request.Verb.GET, "http://example.com/api");
 oauthRequest.addOAuthParameter("oauth_token", "abc123");
 Map<String, String> oauthParams = oauthRequest.getOauthParameters();
 assertEquals("abc123", oauthParams.get("oauth_token"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOAuthParameterWithInvalidKey() {
 oauthRequest = new OAuthRequest(Request.Verb.GET, "http://example.com/api");
 oauthRequest.addOAuthParameter("invalidKey", "value");
 fail("IllegalArgumentException should be thrown for invalid key");
 }

 @Test
 public void testGetOAuthParameters() {
 oauthRequest = new OAuthRequest(Request.Verb.GET, "http://example.com/api");
 oauthRequest.addOAuthParameter("oauth_token", "abc123");
 oauthRequest.addOAuthParameter("scope", "read write");
 Map<String, String> oauthParams = oauthRequest.getOauthParameters();
 assertEquals(2, oauthParams.size());
 assertEquals("abc123", oauthParams.get("oauth_token"));
 assertEquals("read write", oauthParams.get("scope"));
 }

 @Test
 public void testToString() {
 oauthRequest = new OAuthRequest(Request.Verb.GET, "http://example.com/api");
 oauthRequest.addOAuthParameter("oauth_token", "abc123");
 oauthRequest.addOAuthParameter("scope", "read write");
 assertEquals("@OAuthRequest(GET, http://example.com/api)", oauthRequest.toString());
 }

 @Test
 public void testCheckKey() {
 String validKey = "oauth_token";
 String invalidKey = "invalidKey";
 assertEquals(validKey, oauthRequest.checkKey(validKey));
 assertNull(oauthRequest.checkKey(invalidKey));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckKeyWithInvalidPrefix() {
 String invalidKeyPrefix = "prefix_token";
 oauthRequest.checkKey(invalidKeyPrefix);
 fail("IllegalArgumentException should be thrown for invalid prefix");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckKeyWithInvalidScope() {
 oauthRequest.checkKey("scope");
 fail("IllegalArgumentException should be thrown for 'scope' key");
 }
}