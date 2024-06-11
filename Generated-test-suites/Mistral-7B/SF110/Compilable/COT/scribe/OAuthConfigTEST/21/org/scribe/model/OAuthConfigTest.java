package org.scribe.model; // make sure the test class is in the same package as the OAuthConfig class

import static org.junit.Assert.*;

import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

import java.util.HashMap;
import java.util.Map;

public class OAuthConfigTest {

    @org.junit.Test
    public void testOAuthConfig_BasicConstruction() {
        String key = "testKey";
        String secret = "testSecret";
        OAuthConfig config = new OAuthConfig(key, secret);
        assertEquals(key, config.getApiKey());
        assertEquals(secret, config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
    }

    @org.junit.Test
    public void testOAuthConfig_AdvancedConstruction() {
        String key = "testKey";
        String secret = "testSecret";
        String callback = "testCallback";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "testScope";
        OAuthConfig config = new OAuthConfig(key, secret, callback, signatureType, scope);
        assertEquals(key, config.getApiKey());
        assertEquals(secret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertEquals(scope, config.getScope());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testOAuthConfig_NullApiKey() {
        String apiSecret = "testSecret";
        OAuthConfig config = new OAuthConfig(null, apiSecret);
        fail("Expected NullPointerException, but got: " + config);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testOAuthConfig_NullApiSecret() {
        String apiKey = "testKey";
        OAuthConfig config = new OAuthConfig(apiKey, null);
        fail("Expected NullPointerException, but got: " + config);
    }

    @org.junit.Test
    public void testOAuthConfig_EmptyApiKey() {
        String apiKey = "";
        String apiSecret = "testSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertNotNull(config);
        assertEquals("", config.getApiKey());
    }

    @org.junit.Test
    public void testOAuthConfig_EmptyApiSecret() {
        String apiKey = "testKey";
        String apiSecret = "";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertNotNull(config);
        assertEquals(apiSecret, config.getApiSecret());
    }

    @org.junit.Test
    public void testOAuthConfig_NullSignatureType() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        SignatureType[] signatureTypes = SignatureType.values();
        SignatureType signatureType = null;
        for (SignatureType type : signatureTypes) {
            if (type == null) {
                signatureType = type;
                break;
            }
        }
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, signatureType, null);
        assertNotNull(config);
        assertEquals(SignatureType.Header, config.getSignatureType());
    }

    @org.junit.Test
    public void testOAuthConfig_NullScope() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, null, null);
        assertNotNull(config);
        assertFalse(config.hasScope());
    }

    @org.junit.Test
    public void testOAuthConfig_EmptyScope() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, null, "");
        assertNotNull(config);
        assertFalse(config.hasScope());
    }

    @org.junit.Test
    public void testOAuthConfig_DefaultCallback() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertEquals("oob", config.getCallback());
    }

    @org.junit.Test
    public void testOAuthConfig_CustomCallback() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        String callback = "testCallback";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, null, null);
        assertEquals(callback, config.getCallback());
    }
}