package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.*;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestInstance() {
        Verb verb = Verb.GET;
        String url = "https://api.example.com/resource";
        OAuthRequest oAuthRequest = new OAuthRequest(verb, url);
        assertEquals("@OAuthRequest(GET, https://api.example.com/resource)", oAuthRequest.toString());
        assertNull(oAuthRequest.getOauthParameters());
    }

    @Test
    public void testAddOAuthParameter() {
        Verb verb = Verb.GET;
        String url = "https://api.example.com/resource";
        OAuthRequest oAuthRequest = new OAuthRequest(verb, url);
        Map<String, String> oauthParameters = new HashMap<>();
        oAuthRequest.addOAuthParameter("oauth_consumer_key", "1234567890");
        oAuthRequest.addOAuthParameter("oauth_signature", "abcdefghijklmnopqrstuvwxyz12345");
        oAuthRequest.addOAuthParameter("oauth_timestamp", "1516049421");
        oAuthRequest.addOAuthParameter("oauth_nonce", "1234567890123456");
        oAuthRequest.addOAuthParameter("oauth_version", "1.0");
        oAuthRequest.addOAuthParameter("scope", "read write");

        Map<String, String> expectedOAuthParameters = new HashMap<>();
        expectedOAuthParameters.put("oauth_consumer_key", "1234567890");
        expectedOAuthParameters.put("oauth_signature", "abcdefghijklmnopqrstuvwxyz12345");
        expectedOAuthParameters.put("oauth_timestamp", "1516049421");
        expectedOAuthParameters.put("oauth_nonce", "1234567890123456");
        expectedOAuthParameters.put("oauth_version", "1.0");
        expectedOathParameters.put("scope", "read write");

        assertEquals(expectedOAuthParameters, oAuthRequest.getOauthParameters());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddIllegalOAuthParameter() {
        Verb verb = Verb.GET;
        String url = "https://api.example.com/resource";
        OAuthRequest oAuthRequest = new OAuthRequest(verb, url);
        oAuthRequest.addOAuthParameter("param", "value");
    }

    @Test
    public void testGetOAuthParameter() {
        Verb verb = Verb.GET;
        String url = "https://api.example.com/resource";
        OAuthRequest oAuthRequest = new OAuthRequest(verb, url);
        Map<String, String> oauthParameters = new HashMap<>();
        oAuthRequest.addOAuthParameter("oauth_consumer_key", "1234567890");
        oAuthRequest.addOAuthParameter("oauth_signature", "abcdefghijklmnopqrstuvwxyz12345");
        oAuthRequest.addOAuthParameter("oauth_timestamp", "1516049421");
        oAuthRequest.addOAuthParameter("oauth_nonce", "1234567890123456");
        oAuthRequest.addOAuthParameter("oauth_version", "1.0");
        oAuthRequest.addOAuthParameter("scope", "read write");

        assertEquals("1234567890", oAuthRequest.getOauthParameter("oauth_consumer_key"));
        assertNull(oAuthRequest.getOauthParameter("param"));
    }
}