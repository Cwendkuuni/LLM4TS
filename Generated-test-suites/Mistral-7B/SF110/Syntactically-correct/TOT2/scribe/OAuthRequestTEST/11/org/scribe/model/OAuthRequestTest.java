package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("key", "value");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals("value", oauthParameters.get("key"));
    }

    // Edge case: Check if the method can handle empty key or value
    @Test
    public void testAddEmptyOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("", "");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertNull(oauthParameters.get(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("invalidKey", "value");
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("key", "value");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals("value", oauthParameters.get("key"));
    }

    @Test
    public void testGetOauthParametersEmpty() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertTrue(oauthParameters.isEmpty());
    }
}