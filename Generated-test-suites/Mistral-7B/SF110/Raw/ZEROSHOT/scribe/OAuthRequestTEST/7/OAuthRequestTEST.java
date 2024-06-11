package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

import java.util.*;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestCreation() {
        String url = "https://api.example.com/resource";
        Verb verb = Verb.GET;
        OAuthRequest oauthRequest = new OAuthRequest(verb, url);
        assertEquals("@OAuthRequest(GET, https://api.example.com/resource)", oauthRequest.toString());
        assertNull(oauthRequest.getOauthParameters());
    }

    @Test
    public void testAddOAuthParameter() {
        String url = "https://api.example.com/resource";
        Verb verb = Verb.GET;
        OAuthRequest oauthRequest = new OAuthRequest(verb, url);

        oauthRequest.addOAuthParameter("oauth_consumer_key", "abc123");
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertEquals("abc123", oauthParameters.get("oauth_consumer_key"));

        oauthRequest.addOAuthParameter("oauth_nonce", "def456");
        oauthParameters = oauthRequest.getOauthParameters();
        assertEquals("def456", oauthParameters.get("oauth_nonce"));

        oauthRequest.addOAuthParameter("scope", "read write");
        oauthParameters = oauthRequest.getOauthParameters();
        assertEquals("read write", oauthParameters.get("scope"));

        try {
            oauthRequest.addOAuthParameter("invalid_key", "value");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCheckKey() {
        String key = "oauth_consumer_key";
        assertEquals(key, new OAuthRequest().checkKey(key));

        key = "invalid_key";
        try {
            new OAuthRequest().checkKey(key);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        key = "scope";
        assertEquals(key, new OAuthRequest().checkKey(key));
    }
}