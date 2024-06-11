import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithValidParameters() {
 String apiKey = "abc123";
 String apiSecret = "def456";
 String callback = "http://test.com/callback";
 SignatureType signatureType = SignatureType.Header;
 String scope = "read_only";

 OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);

 assertEquals(apiKey, config.getApiKey());
 assertEquals(apiSecret, config.getApiSecret());
 assertEquals(callback, config.getCallback());
 assertEquals(signatureType, config.getSignatureType());
 assertEquals(scope, config.getScope());
 assertTrue(config.hasScope());
 }

 @Test
 public void testOAuthConfigWithNullApiSecret() {
 String apiKey = "abc123";
 String callback = "http://test.com/callback";
 SignatureType signatureType = SignatureType.Header;
 String scope = "read_only";

 OAuthConfig config = new OAuthConfig(apiKey, null, callback, signatureType, scope);

 assertNull(config.getApiSecret());
 }

 @Test
 public void testOAuthConfigWithEmptyApiSecret() {
 String apiKey = "abc123";
 String apiSecret = "";
 String callback = "http://test.com/callback";
 SignatureType signatureType = SignatureType.Header;
 String scope = "read_only";

 OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);

 assertEquals("", config.getApiSecret());
 }

 @Test
 public void testOAuthConfigWithNullCallback() {
 String apiKey = "abc123";
 String apiSecret = "def456";
 SignatureType signatureType = SignatureType.Header;
 String scope = "read_only";

 OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, signatureType, scope);

 assertEquals("oob", config.getCallback());
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullApiKey() {
 String apiSecret = "def456";
 String callback = "http://test.com/callback";
 SignatureType signatureType = SignatureType.Header;
 String scope = "read_only";

 OAuthConfig config = new OAuthConfig(null, apiSecret, callback, signatureType, scope);
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullSignatureType() {
 String apiKey = "abc123";
 String apiSecret = "def456";
 String callback = "http://test.com/callback";
 String scope = "read_only";

 OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, null, scope);
 }
}