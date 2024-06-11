package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class OAuthRequestTest {

    @Test
    public void testOAuthRequestWithNoOAuthParameters() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertTrue(oauthParameters.isEmpty());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "ABC123");
        request.addOAuthParameter("oauth_nonce", "456789");
        request.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
        request.addOAuthParameter("oauth_timestamp", "1234567890");
        request.addOAuthParameter("oauth_token", "DEF456");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals(5, oauthParameters.size());
        assertEquals("ABC123", oauthParameters.get("oauth_consumer_key"));
        // Other assertions for other keys
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNonOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("foo", "bar");
    }

    @Test
    public void testCheckKey() {
        String validKey1 = "oauth_consumer_key";
        String validKey2 = "scope";
        String invalidKey = "foo";
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        assertEquals(validKey1, request.checkKey(validKey1));
        assertEquals(validKey2, request.checkKey(validKey2));
        try {
            request.checkKey(invalidKey);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "ABC123");
        assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
    }

    @Test
    public void testToStringWithOAuthParameters() {
        OAuthRequest request = new OAuthRequest(Request.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "ABC123");
        request.addOAuthParameter("oauth_nonce", "456789");
        request.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
        request.addOAuthParameter("oauth_timestamp", "1234567890");
        request.addOAuthParameter("oauth_token", "DEF456");
        assertEquals("@OAuthRequest(GET, http://example.com) {@OAuthRequest(GET, http://example.com)#oauth_consumer_key=ABC123, oauth_nonce=456789, oauth_signature_method=HMAC-SHA1, oauth_timestamp=1234567890, oauth_token=DEF456}", request.toString());
    }
}