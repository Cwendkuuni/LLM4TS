import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.OAuthConfig;

public class OAuthConfigTest {
    
    
    @Test
    public void testOAuthConfigWithApiKeyAndApiSecret() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret");
        String apiKey = config.getApiKey();
        String apiSecret = config.getApiSecret();
        Assert.assertEquals("testKey", apiKey);
        Assert.assertEquals("testSecret", apiSecret);
    }

    
}