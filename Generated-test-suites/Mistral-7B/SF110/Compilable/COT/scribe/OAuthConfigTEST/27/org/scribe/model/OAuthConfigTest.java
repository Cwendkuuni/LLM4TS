package org.scribe.model;
import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigConstructorWithKeyAndSecret() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        Assert.assertEquals(apiKey, config.getApiKey());
        Assert.assertEquals(apiSecret, config.getApiSecret());
        Assert.assertEquals("oob", config.getCallback());
        Assert.assertEquals(SignatureType.Header, config.getSignatureType());
        Assert.assertNull(config.getScope());
    }

    @Test
    public void testOAuthConfigConstructorWithKeySecretCallbackAndScope() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "http://test.com";
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
    public void testOAuthConfigNullApiKey() {
        String apiSecret = "testApiSecret";
        OAuthConfig config = new OAuthConfig(null, apiSecret);
    }

    @Test(expected = NullPointerException.class)
    public void testOAuthConfigNullApiSecret() {
        String apiKey = "testApiKey";
        OAuthConfig config = new OAuthConfig(apiKey, null);
    }

    @Test(expected = NullPointerException.class)
    public void testOAuthConfigNullSignatureType() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, null, null);
    }

    @Test
    public void testHasScope() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, "testScope");
        Assert.assertTrue(config.hasScope());
        config = new OAuthConfig("testKey", "testSecret");
        Assert.assertFalse(config.hasScope());
    }

    @Test
    public void testGetApiKeyAndApiSecret() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        Assert.assertEquals(apiKey, config.getApiKey());
        Assert.assertEquals(apiSecret, config.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        String callback = "http://test.com";
        OAuthConfig config = new OAuthConfig(null, null, callback, null, null);
        Assert.assertEquals(callback, config.getCallback());
        Assert.assertEquals("oob", config.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        SignatureType signatureType = SignatureType.Header;
        OAuthConfig config = new OAuthConfig(null, null, null, signatureType, null);
        Assert.assertEquals(signatureType, config.getSignatureType());
        Assert.assertEquals(SignatureType.Header, config.getSignatureType());
    }

    @Test
    public void testGetScope() {
        String scope = "testScope";
        OAuthConfig config = new OAuthConfig(null, null, null, null, scope);
        Assert.assertEquals(scope, config.getScope());
        Assert.assertNull(config.getScope());
    }
}