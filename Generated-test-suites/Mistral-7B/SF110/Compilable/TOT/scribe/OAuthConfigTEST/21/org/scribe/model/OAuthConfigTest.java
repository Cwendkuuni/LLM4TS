package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {
    @Test
    public void testOAuthConfigWithApiKeyAndApiSecret() {
        String apiKey = "test_api_key";
        String apiSecret = "test_api_secret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        String expectedApiKey = apiKey;
        String expectedApiSecret = apiSecret;
        String expectedCallback = "oob";
        SignatureType expectedSignatureType = SignatureType.Header;
        String expectedScope = null;
        assertEquals(expectedApiKey, config.getApiKey());
        assertEquals(expectedApiSecret, config.getApiSecret());
        assertEquals(expectedCallback, config.getCallback());
        assertEquals(expectedSignatureType, config.getSignatureType());
        assertEquals(expectedScope, config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithAllArguments() {
        String apiKey = "test_api_key";
        String apiSecret = "test_api_secret";
        String callback = "http://localhost:8080/callback";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "test_scope";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        String expectedApiKey = apiKey;
        String expectedApiSecret = apiSecret;
        String expectedCallback = callback;
        SignatureType expectedSignatureType = signatureType;
        String expectedScope = scope;
        assertEquals(expectedApiKey, config.getApiKey());
        assertEquals(expectedApiSecret, config.getApiSecret());
        assertEquals(expectedCallback, config.getCallback());
        assertEquals(expectedSignatureType, config.getSignatureType());
        assertEquals(expectedScope, config.getScope());
        assertTrue(config.hasScope());
    }

    @Test(expected = NullPointerException.class)
    public void testNullApiKey() {
        String apiSecret = "test_api_secret";
        OAuthConfig config = new OAuthConfig(null, apiSecret);
    }

    @Test(expected = NullPointerException.class)
    public void testNullApiSecret() {
        String apiKey = "test_api_key";
        OAuthConfig config = new OAuthConfig(apiKey, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyApiKey() {
        String apiKey = "";
        String apiSecret = "test_api_secret";
        try {
            new OAuthConfig(apiKey, apiSecret);
        } catch (IllegalArgumentException e) {
            assertEquals("API key cannot be empty", e.getMessage());
            return;
        }
        fail("Exception not thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyApiSecret() {
        String apiKey = "test_api_key";
        String apiSecret = "";
        try {
            new OAuthConfig(apiKey, apiSecret);
        } catch (IllegalArgumentException e) {
            assertEquals("API secret cannot be empty", e.getMessage());
            return;
        }
        fail("Exception not thrown");
    }

    @Test(expected = NullPointerException.class)
    public void testNullCallback() {
        String apiKey = "test_api_key";
        String apiSecret = "test_api_secret";
        SignatureType signatureType = SignatureType.Header;
        String scope = "test_scope";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, signatureType, scope);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyScope() {
        String apiKey = "test_api_key";
        String apiSecret = "test_api_secret";
        String callback = "http://localhost:8080/callback";
        SignatureType signatureType = SignatureType.Header;
        String scope = "";
        try {
            new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        } catch (IllegalArgumentException e) {
            assertEquals("Scope cannot be empty", e.getMessage());
            return;
        }
        fail("Exception not thrown");
    }
}