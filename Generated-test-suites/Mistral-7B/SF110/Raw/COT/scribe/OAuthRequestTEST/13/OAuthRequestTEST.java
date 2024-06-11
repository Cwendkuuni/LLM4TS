package org.scribe.model.test;

import static org.junit.Assert.*;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Request;
import org.scribe.model.Verb;

import java.util.*;

public class OAuthRequestTest {

 private OAuthRequest oAuthRequest;

 @ org.junit.Before
 public void setUp() {
 oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 }

 // Testing constructor and getters
 @ org.junit.Test
 public void testOAuthRequestConstruction() {
 OAuthRequest oauthRequest = new OAuthRequest(Verb.POST, "http://example.com");
 assertEquals(Verb.POST, oauthRequest.getVerb());
 assertEquals("http://example.com", oauthRequest.getUrl());
 assertEquals(new HashMap<>(), oauthRequest.getOauthParameters());
 }

 @ org.junit.Test
 public void testGetOauthParameters() {
 Map<String, String> oauthParameters = new HashMap<>();
 oAuthRequest.addOAuthParameter("key1", "value1");
 oAuthRequest.addOAuthParameter("key2", "value2");
 assertEquals(oauthParameters, oAuthRequest.getOauthParameters());
 }

 // Basic testing for addOAuthParameter method
 @ org.junit.Test
 public void testAddOAuthParameter() {
 oAuthRequest.addOAuthParameter("key1", "value1");
 oAuthRequest.addOAuthParameter("key2", "value2");
 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertTrue(oauthParameters.containsKey("key1") && oauthParameters.containsValue("value1"));
 assertTrue(oauthParameters.containsKey("key2") && oauthParameters.containsValue("value2"));
 }

 // Edge cases and exceptions testing for addOAuthParameter method
 @ org.junit.Test(expected = IllegalArgumentException.class)
 public void testAddOAuthParameterWithInvalidKey() {
 oAuthRequest.addOAuthParameter("invalidKey", "value");
 }

 @ org.junit.Test(expected = IllegalArgumentException.class)
 public void testAddOAuthParameterWithEmptyValue() {
 oAuthRequest.addOAuthParameter("key", "");
 }

 @ org.junit.Test(expected = IllegalArgumentException.class)
 public void testAddOAuthParameterWithEmptyKeyOrValue() {
 oAuthRequest.addOAuthParameter("", "value");
 oAuthRequest.addOAuthParameter("key", "");
 }

 @ org.junit.Test(expected = IllegalArgumentException.class)
 public void testAddOAuthParameterWithDuplicateKey() {
 oAuthRequest.addOAuthParameter("key1", "value1");
 oAuthRequest.addOAuthParameter("key1", "value2");
 }

 // Testing checkKey method
 @ org.junit.Test
 public void testCheckKey() {
 String key1 = "oauth_key1";
 String key2 = "scope";
 String key3 = "invalid_key";
 String key4 = "oauth_key2";

 assertEquals(key1, oAuthRequest.checkKey(key1));
 assertEquals(key2, oAuthRequest.checkKey(key2));
 throw new AssertionError("Expected IllegalArgumentException for 'key3'");
 }

 @ org.junit.Test(expected = IllegalArgumentException.class)
 public void testCheckKeyWithInvalidKey() {
 oAuthRequest.checkKey(key3);
 }

 @ org.junit.Test
 public void testToString() {
 String expected = "@OAuthRequest(GET, http://example.com)";
 assertEquals(expected, oAuthRequest.toString());
 }

}