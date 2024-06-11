package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestInstance() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://api.example.com/resource");
        assertNotNull(oauthRequest);
        assertEquals("@OAuthRequest(GET, https://api.example.com/resource)", oauthRequest.toString());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://api.example.com/resource");
        oauthRequest.addOAuthParameter("oauth_consumer_key", "1234567890");
        oauthRequest.addOAuthParameter("oauth_nonce", "abcdef12345");
        oauthRequest.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
        oauthRequest.addOAuthParameter("oauth_timestamp", "1234567890");
        oauthRequest.addOAuthParameter("oauth_token", "token12345");
        oauthRequest.addOAuthParameter("scope", "read write");

        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertEquals(6, oauthParameters.size());
        assertTrue(oauthParameters.containsKey("oauth_consumer_key"));
        assertTrue(oauthParameters.containsKey("oauth_nonce"));
        assertTrue(oauthParameters.containsKey("oauth_signature_method"));
        assertTrue(oauthParameters.containsKey("oauth_timestamp"));
        assertTrue(oauthParameters.containsKey("oauth_token"));
        assertTrue(oauthParameters.containsKey("scope"));
        assertEquals("1234567890", oauthParameters.get("oauth_consumer_key"));
        assertEquals("abcdef12345", oauthParameters.get("oauth_nonce"));
        assertEquals("HMAC-SHA1", oauthParameters.get("oauth_signature_method"));
        assertEquals("1234567890", oauthParameters.get("oauth_timestamp"));
        assertEquals("token12345", oauthParameters.get("oauth_token"));
        assertEquals("read write", oauthParameters.get("scope"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://api.example.com/resource");
        oauthRequest.addOAuthParameter("invalid_key", "value");
    }

    @Test
    public void testGetOAuthParameters() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://api.example.com/resource");
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertEquals(0, oauthParameters.size());

        oauthRequest.addOAuthParameter("oauth_consumer_key", "1234567890");
        oauthParameters = oauthRequest.getOauthParameters();
        assertEquals(1, oauthParameters.size());
        assertEquals("1234567890", oauthParameters.get("oauth_consumer_key"));

        oauthRequest.addOAuthParameter("oauth_nonce", "abcdef12345");
        oauthParameters = oauthRequest.getOauthParameters();
        assertEquals(2, oauthParameters.size());
        assertEquals("1234567890", oauthParameters.get("oauth_consumer_key"));
        assertEquals("abcdef12345", oauthParameters.get("oauth_nonce"));
    }
}