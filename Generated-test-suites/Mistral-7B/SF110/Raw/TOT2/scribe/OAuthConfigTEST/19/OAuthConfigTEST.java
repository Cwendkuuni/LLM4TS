package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;
import org.scribe.model.OAuthConfig;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigWithApiKeyAndApiSecret() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";

        OAuthConfig oauthConfig = new OAuthConfig(apiKey, apiSecret);

        assertEquals(apiKey, oauthConfig.getApiKey());
        assertEquals(apiSecret, oauthConfig.getApiSecret());
    }

    @Test
    public void testOAuthConfigWithAllArguments() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        String callback = "testCallback";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "testScope";

        OAuthConfig oauthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);

        assertEquals(apiKey, oauthConfig.getApiKey());
        assertEquals(apiSecret, oauthConfig.getApiSecret());
        assertEquals(callback, oauthConfig.getCallback());
        assertEquals(signatureType, oauthConfig.getSignatureType());
        assertEquals(scope, oauthConfig.getScope());
        assertEquals(true, oauthConfig.hasScope());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOAuthConfigWithNullApiKey() {
        String apiSecret = "testSecret";
        String callback = "testCallback";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "testScope";

        new OAuthConfig(null, apiSecret, callback, signatureType, scope);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOAuthConfigWithNullApiSecret() {
        String apiKey = "testKey";
        String callback = "testCallback";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "testScope";

        new OAuthConfig(apiKey, null, callback, signatureType, scope);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOAuthConfigWithEmptyCallback() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "testScope";

        new OAuthConfig(apiKey, apiSecret, "", signatureType, scope);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOAuthConfigWithEmptyScope() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        String callback = "testCallback";
        SignatureType signatureType = SignatureType.QueryString;

        new OAuthConfig(apiKey, apiSecret, callback, signatureType, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOAuthConfigWithInvalidSignatureType() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        String callback = "testCallback";
        String scope = "testScope";

        new OAuthConfig(apiKey, apiSecret, callback, new SignatureType("Invalid"), scope);
    }

    @Test
    public void testOAuthConfigWithEmptyConstructor() {
        OAuthConfig oauthConfig = new OAuthConfig();
        assertNull(oauthConfig.getApiKey());
        assertNull(oauthConfig.getApiSecret());
        assertNull(oauthConfig.getCallback());
        assertNull(oauthConfig.getSignatureType());
        assertNull(oauthConfig.getScope());
        assertFalse(oauthConfig.hasScope());
    }
}