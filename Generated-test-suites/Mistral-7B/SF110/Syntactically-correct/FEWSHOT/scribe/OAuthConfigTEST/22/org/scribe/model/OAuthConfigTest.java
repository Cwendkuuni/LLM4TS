package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigWithApiKeyAndApiSecret() {
        String apiKey = "testApiKey";
        String apiSecret = "testApi Secret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);

        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithApiKeyAndApiSecretAndCallback() {
        String apiKey = "testApiKey";
        String apiSecret = "testApi Secret";
        String callback = "http://test.com/callback";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback);

        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithApiKeyAndApiSecretAndCallbackAndSignatureType() {
        String apiKey = "testApiKey";
        String apiSecret = "testApi Secret";
        String callback = "http://test.com/callback";
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
    public void testOAuthConfigWithApiKeyAndApiSecretAndCallbackAndSignatureTypeAndScope() {
        String apiKey = "testApiKey";
        String apiSecret = "testApi Secret";
        String callback = "http://test.com/callback";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "read_only";
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
        String apiSecret = "testApi Secret";
        String callback = "http://test.com/callback";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "read_only";
        new OAuthConfig(null, apiSecret, callback, signatureType, scope);
    }

    @Test(expected = NullPointerException.class)
    public void testNullApiSecret() {
        String apiKey = "testApiKey";
        String callback = "http://test.com/callback";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "read_only";
        new OAuthConfig(apiKey, null, callback, signatureType, scope);
    }

    @Test(expected = NullPointerException.class)
    public void testNullCallback() {
        String apiKey = "testApiKey";
        String apiSecret = "testApi Secret";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "read_only";
        new OAuthConfig(apiKey, apiSecret, null, signatureType, scope);
    }

    @Test(expected = NullPointerException.class)
    public void testNullSignatureType() {
        String apiKey = "testApiKey";
        String apiSecret = "testApi Secret";
        String callback = "http://test.com/callback";
        String scope = "read_only";
        new OAuthConfig(apiKey, apiSecret, callback, null, scope);
    }

    @Test
    public void testGetScopeWhenHasScope() {
        String apiKey = "testApiKey";
        String apiSecret = "testApi Secret";
        String callback = "http://test.com/callback";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "read_only";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);

        assertEquals(scope, config.getScope());
    }

    @Test
    public void testGetScopeWhenNoScope() {
        String apiKey = "testApiKey";
        String apiSecret = "testApi Secret";
        String callback = "http://test.com/callback";
        SignatureType signatureType = SignatureType.QueryString;
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType);

        assertNull(config.getScope());
    }
}