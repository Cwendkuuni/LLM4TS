package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigWithKeyAndSecret() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
    }

    @Test
    public void testOAuthConfigWithKeySecretCallbackAndScope() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "http://test.com";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "testScope";
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
        String apiSecret = "testApiSecret";
        OAuthConfig config = new OAuthConfig(null, apiSecret);
        fail("Expected NullPointerException but was not thrown");
    }

    @Test(expected = NullPointerException.class)
    public void testNullApiSecret() {
        String apiKey = "testApiKey";
        OAuthConfig config = new OAuthConfig(apiKey, null);
        fail("Expected NullPointerException but was not thrown");
    }

    @Test
    public void testDefaultCallback() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertEquals("oob", config.getCallback());
    }

    @Test
    public void testDefaultSignatureType() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertEquals(SignatureType.Header, config.getSignatureType());
    }

    @Test
    public void testEmptyScope() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String scope = "";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, null, scope);
        assertEquals(scope, config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testNullSignatureType() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String scope = "testScope";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, null, scope);
        assertEquals(SignatureType.Header, config.getSignatureType());
    }

    @Test
    public void testNullCallback() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String scope = "testScope";
        SignatureType signatureType = SignatureType.Header;
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, signatureType, scope);
        assertNull(config.getCallback());
    }

}