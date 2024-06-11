package org.scribe.model;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class OAuthRequestTest {

 private static final String BASE_URL = "http://example.com";
 private static final String VERB = "GET";

 @Test
 public void testConstructOAuthRequest() {
  OAuthRequest request = new OAuthRequest(VERB, BASE_URL);
  assertEquals(VERB, request.getVerb());
  assertEquals(BASE_URL, request.getUrl());
  assertNull(request.getOauthParameters());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckKeyWithInvalidParamName() {
  OAuthRequest request = new OAuthRequest(VERB, BASE_URL);
  request.addOAuthParameter("invalid", "param");
 }

 @Test
 public void testCheckKeyWithValidParamName() {
  OAuthRequest request = new OAuthRequest(VERB, BASE_URL);
  request.addOAuthParameter("oauth_token", "testToken");
  assertSame("oauth_token", request.checkKey("oauth_token"));
  request.addOAuthParameter("scope", "read write");
  assertSame("scope", request.checkKey("scope"));
 }

 @Test
 public void testAddOAuthParameter() {
  OAuthRequest request = new OAuthRequest(VERB, BASE_URL);
  request.addOAuthParameter("oauth_token", "testToken");
  Map<String, String> oauthParameters = request.getOauthParameters();
  assertEquals("testToken", oauthParameters.get("oauth_token"));
 }

 @Test
 public void testGetOauthParameters() {
  OAuthRequest request = new OAuthRequest(VERB, BASE_URL);
  request.addOAuthParameter("oauth_token", "testToken");
  request.addOAuthParameter("oauth_consumer_key", "testConsumerKey");
  Map<String, String> oauthParameters = request.getOauthParameters();
  assertEquals(2, oauthParameters.size());
  assertEquals("testToken", oauthParameters.get("oauth_token"));
  assertEquals("testConsumerKey", oauthParameters.get("oauth_consumer_key"));
 }

 @Test
 public void testToString() {
  OAuthRequest request = new OAuthRequest(VERB, BASE_URL);
  request.addOAuthParameter("oauth_token", "testToken");
  request.addOAuthParameter("oauth_consumer_key", "testConsumerKey");
  String expected = String.format("@OAuthRequest(GET, %s)", BASE_URL);
  String actual = request.toString();
  assertEquals(expected, actual);
 }
}