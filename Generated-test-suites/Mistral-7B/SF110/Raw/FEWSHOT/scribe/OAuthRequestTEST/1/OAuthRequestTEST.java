package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestCreation() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        assertNull(request.getOauthParameters());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_token", "token123");
        Map<String, String> oauthParams = request.getOauthParameters();
        assertEquals("token123", oauthParams.get("oauth_token"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNonOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("foo", "bar");
    }

    @Test
    public void testAddOAuthParameterWithExistingKey() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_token", "token123");
        request.addOAuthParameter("oauth_token", "token456");
        Map<String, String> oauthParams = request.getOauthParameters();
        assertEquals("token456", oauthParams.get("oauth_token"));
    }

    @Test
    public void testGetOAuthParametersEmpty() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        Map<String, String> oauthParams = request.getOauthParameters();
        assertTrue(oauthParams.isEmpty());
    }

    @Test
    public void testGetOAuthParametersWithParameters() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_token", "token123");
        request.addOAuthParameter("oauth_consumer_key", "consumerKey123");
        Map<String, String> oauthParams = request.getOauthParameters();
        assertEquals("consumerKey123", oauthParams.get("oauth_consumer_key"));
        assertEquals("token123", oauthParams.get("oauth_token"));
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Request.Verbs.POST, "http://example.com");
        request.addOAuthParameter("oauth_token", "token123");
        request.addOAuthParameter("oauth_consumer_key", "consumerKey123");
        assertEquals("@OAuthRequest(POST, http://example.com)", request.toString());
    }

    @Test
    public void testCheckKey() {
        assertEquals("oauth_token", OAuthRequest.this.checkKey("oauth_token"));
        assertEquals("scope", OAuthRequest.this.checkKey("scope"));
        assertFalse(OAuthRequest.this.checkKey("foo").startsWith("oauth_"));
        throw new AssertionError("Unexpected exception");
    }

    @Test
    public void testGetOAuthParametersConcurrentAccess() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        ConcurrentHashMap<String, String> oauthParams = new ConcurrentHashMap<String, String>(request.oauthParameters);

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            threads.add(new Thread(() -> oauthParams.put("testKey" + i, "testValue" + i)));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Map<String, String> params = request.getOauthParameters();
        int count = 0;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getKey().startsWith("testKey")) {
                count++;
            }
        }

        assertEquals(1000, count);
    }
}