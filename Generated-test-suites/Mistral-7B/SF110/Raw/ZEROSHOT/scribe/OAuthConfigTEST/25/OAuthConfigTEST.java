import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @Test
 public void testDefaultConstructor() {
 OAuthConfig config = new OAuthConfig();
 Assert.assertNull(config.getApiKey());
 Assert.assertNull(config.getApiSecret());
 Assert.assertEquals("oob", config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testConstructorWithApiKeyAndApiSecret() {
 OAuthConfig config = new OAuthConfig("apiKey", "apiSecret");
 Assert.assertEquals("apiKey", config.getApiKey());
 Assert.assertEquals("apiSecret", config.getApiSecret());
 Assert.assertEquals("oob", config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testConstructorWithApiKeyAndApiSecretAndCallback() {
 String callback = "http://example.com";
 OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", callback);
 Assert.assertEquals("apiKey", config.getApiKey());
 Assert.assertEquals("apiSecret", config.getApiSecret());
 Assert.assertEquals(callback, config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testConstructorWithApiKeyAndApiSecretAndSignatureType() {
 SignatureType signatureType = SignatureType.QueryParameter;
 OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", null, signatureType);
 Assert.assertEquals("apiKey", config.getApiKey());
 Assert.assertEquals("apiSecret", config.getApiSecret());
 Assert.assertEquals("oob", config.getCallback());
 Assert.assertEquals(signatureType, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testConstructorWithApiKeyAndApiSecretAndCallbackAndScope() {
 String callback = "http://example.com";
 String scope = "read_only";
 OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", callback, null, scope);
 Assert.assertEquals("apiKey", config.getApiKey());
 Assert.assertEquals("apiSecret", config.getApiSecret());
 Assert.assertEquals(callback, config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertEquals(scope, config.getScope());
 }

 @Test
 public void testGetters() {
 OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.Header, "scope");
 Assert.assertEquals("apiKey", config.getApiKey());
 Assert.assertEquals("apiSecret", config.getApiSecret());
 Assert.assertEquals("callback", config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertEquals("scope", config.getScope());
 }

 @Test
 public void testHasScope() {
 OAuthConfig config1 = new OAuthConfig();
 Assert.assertFalse(config1.hasScope());
 OAuthConfig config2 = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.Header, "scope");
 Assert.assertTrue(config2.hasScope());
 }

}