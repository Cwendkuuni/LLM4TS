package org.scribe.model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import static org.mockito.Mockito.*;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

public class OAuthRequestTest {

 private OAuthRequest oauthRequest;

 @Test
 public void testOAuthRequestconstruction() {
 oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 assertEquals("GET", oauthRequest.getVerb().name());
 assertEquals("http://example.com", oauthRequest.getUrl());
 assertEquals(new HashMap<>(), oauthRequest.getOauthParameters());
 }

 @Test
 public void testAddingOAuthParameter() {
 oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 oauthRequest.addOAuthParameter("oauth_token", "test_token");
 oauthRequest.addOAuthParameter("scope", "read write");

 Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
 assertEquals("test_token", oauthParameters.get("oauth_token"));
 assertEquals("read write", oauthParameters.get("scope"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddingInvalidOAuthParameterKey() {
 oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 oauthRequest.addOAuthParameter("invalid_key", "test_value");
 fail();
 }

 @Test
 public void testGettingOAuthParameters() {
 oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 oauthRequest.addOAuthParameter("oauth_token", "test_token");
 oauthRequest.addOAuthParameter("scope", "read write");

 Map<String, String> expectedOauthParameters = new HashMap<>();
 expectedOauthParameters.put("oauth_token", "test_token");
 expectedOauthParameters.put("scope", "read write");

 assertEquals(expectedOauthParameters, oauthRequest.getOauthParameters());
 }

 @Test
 public void testToString() {
 oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 oauthRequest.addOAuthParameter("oauth_token", "test_token");
 oauthRequest.addOAuthParameter("scope", "read write");

 String expectedToString = "@OAuthRequest(GET, http://example.com)";
 String toString = oauthRequest.toString();
 assertEquals(expectedToString, toString);
 }

 @Test
 public void testCheckKey() {
 String validKey = "oauth_token";
 String invalidKey = "invalid_key";

 OAuthRequest oauthRequest = mock(OAuthRequest.class);
 when(oauthRequest.checkKey(validKey)).thenReturn(validKey);
 when(oauthRequest.checkKey(invalidKey)).thenThrow(new IllegalArgumentException("Invalid key"));

 String expectedValidKey = "oauth_token";
 String actualValidKey = oauthRequest.checkKey(validKey);
 assertEquals(expectedValidKey, actualValidKey);

 String expectedInvalidKey = null;
 try {
 String actualInvalidKey = oauthRequest.checkKey(invalidKey);
 } catch (IllegalArgumentException e) {
 expectedInvalidKey = e.getMessage();
 }
 assertEquals("Invalid key", expectedInvalidKey);
 }
}