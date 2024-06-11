package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestInstance() {
        OAuthRequest oauthRequest = new OAuthRequest(Request.Verb.GET, "http://example.com/api");
        assertNotNull(oauthRequest);
        assertEquals("@OAuthRequest(GET, http://example.com/api)", oauthRequest.toString());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest oauthRequest = new OAuthRequest(Request.Verb.GET, "http://example.com/api");
        oauthRequest.addOAuthParameter("oauth_token", "abc123");
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertEquals("abc123", oauthParameters.get("oauth_token"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterInvalidKey() {
        OAuthRequest oauthRequest = new OAuthRequest(Request.Verb.GET, "http://example.com/api");
        oauthRequest.addOAuthParameter("invalid_key", "value");
    }

    @Test
    public void testAddMultipleOAuthParameters() {
        OAuthRequest oauthRequest = new OAuthRequest(Request.Verb.GET, "http://example.com/api");
        oauthRequest.addOAuthParameter("oauth_token", "abc123");
        oauthRequest.addOAuthParameter("oauth_token_secret", "def456");
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertEquals("abc123", oauthParameters.get("oauth_token"));
        assertEquals("def456", oauthParameters.get("oauth_token_secret"));
    }

    @Test
    public void testGetOAuthParametersEmpty() {
        OAuthRequest oauthRequest = new OAuthRequest(Request.Verb.GET, "http://example.com/api");
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertTrue(oauthParameters.isEmpty());
    }

    @Test
    public void testGetOAuthParametersWithParameters() {
        OAuthRequest oauthRequest = new OAuthRequest(Request.Verb.GET, "http://example.com/api");
        oauthRequest.addOAuthParameter("oauth_token", "abc123");
        oauthRequest.addOAuthParameter("oauth_token_secret", "def456");
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertEquals(2, oauthParameters.size());
    }
}