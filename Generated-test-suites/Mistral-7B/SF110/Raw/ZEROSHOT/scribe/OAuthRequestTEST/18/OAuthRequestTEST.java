package org.scribe.model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.scribe.model.OAuthRequest;

public class OAuthRequestTest {

	@Test
	public void testOAuthRequestCreation() {
		OAuthRequest request = new OAuthRequest(OAuthVerb.GET, "http://example.com");
		assertNull(request.getOauthParameters());
	}

	@Test
	public void testAddOAuthParameter() {
		OAuthRequest request = new OAuthRequest(OAuthVerb.GET, "http://example.com");
		request.addOAuthParameter("oauth_consumer_key", "abc123");
		Map<String, String> oauthParameters = request.getOauthParameters();
		assertEquals("abc123", oauthParameters.get("oauth_consumer_key"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddInvalidOAuthParameterKey() {
		OAuthRequest request = new OAuthRequest(OAuthVerb.GET, "http://example.com");
		request.addOAuthParameter("invalid_key", "value");
	}

	@Test
	public void testAddOAuthParameterWithSpace() {
		OAuthRequest request = new OAuthRequest(OAuthVerb.GET, "http://example.com");
		request.addOAuthParameter("oauth_consumer_key", "abc 123");
		Map<String, String> oauthParameters = request.getOauthParameters();
		assertEquals("abc 123", oauthParameters.get("oauth_consumer_key"));
	}

	@Test
	public void testAddOAuthParameterWithScope() {
		OAuthRequest request = new OAuthRequest(OAuthVerb.GET, "http://example.com");
		request.addOAuthParameter("scope", "read write");
		Map<String, String> oauthParameters = request.getOauthParameters();
		assertEquals("read write", oauthParameters.get("scope"));
	}

	@Test
	public void testAddMultipleOAuthParameters() {
		OAuthRequest request = new OAuthRequest(OAuthVerb.GET, "http://example.com");
		request.addOAuthParameter("oauth_consumer_key", "abc123");
		request.addOAuthParameter("oauth_nonce", "def456");
		request.addOAuthParameter("scope", "read write");

		Map<String, String> oauthParameters = request.getOauthParameters();
		assertEquals(3, oauthParameters.size());
		assertTrue(oauthParameters.containsKey("oauth_consumer_key"));
		assertTrue(oauthParameters.containsKey("oauth_nonce"));
		assertTrue(oauthParameters.containsKey("scope"));
	}

	@Test
	public void testToString() {
		OAuthRequest request = new OAuthRequest(OAuthVerb.GET, "http://example.com");
		request.addOAuthParameter("oauth_consumer_key", "abc123");
		request.addOAuthParameter("oauth_nonce", "def456");
		request.addOAuthParameter("scope", "read write");

		String toString = request.toString();
		String expected = String.format("@OAuthRequest(GET, %s)", "http://example.com");
		assertTrue(toString.startsWith(expected));

		String[] tokens = toString.split("\\(");
		String lastToken = tokens[tokens.length - 1].replaceAll("\\)", "").replaceAll(" ", "");
		String[] params = lastToken.split(", ");
		assertEquals(3, params.length);
		assertTrue(Arrays.asList(params).containsAll(Arrays.asList("oauth_consumer_key=abc123", "oauth_nonce=def456", "scope=read write")));
	}

	@Test
	public void testCheckKey() {
		assertEquals("oauth_consumer_key", new OAuthRequest().checkKey("oauth_consumer_key"));
		assertEquals("scope", new OAuthRequest().checkKey("scope"));
		assertThrows(IllegalArgumentException.class, () -> new OAuthRequest().checkKey("invalid_key"));
	}
}