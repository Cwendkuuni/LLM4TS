package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigWithKeyAndSecret() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithKeyAndSecretAndCallback() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        String callback = "http://localhost:8080";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithKeyAndSecretAndCallbackAndType() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        String callback = "http://localhost:8080";
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
    public void testOAuthConfigWithKeyAndSecretAndCallbackAndTypeAndScope() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        String callback = "http://localhost:8080";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "read write";
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
        String apiSecret = "def456";
        String callback = "http://localhost:8080";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "read write";
        new OAuthConfig(null, apiSecret, callback, signatureType, scope);
    }

    @Test(expected = NullPointerException.class)
    public void testNullApiSecret() {
        String apiKey = "abc123";
        String callback = "http://localhost:8080";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "read write";
        new OAuthConfig(apiKey, null, callback, signatureType, scope);
    }

    @Test
    public void testEmptyApiKey() {
        String apiSecret = "def456";
        String callback = "http://localhost:8080";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "read write";
        OAuthConfig config = new OAuthConfig("", apiSecret, callback, signatureType, scope);
        assertEquals("", config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertTrue(config.hasScope());
    }

    @Test(expected = NullPointerException.class)
    public void testNullCallback() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "read write";
        new OAuthConfig(apiKey, apiSecret, null, signatureType, scope);
    }

    @Test
    public void testEmptyCallback() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "read write";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, "", signatureType, scope);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals("", config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertTrue(config.hasScope());
    }

    @Test
    public void testNullSignatureType() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        String callback = "http://localhost:8080";
        String scope = "read write";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, null, scope);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertNull(config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertTrue(config.hasScope());
    }

    @Test
    public void testEmptySignatureType() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        String callback = "http://localhost:8080";
        String scope = "read write";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, "", scope);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(SignatureType.HEADER, config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertTrue(config.hasScope());
    }

    @Test
    public void testNullScope() {
        String apiKey = "abc123";
        String apiSecret = "def456";
        String callback = "http://localhost:8080";
        SignatureType signatureType = SignatureType.QueryString;
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, null);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }
}