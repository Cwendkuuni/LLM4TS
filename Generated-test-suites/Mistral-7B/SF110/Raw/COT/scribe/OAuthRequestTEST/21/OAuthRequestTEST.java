package org.scribe.model.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

import java.util.*;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestConstruction() {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        Assert.assertEquals(Verb.GET, oAuthRequest.getVerb());
        Assert.assertEquals("http://example.com", oAuthRequest.getUrl());
        Assert.assertNotNull(oAuthRequest.getOauthParameters());
        Assert.assertTrue(oAuthRequest.getOauthParameters().isEmpty());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        oAuthRequest.addOAuthParameter("oauth_token", "tokenValue");
        Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
        Assert.assertEquals("tokenValue", oauthParameters.get("oauth_token"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        oAuthRequest.addOAuthParameter("invalidKey", "value");
    }

    @Test
    public void testGetOAuthParameters() {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        oAuthRequest.addOAuthParameter("oauth_token", "tokenValue1");
        oAuthRequest.addOAuthParameter("scope", "scopeValue");
        Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
        Assert.assertEquals(2, oauthParameters.size());
        Assert.assertEquals("tokenValue1", oauthParameters.get("oauth_token"));
        Assert.assertEquals("scopeValue", oauthParameters.get("scope"));
    }

    @Test
    public void testToString() {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        oAuthRequest.addOAuthParameter("oauth_token", "tokenValue");
        oAuthRequest.addOAuthParameter("scope", "scopeValue");
        String expected = String.format("@OAuthRequest(GET, %s)", "http://example.com");
        String actual = oAuthRequest.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testOAuthRequestWithEmptyParameters() {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
        Assert.assertTrue(oauthParameters.isEmpty());
    }
}