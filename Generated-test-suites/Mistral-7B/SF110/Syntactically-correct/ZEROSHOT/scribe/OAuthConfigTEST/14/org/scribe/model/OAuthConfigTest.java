import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithNullArgs() {
 OAuthConfig config = new OAuthConfig(null, null);
 Assert.assertNull(config.getApiKey());
 Assert.assertNull(config.getApiSecret());
 Assert.assertEquals("oob", config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithEmptyArgs() {
 OAuthConfig config = new OAuthConfig("", "");
 Assert.assertEquals("", config.getApiKey());
 Assert.assertEquals("", config.getApiSecret());
 Assert.assertEquals("oob", config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithAllArgs() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.QueryParam, "testScope");
 Assert.assertEquals("testKey", config.getApiKey());
 Assert.assertEquals("testSecret", config.getApiSecret());
 Assert.assertEquals("testCallback", config.getCallback());
 Assert.assertEquals(SignatureType.QueryParam, config.getSignatureType());
 Assert.assertEquals("testScope", config.getScope());
 Assert.assertTrue(config.hasScope());
 }

 @Test
 public void testOAuthConfigWithNullCallback() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, SignatureType.QueryParam, "testScope");
 Assert.assertEquals("oob", config.getCallback());
 }

 @Test
 public void testOAuthConfigWithNullSignatureType() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", null, "testScope");
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 }
}