package org.scribe.model;

import static org.junit.Assert.*;
import org.jukesoft.common.collect.MultimapUtils;
import org.junit.Test;
import org.scribe.model.OAuthRequest;

import java.util.*;
import java.util.concurrent.*;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestWithNoOAuthParameters() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
        assertEquals(0, request.getOauthParameters().size());
    }

    @Test
    public void testAddingOAuthParameterWithValidKey() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "abc123");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals("abc123", oauthParameters.get("oauth_consumer_key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddingOAuthParameterWithInvalidKey() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        request.addOAuthParameter("invalidKey", "value");
    }

    @Test
    public void testAddingOAuthParameterWithScopeKey() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        request.addOAuthParameter("scope", "read write");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals("read write", oauthParameters.get("scope"));
    }

    @Test
    public void testAddingMultipleOAuthParameters() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "abc123");
        request.addOAuthParameter("scope", "read write");
        request.addOAuthParameter("oauth_nonce", "xyz789");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals("abc123", oauthParameters.get("oauth_consumer_key"));
        assertEquals("read write", oauthParameters.get("scope"));
        assertEquals("xyz789", oauthParameters.get("oauth_nonce"));
    }

    @Test
    public void testToStringWithOAuthParameters() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "abc123");
        request.addOAuthParameter("scope", "read write");
        request.addOAuthParameter("oauth_nonce", "xyz789");
        String expected = String.format("@OAuthRequest(GET, http://example.com) %s", MultimapUtils.toString(request.getOauthParameters()));
        assertEquals(expected, request.toString());
    }
}