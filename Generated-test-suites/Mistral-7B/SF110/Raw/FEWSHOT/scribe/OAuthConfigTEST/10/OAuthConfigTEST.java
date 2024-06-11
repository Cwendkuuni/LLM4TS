package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigWithNullValues() {
        OAuthConfig config = new OAuthConfig(null, null);
        assertNull(config.getApiKey());
        assertNull(config.getApiSecret());
        assertEquals("oob", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithAllValues() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        String callback = "http://example.com/callback";
        SignatureType signatureType = SignatureType.Query;
        String scope = "read_only,write_access";

        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);

        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertTrue(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithNullCallback() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        SignatureType signatureType = SignatureType.Header;
        String scope = "read_only,write_access";

        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, signatureType, scope);

        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals("oob", config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertTrue(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithNullSignatureType() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        String callback = "http://example.com/callback";
        String scope = "read_only,write_access";

        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, null, scope);

        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertTrue(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithEmptyStringScope() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        String callback = "http://example.com/callback";
        SignatureType signatureType = SignatureType.Header;
        String scope = "";

        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);

        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }
}