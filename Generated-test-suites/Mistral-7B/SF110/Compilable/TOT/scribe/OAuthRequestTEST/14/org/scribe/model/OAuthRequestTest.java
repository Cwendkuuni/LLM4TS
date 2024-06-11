package org.scribe.model;
import org.junit.Before;
import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Request;
import org.scribe.model.Verb;

import java.util.*;

import static org.junit.Assert.*;

public class OAuthRequestTest {

 private OAuthRequest oAuthRequest;

 @Before
 public void setUp() {
 oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 }

 @Test
 public void testAddOAuthParameter() {
 // Add valid OAuth parameter
 oAuthRequest.addOAuthParameter("oauth_token", "token123");
 Map<String, String> expectedOAuthParameters = new HashMap<String, String>();
 expectedOAuthParameters.put("oauth_token", "token123");
 assertEquals(expectedOAuthParameters, oAuthRequest.getOauthParameters());

 // Add duplicate OAuth parameter
 oAuthRequest.addOAuthParameter("oauth_token", "token456");
 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertEquals(1, oauthParameters.size());

 // Add invalid OAuth parameter
 try {
 oAuthRequest.addOAuthParameter("invalid_key", "value");
 fail("Should have thrown IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetOauthParametersEmpty() {
 assertEquals(0, oAuthRequest.getOauthParameters().size());
 }

 @Test
 public void testGetOauthParametersWithParameters() {
 oAuthRequest.addOAuthParameter("oauth_token", "token123");
 oAuthRequest.addOAuthParameter("oauth_nonce", "nonce123");
 Map<String, String> expectedOAuthParameters = new HashMap<String, String>();
 expectedOAuthParameters.put("oauth_token", "token123");
 expectedOAuthParameters.put("oauth_nonce", "nonce123");
 assertEquals(expectedOAuthParameters, oAuthRequest.getOauthParameters());
 }

 @Test
 public void testToString() {
 String expectedToString = "@OAuthRequest(GET, http://example.com)";
 assertEquals(expectedToString, oAuthRequest.toString());
 }
}