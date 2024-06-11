package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigConstructionWithValidArguments() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "testCallback";
        SignatureType signatureType = SignatureType.Header;
        String scope = "testScope";

        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);

        assertEquals(apiKey, oAuthConfig.getApiKey());
        assertEquals(apiSecret, oAuthConfig.getApiSecret());
        assertEquals(callback, oAuthConfig.getCallback());
        assertEquals(signatureType, oAuthConfig.getSignatureType());
        assertEquals(scope, oAuthConfig.getScope());
        assertTrue(oAuthConfig.hasScope());
    }

    @Test
    public void testOAuthConfigConstructionWithNoArgsForCallbackAndSignatureType() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";

        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret);

        assertEquals(apiKey, oAuthConfig.getApiKey());
        assertEquals(apiSecret, oAuthConfig.getApiSecret());
        assertEquals("oob", oAuthConfig.getCallback());
        assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
        assertNull(oAuthConfig.getScope());
        assertFalse(oAuthConfig.hasScope());
    }

    @Test
    public void testGettersForNullValues() {
        OAuthConfig oAuthConfig = new OAuthConfig("testApiKey", "testApiSecret");

        assertNull(oAuthConfig.getApiKey());
        assertNull(oAuthConfig.getApiSecret());
        assertEquals("oob", oAuthConfig.getCallback());
        assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
        assertNull(oAuthConfig.getScope());
        assertFalse(oAuthConfig.hasScope());
    }

    @Test
    public void testGettersForEmptyStrings() {
        OAuthConfig oAuthConfig = new OAuthConfig("", "", "", SignatureType.Header, "");

        assertEquals("", oAuthConfig.getApiKey());
        assertEquals("", oAuthConfig.getApiSecret());
        assertEquals("", oAuthConfig.getCallback());
        assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
        assertEquals("", oAuthConfig.getScope());
        assertFalse(oAuthConfig.hasScope());
    }

    @Test
    public void testIsValidOAuthConfig() {
        String invalidApiKey = "";
        String validApiSecret = "testApiSecret";

        OAuthConfig invalidConfig = new OAuthConfig(invalidApiKey, validApiSecret);
        OAuthConfig validConfig = new OAuthConfig("testApiKey", validApiSecret);

        assertFalse(invalidConfig.isValidOAuthConfig());
        assertTrue(validConfig.isValidOAuthConfig());
    }
}