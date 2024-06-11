package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigWithKeyAndSecret() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithKeySecretAndCallback() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        String callback = "http://example.com/callback";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, null, null);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertNull(config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithKeySecretCallbackAndSignatureType() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        String callback = "http://example.com/callback";
        SignatureType signatureType = SignatureType.QueryString;
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, null);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithKeySecretCallbackAndScope() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        String callback = "http://example.com/callback";
        String scope = "read write";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, null, scope);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertNull(config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertTrue(config.hasScope());
    }

    @Test(expected = NullPointerException.class)
    public void testNullApiKey() {
        String apiSecret = "def456";
        String callback = "http://example.com/callback";
        OAuthConfig config = new OAuthConfig(null, apiSecret, callback, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullApiSecret() {
        String apiKey = "abc123";
        String callback = "http://example.com/callback";
        OAuthConfig config = new OAuthConfig(apiKey, null, callback, null, null);
    }
}