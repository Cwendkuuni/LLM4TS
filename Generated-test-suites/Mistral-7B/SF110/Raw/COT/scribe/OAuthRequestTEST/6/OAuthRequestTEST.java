package org.scribe.model.test;

import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

import java.util.*;
import static org.junit.Assert.*;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertNotNull(request);
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
        assertTrue(request.getOauthParameters() instanceof Map);
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("key1", "value1");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals("value1", oauthParameters.get("key1"));
        request.addOAuthParameter("key2", "value2");
        oauthParameters = request.getOauthParameters();
        assertEquals("value2", oauthParameters.get("key2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("invalidKey", "value");
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("key1", "value1");
        request.addOAuthParameter("key2", "value2");
        String expected = String.format("@OAuthRequest(GET, http://example.com)", "GET", "http://example.com");
        String actual = request.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetEmptyOAuthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertTrue(oauthParameters.isEmpty());
    }

    @Test
    public void testGetOAuthParametersWithValidKeys() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("key1", "value1");
        request.addOAuthParameter("key2", "value2");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals("value1", oauthParameters.get("key1"));
        assertEquals("value2", oauthParameters.get("key2"));
    }

    @Test
    public void testGetOAuthParametersWithInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("key1", "value1");
        request.addOAuthParameter("invalidKey", "value");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertNull(oauthParameters.get("invalidKey"));
    }
}