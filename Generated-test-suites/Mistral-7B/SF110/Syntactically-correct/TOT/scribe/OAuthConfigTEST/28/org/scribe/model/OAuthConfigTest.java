package org.scribe.model;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OAuthConfigTest {
    @Test
    public void testOAuthConfigWithValidArguments() {
        String apiKey = "apiKey123";
        String apiSecret = "apiSecret456";
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
}