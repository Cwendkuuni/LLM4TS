package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.OAuthRequest;

import java.util.*;

public class OAuthRequestTest {

    // Test case for constructor
    @Test
    public void testOAuthRequestConstruction() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        assertNotNull(request);
        assertEquals(Request.Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
        assertTrue(request.getOauthParameters() instanceof Map);
        assertTrue(request.getOauthParameters().isEmpty());
    }

    // Test case for addOAuthParameter method
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("key", "value");
        Map<String, String> params = request.getOauthParameters();
        assertEquals("value", params.get("key"));
    }

    // Test case for checkKey method
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalid() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.checkKey("invalidKey");
    }

    // Test case for checkKey valid key
    @Test
    public void testCheckKeyValid() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.checkKey("oauth_key");
    }

    // Test case for checkKey valid scope
    @Test
    public void testCheckKeyValidScope() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.checkKey("scope");
    }

    // Test case for getOauthParameters method
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("key1", "value1");
        request.addOAuthParameter("key2", "value2");
        Map<String, String> params = request.getOauthParameters();
        assertEquals(2, params.size());
        assertEquals("value1", params.get("key1"));
        assertEquals("value2", params.get("key2"));
    }

    // Test case for addOAuthParameter with existing key
    @Test
    public void testAddOAuthParameterExistingKey() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("key1", "value1");
        request.addOAuthParameter("key1", "value2");
        Map<String, String> params = request.getOauthParameters();
        assertEquals(1, params.size());
        assertEquals("value2", params.get("key1"));
    }

    // Test case for toString method
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("key1", "value1");
        String expected = String.format("@OAuthRequest(GET, http://example.com)", new Object[]{Request.Verb.GET, "http://example.com"});
        assertEquals(expected, request.toString());
    }

    // Test case for addOAuthParameter with null key
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterNullKey() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter(null, "value");
    }

    // Test case for addOAuthParameter with null value
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterNullValue() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("key", null);
    }
}