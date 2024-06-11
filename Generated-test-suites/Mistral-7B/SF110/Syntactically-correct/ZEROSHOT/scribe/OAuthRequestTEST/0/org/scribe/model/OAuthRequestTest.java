package org.scribe.model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.scribe.model.OAuthRequest;

public class OAuthRequestTest {

	@Test
	public void testOAuthRequestConstruction() {
		OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
		assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
		assertNull(request.getOauthParameters());
	}

	@Test
	public void testAddOAuthParameter() {
		OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
		request.addOAuthParameter("oauth_consumer_key", "abc123");
		Map<String, String> oauthParameters = request.getOauthParameters();
		assertEquals("abc123", oauthParameters.get("oauth_consumer_key"));
		request.addOAuthParameter("scope", "read write");
		assertEquals("read write", oauthParameters.get("scope"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddInvalidOAuthParameterKey() {
		OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
		request.addOAuthParameter("invalid_key", "value");
	}

	@Test
	public void testGetOAuthParameters() {
		OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
		request.addOAuthParameter("oauth_consumer_key", "abc123");
		request.addOAuthParameter("oauth_nonce", "def456");
		request.addOAuthParameter("oauth_signature", "ghi789");
		request.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
		request.addOAuthParameter("oauth_timestamp", "1234567890");
		request.addOAuthParameter("oauth_token", "jkl1011");
		Map<String, String> oauthParameters = request.getOauthParameters();
		assertEquals(6, oauthParameters.size());
		assertTrue(Arrays.asList(oauthParameters.keySet().toArray()).containsAll(Arrays.asList("oauth_consumer_key", "oauth_nonce", "oauth_signature", "oauth_signature_method", "oauth_timestamp", "oauth_token")));
	}

	@Test
	public void testToString() {
		OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
		request.addOAuthParameter("oauth_consumer_key", "abc123");
		request.addOAuthParameter("oauth_nonce", "def456");
		request.addOAuthParameter("oauth_signature", "ghi789");
		request.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
		request.addOAuthParameter("oauth_timestamp", "1234567890");
		request.addOAuthParameter("oauth_token", "jkl1011");
		assertTrue(request.toString().contains("@OAuthRequest(GET, http://example.com)"));
		assertTrue(request.toString().contains("oauth_consumer_key=abc123"));
		assertTrue(request.toString().contains("oauth_nonce=def456"));
		assertTrue(request.toString().contains("oauth_signature=ghi789"));
		assertTrue(request.toString().contains("oauth_signature_method=HMAC-SHA1"));
		assertTrue(request.toString().contains("oauth_timestamp=1234567890"));
		assertTrue(request.toString().contains("oauth_token=jkl1011"));
	}
}