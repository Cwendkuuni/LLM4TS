package org.scribe.model;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Request;
import org.scribe.model.Verb;

import java.util.*;
import java.util.concurrent.*;

public class OAuthRequestTest {

    @org.junit.Before
    public void setUp() {
        // Set up any necessary objects or initializations here
    }

    @org.junit.After
    public void tearDown() {
        // Clean up any objects or resources created during the test here
    }

    @org.junit.Test
    public void testOAuthRequestConstruction() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/api");
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://example.com/api", request.getUrl());
        assertTrue(request.getOauthParameters() instanceof Map);
        assertTrue(request.getOauthParameters().isEmpty());
    }

    @org.junit.Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/api");
        request.addOAuthParameter("oauth_consumer_key", "abc123");
        Map<String, String> oauthParameters = request.getOAuthParameters();
        assertEquals("abc123", oauthParameters.get("oauth_consumer_key"));
        request.addOAuthParameter("scope", "read write");
        assertEquals("read write", oauthParameters.get("scope"));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddIllegalOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/api");
        request.addOAuthParameter("illegal_key", "value");
    }

    @org.junit.Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/api");
        request.addOAuthParameter("oauth_consumer_key", "abc123");
        request.addOAuthParameter("scope", "read write");
        String expected = String.format("@OAuthRequest(GET, %s)", "http://example.com/api");
        String actual = request.toString();
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testGetOAuthParameterMapIsImmutable() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/api");
        request.addOAuthParameter("oauth_consumer_key", "abc123");
        Map<String, String> oauthParameters = request.getOAuthParameters();
        oauthParameters.put("new_key", "new_value"); // This should throw an exception
    }

    @org.junit.Test
    public void testGetOAuthParameterValue() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/api");
        request.addOAuthParameter("oauth_consumer_key", "abc123");
        request.addOAuthParameter("oauth_timestamp", "1234567890");
        Map<String, String> oauthParameters = request.getOAuthParameters();
        assertEquals("abc123", oauthParameters.get("oauth_consumer_key"));
        assertEquals(new DateTime(1234567890L * 1000).toString("yyyy-MM-dd HH:mm:ss.SSS"), oauthParameters.get("oauth_timestamp"));
    }

    @org.junit.Test
    public void testGetRequestUrlWithOAuthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com/api");
        request.addOAuthParameter("oauth_consumer_key", "abc123");
        request.addOAuthParameter("oauth_timestamp", "1234567890");
        String expected = "http://example.com/api?oauth_consumer_key=abc123&oauth_timestamp=1234567890";
        String actual = request.getUrlWithOAuthParameters();
        assertEquals(expected, actual);
    }
}