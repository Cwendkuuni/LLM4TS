import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithValidArguments() {
 String apiKey = "testApiKey";
 String apiSecret = "testApiSecret";
 String callback = "http://test.com/callback";
 SignatureType signatureType = SignatureType.Header;
 String scope = "testScope";

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
 String apiKey = "testApiKey";
 String callback = "http://test.com/callback";
 SignatureType signatureType = SignatureType.Header;
 String scope = "testScope";

 OAuthConfig config = new OAuthConfig(apiKey, null, callback, signatureType, scope);

 assertEquals(apiKey, config.getApiKey());
 assertNull(config.getApiSecret());
 assertEquals(callback, config.getCallback());
 assertEquals(signatureType, config.getSignatureType());
 assertEquals(scope, config.getScope());
 assertTrue(config.hasScope());
 }

 @Test
 public void testOAuthConfigWithNullApiKey() {
 String apiSecret = "testApiSecret";
 String callback = "http://test.com/callback";
 SignatureType signatureType = SignatureType.Header;
 String scope = "testScope";

 OAuthConfig config = new OAuthConfig(null, apiSecret, callback, signatureType, scope);

 assertNull(config.getApiKey());
 assertEquals(apiSecret, config.getApiSecret());
 assertEquals(callback, config.getCallback());
 assertEquals(signatureType, config.getSignatureType());
 assertEquals(scope, config.getScope());
 assertTrue(config.hasScope());
 }

 @Test
 public void testOAuthConfigWithNullCallback() {
 String apiKey = "testApiKey";
 String apiSecret = "testApiSecret";
 SignatureType signatureType = SignatureType.Header;
 String scope = "testScope";

 OAuthConfig config = new OAuthConfig(apiKey, apiSecret, null, signatureType, scope);

 assertEquals(apiKey, config.getApiKey());
 assertEquals(apiSecret, config.getApiSecret());
 assertEquals("oob", config.getCallback());
 assertEquals(signatureType, config.getSignatureType());
 assertEquals(scope, config.getScope());
 assertTrue(config.hasScope());
 }

 @Test
 public void testOAuthConfigWithNullSignatureType() {
 String apiKey = "testApiKey";
 String apiSecret = "testApiSecret";
 String callback = "http://test.com/callback";
 String scope = "testScope";

 OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, null, scope);

 assertEquals(apiKey, config.getApiKey());
 assertEquals(apiSecret, config.getApiSecret());
 assertEquals(callback, config.getCallback());
 assertEquals(SignatureType.Header, config.getSignatureType());
 assertEquals(scope, config.getScope());
 assertTrue(config.hasScope());
 }

 @Test
 public void testOAuthConfigWithNullScope() {
 String apiKey = "testApiKey";
 String apiSecret = "testApiSecret";
 String callback = "http://test.com/callback";
 SignatureType signatureType = SignatureType.Header;

 OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, null);

 assertEquals(apiKey, config.getApiKey());
 assertEquals(apiSecret, config.getApiSecret());
 assertEquals(callback, config.getCallback());
 assertEquals(signatureType, config.getSignatureType());
 assertNull(config.getScope());
 assertFalse(config.hasScope());
 }
}