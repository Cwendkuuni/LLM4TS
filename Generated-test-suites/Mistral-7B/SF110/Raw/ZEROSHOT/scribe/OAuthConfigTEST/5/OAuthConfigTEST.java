package org.junit.tests;

import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

import static org.junit.Assert.*;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigWithApiKeyAndApiSecret() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig oauthConfig = new OAuthConfig(apiKey, apiSecret);
        assertEquals(apiKey, oauthConfig.getApiKey());
        assertEquals(apiSecret, oauthConfig.getApiSecret());
    }

    @Test
    public void testOAuthConfigWithApiKeyAndApiSecretAndCallback() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "http://test.com";
        OAuthConfig oauthConfig = new OAuthConfig(apiKey, apiSecret, callback, null, null);
        assertEquals(apiKey, oauthConfig.getApiKey());
        assertEquals(apiSecret, oauthConfig.getApiSecret());
        assertEquals(callback, oauthConfig.getCallback());
    }

    @Test
    public void testOAuthConfigWithApiKeyAndApiSecretAndCallbackAndSignatureType() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "http://test.com";
        SignatureType signatureType = SignatureType.QueryString;
        OAuthConfig oauthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, null);
        assertEquals(apiKey, oauthConfig.getApiKey());
        assertEquals(apiSecret, oauthConfig.getApiSecret());
        assertEquals(callback, oauthConfig.getCallback());
        assertEquals(signatureType, oauthConfig.getSignatureType());
    }

    @Test
    public void testOAuthConfigWithApiKeyAndApiSecretAndSignatureType() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        SignatureType signatureType = SignatureType.Header;
        OAuthConfig oauthConfig = new OAuthConfig(apiKey, apiSecret, null, signatureType, null);
        assertEquals(apiKey, oauthConfig.getApiKey());
        assertEquals(apiSecret, oauthConfig.getApiSecret());
        assertEquals(signatureType, oauthConfig.getSignatureType());
    }

    @Test
    public void testOAuthConfigWithApiKeyAndApiSecretAndScope() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String scope = "testScope";
        OAuthConfig oauthConfig = new OAuthConfig(apiKey, apiSecret, null, null, scope);
        assertEquals(apiKey, oauthConfig.getApiKey());
        assertEquals(apiSecret, oauthConfig.getApiSecret());
        assertEquals(scope, oauthConfig.getScope());
    }

    @Test
    public void testOAuthConfigWithDefaults() {
        OAuthConfig oauthConfig = new OAuthConfig("testApiKey", "testApiSecret");
        assertEquals("oob", oauthConfig.getCallback());
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
        assertNull(oauthConfig.getScope());
    }

    @Test
    public void testHasScope() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String scope = "testScope";
        OAuthConfig oauthConfig = new OAuthConfig(apiKey, apiSecret, null, null, scope);
        assertTrue(oauthConfig.hasScope());
    }

    @Test
    public void testNoScope() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig oauthConfig = new OAuthConfig(apiKey, apiSecret, null, null, null);
        assertFalse(oauthConfig.hasScope());
    }
}