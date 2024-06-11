import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {
    @Test
    public void testOAuthConfigWithTwoArguments() {
        String key = "testApiKey";
        String secret = "testApiSecret";
        OAuthConfig config = new OAuthConfig(key, secret);
        assertEquals(key, config.getApiKey());
        assertEquals(secret, config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }
}