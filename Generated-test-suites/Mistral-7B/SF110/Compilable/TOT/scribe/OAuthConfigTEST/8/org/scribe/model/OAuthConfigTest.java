package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {
    @Test
    public void testOAuthConfigWithValidApiKeyAndApiSecret() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";

        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret);

        String expectedApiKey = apiKey;
        String expectedApiSecret = apiSecret;
        String expectedCallback = "oob";
        SignatureType expectedSignatureType = SignatureType.Header;
        String expectedScope = null;

        assertEquals(expectedApiKey, oAuthConfig.getApiKey());
        assertEquals(expectedApiSecret, oAuthConfig.getApiSecret());
        assertEquals(expectedCallback, oAuthConfig.getCallback());
        assertEquals(expectedSignatureType, oAuthConfig.getSignatureType());
        assertEquals(expectedScope, oAuthConfig.getScope());
        assertFalse(oAuthConfig.hasScope());
    }
}