package org.example;

import org.junit.Assert;
import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

    @Test(expected = NullPointerException.class)
    public void testConstructorNullApiKey() {
        new OAuthConfig(null, "testSecret");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullApiSecret() {
        new OAuthConfig("testKey", null);
    }

    @Test
    public void testConstructorEmptyApiKey() {
        OAuthConfig config = new OAuthConfig("", "testSecret");
        assertEquals("", config.getApiKey());
    }

    @Test
    public void testConstructorEmptyApiSecret() {
        OAuthConfig config = new OAuthConfig("testKey", "");
        assertEquals("", config.getApiSecret());
    }

    @Test
    public void testGetCallbackNull() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret");
        assertEquals("oob", config.getCallback());
    }

    @Test
    public void testGetScopeNull() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret");
        assertNull(config.getScope());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSignatureTypeInvalidType() {
        new OAuthConfig("testKey", "testSecret", null, SignatureType.FOO, null);
    }

    @Test
    public void testGetEmptyApiKey() {
        OAuthConfig config = new OAuthConfig("", "");
        assertEquals("", config.getApiKey());
        assertEquals("", config.getApiSecret());
    }

    @Test
    public void testHasScopeNullScope() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret");
        assertFalse(config.hasScope());
    }

    @Test
    public void testGetApiKey() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret");
        String apiKey = config.getApiKey();
        assertEquals("testKey", apiKey);
    }

    @Test
    public void testGetApiSecret() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret");
        String apiSecret = config.getApiSecret();
        assertEquals("testSecret", apiSecret);
    }

    @Test
    public void testGetCallback() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret");
        String callback = config.getCallback();
        assertEquals("oob", callback);
    }

    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, SignatureType.Query, null);
        SignatureType signatureType = config.getSignatureType();
        assertEquals(SignatureType.Query, signatureType);
    }

    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, "testScope");
        String scope = config.getScope();
        assertEquals("testScope", scope);
    }

    @Test
    public void testHasScope() {
        OAuthConfig config1 = new OAuthConfig("testKey", "testSecret");
        assertFalse(config1.hasScope());

        OAuthConfig config2 = new OAuthConfig("testKey", "testSecret", null, null, "testScope");
        assertTrue(config2.hasScope());
    }
}