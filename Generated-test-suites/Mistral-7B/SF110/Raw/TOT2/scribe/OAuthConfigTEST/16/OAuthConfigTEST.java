import static org.junit.Assert.*;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

    @Test
    public void testGetApiKey() {
        String expectedApiKey = "apiKeyValue";
        OAuthConfig oauthConfig = new OAuthConfig("apiKeyValue", "apiSecretValue");
        String actualApiKey = oauthConfig.getApiKey();
        assertEquals(expectedApiKey, actualApiKey);
    }

    @Test
    public void testNullApiKey() {
        OAuthConfig oauthConfig = new OAuthConfig(null, "apiSecretValue");
    }

    @Test(expected = NullPointerException.class)
    public void testNullApiSecret() {
        OAuthConfig oauthConfig = new OAuthConfig("apiKeyValue", null);
    }

    @Test
    public void testEmptyCallback() {
        String expectedCallback = "";
        OAuthConfig oauthConfig = new OAuthConfig("apiKeyValue", "apiSecretValue", "", null, null);
        String actualCallback = oauthConfig.getCallback();
        assertEquals(expectedCallback, actualCallback);
    }

    @Test
    public void testNullSignatureType() {
        SignatureType expectedSignatureType = null;
        OAuthConfig oauthConfig = new OAuthConfig("apiKeyValue", "apiSecretValue", "callback", null, "scope");
        SignatureType actualSignatureType = oauthConfig.getSignatureType();
        assertEquals(expectedSignatureType, actualSignatureType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownSignatureType() {
        SignatureType unknownSignatureType = new SignatureType("UnknownType");
        OAuthConfig oauthConfig = new OAuthConfig("apiKeyValue", "apiSecretValue", "callback", unknownSignatureType, "scope");
    }

    @Test
    public void testNoArgumentConstructor() {
        OAuthConfig oauthConfig = new OAuthConfig();
        assertNull(oauthConfig.getApiKey());
        assertNull(oauthConfig.getApiSecret());
        assertNull(oauthConfig.getCallback());
        assertNull(oauthConfig.getSignatureType());
        assertNull(oauthConfig.getScope());
    }

    @Test(expected = NullPointerException.class)
    public void testNullApiKeyConstructor() {
        OAuthConfig oauthConfig = new OAuthConfig(null, "apiSecretValue");
    }

    @Test(expected = NullPointerException.class)
    public void testNullApiSecretConstructor() {
        OAuthConfig oauthConfig = new OAuthConfig("apiKeyValue", null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullCallbackConstructor() {
        OAuthConfig oauthConfig = new OAuthConfig("apiKeyValue", "apiSecretValue", null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullScopeConstructor() {
        OAuthConfig oauthConfig = new OAuthConfig("apiKeyValue", "apiSecretValue", "callback", null, null);
    }

    @Test
    public void testGetApiSecret() {
        String expectedApiSecret = "apiSecretValue";
        OAuthConfig oauthConfig = new OAuthConfig("apiKeyValue", "apiSecretValue");
        String actualApiSecret = oauthConfig.getApiSecret();
        assertEquals(expectedApiSecret, actualApiSecret);
    }

    @Test
    public void testGetCallback() {
        String expectedCallback = "oob";
        OAuthConfig oauthConfig = new OAuthConfig("apiKeyValue", "apiSecretValue");
        String actualCallback = oauthConfig.getCallback();
        assertEquals(expectedCallback, actualCallback);
    }

    @Test
    public void testGetSignatureType() {
        SignatureType expectedSignatureType = SignatureType.Header;
        OAuthConfig oauthConfig = new OAuthConfig("apiKeyValue", "apiSecretValue", null, null, null);
        SignatureType actualSignatureType = oauthConfig.getSignatureType();
        assertEquals(expectedSignatureType, actualSignatureType);
    }

    @Test
    public void testGetScope() {
        String expectedScope = "scopeValue";
        OAuthConfig oauthConfig = new OAuthConfig("apiKeyValue", "apiSecretValue", null, null, "scopeValue");
        String actualScope = oauthConfig.getScope();
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testHasScope() {
        boolean hasScope = true;
        OAuthConfig oauthConfig = new OAuthConfig("apiKeyValue", "apiSecretValue", null, null, "scopeValue");
        boolean actualHasScope = oauthConfig.hasScope();
        assertTrue(actualHasScope);
    }

    @Test
    public void testNoScope() {
        boolean hasScope = false;
        OAuthConfig oauthConfig = new OAuthConfig("apiKeyValue", "apiSecretValue", null, null, null);
        boolean actualHasScope = oauthConfig.hasScope();
        assertFalse(actualHasScope);
    }
}