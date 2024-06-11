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
	public void testOAuthRequestConstruction() {
		String url = "http://example.com";
		Verb verb = Verb.GET;
		OAuthRequest request = new OAuthRequest(verb, url);
		assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
		assertEquals(Collections.emptyMap(), request.getOauthParameters());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddOAuthParameterWithInvalidKey() {
		String url = "http://example.com";
		Verb verb = Verb.GET;
		OAuthRequest request = new OAuthRequest(verb, url);
		request.addOAuthParameter("invalidKey", "value");
	}

	@Test
	public void testAddAndGetOAuthParameters() {
		String url = "http://example.com";
		Verb verb = Verb.GET;
		OAuthRequest request = new OAuthRequest(verb, url);

		request.addOAuthParameter("oauth_consumer_key", "testConsumerKey");
		request.addOAuthParameter("oauth_nonce", "testNonce");
		request.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
		request.addOAuthParameter("oauth_timestamp", "1234567890");
		request.addOAuthParameter("oauth_token", "testToken");
		request.addOAuthParameter("oauth_version", "1.0");

		Map<String, String> expectedOAuthParameters = new HashMap<>();
		expectedOAuthParameters.put("oauth_consumer_key", "testConsumerKey");
		expectedOAuthParameters.put("oauth_nonce", "testNonce");
		expectedOAuthParameters.put("oauth_signature_method", "HMAC-SHA1");
		expectedOAuthParameters.put("oauth_timestamp", "1234567890");
		expectedOAuthParameters.put("oauth_token", "testToken");
		expectedOauthParameters.put("oauth_version", "1.0");

		assertEquals(expectedOAuthParameters, request.getOauthParameters());
	}

	@Test
	public void testCheckKey() {
		String validOAuthKey = "oauth_consumer_key";
		String validScope = "scope";
		assertEquals(validOAuthKey, new OAuthRequest().checkKey(validOAuthKey));
		assertEquals(validScope, new OAuthRequest().checkKey(validScope));

		String invalidKey = "invalidKey";
		try {
			new OAuthRequest().checkKey(invalidKey);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testToString() {
		String url = "http://example.com";
		Verb verb = Verb.GET;
		String consumerKey = "testConsumerKey";
		String nonce = "testNonce";
		String signatureMethod = "HMAC-SHA1";
		long timestamp = 1234567890L;
		String token = "testToken";

		OAuthRequest request = new OAuthRequest(verb, url);
		request.addOAuthParameter("oauth_consumer_key", consumerKey);
		request.addOAuthParameter("oauth_nonce", nonce);
		request.addOAuthParameter("oauth_signature_method", signatureMethod);
		request.addOAuthParameter("oauth_timestamp", String.valueOf(timestamp));
		request.addOAuthParameter("oauth_token", token);

		String expectedToString = String.format("@OAuthRequest(GET, http://example.com, %s)", request.getOauthParameters().toString());
		assertEquals(expectedToString, request.toString());
	}
}