
package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigWithKeyAndSecret() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertNotNull(config.getApiKey());
        assertNotNull(config.getApiSecret());
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals("oob", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
    }

    @Test
    public void testOAuthConfigWithAllArguments() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        String callback = "testCallback";
        SignatureType signatureType = SignatureType.Query;
        String scope = "testScope";

        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        assertNotNull(config.getApiKey());
        assertNotNull(config.getApiSecret());
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertEquals(scope, config.getScope());
    }

    @Test(expected = NullPointerException.class)
    public void testOAuthConfigWithNullApiKey() {
        String apiSecret = "testSecret";
        String callback = "testCallback";
        SignatureType signatureType = SignatureType.Header;
        String scope = "testScope";

        try {
            new OAuthConfig(null, apiSecret, callback, signatureType, scope);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testOAuthConfigWithNullApiSecret() {
        String apiKey = "testKey";
        String callback = "testCallback";
        SignatureType signatureType = SignatureType.Header;
        String scope = "testScope";

        try {
            new OAuthConfig(apiKey, null, callback, signatureType, scope);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testOAuthConfigWithEmptyCallback() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        SignatureType signatureType = SignatureType.Header;
        String scope = "testScope";

        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, "", signatureType, scope);
        assertEquals("", config.getCallback());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOAuthConfigWithUnknownSignatureType() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        String callback = "";
        String scope = "testScope";

        try {
            new OAuthConfig(apiKey, apiSecret, callback, SignatureType.Unknown, scope);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOAuthConfigWithEmptyScope() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        String callback = "";
        SignatureType signatureType = SignatureType.Header;

        try {
            new OAuthConfig(apiKey, apiSecret, callback, signatureType, "");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOAuthConfigHasScopeWithEmptyString() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        String callback = "";
        SignatureType signatureType = SignatureType.Header;
        String scope = "";

        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        assertFalse(config.hasScope());
    }
}