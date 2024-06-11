package org.scribe.model;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OAuthConfigTest {
    @Test
    public void testValidOAuthConfig() {
        // Testing constructor with valid arguments
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "http://test.com/callback";
        SignatureType signatureType = SignatureType.Header;
        String scope = "testScope";

        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);

        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertTrue(config.hasScope());
    }
    
    @Test
    public void testDefaultOAuthConfig() {
        // Testing constructor with default arguments
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";

        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);

        assertEquals("oob", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }
    
    @Test
    public void testInvalidSignatureType() {
        // Testing constructor with invalid signatureType
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        String callback = "http://test.com/callback";
        SignatureType signatureType = new SignatureType("INVALID");
        String scope = "testScope";

        try {
            OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullApiKeyAndApiSecret() {
        // Testing constructor with null apiKey and apiSecret
        String callback = "http://test.com/callback";
        SignatureType signatureType = SignatureType.Header;
        String scope = "testScope";

        try {
            OAuthConfig config = new OAuthConfig(null, null, callback, signatureType, scope);
            fail("Expected a NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }
}