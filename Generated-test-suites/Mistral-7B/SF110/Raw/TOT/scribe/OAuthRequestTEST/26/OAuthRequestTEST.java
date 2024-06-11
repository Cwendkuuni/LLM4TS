package org.scribe.model;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.OAuthRequest.Verb;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestCreation() {
        // Testing constructor with an empty OAuthRequest
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        assertNotNull(request);
        assertNull(request.getOauthParameters());

        // Testing constructor with valid oauth parameters
        request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_consumer_key", "testKey1");
        request.addOAuthParameter("oauth_nonce", "testNonce1");
        request.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
        request.addOAuthParameter("oauth_timestamp", "1234567890");
        request.addOAuthParameter("oauth_token", "testToken1");
        request.addOAuthParameter("oauth_version", "1.0");
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("oauth_consumer_key", "testKey1");
        expectedMap.put("oauth_nonce", "testNonce1");
        expectedMap.put("oauth_signature_method", "HMAC-SHA1");
        expectedMap.put("oauth_timestamp", "1234567890");
        expectedMap.put("oauth_token", "testToken1");
        expectedMap.put("oauth_version", "1.0");
        Map<String, String> actualMap = request.getOauthParameters();
        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testAddOAuthParameterWithNullKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        try {
            request.addOAuthParameter(null, "testValue");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must neither be null nor empty", e.getMessage());
        }
    }

    @Test
    public void testAddOAuthParameterWithEmptyValue() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        try {
            request.addOAuthParameter("oauth_signature_method", "");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must neither be null nor empty", e.getMessage());
        }
    }

    @Test
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        try {
            request.addOAuthParameter("invalidKey", "testValue");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("is not a valid OAuth parameter key"));
        }
    }

    @Test
    public void testAddOAuthParameterWithDuplicateKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
        try {
            request.addOAuthParameter("oauth_signature_method", "SHA256");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("already exists in the OAuth request parameters"));
        }
    }

    @Test
    public void testGetOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_consumer_key", "testKey1");
        request.addOAuthParameter("oauth_nonce", "testNonce1");
        request.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
        request.addOAuthParameter("oauth_timestamp", "1234567890");
        request.addOAuthParameter("oauth_token", "testToken1");
        request.addOAuthParameter("oauth_version", "1.0");

        String consumerKey = request.getOAuthParameter("oauth_consumer_key");
        assertEquals("testKey1", consumerKey);

        String unknownParameter = request.getOAuthParameter("oauth_signature");
        assertNull(unknownParameter);
    }

    @Test
    public void testGetOAuthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_consumer_key", "testKey1");
        request.addOAuthParameter("oauth_nonce", "testNonce1");
        request.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
        request.addOAuthParameter("oauth_timestamp", "1234567890");
        request.addOAuthParameter("oauth_token", "testToken1");
        request.addOAuthParameter("oauth_version", "1.0");

        Map<String, String> oauthParameters = request.getOauthParameters();
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("oauth_consumer_key", "testKey1");
        expectedMap.put("oauth_nonce", "testNonce1");
        expectedMap.put("oauth_signature_method", "HMAC-SHA1");
        expectedMap.put("oauth_timestamp", "1234567890");
        expectedMap.put("oauth_token", "testToken1");
        expectedMap.put("oauth_version", "1.0");

        assertEquals(expectedMap, oauthParameters);
    }
}