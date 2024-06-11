package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

import java.util.*;
import java.util.concurrent.*;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestConstructor() {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "https://api.example.com/");
        assertEquals("@OAuthRequest(GET, https://api.example.com/)", oAuthRequest.toString());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "https://api.example.com/");
        oAuthRequest.addOAuthParameter("oauth_token", "123456");
        Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
        assertEquals("123456", oauthParameters.get("oauth_token"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterInvalidKey() {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "https://api.example.com/");
        oAuthRequest.addOAuthParameter("invalidKey", "value");
    }

    @Test
    public void testGetOAuthParametersEmpty() {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "https://api.example.com/");
        Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
        assertTrue(oauthParameters.isEmpty());
    }

    @Test
    public void testGetOAuthParametersWithParameters() {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "https://api.example.com/");
        oAuthRequest.addOAuthParameter("oauth_token", "123456");
        oAuthRequest.addOAuthParameter("oauth_token_secret", "abcdefg");
        oAuthRequest.addOAuthParameter("scope", "read write");
        Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
        assertEquals(3, oauthParameters.size());
        assertEquals("123456", oauthParameters.get("oauth_token"));
        assertEquals("abcdefg", oauthParameters.get("oauth_token_secret"));
        assertEquals("read write", oauthParameters.get("scope"));
    }

    @Test
    public void testToString() {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "https://api.example.com/");
        assertEquals("@OAuthRequest(GET, https://api.example.com/)", oAuthRequest.toString());
    }
}