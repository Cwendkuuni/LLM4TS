import static org.junit.Assert.*;
import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testDefaultConstructor() {
        OAuthConfig config = new OAuthConfig();
        assertEquals(null, config.getApiKey());
        assertEquals(null, config.getApiSecret());
        assertEquals("oob", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertEquals(null, config.getScope());
        assertEquals(false, config.hasScope());
    }

    @Test
    public void testConstructorWithApiKeyAndApiSecret() {
        final String apiKey = "testKey";
        final String apiSecret = "testSecret";

        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals("oob", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertEquals(null, config.getScope());
        assertEquals(false, config.hasScope());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullApiKey() {
        new OAuthConfig(null, "testSecret");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullApiSecret() {
        new OAuthConfig("testKey", null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullCallback() {
        new OAuthConfig("testKey", "testSecret", null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullSignatureType() {
        new OAuthConfig("testKey", "testSecret", "testCallback", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidSignatureType() {
        new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.INVALID);
    }

    @Test
    public void testGetApiKeyWithValidApiKey() {
        final String apiKey = "testKey";
        OAuthConfig config = new OAuthConfig(apiKey, "testSecret");
        assertEquals(apiKey, config.getApiKey());
    }

    @Test(expected = NullPointerException.class)
    public void testGetApiKeyWithNullApiKey() {
        OAuthConfig config = new OAuthConfig(null, "testSecret");
        config.getApiKey();
    }

    @Test
    public void testGetApiSecretWithValidApiSecret() {
        final String apiSecret = "testSecret";
        OAuthConfig config = new OAuthConfig("testKey", apiSecret);
        assertEquals(apiSecret, config.getApiSecret());
    }

    @Test(expected = NullPointerException.class)
    public void testGetApiSecretWithNullApiSecret() {
        OAuthConfig config = new OAuthConfig("testKey", null);
        config.getApiSecret();
    }

    @Test
    public void testGetCallbackWithValidCallback() {
        final String callback = "testCallback";
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", callback, null, null);
        assertEquals(callback, config.getCallback());
    }

    @Test
    public void testGetCallbackWithEmptyString() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "", null, null);
        assertEquals("", config.getCallback());
    }

    @Test
    public void testGetCallbackWithNullAndEmptyString() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, null);
        assertEquals("oob", config.getCallback());
    }

    @Test
    public void testGetSignatureTypeWithValidSignatureType() {
        final SignatureType signatureType = SignatureType.Header;
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", signatureType, null);
        assertEquals(signatureType, config.getSignatureType());
    }

    @Test
    public void testGetSignatureTypeWithNullSignatureType() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", null, null);
        assertEquals(SignatureType.Header, config.getSignatureType());
    }

    @Test
    public void testGetScopeWithValidScope() {
        final String scope = "testScope";
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", null, scope);
        assertEquals(scope, config.getScope());
    }

    @Test
    public void testGetScopeWithNullScope() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", null, null);
        assertEquals(null, config.getScope());
    }

    @Test
    public void testHasScopeWithValidScope() {
        final String scope = "testScope";
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", null, scope);
        assertTrue(config.hasScope());
    }

    @Test
    public void testHasScopeWithNullScope() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", null, null);
        assertFalse(config.hasScope());
    }
}