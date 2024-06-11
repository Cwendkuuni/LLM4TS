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
 public void testOAuthConfigWithKeyAndSecretAndCallback() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "http://test.com/callback");
 Assert.assertEquals("testKey", config.getApiKey());
 Assert.assertEquals("testSecret", config.getApiSecret());
 Assert.assertEquals("http://test.com/callback", config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithKeyAndSecretAndCallbackAndSignatureType() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "http://test.com/callback", SignatureType.QueryString, "testScope");
 Assert.assertEquals("testKey", config.getApiKey());
 Assert.assertEquals("testSecret", config.getApiSecret());
 Assert.assertEquals("http://test.com/callback", config.getCallback());
 Assert.assertEquals(SignatureType.QueryString, config.getSignatureType());
 Assert.assertEquals("testScope", config.getScope());
 Assert.assertTrue(config.hasScope());
 }

 @Test(expected = NullPointerException.class)
 public void testNullApiKey() {
 OAuthConfig config = new OAuthConfig(null, "testSecret");
 // The exception is not shown here, it is expected
 }

 @Test(expected = NullPointerException.class)
 public void testNullApiSecret() {
 OAuthConfig config = new OAuthConfig("testKey", null);
 // The exception is not shown here, it is expected
 }

 @Test(expected = NullPointerException.class)
 public void testNullCallback() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null);
 // The exception is not shown here, it is expected
 }
}