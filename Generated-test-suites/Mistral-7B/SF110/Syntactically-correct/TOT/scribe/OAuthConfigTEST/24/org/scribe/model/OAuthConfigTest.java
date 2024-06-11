package org.scribe.model;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {
    @Test
    public void testOAuthConfigWithValidArguments() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "http://localhost:8080/callback";
        SignatureType signatureType = SignatureType.Header;
        String scope = "testScope";

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
        String apiSecret = "testApiSecret";
        String callback = "http://localhost:8080/callback";
        SignatureType signatureType = SignatureType.Header;
        String scope = "testScope";

        try {
            new OAuthConfig(null, apiSecret, callback, signatureType, scope);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOAuthConfigWithNullApiSecret() {
        String apiKey = "testApiKey";
        String callback = "http://localhost:8080/callback";
        SignatureType signatureType = SignatureType.Header;
        String scope = "testScope";

        try {
            new OAuthConfig(apiKey, null, callback, signatureType, scope);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOAuthConfigWithNullCallback() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        SignatureType signatureType = SignatureType.Header;
        String scope = "testScope";

        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, signatureType, scope);
        assertEquals("oob", config.getCallback());
    }

    @Test
    public void testOAuthConfigWithEmptyApiKey() {
        String apiSecret = "testApiSecret";
        String callback = "http://localhost:8080/callback";
        SignatureType signatureType = SignatureType.Header;
        String scope = "testScope";

        try {
            new OAuthConfig("", apiSecret, callback, signatureType, scope);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOAuthConfigWithEmptyApiSecret() {
        String apiKey = "testApiKey";
        String callback = "http://localhost:8080/callback";
        SignatureType signatureType = SignatureType.Header;
        String scope = "testScope";

        try {
            new OAuthConfig(apiKey, "", callback, signatureType, scope);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}