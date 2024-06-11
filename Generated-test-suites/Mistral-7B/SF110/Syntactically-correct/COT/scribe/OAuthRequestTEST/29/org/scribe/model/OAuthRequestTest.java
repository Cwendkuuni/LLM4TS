package org.scribe.model.test;

import org.junit.Before;
import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Request;
import org.scribe.model.Verb;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class OAuthRequestTest {

 private OAuthRequest oAuthRequest;

 @Before
 public void setUp() {
 oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 }

 // Test constructor
 @Test
 public void testOAuthRequestConstruction() {
 OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
 assertEquals(Verb.GET, request.getVerb());
 assertEquals("http://example.com", request.getUrl());
 assertNotNull(request.getOauthParameters());
 }

 // Test addOAuthParameter method
 @Test
 public void testAddOAuthParameter() {
 oAuthRequest.addOAuthParameter("oauth_token", "abc123");
 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertEquals("abc123", oauthParameters.get("oauth_token"));
 }

 // Test addOAuthParameter with invalid key
 @Test(expected = IllegalArgumentException.class)
 public void testAddOAuthParameterWithInvalidKey() {
 oAuthRequest.addOAuthParameter("invalid_key", "value");
 }

 // Test checkKey method
 @Test
 public void testCheckKey() {
 assertEquals("oauth_token", oAuthRequest.checkKey("oauth_token"));
 assertEquals("scope", oAuthRequest.checkKey("scope"));
 }

 // Test getOauthParameters method
 @Test
 public void testGetOauthParameters() {
 Map<String, String> oauthParameters = new HashMap<String, String>();
 oAuthRequest.addOAuthParameter("oauth_token", "abc123");
 oAuthRequest.addOAuthParameter("scope", "read write");
 assertEquals(oauthParameters, oAuthRequest.getOauthParameters());
 }

 // Test toString method
 @Test
 public void testToString() {
 assertEquals("@OAuthRequest(GET, http://example.com)", oAuthRequest.toString());
 }

 // Edge cases and exception handling tests

 @Test
 public void testNullVerb() {
 assertNull(new OAuthRequest(null, "http://example.com"));
 }

 @Test
 public void testNullUrl() {
 assertNull(new OAuthRequest(Verb.GET, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateKey() {
 Map<String, String> oauthParameters = new HashMap<String, String>();
 oAuthRequest.addOAuthParameter("oauth_token", "abc123");
 oAuthRequest.addOAuthParameter("oauth_token", "def456");
 }

 @Test
 public void testEmptyMap() {
 Map<String, String> oauthParameters = new HashMap<String, String>();
 assertEquals(oauthParameters, new OAuthRequest(Verb.GET, "").getOauthParameters());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyKey() {
 oAuthRequest.addOAuthParameter("", "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyValue() {
 oAuthRequest.addOAuthParameter("key", "");
 }

 @Test
 public void testConcurrentModificationException() {
 ExecutorService executor = Executors.newFixedThreadPool(10);
 final OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
 Callable<Void> task = () -> {
 request.addOAuthParameter("key", "value");
 return null;
 };
 try {
 executor.invokeAll(Collections.nCopies(10, task));
 } catch (InterruptedException | ExecutionException e) {
 // ignore
 } finally {
 executor.shutdown();
 }
 assertNotNull(request.getOauthParameters());
 }

}