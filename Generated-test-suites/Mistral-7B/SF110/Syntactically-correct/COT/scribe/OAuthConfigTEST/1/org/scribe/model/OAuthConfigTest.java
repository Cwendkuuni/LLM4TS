package org.scribe.model.test;

import org.junit.Assert;
import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigWithKeyAndSecret() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        Assert.assertEquals(apiKey, config.getApiKey());
        Assert.assertEquals(apiSecret, config.getApiSecret());
    }

    @Test
    public void testOAuthConfigWithKeySecretCallback() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "http://test.com/callback";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, null, null);
        Assert.assertEquals(apiKey, config.getApiKey());
        Assert.assertEquals(apiSecret, config.getApiSecret());
        Assert.assertEquals(callback, config.getCallback());
    }

    @Test
    public void testOAuthConfigWithKeySecretCallbackSignatureAndScope() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "http://test.com/callback";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "testScope";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        Assert.assertEquals(apiKey, config.getApiKey());
        Assert.assertEquals(apiSecret, config.getApiSecret());
        Assert.assertEquals(callback, config.getCallback());
        Assert.assertEquals(signatureType, config.getSignatureType());
        Assert.assertEquals(scope, config.getScope());
    }

    @Test(expected = NullPointerException.class)
    public void testOAuthConfigWithNullApiKey() {
        String apiSecret = "testApiSecret";
        OAuthConfig config = new OAuthConfig(null, apiSecret);
    }

    @Test(expected = NullPointerException.class)
    public void testOAuthConfigWithNullApiSecret() {
        String apiKey = "testApiKey";
        OAuthConfig config = new OAuthConfig(apiKey, null);
    }

    @Test
    public void testOAuthConfigWithNullCallbackDefaultValues() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        Assert.assertEquals("oob", config.getCallback());
        Assert.assertEquals(SignatureType.Header, config.getSignatureType());
        Assert.assertNull(config.getScope());
    }

    @Test
    public void testOAuthConfigWithEmptyScope() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "http://test.com/callback";
        String scope = "";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, null, scope);
        Assert.assertEquals(apiKey, config.getApiKey());
        Assert.assertEquals(apiSecret, config.getApiSecret());
        Assert.assertEquals(callback, config.getCallback());
        Assert.assertEquals(scope, config.getScope());
        Assert.assertTrue(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithNullScope() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "http://test.com/callback";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, null, null);
        Assert.assertNull(config.getScope());
        Assert.assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithInvalidSignatureType() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "http://test.com/callback";
        SignatureType invalidSignatureType = new SignatureType("InvalidType");
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, invalidSignatureType, null);
        Assert.assertEquals(apiKey, config.getApiKey());
        Assert.assertEquals(apiSecret, config.getApiSecret());
        Assert.assertEquals(callback, config.getCallback());
        Assert.assertEquals(SignatureType.Header, config.getSignatureType()); // Falls back to default value
    }
}