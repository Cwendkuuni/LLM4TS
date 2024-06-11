package org.scribe.model;
import org.junit.Test;
import org.scribe.model.OAuthConfig;
import static org.junit.Assert.assertEquals;

public class OAuthConfigTest {
    @Test
    public void testOAuthConfigWithApiKeyAndApiSecret() {
        String apiKey = "abc123";
        String apiSecret = "def456";

        OAuthConfig oauthConfig = new OAuthConfig(apiKey, apiSecret);

        assertEquals(apiKey, oauthConfig.getApiKey());
        assertEquals(apiSecret, oauthConfig.getApiSecret());
        assertEquals("oob", oauthConfig.getCallback());
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
        assertEquals(null, oauthConfig.getScope());
        assertEquals(false, oauthConfig.hasScope());
    }
}