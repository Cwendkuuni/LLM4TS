package org.scribe.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigWithKeyAndSecret() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertNotNull(config.getApiKey());
        assertNotNull(config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithKeySecretCallbackAndSignatureType() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "testCallback";
        SignatureType signatureType = SignatureType.QueryString;
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, null);
        assertNotNull(config.getApiKey());
        assertNotNull(config.getApiSecret());
        assertNotNull(config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithKeySecretCallbackAndScope() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "testCallback";
        String scope = "testScope";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, null, scope);
        assertNotNull(config.getApiKey());
        assertNotNull(config.getApiSecret());
        assertNotNull(config.getCallback());
        assertNull(config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertTrue(config.hasScope());
    }

    @Test(expected = NullPointerException.class)
    public void testNullApiKey() {
        String apiSecret = "testApiSecret";
        new OAuthConfig(null, apiSecret);
    }

    @Test(expected = NullPointerException.class)
    public void testNullApiSecret() {
        String apiKey = "testApiKey";
        new OAuthConfig(apiKey, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullCallback() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        new OAuthConfig(apiKey, apiSecret, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullSignatureType() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        new OAuthConfig(apiKey, apiSecret, null, null);
    }
}