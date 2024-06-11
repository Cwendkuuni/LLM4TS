import static org.junit.Assert.*;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigConstructorNoArgs() {
        OAuthConfig config = new OAuthConfig();
        assertNull(config.getApiKey());
        assertNull(config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigConstructorWithApiKeyAndApiSecret() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testOAuthConfigConstructorWithAllArguments() {
        String apiKey = "testKey";
        String apiSecret = "testSecret";
        String callback = "testCallback";
        SignatureType signatureType = SignatureType.QueryString;
        String scope = "testScope";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertTrue(config.hasScope());
    }

    @Test(expected = NullPointerException.class)
    public void testOAuthConfigConstructorWithNullApiKey() {
        String apiSecret = "testSecret";
        OAuthConfig config = new OAuthConfig(null, apiSecret);
        fail("Expected NullPointerException but got: " + config.getApiKey());
    }

    @Test(expected = NullPointerException.class)
    public void testOAuthConfigConstructorWithNullApiSecret() {
        String apiKey = "testKey";
        OAuthConfig config = new OAuthConfig(apiKey, null);
        fail("Expected NullPointerException but got: " + config.getApiSecret());
    }

    // Add tests for null and empty inputs, null signatures, and other edge cases here
}