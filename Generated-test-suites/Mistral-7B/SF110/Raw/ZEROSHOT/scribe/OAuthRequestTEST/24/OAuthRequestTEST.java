package org.scribe.model;

import static org.junit.Assert.*;

import java.util.*;

import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.lib.legacy.MockObjectInstance;
import org.jmock.lib.legacy.Test;
import org.jmock.lib.legacy.TestImple;
import org.junit.Before;
import org.junit.Test;

public class OAuthRequestTest {

 private OAuthRequest oauthRequest;
 private Map<String, String> oauthParameters;
 private Verb verb;
 private String url;

 @Before
 public void setUp() {
 ClassImposteriser.install();
 oauthRequest = new TestImple() {
 public OAuthRequest(Verb verb, String url) {
 super();
 this.verb = verb;
 this.url = url;
 this.oauthParameters = new HashMap<String, String>();
 }

 @Override
 public String toString() {
 return String.format("@OAuthRequest(%s, %s)", getVerb(), getUrl());
 }

 public Map<String, String> getOauthParameters() {
 return this.oauthParameters;
 }
 };

 verb = MockObjectInstance.createMock(Verb.class);
 oauthParameters = new HashMap<String, String>();
 }

 @Test
 public void testOAuthRequestWithValidArguments() {
 OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
 request.addOAuthParameter("oauth_consumer_key", "ABCDEFG123456");
 request.addOAuthParameter("oauth_nonce", "12345");
 request.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
 request.addOAuthParameter("oauth_timestamp", "1234567890");
 request.addOAuthParameter("oauth_token", "XYZ1234567890");
 request.addOAuthParameter("scope", "scope1 scope2");

 assertEquals("ABCDEFG123456", request.getOauthParameters().get("oauth_consumer_key"));
 assertEquals("12345", request.getOauthParameters().get("oauth_nonce"));
 assertEquals("HMAC-SHA1", request.getOauthParameters().get("oauth_signature_method"));
 assertEquals("1234567890", request.getOauthParameters().get("oauth_timestamp"));
 assertEquals("XYZ1234567890", request.getOauthParameters().get("oauth_token"));
 assertEquals("scope1 scope2", request.getOauthParameters().get("scope"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddingInvalidKey() {
 oauthRequest.addOAuthParameter("invalid_key", "value");
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testGettingOAuthParameters() {
 oauthRequest.addOAuthParameter("oauth_consumer_key", "ABCDEFG123456");
 oauthRequest.addOAuthParameter("oauth_nonce", "12345");
 oauthRequest.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
 oauthRequest.addOAuthParameter("oauth_timestamp", "1234567890");
 oauthRequest.addOAuthParameter("oauth_token", "XYZ1234567890");
 oauthRequest.addOAuthParameter("scope", "scope1 scope2");

 Map<String, String> oauthParams = oauthRequest.getOauthParameters();
 assertEquals(6, oauthParams.size());
 assertEquals("ABCDEFG123456", oauthParams.get("oauth_consumer_key"));
 assertEquals("12345", oauthParams.get("oauth_nonce"));
 assertEquals("HMAC-SHA1", oauthParams.get("oauth_signature_method"));
 assertEquals("1234567890", oauthParams.get("oauth_timestamp"));
 assertEquals("XYZ1234567890", oauthParams.get("oauth_token"));
 assertEquals("scope1 scope2", oauthParams.get("scope"));
 }
}