import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithKeyAndSecret() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret");
 Assert.assertEquals("testKey", config.getApiKey());
 Assert.assertEquals("testSecret", config.getApiSecret());
 Assert.assertEquals("oob", config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeySecretCallback() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback");
 Assert.assertEquals("testKey", config.getApiKey());
 Assert.assertEquals("testSecret", config.getApiSecret());
 Assert.assertEquals("testCallback", config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeySecretCallbackScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.QueryString, "testScope");
 Assert.assertEquals("testKey", config.getApiKey());
 Assert.assertEquals("testSecret", config.getApiSecret());
 Assert.assertEquals("testCallback", config.getCallback());
 Assert.assertEquals(SignatureType.QueryString, config.getSignatureType());
 Assert.assertEquals("testScope", config.getScope());
 Assert.assertTrue(config.hasScope());
 }

 @Test(expected = NullPointerException.class)
 public void testNullApiKey() {
 OAuthConfig config = new OAuthConfig(null, "testSecret");
 // the exception is thrown in the constructor, so we don't need to invoke any methods
 }

 @Test(expected = NullPointerException.class)
 public void testNullApiSecret() {
 OAuthConfig config = new OAuthConfig("testKey", null);
 // the exception is thrown in the constructor, so we don't need to invoke any methods
 }

 @Test(expected = NullPointerException.class)
 public void testNullSignatureType() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", null, "testScope");
 // the exception is thrown in the constructor, so we don't need to invoke any methods
 }
}