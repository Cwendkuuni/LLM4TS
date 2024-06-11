package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.OAuthConfig;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigWithKeyAndSecret() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret");
        assertEquals("testKey", config.getApiKey());
        assertEquals("testSecret", config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithKeyAndSecretAndCallback() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback");
        assertEquals("testKey", config.getApiKey());
        assertEquals("testSecret", config.getApiSecret());
        assertEquals("testCallback", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithKeyAndSecretAndCallbackAndType() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.Query);
        assertEquals("testKey", config.getApiKey());
        assertEquals("testSecret", config.getApiSecret());
        assertEquals("testCallback", config.getCallback());
        assertEquals(SignatureType.Query, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithKeyAndSecretAndCallbackAndTypeAndScope() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.Query, "testScope");
        assertEquals("testKey", config.getApiKey());
        assertEquals("testSecret", config.getApiSecret());
        assertEquals("testCallback", config.getCallback());
        assertEquals(SignatureType.Query, config.getSignatureType());
        assertEquals("testScope", config.getScope());
        assertTrue(config.hasScope());
    }

    @Test(expected = NullPointerException.class)
    public void testOAuthConfigWithNullKey() {
        new OAuthConfig(null, "testSecret");
    }

    @Test(expected = NullPointerException.class)
    public void testOAuthConfigWithNullSecret() {
        new OAuthConfig("testKey", null);
    }

    @Test
    public void testOAuthConfigWithEmptyStringAsApiKey() {
        OAuthConfig config = new OAuthConfig("", "testSecret");
        assertEquals("", config.getApiKey());
        assertNull(config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithEmptyStringAsApiSecret() {
        OAuthConfig config = new OAuthConfig("testKey", "", "testCallback", SignatureType.Header, "testScope");
        assertEquals("testKey", config.getApiKey());
        assertEquals("", config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertEquals("testScope", config.getScope());
        assertTrue(config.hasScope());
    }
}