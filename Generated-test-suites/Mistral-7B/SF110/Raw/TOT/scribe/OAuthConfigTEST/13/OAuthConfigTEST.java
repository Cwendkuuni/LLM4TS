
package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testDefaultConstructor() {
        OAuthConfig oauthConfig = new OAuthConfig();
        assertNull(oauthConfig.getApiKey());
        assertNull(oauthConfig.getApiSecret());
        assertNull(oauthConfig.getCallback());
        assertNull(oauthConfig.getScope());
        assertEquals(SignatureType.HEADER, oauthConfig.getSignatureType());
    }

    @Test
    public void testGetters() {
        String key = "testKey";
        String secret = "testSecret";
        String callback = "http://test.com";
        SignatureType signatureType = SignatureType.QueryParam;
        String scope = "testScope";

        OAuthConfig oauthConfig = new OAuthConfig(key, secret, callback, signatureType, scope);

        assertEquals(key, oauthConfig.getApiKey());
        assertEquals(secret, oauthConfig.getApiSecret());
        assertEquals(callback, oauthConfig.getCallback());
        assertEquals(signatureType, oauthConfig.getSignatureType());
        assertEquals(scope, oauthConfig.getScope());
        assertTrue(oauthConfig.hasScope());
    }

    @Test
    public void testNullValuesConstructor() {
        OAuthConfig oauthConfig = new OAuthConfig(null, null, null, null, null);
        assertNull(oauthConfig.getApiKey());
        assertNull(oauthConfig.getApiSecret());
        assertNull(oauthConfig.getCallback());
        assertNull(oauthConfig.getScope());
        assertEquals(SignatureType.HEADER, oauthConfig.getSignatureType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSignatureType() {
        String key = "testKey";
        String secret = "testSecret";
        String callback = "http://test.com";
        String signatureType = "InvalidSignatureType"; // No such enum value

        try {
            new OAuthConfig(key, secret, callback, SignatureType.valueOf(signatureType), null);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid SignatureType: " + signatureType, e.getMessage());
        }
    }
}