package org.scribe.model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class OAuthRequestTest {

 private OAuthRequest oauthRequest;

 @Before
 public void setUp() {
 oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 }

 @Test
 public void testOAuthRequestWithValidParameters() {
 Map<String, String> expectedOAuthParameters = new HashMap<String, String>();
 expectedOATHParameters.put("oauth_consumer_key", "testConsumerKey");
 expectedOATHParameters.put("oauth_nonce", "testNonce");
 expectedOATHParameters.put("oauth_signature", "testSignature");
 expectedOATHParameters.put("oauth_signature_method", "testSignatureMethod");
 expectedOATHParameters.put("oauth_timestamp", "testTimestamp");
 expectedOATHParameters.put("oauth_token", "testToken");
 expectedOATHParameters.put("oauth_version", "testVersion");

 oauthRequest.addOAuthParameter("consumer_key", "testConsumerKey");
 oauthRequest.addOAuthParameter("nonce", "testNonce");
 oauthRequest.addOAuthParameter("signature", "testSignature");
 oauthRequest.addOAuthParameter("signature_method", "testSignatureMethod");
 oauthRequest.addOAuthParameter("timestamp", "testTimestamp");
 oauthRequest.addOAuthParameter("token", "testToken");
 oauthRequest.addOAuthParameter("version", "testVersion");

 Map<String, String> actualOAuthParameters = oauthRequest.getOauthParameters();
 assertEquals(expectedOAuthParameters, actualOAuthParameters);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOAuthParameterWithInvalidKey() {
 oauthRequest.addOAuthParameter("invalidKey", "testValue");
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testAddDuplicateOAuthParameter() {
 oauthRequest.addOAuthParameter("oauth_nonce", "testNonce1");
 oauthRequest.addOAuthParameter("oauth_nonce", "testNonce2");

 Map<String, String> actualOAuthParameters = oauthRequest.getOauthParameters();
 assertEquals(1, actualOAuthParameters.size());
 assertEquals("testNonce1", actualOAuthParameters.get("oauth_nonce"));
 }

 @Test
 public void testToString() {
 assertEquals("@OAuthRequest(GET, http://example.com)", oauthRequest.toString());
 }

}