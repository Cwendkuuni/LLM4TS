package org.scribe.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.*;

import static org.junit.Assert.*;

public class OAuthRequestTest {

 private OAuthRequest oAuthRequest;

 @Before
 public void setUp() {
 oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 }

 @After
 public void tearDown() {
 oAuthRequest = null;
 }

 @Test
 public void testOAuthRequestInstance() {
 OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 assertInstanceOf(OAuthRequest.class, oAuthRequest);
 }

 @Test
 public void testConstructor() {
 OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 assertEquals(Verb.GET, oAuthRequest.getVerb());
 assertEquals("http://example.com", oAuthRequest.getUrl());
 assertEquals(new HashMap<>(), oAuthRequest.getOauthParameters());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOAuthParameterWithInvalidKey() {
 oAuthRequest.addOAuthParameter("InvalidKey", "Value");
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testAddOAuthParameter() {
 oAuthRequest.addOAuthParameter("oauth_nonce", "nonceValue");
 oAuthRequest.addOAuthParameter("scope", "read write");
 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertEquals("nonceValue", oauthParameters.get("oauth_nonce"));
 assertEquals("read write", oauthParameters.get("scope"));
 }

 @Test
 public void testGetOauthParameters() {
 oAuthRequest.addOAuthParameter("oauth_nonce", "nonceValue");
 oAuthRequest.addOAuthParameter("scope", "read write");
 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertEquals(2, oauthParameters.size());
 }

 @Test
 public void testToString() {
 OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 oAuthRequest.addOAuthParameter("oauth_nonce", "nonceValue");
 oAuthRequest.addOAuthParameter("scope", "read write");
 String expected = "@OAuthRequest(GET, http://example.com)";
 String actual = oAuthRequest.toString();
 assertEquals(expected, actual);
 }
}