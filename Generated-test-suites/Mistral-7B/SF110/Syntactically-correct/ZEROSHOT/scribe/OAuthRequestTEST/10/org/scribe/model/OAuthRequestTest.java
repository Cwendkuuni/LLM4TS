package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class OAuthRequestTest {

 private static final String VALID_URL = "https://api.example.com/resource";
 private static final Verb VALID_VERB = Verb.GET;
 private static final String VALID_KEY = "oauth_token";
 private static final String VALID_VALUE = "abc123";

 @Test
 public void testOAuthRequestConstruction() {
 OAuthRequest oauthRequest = new OAuthRequest(VALID_VERB, VALID_URL);
 assertNotNull(oauthRequest);
 }

 @Test
 public void testGetVerb() {
 OAuthRequest oauthRequest = new OAuthRequest(VALID_VERB, VALID_URL);
 assertEquals(VALID_VERB, oauthRequest.getVerb());
 }

 @Test
 public void testGetUrl() {
 OAuthRequest oauthRequest = new OAuthRequest(VALID_VERB, VALID_URL);
 assertEquals(VALID_URL, oauthRequest.getUrl());
 }

 @Test
 public void testToString() {
 OAuthRequest oauthRequest = new OAuthRequest(VALID_VERB, VALID_URL);
 assertEquals("@OAuthRequest(GET, https://api.example.com/resource)", oauthRequest.toString());
 }

 @Test
 public void testAddOAuthParameter() {
 OAuthRequest oauthRequest = new OAuthRequest(VALID_VERB, VALID_URL);
 oauthRequest.addOAuthParameter(VALID_KEY, VALID_VALUE);
 Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
 assertEquals(VALID_VALUE, oauthParameters.get(VALID_KEY));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddInvalidParameterKey() {
 OAuthRequest oauthRequest = new OAuthRequest(VALID_VERB, VALID_URL);
 oauthRequest.addOAuthParameter("invalidKey", VALID_VALUE);
 }

 @Test
 public void testGetOauthParameters() {
 OAuthRequest oauthRequest = new OAuthRequest(VALID_VERB, VALID_URL);
 oauthRequest.addOAuthParameter(VALID_KEY, VALID_VALUE);
 Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
 assertNotNull(oauthParameters);
 assertEquals(1, oauthParameters.size());
 }

 @Test
 public void testGetOauthParametersEmpty() {
 OAuthRequest oauthRequest = new OAuthRequest(VALID_VERB, VALID_URL);
 Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
 assertNotNull(oauthParameters);
 assertEquals(0, oauthParameters.size());
 }

 @Test
 public void testCheckKey() {
 assertEquals("oauth_token", OAuthRequest.checkKey("oauth_token"));
 assertEquals("scope", OAuthRequest.checkKey("scope"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckInvalidKey() {
 assertThrows(IllegalArgumentException.class, () -> OAuthRequest.checkKey("invalidKey"));
 }

 @Test
 public void testCloneWithOAuthParameters() {
 OAuthRequest originalRequest = new OAuthRequest(VALID_VERB, VALID_URL);
 originalRequest.addOAuthParameter(VALID_KEY, VALID_VALUE);
 OAuthRequest clonedRequest = (OAuthRequest) originalRequest.clone();
 Map<String, String> originalOauthParameters = originalRequest.getOauthParameters();
 Map<String, String> clonedOauthParameters = clonedRequest.getOauthParameters();
 assertEquals(originalOauthParameters, clonedOauthParameters);
 }

 @Test
 public void testCloneWithoutOAuthParameters() {
 OAuthRequest originalRequest = new OAuthRequest(VALID_VERB, VALID_URL);
 OAuthRequest clonedRequest = (OAuthRequest) originalRequest.clone();
 Map<String, String> originalOauthParameters = originalRequest.getOauthParameters();
 Map<String, String> clonedOauthParameters = clonedRequest.getOauthParameters();
 assertNull(originalOauthParameters);
 assertNull(clonedOauthParameters);
 }
}