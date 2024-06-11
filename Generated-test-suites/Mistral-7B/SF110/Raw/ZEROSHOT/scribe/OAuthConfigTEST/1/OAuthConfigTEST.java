import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithNullArguments() {
 OAuthConfig config = new OAuthConfig(null, null);
 assertNull(config.getApiKey());
 assertNull(config.getApiSecret());
 assertNull(config.getCallback());
 assertNull(config.getSignatureType());
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithValidArguments() {
 String apiKey = "abc123";
 String apiSecret = "def456";
 String callback = "http://localhost:8080/callback";
 SignatureType signatureType = SignatureType.Header;
 String scope = "read_only";

 OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
 assertEquals(apiKey, config.getApiKey());
 assertEquals(apiSecret, config.getApiSecret());
 assertEquals(callback, config.getCallback());
 assertEquals(signatureType, config.getSignatureType());
 assertEquals(scope, config.getScope());
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

 @Test
 public void testOAuthConfigWithNullSignatureType() {
 String apiKey = "abc123";
 String apiSecret = "def456";
 String callback = "http://localhost:8080/callback";
 String scope = "read_only";

 OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, null, scope);
 assertEquals(SignatureType.Header, config.getSignatureType());
 }

 @Test
 public void testOAuthConfigWithNullScope() {
 String apiKey = "abc123";
 String apiSecret = "def456";
 String callback = "http://localhost:8080/callback";
 SignatureType signatureType = SignatureType.Header;

 OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, null);
 assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithHasScope() {
 String apiKey = "abc123";
 String apiSecret = "def456";
 String callback = "http://localhost:8080/callback";
 SignatureType signatureType = SignatureType.Header;
 String scope = "read_only";

 OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
 assertTrue(config.hasScope());
 }
}