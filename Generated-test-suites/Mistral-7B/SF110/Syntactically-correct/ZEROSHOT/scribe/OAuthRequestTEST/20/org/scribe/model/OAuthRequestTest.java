package org.test.scribe;

import static org.junit.Assert.*;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

import java.util.*;

public class OAuthRequestTest {

    @org.junit.Before
    public void setUp() {
        // Initialize test objects
    }

    @org.junit.Test
    public void testOAuthRequestConstruction() {
        // Test construction with valid parameters
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertNotNull(request);
        assertEquals("GET", request.getVerb().name());
        assertEquals("http://example.com", request.getUrl());
        assertTrue(request.getOauthParameters() instanceof Map);
        assertTrue(request.getOauthParameters().isEmpty());

        // Test construction with invalid verb
        try {
            OAuthRequest requestWithInvalidVerb = new OAuthRequest(null, "http://example.com");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Verb cannot be null", e.getMessage());
        }

        // Test construction with invalid URL
        try {
            OAuthRequest requestWithInvalidUrl = new OAuthRequest(Verb.GET, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("URL cannot be null", e.getMessage());
        }
    }

    @org.junit.Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_token", "abc123");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals("abc123", oauthParameters.get("oauth_token"));
        request.addOAuthParameter("scope", "read write");
        assertEquals("read write", oauthParameters.get("scope"));
        try {
            request.addOAuthParameter("invalid_key", "value");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }

    @org.junit.Test
    public void testCheckKey() {
        assertEquals("oauth_token", OAuthRequest.this.checkKey("oauth_token"));
        assertEquals("scope", OAuthRequest.this.checkKey("scope"));
        try {
            OAuthRequest.this.checkKey("invalid_key");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }

    @org.junit.Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
    }

    @org.junit.Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        Map<String, String> oauthParameters = new HashMap<>();
        oauthParameters.put("oauth_token", "abc123");
        oauthParameters.put("scope", "read write");
        request.setOauthParameters(oauthParameters);
        Map<String, String> actualOauthParameters = request.getOauthParameters();
        assertEquals(oauthParameters, actualOauthParameters);
    }

    @org.junit.Test
    public void testSetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        Map<String, String> oauthParameters = new HashMap<>();
        oauthParameters.put("oauth_token", "abc123");
        oauthParameters.put("scope", "read write");
        request.setOauthParameters(oauthParameters);
        Map<String, String> actualOauthParameters = request.getOauthParameters();
        assertEquals(oauthParameters, actualOauthParameters);
    }

    @org.junit.After
    public void tearDown() {
        // Clean up test objects
    }

}