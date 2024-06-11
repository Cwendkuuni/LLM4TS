package org.scribe.model;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.scribe.model.OAuthRequest;

import java.util.*;
import java.util.concurrent.*;

public class OAuthRequestTest {

 private static final String OAUTH_PREFIX = "oauth_";

 @Test
 public void testOAuthRequestConstructor() {
 OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.example.com/");
 assertEquals("GET", request.getVerb().toString());
 assertEquals("https://api.example.com/", request.getUrl());
 assertNotNull(request.getOauthParameters());
 assertFalse(request.getOauthParameters().isEmpty());
 }

 @Test
 public void testAddOAuthParameter() {
 OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.example.com/");
 request.addOAuthParameter("oauth_consumer_key", "ABCDEFG12345");
 request.addOAuthParameter("oauth_nonce", "1234567890");
 request.addOAuthParameter("oauth_signature", "1234567890123456789012345678901234567890");
 request.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
 request.addOAuthParameter("oauth_timestamp", "1234567890");
 request.addOAuthParameter("oauth_token", "ABCDEFG12345");
 request.addOAuthParameter("oauth_version", "1.0");

 Map<String, String> params = request.getOauthParameters();
 assertNotNull(params);
 assertFalse(params.isEmpty());
 assertEquals("ABCDEFG12345", params.get("oauth_consumer_key"));
 assertEquals("1234567890", params.get("oauth_nonce"));
 assertEquals("1234567890123456789012345678901234567890", params.get("oauth_signature"));
 assertEquals("HMAC-SHA1", params.get("oauth_signature_method"));
 assertEquals("1234567890", params.get("oauth_timestamp"));
 assertEquals("ABCDEFG12345", params.get("oauth_token"));
 assertEquals("1.0", params.get("oauth_version"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOAuthParameterWithInvalidKey() {
 OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.example.com/");
 request.addOAuthParameter("invalid_key", "ABCDEFG12345");
 fail("IllegalArgumentException should be thrown");
 }

 @Test
 public void testCheckKey() {
 assertEquals("oauth_key", OAuthRequest.this.checkKey("oauth_key"));
 assertEquals("scope", OAuthRequest.this.checkKey("scope"));
 assertEquals("oauth_key", OAuthRequest.this.checkKey("oauth_consumer_key"));
 assertEquals("oauth_key", OAuthRequest.this.checkKey("oauth_token"));
 assertEquals("oauth_key", OAuthRequest.this.checkKey("oauth_signature"));
 assertEquals("oauth_key", OAuthRequest.this.checkKey("oauth_nonce"));
 assertEquals("oauth_key", OAuthRequest.this.checkKey("oauth_version"));

 try {
 OAuthRequest.this.checkKey("invalid_key");
 fail("IllegalArgumentException should be thrown");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testGetOauthParameters() {
 OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.example.com/");
 request.addOAuthParameter("oauth_consumer_key", "ABCDEFG12345");
 request.addOAuthParameter("oauth_nonce", "1234567890");
 request.addOAuthParameter("oauth_signature", "1234567890123456789012345678901234567890");
 request.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
 request.addOauthParameter("oauth_timestamp", String.valueOf(new DateTime().getMillis() / 1000L));
 request.addOAuthParameter("oauth_token", "ABCDEFG12345");
 request.addOAuthParameter("oauth_version", "1.0");

 Map<String, String> params = request.getOauthParameters();
 assertNotNull(params);
 assertFalse(params.isEmpty());
 }

 @Test
 public void testToString() {
 OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.example.com/");
 request.addOAuthParameter("oauth_consumer_key", "ABCDEFG12345");
 request.addOAuthParameter("oauth_nonce", "1234567890");
 request.addOAuthParameter("oauth_signature", "1234567890123456789012345678901234567890");
 request.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
 request.addOAuthParameter("oauth_timestamp", String.valueOf(new DateTime().getMillis() / 1000L));
 request.addOAuthParameter("oauth_token", "ABCDEFG12345");
 request.addOAuthParameter("oauth_version", "1.0");

 String expected = String.format("@OAuthRequest(GET, %s)", ISODateTimeFormat.dateTimeNoMillis().print(new DateTime()));
 String actual = request.toString();
 assertEquals(expected, actual);
 }
}