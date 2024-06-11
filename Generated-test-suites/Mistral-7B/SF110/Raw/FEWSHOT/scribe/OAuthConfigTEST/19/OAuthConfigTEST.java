package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.OAuthConfig;

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
    public void testOAuthConfigWithKeySecretCallbackAndSignatureType() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        String callback = "http://example.com/callback";
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
        String callback = "http://example.com/callback";
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

    @Test(expected = NullPointerException.class)
    public void testNullApiKey() {
        String apiSecret = "def456";
        String callback = null;
        SignatureType signatureType = SignatureType.Header;
        String scope = "read write";
        new OAuthConfig(null, apiSecret, callback, signatureType, scope);
    }

    @Test(expected = NullPointerException.class)
    public void testNullApiSecret() {
        String apiKey = "abc123";
        String callback = null;
        SignatureType signatureType = SignatureType.Header;
        String scope = "read write";
        new OAuthConfig(apiKey, null, callback, signatureType, scope);
    }

    @Test(expected = NullPointerException.class)
    public void testNullCallback() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        SignatureType signatureType = SignatureType.Header;
        String scope = "read write";
        new OAuthConfig(apiKey, apiSecret, null, signatureType, scope);
    }

    @Test(expected = NullPointerException.class)
    public void testNullSignatureType() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        String callback = "http://example.com/callback";
        String scope = "read write";
        new OAuthConfig(apiKey, apiSecret, callback, null, scope);
    }
}