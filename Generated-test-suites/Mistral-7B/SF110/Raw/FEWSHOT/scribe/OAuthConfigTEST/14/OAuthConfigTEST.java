package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback);

        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithKeySecretCallbackAndType() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        String callback = "http://example.com/callback";
        SignatureType signatureType = SignatureType.Query;
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, null);

        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithKeySecretCallbackTypeAndScope() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        String callback = "http://example.com/callback";
        SignatureType signatureType = SignatureType.Query;
        String scope = "read_only";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);

        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertTrue(config.hasScope());
    }
}