package org.scribe.model;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.scribe.model.OAuthRequest;

import org.junit.Test;

public class OAuthRequestTest {

	@Test
	public void testCreateOAuthRequest() {
		Verb verb = Verb.GET;
		String url = "http://example.com";
		OAuthRequest oAuthRequest = new OAuthRequest(verb, url);
		assertNotNull(oAuthRequest);
		assertEquals(verb, oAuthRequest.getVerb());
		assertEquals(url, oAuthRequest.getUrl());
		assertTrue(oAuthRequest.getOauthParameters() instanceof Map);
	}

	@Test
	public void testAddOAuthParameter() {
		Verb verb = Verb.GET;
		String url = "http://example.com";
		OAuthRequest oAuthRequest = new OAuthRequest(verb, url);
		String key = "oauth_token";
		String value = "test_token";
		oAuthRequest.addOAuthParameter(key, value);
		Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
		assertEquals(value, oauthParameters.get(key));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddNonOAuthParameter() {
		Verb verb = Verb.GET;
		String url = "http://example.com";
		OAuthRequest oAuthRequest = new OAuthRequest(verb, url);
		String key = "test_param";
		String value = "test_value";
		oAuthRequest.addOAuthParameter(key, value);
	}

	@Test
	public void testAddScopeParameter() {
		Verb verb = Verb.GET;
		String url = "http://example.com";
		OAuthRequest oAuthRequest = new OAuthRequest(verb, url);
		String key = "scope";
		String value = "read write";
		oAuthRequest.addOAuthParameter(key, value);
		Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
		assertEquals(value, oauthParameters.get(key));
	}

	@Test
	public void testToString() {
		Verb verb = Verb.GET;
		String url = "http://example.com";
		OAuthRequest oAuthRequest = new OAuthRequest(verb, url);
		String expected = String.format("@OAuthRequest(GET, %s)", url);
		String actual = oAuthRequest.toString();
		assertEquals(expected, actual);
	}
}