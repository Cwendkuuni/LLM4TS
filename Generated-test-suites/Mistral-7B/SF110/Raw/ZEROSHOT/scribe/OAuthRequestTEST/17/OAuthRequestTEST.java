package org.scribe.model;

import org.junit.Before;
import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OAuthRequestTest {

 private OAuthRequest oAuthRequest;

 @Before
 public void setUp() {
 oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 }

 @Test
 public void testConstruction() {
 assertEquals("GET", oAuthRequest.getVerb());
 assertEquals("http://example.com", oAuthRequest.getUrl());
 assertNotNull(oAuthRequest.getOauthParameters());
 }

 @Test
 public void testAddOAuthParameter() {
 oAuthRequest.addOAuthParameter("oauth_consumer_key", "test_consumer_key");
 oAuthRequest.addOAuthParameter("scope", "read write");

 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertEquals("test_consumer_key", oauthParameters.get("oauth_consumer_key"));
 assertEquals("read write", oauthParameters.get("scope"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddInvalidOAuthParameterName() {
 oAuthRequest.addOAuthParameter("invalid_key", "test_value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddInvalidOAuthParameterNameWithSpace() {
 oAuthRequest.addOAuthParameter("invalid key", "test_value");
 }

 @Test
 public void testGetOAuthParameters() {
 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertNotNull(oauthParameters);
 assertFalse(oauthParameters.isEmpty());
 }

 @Test
 public void testToString() {
 String expected = String.format("@OAuthRequest(GET, %s)", oAuthRequest.getUrl());
 assertEquals(expected, oAuthRequest.toString());
 }

 @Test
 public void testConstructorWithNullVerb() {
 assertThrows(NullPointerException.class, () -> new OAuthRequest(null, "http://example.com"));
 }

 @Test
 public void testConstructorWithNullUrl() {
 assertThrows(NullPointerException.class, () -> new OAuthRequest(Verb.GET, null));
 }

 @Test
 public void testAddDuplicateOAuthParameter() {
 oAuthRequest.addOAuthParameter("oauth_consumer_key", "test_consumer_key");
 oAuthRequest.addOAuthParameter("oauth_consumer_key", "test_consumer_key_2");

 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertEquals("test_consumer_key", oauthParameters.get("oauth_consumer_key"));
 }

 @Test
 public void testGetOAuthParameter() {
 oAuthRequest.addOAuthParameter("oauth_consumer_key", "test_consumer_key");

 String consumerKey = oAuthRequest.getOAuthParameter("oauth_consumer_key");
 assertNotNull(consumerKey);
 assertEquals("test_consumer_key", consumerKey);
 }

 @Test
 public void testGetNullOAuthParameter() {
 String nullParameter = oAuthRequest.getOAuthParameter("invalid_key");
 assertNull(nullParameter);
 }

 @Test
 public void testRemoveOAuthParameter() {
 oAuthRequest.addOAuthParameter("oauth_consumer_key", "test_consumer_key");

 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 oAuthRequest.removeOAuthParameter("oauth_consumer_key");

 assertFalse(oauthParameters.containsKey("oauth_consumer_key"));
 }

 @Test
 public void testClearOAuthParameters() {
 oAuthRequest.addOAuthParameter("oauth_consumer_key", "test_consumer_key");

 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 oAuthRequest.clearOAuthParameters();

 assertTrue(oauthParameters.isEmpty());
 }
}