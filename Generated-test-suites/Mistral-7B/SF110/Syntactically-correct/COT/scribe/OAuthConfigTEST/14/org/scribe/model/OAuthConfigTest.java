package org.scribe.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigWithKeyAndSecret() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);

        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.HEADER, config.getSignatureType());
        assertNull(config.getScope());
    }

    @Test
    public void testOAuthConfigWithKeySecretCallbackAndType() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "testCallback";
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
    }

    @Test(expected = NullPointerException.class)
    public void testNullApiSecret() {
        String apiKey = "testApiKey";
        OAuthConfig config = new OAuthConfig(apiKey, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullCallback() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSignatureType() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, null, null);
    }

    @Test
    public void testEmptyScope() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = null;
        SignatureType signatureType = SignatureType.Header;
        String scope = "";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithNullScope() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = null;
        SignatureType signatureType = SignatureType.Header;
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, null);
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithEmptyCallback() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "";
        SignatureType signatureType = SignatureType.Header;
        String scope = "testScope";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        assertEquals("oob", config.getCallback());
    }
}