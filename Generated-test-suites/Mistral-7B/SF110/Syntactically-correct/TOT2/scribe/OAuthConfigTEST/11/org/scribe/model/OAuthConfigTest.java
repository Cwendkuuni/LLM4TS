import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.scribe.model.OAuthConfig;

public class OAuthConfigTest {
    @Test
    public void testOAuthConfigWithApiKeyAndApiSecret() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
    }
}