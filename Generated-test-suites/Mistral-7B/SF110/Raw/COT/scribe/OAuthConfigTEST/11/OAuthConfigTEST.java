package org.scribe.model.test;

import static org.junit.Assert.*;

import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigConstructorWithKeyAndSecret() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret);
        assertEquals(apiKey, oAuthConfig.getApiKey());
        assertEquals(apiSecret, oAuthConfig.getApiSecret());
    }

    @Test
    public void testOAuthConfigConstructorWithAllParams() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "testCallback";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "testScope";
        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        assertEquals(apiKey, oAuthConfig.getApiKey());
        assertEquals(apiSecret, oAuthConfig.getApiSecret());
        assertEquals(callback, oAuthConfig.getCallback());
        assertEquals(signatureType, oAuthConfig.getSignatureType());
        assertEquals(scope, oAuthConfig.getScope());
    }

    @Test(expected = NullPointerException.class)
    public void testOAuthConfigConstructorWithNullApiKey() {
        String apiSecret = "testApiSecret";
        OAuthConfig oAuthConfig = new OAuthConfig(null, apiSecret);
    }

    @Test(expected = NullPointerException.class)
    public void testOAuthConfigConstructorWithNullApiSecret() {
        String apiKey = "testApiKey";
        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, null);
    }

    @Test
    public void testGetApiKey() {
        String apiKey = "testApiKey";
        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, "testApiSecret");
        assertEquals(apiKey, oAuthConfig.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        String apiSecret = "testApiSecret";
        OAuthConfig oAuthConfig = new OAuthConfig("testApiKey", apiSecret);
        assertEquals(apiSecret, oAuthConfig.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        String callback = "testCallback";
        OAuthConfig oAuthConfig = new OAuthConfig("testApiKey", "testApiSecret", callback, null, null);
        assertEquals(callback, oAuthConfig.getCallback());
        OAuthConfig oAuthConfig2 = new OAuthConfig("testApiKey", "testApiSecret");
        assertEquals("oob", oAuthConfig2.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        SignatureType signatureType = SignatureType.Header;
        OAuthConfig oAuthConfig = new OAuthConfig("testApiKey", "testApiSecret", null, signatureType, null);
        assertEquals(signatureType, oAuthConfig.getSignatureType());
        OAuthConfig oAuthConfig2 = new OAuthConfig("testApiKey", "testApiSecret");
        assertEquals(SignatureType.Header, oAuthConfig2.getSignatureType());
    }

    @Test
    public void testGetScope() {
        String scope = "testScope";
        OAuthConfig oAuthConfig = new OAuthConfig("testApiKey", "testApiSecret", null, null, scope);
        assertEquals(scope, oAuthConfig.getScope());
        OAuthConfig oAuthConfig2 = new OAuthConfig("testApiKey", "testApiSecret");
        assertNull(oAuthConfig2.getScope());
    }

    @Test
    public void testHasScope() {
        String scope = "testScope";
        OAuthConfig oAuthConfig = new OAuthConfig("testApiKey", "testApiSecret", null, null, scope);
        assertTrue(oAuthConfig.hasScope());
        OAuthConfig oAuthConfig2 = new OAuthConfig("testApiKey", "testApiSecret");
        assertFalse(oAuthConfig2.hasScope());
    }
}