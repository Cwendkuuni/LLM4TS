package org.scribe.model;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.scribe.model.OAuthRequest;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestConstruction() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        assertNotNull(request);
        assertEquals(OAuthRequest.Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
        assertNull(request.getOauthParameters());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_token", "token123");
        Map<String, String> oauthParams = request.getOauthParameters();
        assertEquals("token123", oauthParams.get("oauth_token"));
        assertEquals(1, oauthParams.size());
    }

    @Test
    public void testAddDuplicateOAuthParameter() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_token", "token123");
        request.addOAuthParameter("oauth_token", "token456");
        Map<String, String> oauthParams = request.getOauthParameters();
        assertEquals("token123", oauthParams.get("oauth_token"));
        assertEquals(1, oauthParams.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNonOAuthParameter() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        request.addOAuthParameter("non_oauth_param", "value");
    }

    @Test
    public void testAddScopeParameter() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        request.addOAuthParameter("scope", "read write");
        Map<String, String> oauthParams = request.getOauthParameters();
        assertEquals("read write", oauthParams.get("scope"));
        assertEquals(1, oauthParams.size());
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_token", "token123");
        assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
    }

    @Test
    public void testGetOAuthParametersEmpty() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        Map<String, String> oauthParams = request.getOauthParameters();
        assertNull(oauthParams);
    }

    @Test
    public void testGetOAuthParametersNonEmpty() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_token", "token123");
        Map<String, String> oauthParams = request.getOauthParameters();
        assertNotNull(oauthParams);
        assertEquals("token123", oauthParams.get("oauth_token"));
        assertEquals(1, oauthParams.size());
    }
}