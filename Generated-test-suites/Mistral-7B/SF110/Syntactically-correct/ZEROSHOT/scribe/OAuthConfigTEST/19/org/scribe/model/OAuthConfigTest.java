import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecret() {
 OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret");
 Assert.assertEquals("testKey", oAuthConfig.getApiKey());
 Assert.assertEquals("testSecret", oAuthConfig.getApiSecret());
 Assert.assertEquals("oob", oAuthConfig.getCallback());
 Assert.assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
 Assert.assertNull(oAuthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecretAndCallback() {
 OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret", "http://callback.com");
 Assert.assertEquals("testKey", oAuthConfig.getApiKey());
 Assert.assertEquals("testSecret", oAuthConfig.getApiSecret());
 Assert.assertEquals("http://callback.com", oAuthConfig.getCallback());
 Assert.assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
 Assert.assertNull(oAuthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecretAndCallbackAndSignatureType() {
 OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret", "http://callback.com", SignatureType.QueryString);
 Assert.assertEquals("testKey", oAuthConfig.getApiKey());
 Assert.assertEquals("testSecret", oAuthConfig.getApiSecret());
 Assert.assertEquals("http://callback.com", oAuthConfig.getCallback());
 Assert.assertEquals(SignatureType.QueryString, oAuthConfig.getSignatureType());
 Assert.assertNull(oAuthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithApiKeyAndApiSecretAndCallbackAndSignatureTypeAndScope() {
 OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret", "http://callback.com", SignatureType.Header, "testScope");
 Assert.assertEquals("testKey", oAuthConfig.getApiKey());
 Assert.assertEquals("testSecret", oAuthConfig.getApiSecret());
 Assert.assertEquals("http://callback.com", oAuthConfig.getCallback());
 Assert.assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
 Assert.assertEquals("testScope", oAuthConfig.getScope());
 }

 @Test
 public void testHasScope() {
 OAuthConfig oAuthConfig1 = new OAuthConfig();
 Assert.assertFalse(oAuthConfig1.hasScope());
 
 OAuthConfig oAuthConfig2 = new OAuthConfig("testKey", "testSecret", null, null, "testScope");
 Assert.assertTrue(oAuthConfig2.hasScope());
 }

}