package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

import org.scribe.model.OAuthRequest;

import java.util.*;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestInstance() {
        OAuthRequest oAuthRequest = new OAuthRequest(OAuthRequest.Verb.GET, "https://api.example.com");
        assertNotNull(oAuthRequest);
        assertEquals(OAuthRequest.Verb.GET, oAuthRequest.getVerb());
        assertEquals("https://api.example.com", oAuthRequest.getUrl());
        assertNull(oAuthRequest.getOauthParameters());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest oAuthRequest = new OAuthRequest(OAuthRequest.Verb.GET, "https://api.example.com");
        oAuthRequest.addOAuthParameter("oauth_token", "abc123");
        Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
        assertEquals("abc123", oauthParameters.get("oauth_token"));
        assertEquals(1, oauthParameters.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddIllegalKeyParameter() {
        OAuthRequest oAuthRequest = new OAuthRequest(OAuthRequest.Verb.GET, "https://api.example.com");
        oAuthRequest.addOAuthParameter("illegalKey", "value");
    }

    @Test
    public void testAddScopeParameter() {
        OAuthRequest oAuthRequest = new OAuthRequest(OAuthRequest.Verb.GET, "https://api.example.com");
        oAuthRequest.addOAuthParameter("scope", "read write");
        Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
        assertEquals("read write", oauthParameters.get("scope"));
    }

    @Test
    public void testGetOAuthParameters() {
        OAuthRequest oAuthRequest = new OAuthRequest(OAuthRequest.Verb.GET, "https://api.example.com");
        oAuthRequest.addOAuthParameter("oauth_token", "abc123");
        oAuthRequest.addOAuthParameter("oauth_consumer_key", "efg456");
        oAuthRequest.addOAuthParameter("scope", "read write");
        Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
        assertEquals(3, oauthParameters.size());
        assertEquals("abc123", oauthParameters.get("oauth_token"));
        assertEquals("efg456", oauthParameters.get("oauth_consumer_key"));
        assertEquals("read write", oauthParameters.get("scope"));
    }

    @Test
    public void testToString() {
        OAuthRequest oAuthRequest = new OAuthRequest(OAuthRequest.Verb.GET, "https://api.example.com");
        oAuthRequest.addOAuthParameter("oauth_token", "abc123");
        oAuthRequest.addOAuthParameter("oauth_consumer_key", "efg456");
        oAuthRequest.addOAuthParameter("scope", "read write");
        String oAuthRequestString = oAuthRequest.toString();
        String expectedOAuthRequestString = "@OAuthRequest(GET, https://api.example.com)";
        assertNotSame(expectedOAuthRequestString, oAuthRequestString);
        assertTrue(oAuthRequestString.startsWith("@OAuthRequest("));
    }
}