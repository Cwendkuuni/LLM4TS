package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestCreation() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.example.com/resource");
        assertNull(request.getOauthParameters());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.example.com/resource");
        request.addOAuthParameter("oauth_token", "123456");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals("123456", oauthParameters.get("oauth_token"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNonOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.example.com/resource");
        request.addOAuthParameter("non_oauth_param", "value");
    }

    @Test
    public void testAddScopeParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.example.com/resource");
        request.addOAuthParameter("scope", "read write");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals("read write", oauthParameters.get("scope"));
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.example.com/resource");
        request.addOAuthParameter("oauth_token", "123456");
        request.addOAuthParameter("oauth_consumer_key", "abcdefg");
        Map<String, String> oauthParameters = request.getOauthParameters();
        String expected = String.format("@OAuthRequest(GET, %s, %s)", "https://api.example.com/resource", oauthParameters.toString());
        assertEquals(expected, request.toString());
    }
}