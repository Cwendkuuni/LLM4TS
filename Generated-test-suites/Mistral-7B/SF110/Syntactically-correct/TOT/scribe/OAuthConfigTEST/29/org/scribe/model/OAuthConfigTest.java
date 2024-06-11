import org.junit.Assert;
import org.junit.Test;
import org.scribe.model.OAuthConfig;

public class OAuthConfigTest {
    
    
    @Test
    public void testOAuthConfigWithApiKeyAndApiSecret() {
        String key = "testKey";
        String secret = "testSecret";

        OAuthConfig config = new OAuthConfig(key, secret);

        Assert.assertEquals(key, config.getApiKey());
        Assert.assertEquals(secret, config.getApiSecret());
        Assert.assertEquals("oob", config.getCallback());
        Assert.assertEquals(SignatureType.Header, config.getSignatureType());
        Assert.assertNull(config.getScope());
        Assert.assertFalse(config.hasScope());
    }

    
}