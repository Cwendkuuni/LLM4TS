package org.scribe.model;
import org.junit.Test;
import org.scribe.model.OAuthConfig;

public class OAuthConfigTest {

    @Test
    public void testGetApiKey() {
        String apiKey = "testApiKey";
        OAuthConfig oAuthConfig = new OAuthConfig(apiKey, "testApiSecret");
        String retrievedApiKey = oAuthConfig.getApiKey();
        assert (retrievedApiKey.equals(apiKey));
    }
}