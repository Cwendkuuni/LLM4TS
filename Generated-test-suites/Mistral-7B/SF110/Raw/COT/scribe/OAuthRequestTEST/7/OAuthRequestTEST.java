package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class OAuthRequestTest {

 private OAuthRequest oAuthRequest;

 @Before
 public void setUp() {
 oAuthRequest = new OAuthRequest(Verb.GET, "https://api.example.com/");
 }

 // Test constructor
 @Test
 public void testOAuthRequestConstruction() {
 assertEquals("GET", oAuthRequest.getVerb());
 assertEquals("https://api.example.com/", oAuthRequest.getUrl());
 assertTrue(oAuthRequest.getOauthParameters() instanceof Map);
 }

 // Test addOAuthParameter method
 @Test
 public void testAddOAuthParameter() {
 oAuthRequest.addOAuthParameter("consumer_key", "test_key");
 oAuthRequest.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
 oAuthRequest.addOAuthParameter("scope", "read write");
 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertEquals("test_key", oauthParameters.get("consumer_key"));
 assertEquals("HMAC-SHA1", oauthParameters.get("oauth_signature_method"));
 assertEquals("read write", oauthParameters.get("scope"));
 }

 // Test checkKey method
 @Test(expected = IllegalArgumentException.class)
 public void testCheckKeyIllegalArgumentException() {
 oAuthRequest.checkKey("invalid_key");
 }

 @Test
 public void testCheckKeyValidKey() {
 assertEquals("oauth_signature", oAuthRequest.checkKey("oauth_signature"));
 }

 @Test
 public void testCheckKeyScope() {
 assertEquals("scope", oAuthRequest.checkKey("scope"));
 }

 // Test getOauthParameters method
 @Test
 public void testGetOauthParametersEmptyMap() {
 assertTrue(oAuthRequest.getOauthParameters().isEmpty());
 }

 // Test toString method
 @Test
 public void testToString() {
 assertEquals("@OAuthRequest(GET, https://api.example.com/)", oAuthRequest.toString());
 }

 // Test addOAuthParameter with existing key
 @Test(expected = IllegalStateException.class)
 public void testAddOAuthParameterWithExistingKey() {
 oAuthRequest.addOAuthParameter("oauth_nonce", "test_nonce");
 oAuthRequest.addOAuthParameter("oauth_nonce", "another_test_nonce");
 }

 // Test addOAuthParameter with null key or value
 @Test(expected = NullPointerException.class)
 public void testAddOAuthParameterNullKey() {
 oAuthRequest.addOAuthParameter(null, "test_value");
 }

 @Test(expected = NullPointerException.class)
 public void testAddOAuthParameterNullValue() {
 oAuthRequest.addOAuthParameter("oauth_token", null);
 }
}