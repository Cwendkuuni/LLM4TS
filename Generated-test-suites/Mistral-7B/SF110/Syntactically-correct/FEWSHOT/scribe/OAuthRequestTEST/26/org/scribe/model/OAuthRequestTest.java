package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestWithNoOAuthParameters() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertTrue(oauthParameters.isEmpty());
    }

    @Test
    public void testAddingOAuthParameterWithValidKey() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "consumer_key_value");
        Map<String, String> oauthParameters = request.getOAuthParameters();
        assertEquals("consumer_key_value", oauthParameters.get("oauth_consumer_key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddingOAuthParameterWithInvalidKey() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("invalid_key", "value");
    }

    @Test
    public void testAddingOAuthParameterWithScopeKey() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("scope", "scope_value");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals("scope_value", oauthParameters.get("scope"));
    }

    @Test
    public void testGettingOAuthParametersWithNoParameters() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertTrue(oauthParameters.isEmpty());
    }

    @Test
    public void testGettingOAuthParametersWithMultipleParameters() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "consumer_key_value");
        request.addOAuthParameter("oauth_nonce", "nonce_value");
        request.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
        request.addOAuthParameter("oauth_timestamp", "timestamp_value");
        request.addOAuthParameter("oauth_token", "token_value");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals(5, oauthParameters.size());
        assertTrue(oauthParameters.containsKey("oauth_consumer_key"));
        assertTrue(oauthParameters.containsKey("oauth_nonce"));
        assertTrue(oauthParameters.containsKey("oauth_signature_method"));
        assertTrue(oauthParameters.containsKey("oauth_timestamp"));
        assertTrue(oauthParameters.containsKey("oauth_token"));
    }

    @Test
    public void testToStringWithNoOAuthParameters() {
        OAuthRequest request = new OAuthRequest(Request.Verbs.POST, "http://example.com");
        assertEquals("@OAuthRequest(POST, http://example.com)", request.toString());
    }

    @Test
    public void testToStringWithMultipleOAuthParameters() {
        OAuthRequest request = new OAuthRequest(Request.Verbs.POST, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "consumer_key_value");
        request.addOAuthParameter("oauth_nonce", "nonce_value");
        request.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
        request.addOAuthParameter("oauth_timestamp", "timestamp_value");
        request.addOAuthParameter("oauth_token", "token_value");
        assertEquals("@OAuthRequest(POST, http://example.com) [oauth_consumer_key=consumer_key_value, oauth_nonce=nonce_value, oauth_signature_method=HMAC-SHA1, oauth_timestamp=timestamp_value, oauth_token=token_value]", request.toString());
    }
}