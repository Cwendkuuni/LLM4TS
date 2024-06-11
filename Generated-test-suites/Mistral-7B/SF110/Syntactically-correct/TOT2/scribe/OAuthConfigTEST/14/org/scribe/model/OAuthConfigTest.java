import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

    @Test
    public void testDefaultConstructor() {
        OAuthConfig config = new OAuthConfig();
        assertNull(config.getApiKey());
        assertNull(config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testConstructorWithKeyAndSecret() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullApiKey() {
        String apiSecret = "testApiSecret";
        new OAuthConfig(null, apiSecret);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullApiSecret() {
        String apiKey = "testApiKey";
        new OAuthConfig(apiKey, null);
    }

    @Test
    public void testConstructorWithEmptyCallback() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, "", SignatureType.Header, null);
        assertEquals("", config.getCallback());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidSignatureType() {
        String apiKey = "testApiKey";
        String apiSecret = "testApiSecret";
        new OAuthConfig(apiKey, apiSecret, "", SignatureType.FOO, null);
    }

    @Test
    public void testLongStringsForApiKeyAndApiSecret() {
        String apiKey = new String(new char[1024]);
        String apiSecret = new String(new char[1024]);
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        //Check if getter methods return correct values
    }

    @Test
    public void testLongStringForScope() {
        String scope = new String(new char[1024]);
        OAuthConfig config = new OAuthConfig("testApiKey", "testApiSecret", null, SignatureType.Header, scope);
        assertEquals(scope, config.getScope());
    }

    @Test
    public void testGetApiKey() {
        String apiKey = "testApiKey";
        OAuthConfig config = new OAuthConfig(apiKey, "testApiSecret");
        assertEquals(apiKey, config.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        String apiSecret = "testApiSecret";
        OAuthConfig config = new OAuthConfig("testApiKey", apiSecret);
        assertEquals(apiSecret, config.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        String callback = "testCallback";
        OAuthConfig config = new OAuthConfig("testApiKey", "testApiSecret", callback, SignatureType.Header, null);
        assertEquals(callback, config.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("testApiKey", "testApiSecret", null, null, null);
        assertEquals(SignatureType.Header, config.getSignatureType());
    }

    @Test
    public void testGetScope() {
        String scope = "testScope";
        OAuthConfig config = new OAuthConfig("testApiKey", "testApiSecret", null, SignatureType.Header, scope);
        assertEquals(scope, config.getScope());
    }

    @Test
    public void testHasScope() {
        String scope = "testScope";
        OAuthConfig config = new OAuthConfig("testApiKey", "testApiSecret", null, SignatureType.Header, scope);
        assertTrue(config.hasScope());

        OAuthConfig noScopeConfig = new OAuthConfig("testApiKey", "testApiSecret", null, SignatureType.Header, null);
        assertFalse(noScopeConfig.hasScope());
    }
}