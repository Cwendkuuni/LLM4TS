import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class OAuthConfigTest {
    
    @Test
    public void testOAuthConfigWithApiKeyAndApiSecret() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";

        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);

        assertNotNull(config.getApiKey());
        assertNotNull(config.getApiSecret());
    }
    
}