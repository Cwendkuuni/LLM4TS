package org.scribe.model;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.*;

import org.junit.Test;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestConstruction() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/api");
        assertNull(request.getOauthParameters());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/api");
        request.addOAuthParameter("oauth_token", "abc123");
        Map<String, String> params = request.getOauthParameters();
        assertEquals("abc123", params.get("oauth_token"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalidOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/api");
        request.addOAuthParameter("invalid", "value");
    }

    @Test
    public void testCheckKey() {
        assertEquals("oauth_parameter", OAuthRequest.checkKey("oauth_parameter"));
        assertEquals("scope", OAuthRequest.checkKey("scope"));
        assertThrows(IllegalArgumentException.class, () -> OAuthRequest.checkKey("invalid_parameter"));
    }

    @Test
    public void testGetOAuthParametersEmpty() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/api");
        Map<String, String> params = request.getOauthParameters();
        assertTrue(params.isEmpty());
    }

    @Test
    public void testGetOAuthParametersWithParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/api");
        request.addOAuthParameter("oauth_token", "abc123");
        request.addOAuthParameter("oauth_token_secret", "def456");
        request.addOAuthParameter("scope", "read write");
        Map<String, String> params = request.getOauthParameters();
        assertEquals("abc123", params.get("oauth_token"));
        assertEquals("def456", params.get("oauth_token_secret"));
        assertEquals("read write", params.get("scope"));
    }
}