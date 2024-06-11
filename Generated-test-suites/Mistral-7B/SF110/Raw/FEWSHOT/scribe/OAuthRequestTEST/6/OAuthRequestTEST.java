package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestInstance() {
        Verb verb = Verb.GET;
        String url = "https://api.example.com";
        OAuthRequest oAuthRequest = new OAuthRequest(verb, url);
        assertEquals("@OAuthRequest(GET, https://api.example.com)", oAuthRequest.toString());
        assertNull(oAuthRequest.getOauthParameters());
    }

    @Test
    public void testAddOAuthParameter() {
        Verb verb = Verb.GET;
        String url = "https://api.example.com";
        String key = "token";
        String value = "abc123";
        OAuthRequest oAuthRequest = new OAuthRequest(verb, url);
        oAuthRequest.addOAuthParameter(key, value);
        Map<String, String> oauthParams = oAuthRequest.getOauthParameters();
        assertEquals(1, oauthParams.size());
        assertEquals(value, oauthParams.get(key));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNonOAuthParameter() {
        Verb verb = Verb.GET;
        String url = "https://api.example.com";
        String key = "foo";
        OAuthRequest oAuthRequest = new OAuthRequest(verb, url);
        oAuthRequest.addOAuthParameter(key, "bar");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKey() {
        Verb verb = Verb.GET;
        String url = "https://api.example.com";
        OAuthRequest oAuthRequest = new OAuthRequest(verb, url);
        oAuthRequest.addOAuthParameter("not_valid_key", "bar");
    }

    @Test
    public void testGetOAuthParameters() {
        Verb verb = Verb.GET;
        String url = "https://api.example.com";
        String key1 = "token";
        String value1 = "abc123";
        String key2 = "scope";
        String value2 = "read write";
        OAuthRequest oAuthRequest = new OAuthRequest(verb, url);
        oAuthRequest.addOAuthParameter(key1, value1);
        oAuthRequest.addOAuthParameter(key2, value2);
        Map<String, String> oauthParams = oAuthRequest.getOauthParameters();
        assertEquals(2, oauthParams.size());
        assertEquals(value1, oauthParams.get(key1));
        assertEquals(value2, oauthParams.get(key2));
    }

    @Test
    public void testGetOAuthParameter() {
        Verb verb = Verb.GET;
        String url = "https://api.example.com";
        String key = "token";
        String value = "abc123";
        OAuthRequest oAuthRequest = new OAuthRequest(verb, url);
        oAuthRequest.addOAuthParameter(key, value);
        String oauthValue = oAuthRequest.getOauthParameter(key);
        assertEquals(value, oauthValue);
    }

    @Test
    public void testGetNonexistentOAuthParameter() {
        Verb verb = Verb.GET;
        String url = "https://api.example.com";
        String key = "nonexistent";
        OAuthRequest oAuthRequest = new OAuthRequest(verb, url);
        assertNull(oAuthRequest.getOauthParameter(key));
    }
}