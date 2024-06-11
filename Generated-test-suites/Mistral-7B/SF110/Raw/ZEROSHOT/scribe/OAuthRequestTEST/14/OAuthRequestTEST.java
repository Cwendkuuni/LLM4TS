package org.scribe.model;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.scribe.model.OAuthRequest;

public class OAuthRequestTest {

	@Test
	public void testOAuthRequestWithValidVerbAndUrl() {
		OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
		assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOAuthRequestWithInvalidVerb() {
		OAuthRequest request = new OAuthRequest(null, "http://example.com");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOAuthRequestWithInvalidUrl() {
		OAuthRequest request = new OAuthRequest(Request.Verb.GET, null);
	}

	@Test
	public void testAddOAuthParameter() {
		OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
		request.addOAuthParameter("oauth_consumer_key", "consumerKey");
		Map<String, String> oauthParameters = request.getOauthParameters();
		assertEquals("consumerKey", oauthParameters.get("oauth_consumer_key"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddOAuthParameterWithInvalidKey() {
		OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
		request.addOAuthParameter("invalidKey", "value");
	}

	@Test
	public void testAddOAuthParameterWithMultipleParameters() {
		OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
		request.addOAuthParameter("oauth_consumer_key", "consumerKey");
		request.addOAuthParameter("oauth_token", "token");
		request.addOAuthParameter("scope", "read write");
		Map<String, String> oauthParameters = request.getOauthParameters();
		assertEquals("consumerKey", oauthParameters.get("oauth_consumer_key"));
		assertEquals("token", oauthParameters.get("oauth_token"));
		assertEquals("read write", oauthParameters.get("scope"));
	}

	@Test
	public void testToStringWithOAuthParameters() {
		OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
		request.addOAuthParameter("oauth_consumer_key", "consumerKey");
		request.addOAuthParameter("oauth_token", "token");
		request.addOAuthParameter("scope", "read write");
		assertEquals("@OAuthRequest(GET, http://example.com?oauth_consumer_key=consumerKey&oauth_token=token&scope=read%20write)", request.toString());
	}

	@Test
	public void testGetOAuthParameters() {
		OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
		request.addOAuthParameter("oauth_consumer_key", "consumerKey");
		request.addOAuthParameter("oauth_token", "token");
		request.addOAuthParameter("scope", "read write");
		Map<String, String> oauthParameters = request.getOauthParameters();
		assertEquals(3, oauthParameters.size());
		assertTrue(oauthParameters.containsKey("oauth_consumer_key"));
		assertTrue(oauthParameters.containsKey("oauth_token"));
		assertTrue(oauthParameters.containsKey("scope"));
	}
}