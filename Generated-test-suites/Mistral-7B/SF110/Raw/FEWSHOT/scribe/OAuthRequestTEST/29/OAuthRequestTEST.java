package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

import org.scribe.model.OAuthRequest;

import java.util.*;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestConstruction() {
        Verb verb = Verb.GET;
        String url = "http://example.com";
        OAuthRequest request = new OAuthRequest(verb, url);
        assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
        assertNull(request.getOauthParameters());
    }

    @Test
    public void testAddOAuthParameter() {
        Verb verb = Verb.GET;
        String url = "http://example.com";
        OAuthRequest request = new OAuthRequest(verb, url);

        request.addOAuthParameter("oauth_consumer_key", "key123");
        Map<String, String> oauthParams = request.getOauthParameters();
        assertEquals("key123", oauthParams.get("oauth_consumer_key"));

        request.addOAuthParameter("scope", "read write");
        oauthParams = request.getOauthParameters();
        assertEquals("read write", oauthParams.get("scope"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalidOAuthParameter() {
        Verb verb = Verb.GET;
        String url = "http://example.com";
        OAuthRequest request = new OAuthRequest(verb, url);
        request.addOAuthParameter("invalidkey", "value");
    }

    @Test
    public void testGetOAuthParameters() {
        Verb verb = Verb.GET;
        String url = "http://example.com";
        OAuthRequest request = new OAuthRequest(verb, url);

        request.addOAuthParameter("oauth_consumer_key", "key123");
        Map<String, String> oauthParams = request.getOauthParameters();
        assertNotNull(oauthParams);
        assertEquals(2, oauthParams.size());
    }

    @Test
    public void testToStringWithOAuthParameters() {
        Verb verb = Verb.GET;
        String url = "http://example.com";
        OAuthRequest request = new OAuthRequest(verb, url);

        request.addOAuthParameter("oauth_consumer_key", "key123");
        request.addOAuthParameter("scope", "read write");
        String expected = String.format("@OAuthRequest(GET, http://example.com) %s", request.getOauthParameters().toString());
        assertEquals(expected, request.toString());
    }
}