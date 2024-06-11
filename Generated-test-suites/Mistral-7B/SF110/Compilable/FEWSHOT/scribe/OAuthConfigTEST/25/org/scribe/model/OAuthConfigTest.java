package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfig() {
        String apiKey = "abc123";
        String apiSecret = "def456";

        OAuthConfig config1 = new OAuthConfig(apiKey, apiSecret);
        assertEquals(apiKey, config1.getApiKey());
        assertEquals(apiSecret, config1.getApiSecret());
        assertNull(config1.getCallback());
        assertEquals(SignatureType.Header, config1.getSignatureType());
        assertNull(config1.getScope());

        String callback = "http://localhost:8080/callback";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "read write";

        OAuthConfig config2 = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        assertEquals(apiKey, config2.getApiKey());
        assertEquals(apiSecret, config2.getApiSecret());
        assertEquals(callback, config2.getCallback());
        assertEquals(signatureType, config2.getSignatureType());
        assertEquals(scope, config2.getScope());
    }

    @Test
    public void testOAuthConfigWithNullValues() {
        OAuthConfig config = new OAuthConfig(null, null);
        assertNull(config.getApiKey());
        assertNull(config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
    }

    @Test
    public void testOAuthConfigWithNullSignatureType() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        String callback = "http://localhost:8080/callback";
        String scope = "read write";

        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, null, scope);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertNotNull(config.getSignatureType());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertEquals(scope, config.getScope());
    }

    @Test
    public void testOAuthConfigWithEmptyScope() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        String callback = "http://localhost:8080/callback";
        String scope = "";

        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, null, scope);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertNull(config.getSignatureType());
        assertNull(config.getScope());
    }

    @Test
    public void testOAuthConfigWithNullScope() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        String callback = "http://localhost:8080/callback";

        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, null, null);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertNull(config.getSignatureType());
        assertNull(config.getScope());
    }
}