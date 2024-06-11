package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

import org.scribe.model.OAuthRequest;

import java.util.*;

public class OAuthRequestTest {
    @Test
    public void testOAuthRequestCreation() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        assertNotNull(request);
        assertEquals(OAuthRequest.Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
        assertTrue(request.getOauthParameters() instanceof Map);
        assertTrue(request.getOauthParameters().isEmpty());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "abc123");
        Map<String, String> params = request.getOauthParameters();
        assertEquals("abc123", params.get("oauth_consumer_key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNonOAuthParameter() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        request.addOAuthParameter("foo", "bar");
    }

    @Test
    public void testAddScopeParameter() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        request.addOAuthParameter("scope", "read write");
        Map<String, String> params = request.getOauthParameters();
        assertEquals("read write", params.get("scope"));
    }

    @Test
    public void testOAuthRequestToString() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "abc123");
        request.addOAuthParameter("oauth_token", "xyz789");
        Map<String, String> params = request.getOauthParameters();
        assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
        assertEquals("oauth_consumer_key=abc123&oauth_token=xyz789", params.toString());
    }

    @Test
    public void testOAuthRequestWithMultipleOAuthParameters() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "abc123");
        request.addOAuthParameter("oauth_token", "xyz789");
        request.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
        request.addOAuthParameter("oauth_timestamp", "1234567890");
        Map<String, String> params = request.getOauthParameters();
        assertEquals("abc123", params.get("oauth_consumer_key"));
        assertEquals("xyz789", params.get("oauth_token"));
        assertEquals("HMAC-SHA1", params.get("oauth_signature_method"));
        assertEquals("1234567890", params.get("oauth_timestamp"));
    }
}