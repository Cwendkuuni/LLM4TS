package org.scribe.model.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigConstructorWithApiKeyAndApiSecret() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret);
        String actualApiKey = oAuthConfig.getApiKey();
        String actualApiSecret = oAuthConfig.getApiSecret();
        assertEquals("testApiKey", actualApiKey);
        assertEquals("testApiSecret", actualApiSecret);
    }

    @Test
    public void testOAuthConfigConstructorWithApiKeySecretAndCallback() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "testCallback";
        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback);
        String actualApiKey = oAuthConfig.getApiKey();
        String actualApiSecret = oAuthConfig.getApiSecret();
        String actualCallback = oAuthConfig.getCallback();
        assertEquals("testApiKey", actualApiKey);
        assertEquals("testApiSecret", actualApiSecret);
        assertEquals("testCallback", actualCallback);
    }

    @Test
    public void testOAuthConfigConstructorWithApiKeySecretCallbackSignatureTypeAndScope() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "testCallback";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "testScope";
        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        String actualApiKey = oAuthConfig.getApiKey();
        String actualApiSecret = oAuthConfig.getApiSecret();
        String actualCallback = oAuthConfig.getCallback();
        SignatureType actualSignatureType = oAuthConfig.getSignatureType();
        String actualScope = oAuthConfig.getScope();
        assertEquals("testApiKey", actualApiKey);
        assertEquals("testApiSecret", actualApiSecret);
        assertEquals("testCallback", actualCallback);
        assertEquals(SignatureType.QueryString, actualSignatureType);
        assertEquals("testScope", actualScope);
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

    @Test(expected = NullPointerException.class)
    public void testOAuthConfigConstructorWithNullCallback() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, null);
    }

    @Test
    public void testHasScopeMethod() {
        OAuthConfig oAuthConfig = new OAuthConfig("testApiKey", "testApiSecret");
        assertFalse(oAuthConfig.hasScope());
        
        OAuthConfig oAuthConfigWithScope = new OAuthConfig("testApiKey", "testApiSecret", null, null, "testScope");
        assertTrue(oAuthConfigWithScope.hasScope());
    }

    @Test
    public void testGetCallbackWithNullCallback() {
        OAuthConfig oAuthConfig = new OAuthConfig("testApiKey", "testApiSecret");
        String actualCallback = oAuthConfig.getCallback();
        assertEquals("oob", actualCallback);
    }

    @Test
    public void testGetCallbackWithProvidedCallback() {
        String callback = "testCallback";
        OAuthConfig oAuthConfig = new OAuthConfig("testApiKey", "testApiSecret", callback, null, null);
        String actualCallback = oAuthConfig.getCallback();
        assertEquals(callback, actualCallback);
    }

    @Test
    public void testGetSignatureTypeWithNullSignatureType() {
        OAuthConfig oAuthConfig = new OAuthConfig("testApiKey", "testApiSecret");
        SignatureType actualSignatureType = oAuthConfig.getSignatureType();
        assertEquals(SignatureType.Header, actualSignatureType);
    }

    @Test
    public void testGetSignatureTypeWithProvidedSignatureType() {
        SignatureType signatureType = SignatureType.QueryString;
        OAuthConfig oAuthConfig = new OAuthConfig("testApiKey", "testApiSecret", null, signatureType, null);
        SignatureType actualSignatureType = oAuthConfig.getSignatureType();
        assertEquals(signatureType, actualSignatureType);
    }

}