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
        request.addOAuthParameter("oauth_token", "abc123");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals("abc123", oauthParameters.get("oauth_token"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddingOAuthParameterWithInvalidKey() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("invalid_key", "value");
    }

    @Test
    public void testAddingScopeParameter() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("scope", "read write");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals("read write", oauthParameters.get("scope"));
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_token", "abc123");
        request.addOAuthParameter("scope", "read write");
        String expected = String.format("@OAuthRequest(GET, http://example.com)", "");
        String actual = request.toString();
        assertEquals(expected, actual);
    }
}