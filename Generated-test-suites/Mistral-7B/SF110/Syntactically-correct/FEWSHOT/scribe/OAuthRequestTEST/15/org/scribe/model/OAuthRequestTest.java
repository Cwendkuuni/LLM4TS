package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OAuthRequestTest {
    @Test
    public void testOAuthRequestCreation() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        assertNotNull(request);
        assertEquals(Request.Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
        assertNull(request.getOauthParameters());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_token", "1234567890");
        Map<String, String> oauthParams = request.getOauthParameters();
        assertEquals("1234567890", oauthParams.get("oauth_token"));

        request.addOAuthParameter("scope", "read write");
        oauthParams = request.getOauthParameters();
        assertEquals("read write", oauthParams.get("scope"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("invalid_key", "value");
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_token", "1234567890");
        request.addOAuthParameter("scope", "read write");

        Map<String, String> oauthParams = request.getOauthParameters();
        assertEquals(2, oauthParams.size());
        assertEquals("1234567890", oauthParams.get("oauth_token"));
        assertEquals("read write", oauthParams.get("scope"));
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_token", "1234567890");
        request.addOAuthParameter("scope", "read write");

        String expected = String.format("@OAuthRequest(GET, %s)", "http://example.com");
        String actual = request.toString();
        assertEquals(expected, actual);

        request.getOauthParameters().put("oauth_signature", "signature_value");
        expected = String.format("@OAuthRequest(GET, %s) { oauth_token=1234567890, scope=read write, oauth_signature=signature_value}", "http://example.com");
        actual = request.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEmptyOAuthParameters() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        Map<String, String> oauthParams = request.getOauthParameters();
        assertTrue(oauthParams.isEmpty());
    }

    @Test
    public void testIterableOAuthParameters() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_token", "1234567890");
        request.addOAuthParameter("scope", "read write");

        Map<String, String> oauthParams = request.getOauthParameters();
        for (Map.Entry<String, String> entry : oauthParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("Key: " + key + ", Value: " + value);
            assertTrue(key.startsWith("oauth_") || key.equals("scope"));
        }
    }
}