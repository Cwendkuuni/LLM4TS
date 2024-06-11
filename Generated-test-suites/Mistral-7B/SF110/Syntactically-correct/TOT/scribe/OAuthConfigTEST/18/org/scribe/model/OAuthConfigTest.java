import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

import static org.junit.Assert.assertEquals;

public class OAuthConfigTest {
    @Test
    public void testOAuthConfigWithApiKeyAndApiSecret() {
        String key = "testApiKey";
        String secret = "testApiSecret";

        OAuthConfig oAuthConfig = new OAuthConfig(key, secret);

        assertEquals(key, oAuthConfig.getApiKey());
        assertEquals(secret, oAuthConfig.getApiSecret());
    }
}