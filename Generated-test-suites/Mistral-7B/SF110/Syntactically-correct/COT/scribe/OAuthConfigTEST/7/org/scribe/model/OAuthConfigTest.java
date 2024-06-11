package org.scribe.model.test;

import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Signatures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigWithKeyAndSecret() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertNull(config.getCallback());
        assertSame(Signatures.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigWithKeySecretCallbackScopeAndSignatureType() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        String callback = "http://test.com";
        SignatureType signatureType = Signatures.QueryString;
        String scope = "testScope";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertSame(signatureType, config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertTrue(config.hasScope());
    }

    @Test(expected = NullPointerException.class)
    public void testNullApiKey() {
        String apiSecret = "testSecret";
        OAuthConfig config = new OAuthConfig(null, apiSecret);
        assertThrows(NullPointerException.class, () -> config.getApiKey());
    }

    @Test(expected = NullPointerException.class)
    public void testNullApiSecret() {
        String apiKey = "testKey";
        OAuthConfig config = new OAuthConfig(apiKey, null);
        assertThrows(NullPointerException.class, () -> config.getApiSecret());
    }

    @Test(expected = NullPointerException.class)
    public void testNullCallback() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        SignatureType signatureType = Signatures.Header;
        SignatureType wrongSignatureType = Signatures.QueryString;
        String scope = "testScope";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, signatureType, scope);
        assertThrows(NullPointerException.class, () -> config.getCallback());
        assertSame(signatureType, config.getSignatureType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongSignatureType() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        String callback = "http://test.com";
        SignatureType signatureType = Signatures.Header;
        SignatureType wrongSignatureType = "WRONG_TYPE";
        String scope = "testScope";
        assertThrows(IllegalArgumentException.class, () -> new OAuthConfig(apiKey, apiSecret, callback, wrongSignatureType, scope));
    }
}