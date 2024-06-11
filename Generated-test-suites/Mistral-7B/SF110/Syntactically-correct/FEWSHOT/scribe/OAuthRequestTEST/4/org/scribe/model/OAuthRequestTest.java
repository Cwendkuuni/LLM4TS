package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

import org.scribe.model.OAuthRequest;

import java.util.*;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestConstruction() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "https://api.example.com/resource");
        assertNotNull(request);
        assertEquals(OAuthRequest.Verb.GET, request.getVerb());
        assertEquals("https://api.example.com/resource", request.getUrl());
        assertTrue(request.getOauthParameters() instanceof Map);
        assertTrue(request.getOauthParameters().isEmpty());
    }

    @Test
    public void testAddOAuthParameterAndGetOAuthParameters() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "https://api.example.com/resource");
        request.addOAuthParameter("oauth_token", "abcd1234");
        request.addOAuthParameter("oauth_token_secret", "efg123456");
        request.addOAuthParameter("oauth_consumer_key", "xyz123456");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals("abcd1234", oauthParameters.get("oauth_token"));
        assertEquals("efg123456", oauthParameters.get("oauth_token_secret"));
        assertEquals("xyz123456", oauthParameters.get("oauth_consumer_key"));
        assertEquals(3, oauthParameters.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalidOAuthParameter() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "https://api.example.com/resource");
        request.addOAuthParameter("invalid_parameter", "value");
    }

    @Test
    public void testCheckKey() {
        assertEquals("oauth_parameter", OAuthRequest.this.checkKey("oauth_parameter"));
        assertEquals("scope", OAuthRequest.this.checkKey("scope"));
        assertThrows(IllegalArgumentException.class, () -> OAuthRequest.this.checkKey("invalid_parameter"));
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "https://api.example.com/resource");
        request.addOAuthParameter("oauth_token", "abcd1234");
        request.addOAuthParameter("oauth_token_secret", "efg123456");
        request.addOAuthParameter("oauth_consumer_key", "xyz123456");
        String expected = String.format("@OAuthRequest(GET, https://api.example.com/resource)", "GET", "https://api.example.com/resource");
        String result = request.toString();
        assertEquals(expected, result);
    }
}