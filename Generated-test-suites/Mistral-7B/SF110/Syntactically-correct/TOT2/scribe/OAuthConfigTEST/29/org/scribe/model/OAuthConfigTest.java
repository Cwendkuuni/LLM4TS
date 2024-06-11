package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigWithKeyAndSecret() {
        String key = "testKey";
        String secret = "testSecret";
        OAuthConfig config = new OAuthConfig(key, secret);
        String expectedApiKey = key;
        String expectedApiSecret = secret;
        String expectedCallback = "oob";
        SignatureType expectedSignatureType = SignatureType.Header;
        String expectedScope = null;
        assertNotNull(config);
        assertEquals(expectedApiKey, config.getApiKey());
        assertEquals(expectedApiSecret, config.getApiSecret());
        assertEquals(expectedCallback, config.getCallback());
        assertEquals(expectedSignatureType, config.getSignatureType());
        assertEquals(expectedScope, config.getScope());
        assertEquals(false, config.hasScope());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOAuthConfigWithNullApiKey() {
        String apiKey = null;
        String apiSecret = "testSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOAuthConfigWithNullApiSecret() {
        String apiKey = "testKey";
        String apiSecret = null;
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOAuthConfigWithNullCallback() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null);
    }

    @Test
    public void testOAuthConfigWithEmptyCallback() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        String expectedCallback = "";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, "", null, null);
        assertEquals(expectedCallback, config.getCallback());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOAuthConfigWithNullScope() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        String callback = "oob";
        String scope = null;
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, null, scope);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOAuthConfigWithEmptyScope() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        String callback = "oob";
        String scope = "";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, null, scope);
    }

    @Test
    public void testOAuthConfigWithCallback() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        String callback = "testCallback";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, null, null);
        assertEquals(callback, config.getCallback());
    }

    @Test
    public void testOAuthConfigWithScope() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        SignatureType signatureType = SignatureType.Header;
        String scope = "testScope";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, signatureType, scope);
        assertEquals(signatureType, config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertTrue(config.hasScope());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOAuthConfigWithInvalidSignatureType() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        String callback = "oob";
        String scope = "testScope";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, null, scope);
        SignatureType invalidSignatureType = new SignatureType("InvalidSignatureType");
        config.setSignatureType(invalidSignatureType);
    }
}