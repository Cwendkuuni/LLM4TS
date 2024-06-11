package org.scribe.model;
import static org.junit.Assert.*;
import org.junit.Test;

public class OAuthConfigTest {
    @Test
    public void testOAuthConfigWithRequiredArguments() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret);
        assertEquals(apiKey, oAuthConfig.getApiKey());
        assertEquals(apiSecret, oAuthConfig.getApiSecret());
    }

    @Test
    public void testOAuthConfigWithOptionalArguments() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "testCallback";
        SignatureType signatureType = SignatureType.QueryString;
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
    public void testOAuthConfigWithNullAndEmptyStrings() {
        String apiKey = null;
        String apiSecret = "";
        String callback = null;
        SignatureType signatureType = null;
        String scope = null;
        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        assertNull(apiKey, () -> oAuthConfig.getApiKey());
        assertEquals("oob", oAuthConfig.getCallback());
        assertNull(signatureType);
        assertNull(scope);
    }

    @Test
    public void testGettersForNonDefaultValues() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "testCallback";
        SignatureType signatureType = SignatureType.QueryString;
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
    public void testGettersForDefaultValues() {
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
    public void testGettersForEmptyStrings() {
        String apiKey = "";
        String apiSecret = "";
        String callback = "";
        SignatureType signatureType = SignatureType.Header;
        String scope = "";
        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        assertEquals("", oAuthConfig.getApiKey());
        assertEquals("", oAuthConfig.getApiSecret());
        assertEquals("", oAuthConfig.getCallback());
        assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
        assertNull(oAuthConfig.getScope());
        assertFalse(oAuthConfig.hasScope());
    }
}