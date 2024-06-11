import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @Test
 public void testDefaultConstructor() {
 OAuthConfig config = new OAuthConfig("");
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
 OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callbackUrl");
 Assert.assertEquals("apiKey", config.getApiKey());
 Assert.assertEquals("apiSecret", config.getApiSecret());
 Assert.assertEquals("callbackUrl", config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testConstructorWithApiKeyAndApiSecretAndCallbackAndSignatureType() {
 OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callbackUrl", SignatureType.QueryParam);
 Assert.assertEquals("apiKey", config.getApiKey());
 Assert.assertEquals("apiSecret", config.getApiSecret());
 Assert.assertEquals("oob", config.getCallback()); // default value when null is passed
 Assert.assertEquals(SignatureType.QueryParam, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testConstructorWithApiKeyAndApiSecretAndCallbackAndSignatureTypeAndScope() {
 OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callbackUrl", SignatureType.QueryParam, "scope");
 Assert.assertEquals("apiKey", config.getApiKey());
 Assert.assertEquals("apiSecret", config.getApiSecret());
 Assert.assertEquals("callbackUrl", config.getCallback());
 Assert.assertEquals(SignatureType.QueryParam, config.getSignatureType());
 Assert.assertEquals("scope", config.getScope());
 }

 @Test
 public void testGetters() {
 OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callbackUrl", SignatureType.QueryParam, "scope");
 Assert.assertEquals("apiKey", config.getApiKey());
 Assert.assertEquals("apiSecret", config.getApiSecret());
 Assert.assertEquals("callbackUrl", config.getCallback());
 Assert.assertEquals(SignatureType.QueryParam, config.getSignatureType());
 Assert.assertEquals("scope", config.getScope());
 }

 @Test
 public void testHasScope() {
 OAuthConfig config1 = new OAuthConfig("");
 Assert.assertFalse(config1.hasScope());
 OAuthConfig config2 = new OAuthConfig("", "", "", null, "scope");
 Assert.assertTrue(config2.hasScope());
 }
}