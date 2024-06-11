import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithKeyAndSecret() {
 OAuthConfig config = new OAuthConfig("testApiKey", "testApiSecret");
 Assert.assertEquals("testApiKey", config.getApiKey());
 Assert.assertEquals("testApiSecret", config.getApiSecret());
 Assert.assertEquals("oob", config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeySecretCallback() {
 OAuthConfig config = new OAuthConfig("testApiKey", "testApiSecret", "testCallback");
 Assert.assertEquals("testApiKey", config.getApiKey());
 Assert.assertEquals("testApiSecret", config.getApiSecret());
 Assert.assertEquals("testCallback", config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeySecretCallbackSignatureType() {
 OAuthConfig config = new OAuthConfig("testApiKey", "testApiSecret", "testCallback", SignatureType.QueryString);
 Assert.assertEquals("testApiKey", config.getApiKey());
 Assert.assertEquals("testApiSecret", config.getApiSecret());
 Assert.assertEquals("testCallback", config.getCallback());
 Assert.assertEquals(SignatureType.QueryString, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeySecretCallbackSignatureTypeScope() {
 OAuthConfig config = new OAuthConfig("testApiKey", "testApiSecret", "testCallback", SignatureType.Header, "testScope");
 Assert.assertEquals("testApiKey", config.getApiKey());
 Assert.assertEquals("testApiSecret", config.getApiSecret());
 Assert.assertEquals("testCallback", config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertEquals("testScope", config.getScope());
 }

 @Test
 public void testHasScope() {
 OAuthConfig config1 = new OAuthConfig("testApiKey", "testApiSecret");
 Assert.assertFalse(config1.hasScope());
 
 OAuthConfig config2 = new OAuthConfig("testApiKey", "testApiSecret", null, null, "testScope");
 Assert.assertTrue(config2.hasScope());
 }

}