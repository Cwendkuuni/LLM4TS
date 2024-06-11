package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigWithKeyAndSecret() {
        String apiKey = "myApiKey";
        String apiSecret = "myApiSecret";
        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret);
        assertEquals(apiKey, oAuthConfig.getApiKey());
        assertEquals(apiSecret, oAuthConfig.getApiSecret());
        assertNull(oAuthConfig.getCallback());
        assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
        assertNull(oAuthConfig.getScope());
    }

    @Test
    public void testOAuthConfigWithKeyAndSecretAndCallback() {
        String apiKey = "myApiKey";
        String apiSecret = "myApiSecret";
        String callback = "http://example.com";
        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback);
        assertEquals(apiKey, oAuthConfig.getApiKey());
        assertEquals(apiSecret, oAuthConfig.getApiSecret());
        assertEquals(callback, oAuthConfig.getCallback());
        assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
        assertNull(oAuthConfig.getScope());
    }

    @Test
    public void testOAuthConfigWithKeyAndSecretAndCallbackAndSignatureType() {
        String apiKey = "myApiKey";
        String apiSecret = "myApiSecret";
        String callback = "http://example.com";
        SignatureType signatureType = SignatureType.Query;
        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType);
        assertEquals(apiKey, oAuthConfig.getApiKey());
        assertEquals(apiSecret, oAuthConfig.getApiSecret());
        assertEquals(callback, oAuthConfig.getCallback());
        assertEquals(signatureType, oAuthConfig.getSignatureType());
        assertNull(oAuthConfig.getScope());
    }

    @Test
    public void testOAuthConfigWithKeyAndSecretAndCallbackAndSignatureTypeAndScope() {
        String apiKey = "myApiKey";
        String apiSecret = "myApiSecret";
        String callback = "http://example.com";
        SignatureType signatureType = SignatureType.Query;
        String scope = "read write";
        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        assertEquals(apiKey, oAuthConfig.getApiKey());
        assertEquals(apiSecret, oAuthConfig.getApiSecret());
        assertEquals(callback, oAuthConfig.getCallback());
        assertEquals(signatureType, oAuthConfig.getSignatureType());
        assertEquals(scope, oAuthConfig.getScope());
        assertEquals(true, oAuthConfig.hasScope());
    }

    @Test
    public void testGettersReturnCorrectValuesWhenNullValuesArePassed() {
        String apiKey = null;
        String apiSecret = null;
        String callback = null;
        SignatureType signatureType = null;
        String scope = null;
        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        assertNull(oAuthConfig.getApiKey());
        assertNull(oAuthConfig.getApiSecret());
        assertNull(oAuthConfig.getCallback());
        assertNull(oAuthConfig.getSignatureType());
        assertNull(oAuthConfig.getScope());
        assertFalse(oAuthConfig.hasScope());
    }
}