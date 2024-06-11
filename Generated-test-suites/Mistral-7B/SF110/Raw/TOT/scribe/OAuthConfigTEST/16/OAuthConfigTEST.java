package org.example;

import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigWithApiKeyAndApiSecret() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
    }

    @Test
    public void testOAuthConfigWithNullApiKeyAndApiSecretButValidCallback() {
        String callback = "testCallback";
        OAuthConfig config = new OAuthConfig(null, null, callback);
        assertNull(config.getApiKey());
        assertNull(config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertNotNull(config.getSignatureType());
        assertNull(config.getScope());
    }

    @Test
    public void testOAuthConfigWithApiKeyAndApiSecretAndCallback() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "testCallback";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertNotNull(config.getSignatureType());
        assertNull(config.getScope());
    }

    @Test
    public void testOAuthConfigWithNullApiKeyAndApiSecretButValidSignatureType() {
        String apiSecret = "testApiSecret";
        SignatureType signatureType = SignatureType.Body;
        OAuthConfig config = new OAuthConfig(null, null, null, signatureType);
        assertNull(config.getApiKey());
        assertNull(config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertNull(config.getScope());
        assertTrue(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithApiKeyAndApiSecretAndCallbackAndSignatureType() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "testCallback";
        SignatureType signatureType = SignatureType.QueryString;
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertNull(config.getScope());
        assertTrue(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithNullApiKeyAndApiSecretButValidScope() {
        String scope = "testScope";
        OAuthConfig config = new OAuthConfig(null, null, null, null, scope);
        assertNull(config.getApiKey());
        assertNull(config.getApiSecret());
        assertNull(config.getCallback());
        assertNull(config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertTrue(config.hasScope());
    }
}