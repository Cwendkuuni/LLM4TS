import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigConstructionWithKeyAndSecret() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret");
 Assert.assertEquals("testKey", config.getApiKey());
 Assert.assertEquals("testSecret", config.getApiSecret());
 Assert.assertEquals("oob", config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigConstructionWithKeySecretCallbackAndSignatureType() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.QueryString, "testScope");
 Assert.assertEquals("testKey", config.getApiKey());
 Assert.assertEquals("testSecret", config.getApiSecret());
 Assert.assertEquals("testCallback", config.getCallback());
 Assert.assertEquals(SignatureType.QueryString, config.getSignatureType());
 Assert.assertEquals("testScope", config.getScope());
 }

 @Test
 public void testOAuthConfigConstructionWithKeySecretCallbackOnly() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback");
 Assert.assertEquals("testKey", config.getApiKey());
 Assert.assertEquals("testSecret", config.getApiSecret());
 Assert.assertEquals("testCallback", config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigHasScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, "testScope");
 Assert.assertTrue(config.hasScope());
 }

 @Test
 public void testOAuthConfigNoScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret");
 Assert.assertFalse(config.hasScope());
 }
}