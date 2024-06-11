package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigWithKeyAndSecret() {
        String apiKey = "apiKey123";
        String apiSecret = "apiSecret456";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithKeySecretCallbackAndSignatureType() {
        String apiKey = "apiKey123";
        String apiSecret = "apiSecret456";
        String callback = "http://localhost:8080/callback";
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
    public void testOAuthConfigWithKeySecretCallbackSignatureTypeAndScope() {
        String apiKey = "apiKey123";
        String apiSecret = "apiSecret456";
        String callback = "http://localhost:8080/callback";
        SignatureType signatureType = SignatureType.Header;
        String scope = "read write";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertTrue(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithNullApiKeyAndSecret() {
        OAuthConfig config = new OAuthConfig(null, null);
        assertNull(config.getApiKey());
        assertNull(config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithNullApiKey() {
        String apiSecret = "apiSecret456";
        OAuthConfig config = new OAuthConfig(null, apiSecret);
        assertNull(config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithNullApiSecret() {
        String apiKey = "apiKey123";
        OAuthConfig config = new OAuthConfig(apiKey, null);
        assertEquals(apiKey, config.getApiKey());
        assertNull(config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithNullCallback() {
        String apiKey = "apiKey123";
        String apiSecret = "apiSecret456";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithNullSignatureType() {
        String apiKey = "apiKey123";
        String apiSecret = "apiSecret456";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, null, null);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertNull(config.getCallback());
        assertNull(config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithNullScope() {
        String apiKey = "apiKey123";
        String apiSecret = "apiSecret456";
        SignatureType signatureType = SignatureType.Header;
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, signatureType, null);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }
}