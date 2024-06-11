package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.OAuthRequest;

import java.util.*;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestConstruction() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        assertNotNull(oauthRequest);
        assertEquals("GET", oauthRequest.getVerb().name());
        assertEquals("http://example.com", oauthRequest.getUrl());
        assertNull(oauthRequest.getOauthParameters());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        oauthRequest.addOAuthParameter("oauth_consumer_key", "abc123");
        Map<String, String> oauthParams = oauthRequest.getOauthParameters();
        assertEquals("abc123", oauthParams.get("oauth_consumer_key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        oauthRequest.addOAuthParameter("invalid_key", "value");
    }

    @Test
    public void testGetOAuthParameters() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        oauthRequest.addOAuthParameter("oauth_consumer_key", "abc123");
        oauthRequest.addOAuthParameter("scope", "read write");
        Map<String, String> oauthParams = oauthRequest.getOauthParameters();
        assertEquals(2, oauthParams.size());
        assertTrue(oauthParams.containsKey("oauth_consumer_key"));
        assertTrue(oauthParams.containsKey("scope"));
    }

    @Test
    public void testToString() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        oauthRequest.addOAuthParameter("oauth_consumer_key", "abc123");
        oauthRequest.addOAuthParameter("scope", "read write");
        String expected = String.format("@OAuthRequest(GET, %s)", "http://example.com");
        String actual = oauthRequest.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckKey() {
        String validKey = "oauth_consumer_key";
        String validKeyStartsWith = "oauth_signature_method";
        String invalidKey = "invalid_key";
        String scopeKey = "scope";

        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");

        assertEquals(validKey, oauthRequest.checkKey(validKey));
        assertEquals(validKeyStartsWith, oauthRequest.checkKey(validKeyStartsWith));
        throwExpectedException(IllegalArgumentException.class, () -> oauthRequest.checkKey(invalidKey));
        assertEquals(scopeKey, oauthRequest.checkKey(scopeKey));
    }

    private void throwExpectedException(Class<? extends Throwable> exceptionClass, Runnable runner) {
        try {
            runner.run();
            fail("Expected exception not thrown");
        } catch (Throwable e) {
            if (exceptionClass.isAssignableFrom(e.getClass())) {
                return;
            }
            throw new RuntimeException("Unexpected exception type thrown", e);
        }
    }

    @Test
    public void testAddConcurrentOAuthParameters() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        ExecutorService executor = Executors.newFixedThreadPool(10);

        List<Future<Void>> futures = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Callable<Void> callable = () -> {
                oauthRequest.addOAuthParameter(String.valueOf(i), String.valueOf(i));
                return null;
            };
            futures.add(executor.submit(callable));
        }

        for (Future<Void> future : futures) {
            future.get();
        }

        Map<String, String> oauthParams = oauthRequest.getOauthParameters();
        assertEquals(10, oauthParams.size());
    }

    @Test
    public void testAddNullValueOAuthParameter() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        oauthRequest.addOAuthParameter("oauth_consumer_key", null);
        Map<String, String> oauthParams = oauthRequest.getOauthParameters();
        assertNull(oauthParams.get("oauth_consumer_key"));
    }

    @Test
    public void testAddEmptyValueOAuthParameter() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        oauthRequest.addOAuthParameter("oauth_consumer_key", "");
        Map<String, String> oauthParams = oauthRequest.getOauthParameters();
        assertEquals("", oauthParams.get("oauth_consumer_key"));
    }
}