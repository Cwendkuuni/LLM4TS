package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

import org.scribe.model.OAuthRequest;

import java.util.*;

public class OAuthRequestTest {
    @Test
    public void testOAuthRequestCreation() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "https://api.example.com/resource");
        assertNotNull(request);
        assertEquals(Request.Verb.GET, request.getVerb());
        assertEquals("https://api.example.com/resource", request.getUrl());
        assertNull(request.getOauthParameters());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "https://api.example.com/resource");
        request.addOAuthParameter("oauth_token", "abc123");
        Map<String, String> oauthParams = request.getOauthParameters();
        assertEquals("abc123", oauthParams.get("oauth_token"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalidOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "https://api.example.com/resource");
        request.addOAuthParameter("invalid_key", "value");
    }

    @Test
    public void testGetOAuthParameters() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "https://api.example.com/resource");
        request.addOAuthParameter("oauth_token", "abc123");
        request.addOAuthParameter("scope", "read write");
        Map<String, String> oauthParams = request.getOauthParameters();
        assertEquals("abc123", oauthParams.get("oauth_token"));
        assertEquals("read write", oauthParams.get("scope"));
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "https://api.example.com/resource");
        request.addOAuthParameter("oauth_token", "abc123");
        request.addOAuthParameter("scope", "read write");
        assertEquals("@OAuthRequest(GET, https://api.example.com/resource)", request.toString());
    }
}