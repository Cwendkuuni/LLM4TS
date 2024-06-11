package org.scribe.model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.scribe.model.OAuthRequest;

public class OAuthRequestTest {

	@Test
	public void testOAuthRequestCreation() {
		String url = "https://api.example.com/resource";
		OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, url);
		assertEquals(OAuthRequest.Verb.GET, request.getVerb());
		assertEquals(url, request.getUrl());
		assertEquals(Collections.emptyMap(), request.getOauthParameters());
	}

	@Test
	public void testOAuthRequestAddParameter() {
		String url = "https://api.example.com/resource";
		OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, url);
		Map<String, String> oauthParameters = new HashMap<String, String>();
		oauthParameters.put("oauth_token", "token1");
		oauthParameters.put("oauth_consumer_key", "consumerKey1");
		request.addOAuthParameter("oauth_token", "token1");
		request.addOAuthParameter("oauth_consumer_key", "consumerKey1");
		assertEquals(oauthParameters, request.getOauthParameters());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOAuthRequestAddNonOAuthParameter() {
		String url = "https://api.example.com/resource";
		OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, url);
		request.addOAuthParameter("non_oauth_param", "value");
	}

	@Test
	public void testOAuthRequestAddScopeParameter() {
		String url = "https://api.example.com/resource";
		OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, url);
		Map<String, String> oauthParameters = new HashMap<String, String>();
		oauthParameters.put("oauth_token", "token1");
		oauthParameters.put("scope", "scope1 scope2");
		request.addOAuthParameter("scope", "scope1 scope2");
		assertEquals(oauthParameters, request.getOauthParameters());
	}

	@Test
	public void testOAuthRequestToString() {
		String url = "https://api.example.com/resource";
		OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, url);
		Map<String, String> oauthParameters = new HashMap<String, String>();
		oauthParameters.put("oauth_token", "token1");
		oauthParameters.put("oauth_consumer_key", "consumerKey1");
		request.addOAuthParameter("oauth_token", "token1");
		request.addOAuthParameter("oauth_consumer_key", "consumerKey1");
		String expected = String.format("@OAuthRequest(GET, %s)", url);
		String actual = request.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void testCheckKey() {
		assertEquals("oauth_token", OAuthRequest.checkKey("oauth_token"));
		assertEquals("scope", OAuthRequest.checkKey("scope"));
		try {
			OAuthRequest.checkKey("non_oauth_param");
			fail();
		} catch (IllegalArgumentException e) {
			// expected
			assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
		}
		try {
			OAuthRequest.checkKey("oauth_non_param");
			fail();
		} catch (IllegalArgumentException e) {
			// expected
			assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
		}
	}
}