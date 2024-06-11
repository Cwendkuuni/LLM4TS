package org.junit.test;

import org.junit.Test;
import org.scribe.model.OAuthRequest;
import static org.junit.Assert.*;

public class OAuthRequestTest {
    // Test case for constructor
    @Test
    public void testOAuthRequestConstruction() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
    }

    // Test case for addOAuthParameter method with empty key and value
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithEmptyKeyOrValue() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.POST, "http://example.com");
        request.addOAuthParameter("", "");
    }

    // Test case for addOAuthParameter method with invalid key
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.POST, "http://example.com");
        request.addOAuthParameter("invalidKey", "testValue");
    }

    // Test case for addOAuthParameter method with duplicate key
    @Test(expected = IllegalStateException.class)
    public void testAddOAuthParameterWithDuplicateKey() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.POST, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "testKey1");
        request.addOAuthParameter("oauth_consumer_key", "testKey2");
    }

    // Test case for getOauthParameters method
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(OAuthRequest.Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "testKey");
        Map<String, String> parameters = request.getOauthParameters();
        assertEquals("testKey", parameters.get("oauth_consumer_key"));
    }
}