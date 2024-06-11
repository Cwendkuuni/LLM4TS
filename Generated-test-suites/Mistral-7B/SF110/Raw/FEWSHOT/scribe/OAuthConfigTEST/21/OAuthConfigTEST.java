package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigDefaultConstructor() {
        OAuthConfig config = new OAuthConfig(null, null);
        assertNull(config.getApiKey());
        assertNull(config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
    }

    @Test
    public void testOAuthConfigWithApiKeyAndApiSecret() {
        String apiKey = "apiKey123";
        String apiSecret = "apiSecret456";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
    }

    @Test
    public void testOAuthConfigWithApiKeyAndApiSecretAndCallback() {
        String apiKey = "apiKey123";
        String apiSecret = "apiSecret456";
        String callback = "http://example.com";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
    }

    @Test
    public void testOAuthConfigWithApiKeyAndApiSecretAndCallbackAndSignatureType() {
        String apiKey = "apiKey123";
        String apiSecret = "apiSecret456";
        String callback = "http://example.com";
        SignatureType signatureType = SignatureType.QueryString;
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertNull(config.getScope());
    }

    @Test
    public void testOAuthConfigWithApiKeyAndApiSecretAndCallbackAndSignatureTypeAndScope() {
        String apiKey = "apiKey123";
        String apiSecret = "apiSecret456";
        String callback = "http://example.com";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "scope123";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertTrue(config.hasScope());
    }
}