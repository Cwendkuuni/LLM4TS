package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

public class OAuthRequestTest {
    @Test
    public void testOAuthRequestCreation() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        assertNull(request.getOauthParameters());
        assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "abc123");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals("abc123", oauthParameters.get("oauth_consumer_key"));
        assertEquals(1, oauthParameters.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterInvalidKey() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("invalid_key", "value");
    }

    @Test
    public void testAddOAuthParameterWithScope() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("scope", "read write");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals("read write", oauthParameters.get("scope"));
        assertEquals(1, oauthParameters.size());
    }

    @Test
    public void testGetOAuthParameters() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "abc123");
        request.addOAuthParameter("oauth_token", "def456");
        request.addOAuthParameter("scope", "read write");

        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals(3, oauthParameters.size());
        assertEquals("abc123", oauthParameters.get("oauth_consumer_key"));
        assertEquals("def456", oauthParameters.get("oauth_token"));
        assertEquals("read write", oauthParameters.get("scope"));
    }

    @Test
    public void testToStringWithOAuthParameters() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "abc123");
        request.addOAuthParameter("oauth_token", "def456");
        request.addOAuthParameter("scope", "read write");

        assertEquals("@OAuthRequest(GET, http://example.com, {oauth_consumer_key=abc123, oauth_token=def456, scope=read write})", request.toString());
    }
}