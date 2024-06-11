package org.scribe.model;

import static org.junit.Assert.*;

import java.util.*;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

public class OAuthRequestTest {

 private OAuthRequest oAuthRequest;

 @Before
 public void setUp() {
 oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
 }

 @Test
 public void testOAuthRequestCreation() {
 OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
 assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
 }

 @Test
 public void testAddOAuthParameter() {
 oAuthRequest.addOAuthParameter("oauth_consumer_key", "abcd1234");
 oAuthRequest.addOAuthParameter("scope", "read write");
 Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
 assertEquals("abcd1234", oauthParameters.get("oauth_consumer_key"));
 assertEquals("read write", oauthParameters.get("scope"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddInvalidOAuthParameterKey() {
 oAuthRequest.addOAuthParameter("invalid_key", "value");
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testGetOauthParameters() {
 Map<String, String> oauthParameters = new HashMap<>();
 oAuthRequest.addOAuthParameter("oauth_consumer_key", "abcd1234");
 oAuthRequest.addOAuthParameter("scope", "read write");
 assertEquals(oauthParameters, oAuthRequest.getOauthParameters());
 }

 @Test
 public void testToString() {
 OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");
 request.addOAuthParameter("oauth_consumer_key", "abcd1234");
 request.addOAuthParameter("oauth_nonce", "12345");
 request.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
 request.addOAuthParameter("oauth_timestamp", "1234567890");
 request.addOAuthParameter("oauth_token", "efgh678901");
 request.addOAuthParameter("oauth_version", "1.0");
 assertEquals("@OAuthRequest(POST, http://example.com)[oauth_consumer_key=abcd1234, oauth_nonce=12345, oauth_signature_method=HMAC-SHA1, oauth_timestamp=1234567890, oauth_token=efgh678901, oauth_version=1.0]", request.toString());
 }
}