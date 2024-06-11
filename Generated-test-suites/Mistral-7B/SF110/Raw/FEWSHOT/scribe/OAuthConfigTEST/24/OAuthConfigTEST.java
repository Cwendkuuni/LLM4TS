package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigWithKeyAndSecret() {
        String key = "myApiKey";
        String secret = "myApiSecret";
        OAuthConfig config = new OAuthConfig(key, secret);
        assertEquals(key, config.getApiKey());
        assertEquals(secret, config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithKeySecretAndCallback() {
        String key = "myApiKey";
        String secret = "myApiSecret";
        String callback = "http://myapp.com/callback";
        OAuthConfig config = new OAuthConfig(key, secret, callback);
        assertEquals(key, config.getApiKey());
        assertEquals(secret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithKeySecretCallbackAndType() {
        String key = "myApiKey";
        String secret = "myApiSecret";
        String callback = "http://myapp.com/callback";
        SignatureType type = SignatureType.QueryString;
        OAuthConfig config = new OAuthConfig(key, secret, callback, type);
        assertEquals(key, config.getApiKey());
        assertEquals(secret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(type, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithKeySecretCallbackTypeAndScope() {
        String key = "myApiKey";
        String secret = "myApiSecret";
        String callback = "http://myapp.com/callback";
        SignatureType type = SignatureType.QueryString;
        String scope = "read write";
        OAuthConfig config = new OAuthConfig(key, secret, callback, type, scope);
        assertEquals(key, config.getApiKey());
        assertEquals(secret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(type, config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertTrue(config.hasScope());
    }

    @Test(expected = NullPointerException.class)
    public void testNullApiKey() {
        String apiSecret = "myApiSecret";
        String callback = "http://myapp.com/callback";
        SignatureType signatureType = SignatureType.Header;
        String scope = "read write";
        new OAuthConfig(null, apiSecret, callback, signatureType, scope);
    }

    @Test(expected = NullPointerException.class)
    public void testNullApiSecret() {
        String apiKey = "myApiKey";
        String callback = "http://myapp.com/callback";
        SignatureType signatureType = SignatureType.Header;
        String scope = "read write";
        new OAuthConfig(apiKey, null, callback, signatureType, scope);
    }

    @Test(expected = NullPointerException.class)
    public void testNullCallback() {
        String apiKey = "myApiKey";
        String apiSecret = "myApiSecret";
        SignatureType signatureType = SignatureType.Header;
        String scope = "read write";
        new OAuthConfig(apiKey, apiSecret, null, signatureType, scope);
    }

    @Test(expected = NullPointerException.class)
    public void testNullSignatureType() {
        String apiKey = "myApiKey";
        String apiSecret = "myApiSecret";
        String callback = "http://myapp.com/callback";
        String scope = "read write";
        new OAuthConfig(apiKey, apiSecret, callback, null, scope);
    }
}