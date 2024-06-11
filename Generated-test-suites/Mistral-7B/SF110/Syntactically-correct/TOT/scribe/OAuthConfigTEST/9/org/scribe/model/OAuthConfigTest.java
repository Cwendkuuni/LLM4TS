import org.junit.Test;
import org.junit.Assert;

public class OAuthConfigTest {
    // Test case for default constructor
    @Test
    public void testDefaultConstructor() {
        OAuthConfig config = new OAuthConfig("defaultKey", "defaultSecret");
        Assert.assertEquals("oob", config.getCallback());
        Assert.assertEquals(SignatureType.Header, config.getSignatureType());
        Assert.assertNull(config.getScope());
    }

    // Test case for constructor with all parameters
    @Test
    public void testConstructorWithAllParameters() {
        String key = "testKey";
        String secret = "testSecret";
        String callback = "http://test.com";
        SignatureType signatureType = SignatureType.Query;
        String scope = "testScope";

        OAuthConfig config = new OAuthConfig(key, secret, callback, signatureType, scope);

        Assert.assertEquals(key, config.getApiKey());
        Assert.assertEquals(secret, config.getApiSecret());
        Assert.assertEquals(callback, config.getCallback());
        Assert.assertEquals(signatureType, config.getSignatureType());
        Assert.assertEquals(scope, config.getScope());
        Assert.assertTrue(config.hasScope());
    }
}