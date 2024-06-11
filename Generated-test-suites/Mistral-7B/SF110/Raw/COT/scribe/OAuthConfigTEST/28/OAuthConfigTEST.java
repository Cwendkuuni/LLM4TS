package org.scribe.model.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigConstructorWithKeyAndSecret() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
    }

    @Test
    public void testOAuthConfigConstructorWithKeySecretCallbackScopeAndSignatureType() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "http://test.com/callback";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "testScope";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertEquals(scope, config.getScope());
    }

    @Test
    public void testOAuthConfigDefaultCallback() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertEquals("oob", config.getCallback());
    }

    @Test
    public void testOAuthConfigDefaultSignatureType() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertEquals(SignatureType.Header, config.getSignatureType());
    }

    @Test(expected = NullPointerException.class)
    public void testOAuthConfigNullApiKey() {
        String apiSecret = "testApiSecret";
        try {
            new OAuthConfig(null, apiSecret);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            //expected exception
        }
    }

    @Test(expected = NullPointerException.class)
    public void testOAuthConfigNullApiSecret() {
        String apiKey = "testApiKey";
        try {
            new OAuthConfig(apiKey, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            //expected exception
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOAuthConfigInvalidSignatureType() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        SignatureType invalidSignatureType = new SignatureType("InvalidType");
        try {
            new OAuthConfig(apiKey, apiSecret, null, invalidSignatureType, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected exception
        }
    }

    @Test
    public void testOAuthConfigHasScope() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String scope = "testScope";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, null, scope);
        assertTrue(config.hasScope());
    }

    @Test
    public void testOAuthConfigNoScope() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertFalse(config.hasScope());
    }
}