package org.scribe.model;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class OAuthRequestTest {

 private static final String URL = "https://api.example.com/resource";
 private static final String VERB = "GET";

 @Test
 public void testOAuthRequestCreation() {
  OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, URL);
  assertNull(oauthRequest.getOauthParameters());
 }

 @Test
 public void testAddOAuthParameter() {
  OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, URL);
  oauthRequest.addOAuthParameter("oauth_consumer_key", "abc123");
  Map<String, String> params = oauthRequest.getOauthParameters();
  assertEquals("abc123", params.get("oauth_consumer_key"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddInvalidOAuthParameter() {
  OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, URL);
  oauthRequest.addOAuthParameter("invalidKey", "value");
 }

 @Test
 public void testAddScopeParameter() {
  OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, URL);
  oauthRequest.addOAuthParameter("scope", "read write");
  Map<String, String> params = oauthRequest.getOauthParameters();
  assertEquals("read write", params.get("scope"));
 }

 @Test
 public void testGetOAuthParameters() {
  OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, URL);
  oauthRequest.addOAuthParameter("oauth_consumer_key", "abc123");
  oauthRequest.addOAuthParameter("oauth_nonce", "def456");
  oauthRequest.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
  oauthRequest.addOAuthParameter("oauth_timestamp", "723529717");
  oauthRequest.addOAuthParameter("oauth_version", "1.0");
  Map<String, String> params = oauthRequest.getOauthParameters();
  assertEquals(5, params.size());
  assertTrue(params.containsKey("oauth_consumer_key"));
  assertTrue(params.containsKey("oauth_nonce"));
  assertTrue(params.containsKey("oauth_signature_method"));
  assertTrue(params.containsKey("oauth_timestamp"));
  assertTrue(params.containsKey("oauth_version"));
 }

 @Test
 public void testToString() {
  OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, URL);
  oauthRequest.addOAuthParameter("oauth_consumer_key", "abc123");
  oauthRequest.addOAuthParameter("oauth_nonce", "def456");
  oauthRequest.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
  oauthRequest.addOAuthParameter("oauth_timestamp", "723529717");
  oauthRequest.addOAuthParameter("oauth_version", "1.0");
  String expected = String.format("@OAuthRequest(GET, %s)", URL);
  String actual = oauthRequest.toString();
  assertEquals(expected, actual);
 }
}