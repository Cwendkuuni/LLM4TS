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
        OAuthConfig oauthConfig = new OAuthConfig(apiKey, apiSecret);
        assertEquals(apiKey, oauthConfig.getApiKey());
        assertEquals(apiSecret, oauthConfig.getApiSecret());
        assertNull(oauthConfig.getCallback());
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
        assertNull(oauthConfig.getScope());
    }

    @Test
    public void testOAuthConfigConstructorWithKeySecretCallbackScopeAndSignatureType() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "testCallback";
        SignatureType signatureType = SignatureType.Query;
        String scope = "testScope";
        OAuthConfig oauthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        assertEquals(apiKey, oauthConfig.getApiKey());
        assertEquals(apiSecret, oauthConfig.getApiSecret());
        assertEquals(callback, oauthConfig.getCallback());
        assertEquals(signatureType, oauthConfig.getSignatureType());
        assertEquals(scope, oauthConfig.getScope());
    }

    @Test(expected = NullPointerException.class)
    public void testOAuthConfigConstructorWithNullApiKey() {
        String apiSecret = "testApiSecret";
        OAuthConfig oauthConfig = new OAuthConfig(null, apiSecret);
        fail("Expected NullPointerException but got: " + oauthConfig);
    }

    @Test(expected = NullPointerException.class)
    public void testOAuthConfigConstructorWithNullApiSecret() {
        String apiKey = "testApiKey";
        OAuthConfig oauthConfig = new OAuthConfig(apiKey, null);
        fail("Expected NullPointerException but got: " + oauthConfig);
    }

    @Test
    public void testGetApiKey() {
        String apiKey = "testApiKey";
        OAuthConfig oauthConfig = new OAuthConfig(apiKey, "testApiSecret");
        assertEquals(apiKey, oauthConfig.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        String apiSecret = "testApiSecret";
        OAuthConfig oauthConfig = new OAuthConfig("testApiKey", apiSecret);
        assertEquals(apiSecret, oauthConfig.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        String callback = "testCallback";
        OAuthConfig oauthConfig = new OAuthConfig("testApiKey", "testApiSecret", callback);
        assertEquals(callback, oauthConfig.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        SignatureType signatureType = SignatureType.Query;
        OAuthConfig oauthConfig = new OAuthConfig("testApiKey", "testApiSecret", null, signatureType, null);
        assertEquals(signatureType, oauthConfig.getSignatureType());
    }

    @Test
    public void testGetScope() {
        String scope = "testScope";
        OAuthConfig oauthConfig = new OAuthConfig("testApiKey", "testApiSecret", null, null, scope);
        assertEquals(scope, oauthConfig.getScope());
    }

    @Test
    public void testHasScope() {
        OAuthConfig oauthConfig1 = new OAuthConfig("testApiKey", "testApiSecret");
        assertFalse(oauthConfig1.hasScope());

        String scope = "testScope";
        OAuthConfig oauthConfig2 = new OAuthConfig("testApiKey", "testApiSecret", null, null, scope);
        assertTrue(oauthConfig2.hasScope());
    }

}