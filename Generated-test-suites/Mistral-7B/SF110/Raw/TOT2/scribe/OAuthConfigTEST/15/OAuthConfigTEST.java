package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigWithApiKeyAndApiSecret() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret);
        String expectedApiKey = apiKey;
        String expectedApiSecret = apiSecret;
        String expectedCallback = "oob";
        SignatureType expectedSignatureType = SignatureType.Header;
        String expectedScope = null;

        assertSame("apiKey should be set correctly", expectedApiKey, oAuthConfig.getApiKey());
        assertSame("apiSecret should be set correctly", expectedApiSecret, oAuthConfig.getApiSecret());
        assertEquals("callback should be set to 'oob' by default", expectedCallback, oAuthConfig.getCallback());
        assertSame("signatureType should be set to 'Header' by default", expectedSignatureType, oAuthConfig.getSignatureType());
        assertNull("scope should be set to null by default", oAuthConfig.getScope());
    }

    @Test
    public void testOAuthConfigWithApiKeySecretCallbackSignatureTypeAndScope() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "testCallback";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "testScope";
        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        String expectedApiKey = apiKey;
        String expectedApiSecret = apiSecret;
        String expectedCallback = callback;
        SignatureType expectedSignatureType = signatureType;
        String expectedScope = scope;

        assertSame("apiKey should be set correctly", expectedApiKey, oAuthConfig.getApiKey());
        assertSame("apiSecret should be set correctly", expectedApiSecret, oAuthConfig.getApiKey());
        assertSame("callback should be set correctly", expectedCallback, oAuthConfig.getCallback());
        assertSame("signatureType should be set correctly", expectedSignatureType, oAuthConfig.getSignatureType());
        assertSame("scope should be set correctly", expectedScope, oAuthConfig.getScope());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOAuthConfigWithNullApiKey() {
        String apiSecret = "testApiSecret";
        OAuthConfig oAuthConfig = new OAuthConfig(null, apiSecret);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOAuthConfigWithNullApiSecret() {
        String apiKey = "testApiKey";
        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, null);
    }

    @Test
    public void testOAuthConfigWithEmptyApiKey() {
        String apiKey = "";
        String apiSecret = "testApiSecret";
        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret);
        String expectedApiKey = apiKey;
        String expectedApiSecret = apiSecret;
        String expectedCallback = "oob";
        SignatureType expectedSignatureType = SignatureType.Header;
        String expectedScope = null;

        assertSame("apiKey should be set correctly", expectedApiKey, oAuthConfig.getApiKey());
        assertSame("apiSecret should be set correctly", expectedApiSecret, oAuthConfig.getApiSecret());
        assertEquals("callback should be set to 'oob' by default", expectedCallback, oAuthConfig.getCallback());
        assertSame("signatureType should be set to 'Header' by default", expectedSignatureType, oAuthConfig.getSignatureType());
        assertNull("scope should be set to null by default", oAuthConfig.getScope());
    }

    @Test
    public void testOAuthConfigWithEmptyApiSecret() {
        String apiKey = "testApiKey";
        String apiSecret = "";
        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret);
        String expectedApiKey = apiKey;
        String expectedApiSecret = apiSecret;
        String expectedCallback = "oob";
        SignatureType expectedSignatureType = SignatureType.Header;
        String expectedScope = null;

        assertSame("apiKey should be set correctly", expectedApiKey, oAuthConfig.getApiKey());
        assertSame("apiSecret should be set correctly", expectedApiSecret, oAuthConfig.getApiSecret());
        assertEquals("callback should be set to 'oob' by default", expectedCallback, oAuthConfig.getCallback());
        assertSame("signatureType should be set to 'Header' by default", expectedSignatureType, oAuthConfig.getSignatureType());
        assertNull("scope should be set to null by default", oAuthConfig.getScope());
    }

    @Test
    public void testHasScope() {
        OAuthConfig oAuthConfig = new OAuthConfig("testApiKey", "testApiSecret");
        assertFalse("hasScope() should return false for an OAuthConfig with no scope", oAuthConfig.hasScope());

        OAuthConfig oAuthConfigWithScope = new OAuthConfig("testApiKey", "testApiSecret", null, null, "testScope");
        assertTrue("hasScope() should return true for an OAuthConfig with a scope", oAuthConfigWithScope.hasScope());
    }
}