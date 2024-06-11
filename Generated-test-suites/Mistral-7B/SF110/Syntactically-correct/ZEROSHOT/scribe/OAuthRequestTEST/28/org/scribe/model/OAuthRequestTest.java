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
	public void testOAuthRequestInstance() {
		OAuthRequest request = new OAuthRequest(OAuthVerb.GET, "http://example.com");
		assertNotNull(request);
		assertEquals(OAuthRequest.class, request.getClass());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckKeyIllegalArgumentException() {
		OAuthRequest request = new OAuthRequest(OAuthVerb.GET, "http://example.com");
		request.checkKey("invalidKey");
	}

	@Test
	public void testCheckKeyValidKey() {
		OAuthRequest request = new OAuthRequest(OAuthVerb.GET, "http://example.com");
		assertEquals("invalidKey", request.checkKey("invalidKey"));
	}

	@Test
	public void testCheckKeyOAuthParameter() {
		OAuthRequest request = new OAuthRequest(OAuthVerb.GET, "http://example.com");
		assertEquals("oauth_nonce", request.checkKey("oauth_nonce"));
		assertEquals("scope", request.checkKey("scope"));
	}

	@Test
	public void testAddOAuthParameter() {
		OAuthRequest request = new OAuthRequest(OAuthVerb.GET, "http://example.com");
		Map<String, String> oauthParameters = new HashMap<String, String>();
		request.addOAuthParameter("oauth_token", "tokenValue");
		assertEquals("tokenValue", request.getOauthParameters().get("oauth_token"));
	}

	@Test
	public void testGetOauthParametersEmptyMap() {
		OAuthRequest request = new OAuthRequest(OAuthVerb.GET, "http://example.com");
		assertEquals(Collections.emptyMap(), request.getOauthParameters());
	}

	@Test
	public void testGetOauthParametersMapNotEmpty() {
		OAuthRequest request = new OAuthRequest(OAuthVerb.GET, "http://example.com");
		request.addOAuthParameter("oauth_token", "tokenValue");
		request.addOAuthParameter("oauth_consumer_key", "consumerKeyValue");
		Map<String, String> oauthParameters = request.getOauthParameters();
		assertEquals("tokenValue", oauthParameters.get("oauth_token"));
		assertEquals("consumerKeyValue", oauthParameters.get("oauth_consumer_key"));
		assertTrue(oauthParameters.containsKey("oauth_token"));
		assertTrue(oauthParameters.containsKey("oauth_consumer_key"));
	}

	@Test
	public void testToString() {
		OAuthRequest request = new OAuthRequest(OAuthVerb.GET, "http://example.com");
		assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
	}
}