package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigWithKeyAndSecret() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);

        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
    }

    @Test
    public void testOAuthConfigWithKeySecretAndCallback() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        String callback = "http://localhost:8080/callback";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback);

        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
    }

    @Test
    public void testOAuthConfigWithKeySecretCallbackAndSignatureType() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        String callback = "http://localhost:8080/callback";
        SignatureType signatureType = SignatureType.QueryString;
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType);

        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertNull(config.getScope());
    }

    @Test
    public void testOAuthConfigWithKeySecretCallbackSignatureTypeAndScope() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        String callback = "http://localhost:8080/callback";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "scope1 scope2";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);

        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertTrue(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithNullKey() {
        String apiSecret = "testSecret";
        String callback = "http://localhost:8080/callback";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "scope1 scope2";

        assertNull(new OAuthConfig(null, apiSecret, callback, signatureType, scope));
    }

    @Test
    public void testOAuthConfigWithNullSecret() {
        String apiKey = "testKey";
        String callback = "http://localhost:8080/callback";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "scope1 scope2";

        assertNull(new OAuthConfig(apiKey, null, callback, signatureType, scope));
    }

    @Test
    public void testOAuthConfigWithNullCallback() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        SignatureType signatureType = SignatureType.Header;
        String scope = "scope1 scope2";

        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, signatureType, scope);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertNull(config.getScope());
    }

    @Test
    public void testOAuthConfigWithNullSignatureType() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        String callback = "http://localhost:8080/callback";
        String scope = "scope1 scope2";

        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, null, scope);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertNull(config.getSignatureType());
        assertNull(config.getScope());
    }

    @Test
    public void testOAuthConfigWithNullScope() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        String callback = "http://localhost:8080/callback";
        SignatureType signatureType = SignatureType.Header;

        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, null);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertNull(config.getScope());
    }
}