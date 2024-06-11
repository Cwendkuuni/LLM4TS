package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestCreation() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertNotNull(request);
        assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
        assertNull(request.getOauthParameters());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_token", "abc123");
        Map<String, String> oauthParams = request.getOauthParameters();
        assertEquals("abc123", oauthParams.get("oauth_token"));
        assertEquals(1, oauthParams.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNonOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("foo", "bar");
    }

    @Test
    public void testAddOAuthParameterWithExistingKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_token", "abc123");
        request.addOAuthParameter("oauth_token", "def456");
        Map<String, String> oauthParams = request.getOauthParameters();
        assertEquals("def456", oauthParams.get("oauth_token"));
    }

    @Test
    public void testGetOAuthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_token", "abc123");
        request.addOAuthParameter("scope", "read write");
        Map<String, String> oauthParams = request.getOauthParameters();
        assertEquals(2, oauthParams.size());
        assertEquals("read write", oauthParams.get("scope"));
    }

    @Test
    public void testToStringWithOAuthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_token", "abc123");
        request.addOAuthParameter("scope", "read write");
        assertEquals("@OAuthRequest(GET, http://example.com) [oauth_token=abc123, scope=read write]", request.toString());
    }
}