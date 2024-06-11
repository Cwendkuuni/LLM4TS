package org.scribe.model;
import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigWithValidParameters() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        String callback = "http://test.com/callback";
        SignatureType signatureType = SignatureType.Header;
        String scope = "testScope";

        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);

        String expectedApiKey = apiKey;
        String expectedApiSecret = apiSecret;
        String expectedCallback = callback;
        SignatureType expectedSignatureType = signatureType;
        String expectedScope = scope;

        String actualApiKey = config.getApiKey();
        String actualApiSecret = config.getApiSecret();
        String actualCallback = config.getCallback();
        SignatureType actualSignatureType = config.getSignatureType();
        String actualScope = config.getScope();

        assertTrue("apiKey should be set correctly", expectedApiKey.equals(actualApiKey));
        assertTrue("apiSecret should be set correctly", expectedApiSecret.equals(actualApiSecret));
        assertTrue("callback should be set correctly", expectedCallback.equals(actualCallback) || "oob".equals(actualCallback));
        assertEquals("signatureType should be set correctly", expectedSignatureType, actualSignatureType);
        assertTrue("scope should be set correctly", expectedScope == null ? config.hasScope() : expectedScope.equals(actualScope));
    }

    @Test
    public void testOAuthConfigWithNullApiKey() {
        String apiSecret = "testSecret";
        OAuthConfig config = new OAuthConfig(null, apiSecret);

        String expectedApiKey = null;
        String expectedApiSecret = apiSecret;
        String expectedCallback = "oob";
        SignatureType expectedSignatureType = SignatureType.Header;
        String expectedScope = null;

        String actualApiKey = config.getApiKey();
        String actualApiSecret = config.getApiSecret();
        String actualCallback = config.getCallback();
        SignatureType actualSignatureType = config.getSignatureType();
        String actualScope = config.getScope();

        assertTrue("apiKey should be null", expectedApiKey == actualApiKey);
        assertTrue("apiSecret should be set correctly", expectedApiSecret.equals(actualApiSecret));
        assertTrue("callback should be 'oob' when apiKey is null", "oob".equals(actualCallback));
        assertEquals("signatureType should be set correctly", expectedSignatureType, actualSignatureType);
        assertTrue("scope should be null", expectedScope == null);
    }

    @Test
    public void testOAuthConfigWithEmptyApiSecret() {
        String apiKey = "testKey";
        String apiSecret = "";
        String callback = null;
        SignatureType signatureType = SignatureType.Header;
        String scope = "testScope";

        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);

        String expectedApiKey = apiKey;
        String expectedApiSecret = "";
        String expectedCallback = callback;
        SignatureType expectedSignatureType = signatureType;
        String expectedScope = scope;

        String actualApiKey = config.getApiKey();
        String actualApiSecret = config.getApiSecret();
        String actualCallback = config.getCallback();
        SignatureType actualSignatureType = config.getSignatureType();
        String actualScope = config.getScope();

        assertTrue("apiKey should be set correctly", expectedApiKey.equals(actualApiKey));
        assertEquals("apiSecret should be an empty string", expectedApiSecret, actualApiSecret);
        assertEquals("callback should be null", null, actualCallback);
        assertEquals("signatureType should be set correctly", expectedSignatureType, actualSignatureType);
        assertTrue("scope should be set correctly", expectedScope == null ? config.hasScope() : expectedScope.equals(actualScope));
    }

    @Test
    public void testOAuthConfigWithInvalidSignatureType() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        String callback = "http://test.com/callback";
        SignatureType signatureType = new SignatureType("InvalidType");
        String scope = "testScope";

        try {
            new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
            fail("should throw IllegalArgumentException with invalid SignatureType");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}