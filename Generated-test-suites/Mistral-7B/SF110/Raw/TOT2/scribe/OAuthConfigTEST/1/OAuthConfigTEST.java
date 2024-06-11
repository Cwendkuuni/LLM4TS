package org.example.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.OAuthConfig;

public class OAuthConfigTest {

    @Test
    public void testGetApiKey() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret");
        String apiKey = config.getApiKey();
        Assert.assertEquals("testKey", apiKey);
    }

    @Test
    public void testGetApiKeyWithNullApiKey() {
        OAuthConfig config = new OAuthConfig(null, "testSecret");
        Assert.assertNull(config.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret");
        String apiSecret = config.getApiSecret();
        Assert.assertEquals("testSecret", apiSecret);
    }

    @Test
    public void testGetApiSecretWithNullApiSecret() {
        OAuthConfig config = new OAuthConfig("testKey", null);
        Assert.assertNull(config.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback");
        String callback = config.getCallback();
        Assert.assertEquals("testCallback", callback);
    }

    @Test
    public void testGetCallbackWithNullCallback() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret");
        String callback = config.getCallback();
        Assert.assertEquals("oob", callback);
    }

    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret");
        SignatureType signatureType = config.getSignatureType();
        Assert.assertEquals(SignatureType.Header, signatureType);
    }

    @Test
    public void testGetSignatureTypeWithNullSignatureType() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret");
        SignatureType signatureType = config.getSignatureType();
        Assert.assertEquals(SignatureType.Header, signatureType);
    }

    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, "testScope");
        String scope = config.getScope();
        Assert.assertEquals("testScope", scope);
    }

    @Test
    public void testGetScopeWithNullScope() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret");
        String scope = config.getScope();
        Assert.assertNull(scope);
    }

    @Test
    public void testHasScopeWithNullScope() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret");
        boolean hasScope = config.hasScope();
        Assert.assertFalse(hasScope);
    }

    @Test
    public void testHasScopeWithScope() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, "testScope");
        boolean hasScope = config.hasScope();
        Assert.assertTrue(hasScope);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullApiKey() {
        OAuthConfig config = new OAuthConfig(null, "testSecret");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullApiSecret() {
        OAuthConfig config = new OAuthConfig("testKey", null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullCallback() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullSignatureType() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null);
    }
}

//