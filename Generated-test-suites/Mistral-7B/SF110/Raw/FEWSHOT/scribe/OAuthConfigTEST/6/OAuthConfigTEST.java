package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigWithApiKeyAndApiSecret() {
        String apiKey = "apiKey123";
        String apiSecret = "apiSecret456";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithApiKeyAndApiSecretAndCallback() {
        String apiKey = "apiKey123";
        String apiSecret = "apiSecret456";
        String callback = "http://example.com/callback";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithApiKeyAndApiSecretAndCallbackAndSignatureType() {
        String apiKey = "apiKey123";
        String apiSecret = "apiSecret456";
        String callback = "http://example.com/callback";
        SignatureType signatureType = SignatureType.QueryParam;
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithApiKeyAndApiSecretAndCallbackAndSignatureTypeAndScope() {
        String apiKey = "apiKey123";
        String apiSecret = "apiSecret456";
        String callback = "http://example.com/callback";
        SignatureType signatureType = SignatureType.QueryParam;
        String scope = "scope123";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertTrue(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithNullApiKey() {
        String apiSecret = "apiSecret456";
        String callback = "http://example.com/callback";
        SignatureType signatureType = SignatureType.QueryParam;
        String scope = "scope123";
        try {
            new OAuthConfig(null, apiSecret, callback, signatureType, scope);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testOAuthConfigWithNullApiSecret() {
        String apiKey = "apiKey123";
        String callback = "http://example.com/callback";
        SignatureType signatureType = SignatureType.QueryParam;
        String scope = "scope123";
        try {
            new OAuthConfig(apiKey, null, callback, signatureType, scope);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testOAuthConfigWithEmptyApiKey() {
        String apiSecret = "apiSecret456";
        String callback = "http://example.com/callback";
        SignatureType signatureType = SignatureType.QueryParam;
        String scope = "scope123";
        OAuthConfig config = new OAuthConfig("", apiSecret, callback, signatureType, scope);
        assertEquals("", config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithEmptyApiSecret() {
        String apiKey = "apiKey123";
        String callback = "http://example.com/callback";
        SignatureType signatureType = SignatureType.QueryParam;
        String scope = "scope123";
        OAuthConfig config = new OAuthConfig(apiKey, "", callback, signatureType, scope);
        assertEquals(apiKey, config.getApiKey());
        assertEquals("", config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithNullCallback() {
        String apiKey = "apiKey123";
        String apiSecret = "apiSecret456";
        SignatureType signatureType = SignatureType.QueryParam;
        String scope = "scope123";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, signatureType, scope);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithNullSignatureType() {
        String apiKey = "apiKey123";
        String apiSecret = "apiSecret456";
        String callback = "http://example.com/callback";
        String scope = "scope123";
        try {
            new OAuthConfig(apiKey, apiSecret, callback, null, scope);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testOAuthConfigWithNullScope() {
        String apiKey = "apiKey123";
        String apiSecret = "apiSecret456";
        String callback = "http://example.com/callback";
        SignatureType signatureType = SignatureType.QueryParam;
        try {
            new OAuthConfig(apiKey, apiSecret, callback, signatureType, null);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}