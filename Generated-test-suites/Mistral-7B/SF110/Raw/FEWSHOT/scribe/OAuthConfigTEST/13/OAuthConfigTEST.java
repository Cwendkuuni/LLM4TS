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
        String callback = "http://example.com/oauth-callback";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback);

        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithKeySecretCallbackAndSignatureType() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        String callback = "http://example.com/oauth-callback";
        SignatureType signatureType = SignatureType.QueryString;
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType);

        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithKeySecretCallbackSignatureTypeAndScope() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        String callback = "http://example.com/oauth-callback";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "scope1 scope2";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);

        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertTrue(config.hasScope());
    }

    @Test(expected = NullPointerException.class)
    public void testNullApiKey() {
        String apiSecret = "def456";
        String callback = "http://example.com/oauth-callback";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "scope1 scope2";
        new OAuthConfig(null, apiSecret, callback, signatureType, scope);
    }

    @Test(expected = NullPointerException.class)
    public void testNullApiSecret() {
        String apiKey = "abc123";
        String callback = "http://example.com/oauth-callback";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "scope1 scope2";
        new OAuthConfig(apiKey, null, callback, signatureType, scope);
    }
}