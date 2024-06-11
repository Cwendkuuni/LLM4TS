package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

import org.scribe.model.OAuthRequest;

import java.util.HashMap;
import java.util.Map;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestConstruction() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "https://api.example.com/");
        assertNull(request.getOauthParameters());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "https://api.example.com/");
        request.addOAuthParameter("oauth_consumer_key", "abc123");
        Map<String, String> params = request.getOauthParameters();
        assertEquals("abc123", params.get("oauth_consumer_key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "https://api.example.com/");
        request.addOAuthParameter("invalidKey", "value");
    }

    @Test
    public void testGetOAuthParameters() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "https://api.example.com/");
        request.addOAuthParameter("oauth_consumer_key", "abc123");
        request.addOAuthParameter("oauth_token", "def456");
        Map<String, String> params = request.getOauthParameters();
        assertEquals(2, params.size());
        assertEquals("abc123", params.get("oauth_consumer_key"));
        assertEquals("def456", params.get("oauth_token"));
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "https://api.example.com/");
        request.addOAuthParameter("oauth_consumer_key", "abc123");
        request.addOauthParameter("scope", "read write");
        String expected = String.format("@OAuthRequest(GET, https://api.example.com/)", request.getVerb(), request.getUrl());
        assertEquals(expected, request.toString());
    }
}