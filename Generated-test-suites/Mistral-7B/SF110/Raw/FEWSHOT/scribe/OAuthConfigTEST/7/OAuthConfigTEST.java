package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigWithKeyAndSecret() {
        String key = "myApiKey";
        String secret = "myApiSecret";
        OAuthConfig oauthConfig = new OAuthConfig(key, secret);
        assertEquals(key, oauthConfig.getApiKey());
        assertEquals(secret, oauthConfig.getApiSecret());
        assertNull(oauthConfig.getCallback());
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
        assertNull(oauthConfig.getScope());
        assertFalse(oauthConfig.hasScope());
    }

    @Test
    public void testOAuthConfigWithKeyAndSecretAndCallback() {
        String key = "myApiKey";
        String secret = "myApiSecret";
        String callback = "http://localhost:8080/callback";
        OAuthConfig oauthConfig = new OAuthConfig(key, secret, callback);
        assertEquals(key, oauthConfig.getApiKey());
        assertEquals(secret, oauthConfig.getApiSecret());
        assertEquals(callback, oauthConfig.getCallback());
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
        assertNull(oauthConfig.getScope());
        assertFalse(oauthConfig.hasScope());
    }

    @Test
    public void testOAuthConfigWithKeyAndSecretAndCallbackAndSignatureType() {
        String key = "myApiKey";
        String secret = "myApiSecret";
        String callback = "http://localhost:8080/callback";
        SignatureType signatureType = SignatureType.Query;
        OAuthConfig oauthConfig = new OAuthConfig(key, secret, callback, signatureType);
        assertEquals(key, oauthConfig.getApiKey());
        assertEquals(secret, oauthConfig.getApiSecret());
        assertEquals(callback, oauthConfig.getCallback());
        assertEquals(signatureType, oauthConfig.getSignatureType());
        assertNull(oauthConfig.getScope());
        assertFalse(oauthConfig.hasScope());
    }

    @Test
    public void testOAuthConfigWithKeyAndSecretAndCallbackAndSignatureTypeAndScope() {
        String key = "myApiKey";
        String secret = "myApiSecret";
        String callback = "http://localhost:8080/callback";
        SignatureType signatureType = SignatureType.Header;
        String scope = "read write";
        OAuthConfig oauthConfig = new OAuthConfig(key, secret, callback, signatureType, scope);
        assertEquals(key, oauthConfig.getApiKey());
        assertEquals(secret, oauthConfig.getApiSecret());
        assertEquals(callback, oauthConfig.getCallback());
        assertEquals(signatureType, oauthConfig.getSignatureType());
        assertEquals(scope, oauthConfig.getScope());
        assertTrue(oauthConfig.hasScope());
    }

    @Test
    public void testGettersReturnExpectedValues() {
        String key = "myApiKey";
        String secret = "myApiSecret";
        String callback = "http://localhost:8080/callback";
        SignatureType signatureType = SignatureType.Header;
        String scope = "read write";
        OAuthConfig oauthConfig = new OAuthConfig(key, secret, callback, signatureType, scope);
        assertEquals(key, oauthConfig.getApiKey());
        assertEquals(secret, oauthConfig.getApiSecret());
        assertEquals(callback, oauthConfig.getCallback());
        assertEquals(signatureType, oauthConfig.getSignatureType());
        assertEquals(scope, oauthConfig.getScope());
        assertEquals("oob", oauthConfig.getCallback().equals(null) ? "" : oauthConfig.getCallback());
    }

    @Test
    public void testDifferentInstancesHaveDifferentValues() {
        String key1 = "myApiKey1";
        String secret1 = "myApiSecret1";
        String key2 = "myApiKey2";
        String secret2 = "myApiSecret2";
        OAuthConfig oauthConfig1 = new OAuthConfig(key1, secret1);
        OAuthConfig oauthConfig2 = new OAuthConfig(key2, secret2);
        assertNotEquals(oauthConfig1, oauthConfig2);
        assertNotEquals(oauthConfig1.getApiKey(), oauthConfig2.getApiKey());
        assertNotEquals(oauthConfig1.getApiSecret(), oauthConfig2.getApiSecret());
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeyThrowsException() {
        String nullKey = null;
        String secret = "myApiSecret";
        new OAuthConfig(nullKey, secret);
    }

    @Test(expected = NullPointerException.class)
    public void testNullSecretThrowsException() {
        String key = "myApiKey";
        String nullSecret = null;
        new OAuthConfig(key, nullSecret);
    }
}