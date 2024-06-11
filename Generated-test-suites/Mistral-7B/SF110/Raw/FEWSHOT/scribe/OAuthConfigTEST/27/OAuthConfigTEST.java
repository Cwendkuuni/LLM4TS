package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testDefaultConstructor() {
        OAuthConfig config = new OAuthConfig("");
        assertNull(config.getApiKey());
        assertNull(config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
    }

    @Test
    public void testConstructorWithKeyAndSecret() {
        String apiKey = "key1";
        String apiSecret = "secret1";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
    }

    @Test
    public void testConstructorWithKeyAndSecretAndCallback() {
        String apiKey = "key1";
        String apiSecret = "secret1";
        String callback = "http://example.com/callback";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
    }

    @Test
    public void testConstructorWithKeyAndSecretAndCallbackAndType() {
        String apiKey = "key1";
        String apiSecret = "secret1";
        String callback = "http://example.com/callback";
        SignatureType signatureType = SignatureType.QueryString;
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertNull(config.getScope());
    }

    @Test
    public void testConstructorWithKeyAndSecretAndCallbackAndTypeAndScope() {
        String apiKey = "key1";
        String apiSecret = "secret1";
        String callback = "http://example.com/callback";
        SignatureType signatureType = SignatureType.Header;
        String scope = "scope1 scope2";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertEquals(scope, config.getScope());
    }

    @Test
    public void testGetters() {
        String apiKey = "key1";
        String apiSecret = "secret1";
        String callback = "http://example.com/callback";
        SignatureType signatureType = SignatureType.Header;
        String scope = "scope1 scope2";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);

        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertEquals(true, config.hasScope());
    }

    @Test
    public void testGettersWithNullValues() {
        String apiKey = null;
        String apiSecret = null;
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);

        assertNull(config.getApiKey());
        assertNull(config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
    }

    @Test
    public void testGettersWithNullValuesAndCallback() {
        String apiKey = null;
        String apiSecret = null;
        String callback = null;
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback);

        assertNull(config.getApiKey());
        assertNull(config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
    }

}