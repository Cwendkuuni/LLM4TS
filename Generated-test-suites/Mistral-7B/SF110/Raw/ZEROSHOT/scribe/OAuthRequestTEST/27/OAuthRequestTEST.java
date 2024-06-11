package org.scribe.model;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class OAuthRequestTest {

 private static final String URL_TEST = "https://api.example.com/";
 private static final OAuthRequest REQUEST_TEST = new OAuthRequest(OAuth.Verb.GET, URL_TEST);

 @Test
 public void testOAuthRequestInstance() {
  assertEquals("@OAuthRequest(GET, https://api.example.com/)", REQUEST_TEST.toString());
 }

 @Test
 public void testAddOAuthParameter() {
  REQUEST_TEST.addOAuthParameter("oauth_consumer_key", "test_consumer_key");
  Map<String, String> oauthParameters = REQUEST_TEST.getOauthParameters();
  assertEquals("test_consumer_key", oauthParameters.get("oauth_consumer_key"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddInvalidOAuthParameter() {
  REQUEST_TEST.addOAuthParameter("invalid_key", "invalid_value");
 }

 @Test
 public void testAddScopeParameter() {
  REQUEST_TEST.addOAuthParameter("scope", "scope_test");
  Map<String, String> oauthParameters = REQUEST_TEST.getOauthParameters();
  assertEquals("scope_test", oauthParameters.get("scope"));
 }

 @Test
 public void testGetOAuthParameters() {
  REQUEST_TEST.addOAuthParameter("oauth_consumer_key", "test_consumer_key");
  REQUEST_TEST.addOAuthParameter("oauth_nonce", "test_nonce");
  REQUEST_TEST.addOAuthParameter("oauth_signature", "test_signature");
  REQUEST_TEST.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
  REQUEST_TEST.addOAuthParameter("oauth_timestamp", "test_timestamp");
  REQUEST_TEST.addOAuthParameter("oauth_token", "test_token");
  REQUEST_TEST.addOAuthParameter("oauth_version", "1.0");
  Map<String, String> oauthParameters = REQUEST_TEST.getOauthParameters();
  assertTrue(oauthParameters.containsKey("oauth_consumer_key"));
  assertTrue(oauthParameters.containsKey("oauth_nonce"));
  assertTrue(oauthParameters.containsKey("oauth_signature"));
  assertTrue(oauthParameters.containsKey("oauth_signature_method"));
  assertTrue(oauthParameters.containsKey("oauth_timestamp"));
  assertTrue(oauthParameters.containsKey("oauth_token"));
  assertTrue(oauthParameters.containsKey("oauth_version"));
 }

}