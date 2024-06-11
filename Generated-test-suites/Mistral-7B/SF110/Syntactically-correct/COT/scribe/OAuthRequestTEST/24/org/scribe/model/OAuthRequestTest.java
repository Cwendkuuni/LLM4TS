package org.scribe.model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.scribe.model.OAuthRequest;

public class OAuthRequestTest {

 private OAuthRequest request;

 @Test
 public void testOAuthRequestConstruction() {
 request = new OAuthRequest(Request.Verb.GET, "http://example.com");
 assertNotNull(request);
 assertSame(Request.Verb.GET, request.getVerb());
 assertSame("http://example.com", request.getUrl());
 assertNotNull(request.getOauthParameters());
 assertEmpty(request.getOauthParameters());
 }

 @Test
 public void testAddOAuthParameter() {
 request = new OAuthRequest(Request.Verb.GET, "http://example.com");
 request.addOAuthParameter("oauth_consumer_key", "consumerKeyValue");
 request.addOAuthParameter("oauth_token", "tokenValue");
 request.addOAuthParameter("scope", "scopeValue");

 Map<String, String> expectedOAuthParameters = new HashMap<>();
 expectedOAuthParameters.put("oauth_consumer_key", "consumerKeyValue");
 expectedOAuthParameters.put("oauth_token", "tokenValue");
 expectedOauthParameters.put("scope", "scopeValue");

 Map<String, String> actualOAuthParameters = request.getOauthParameters();
 assertEquals(expectedOAuthParameters, actualOAuthParameters);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNonOAuthParameter() {
 request = new OAuthRequest(Request.Verb.GET, "http://example.com");
 request.addOAuthParameter("non_oauth_parameter", "value");
 fail("IllegalArgumentException should be thrown but was not");
 }

 @Test
 public void testCheckKey() {
 assertSame("oauth_parameter", request.checkKey("oauth_parameter"));
 assertSame("scope", request.checkKey("scope"));
 assertThrows(IllegalArgumentException.class, () -> request.checkKey("non_oauth_parameter"));
 }

 @Test
 public void testGetOauthParametersEmpty() {
 request = new OAuthRequest(Request.Verb.GET, "http://example.com");
 Map<String, String> actualOAuthParameters = request.getOauthParameters();
 assertEmpty(actualOAuthParameters);
 }

 @Test
 public void testGetOauthParametersWithParameters() {
 request = new OAuthRequest(Request.Verb.GET, "http://example.com");
 request.addOAuthParameter("oauth_consumer_key", "consumerKeyValue");
 request.addOAuthParameter("oauth_token", "tokenValue");
 request.addOAuthParameter("scope", "scopeValue");

 Map<String, String> actualOAuthParameters = request.getOauthParameters();
 Map<String, String> expectedOAuthParameters = new HashMap<>();
 expectedOAuthParameters.put("oauth_consumer_key", "consumerKeyValue");
 expectedOAuthParameters.put("oauth_token", "tokenValue");
 expectedOAuthParameters.put("scope", "scopeValue");

 assertEquals(expectedOAuthParameters, actualOAuthParameters);
 }

 @Test
 public void testToString() {
 request = new OAuthRequest(Request.Verb.GET, "http://example.com");
 request.addOAuthParameter("oauth_consumer_key", "consumerKeyValue");
 request.addOAuthParameter("oauth_token", "tokenValue");
 request.addOAuthParameter("scope", "scopeValue");

 String expectedToString = String.format("@OAuthRequest(GET, %s)", "http://example.com");
 String actualToString = request.toString();
 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testAddDuplicateOAuthParameter() {
 request = new OAuthRequest(Request.Verb.GET, "http://example.com");
 request.addOAuthParameter("oauth_consumer_key", "consumerKeyValue");
 request.addOAuthParameter("oauth_consumer_key", "anotherValue");

 Map<String, String> actualOAuthParameters = request.getOauthParameters();
 Map<String, String> expectedOAuthParameters = new HashMap<>();
 expectedOAuthParameters.put("oauth_consumer_key", "consumerKeyValue");

 assertEquals(expectedOAuthParameters, actualOAuthParameters);
 }

 @Test
 public void testGetEmptyOAuthParametersCopy() {
 request = new OAuthRequest(Request.Verb.GET, "http://example.com");
 Map<String, String> actualOAuthParameters = request.getOauthParameters();
 actualOAuthParameters.clear();

 Map<String, String> expectedOAuthParameters = new HashMap<>(request.getOauthParameters());
 expectedOAuthParameters.clear();

 assertEquals(expectedOAuthParameters, actualOAuthParameters);
 }

 @Test
 public void testGetNonEmptyOAuthParametersCopy() {
 request = new OAuthRequest(Request.Verb.GET, "http://example.com");
 request.addOAuthParameter("oauth_consumer_key", "consumerKeyValue");
 request.addOAuthParameter("oauth_token", "tokenValue");
 request.addOAuthParameter("scope", "scopeValue");

 Map<String, String> actualOAuthParameters = request.getOauthParameters();
 Map<String, String> expectedOAuthParameters = new HashMap<>(request.getOauthParameters());

 assertNotSame(actualOAuthParameters, expectedOAuthParameters);
 assertEquals(expectedOAuthParameters, actualOAuthParameters);
 }

 @Test
 public void testGetOauthParametersNull() {
 request = new OAuthRequest(Request.Verb.GET, "http://example.com");
 request.setOauthParameters(null);

 Map<String, String> expectedOAuthParameters = Collections.emptyMap();
 Map<String, String> actualOAuthParameters = request.getOauthParameters();

 assertEquals(expectedOAuthParameters, actualOAuthParameters);
 }

 @Test
 public void testGetOauthParametersConcurrentModification() {
 request = new OAuthRequest(Request.Verb.GET, "http://example.com");
 final Map<String, String> originalOAuthParameters = request.getOauthParameters();
 Thread thread1 = new Thread(() -> {
 request.addOAuthParameter("key1", "value1");
 });
 Thread thread2 = new Thread(() -> {
 request.addOAuthParameter("key2", "value2");
 });

 thread1.start();
 thread2.start();
 try {
 thread1.join();
 thread2.join();
 } catch (InterruptedException e) {
 throw new RuntimeException(e);
 }

 Map<String, String> expectedOAuthParameters = new HashMap<>(originalOAuthParameters);
 expectedOAuthParameters.put("key1", "value1");
 expectedOAuthParameters.put("key2", "value2");

 Map<String, String> actualOAuthParameters = request.getOauthParameters();
 assertEquals(expectedOAuthParameters, actualOAuthParameters);
 }
}