import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 private String apiKey;
 private String apiSecret;
 private String callback;
 private SignatureType signatureType;
 private String scope;

 @Test
 public void testOAuthConfigWithKeyAndSecret() {
 OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret");
 Assert.assertEquals("testKey", oAuthConfig.getApiKey());
 Assert.assertEquals("testSecret", oAuthConfig.getApiSecret());
 }

 @Test
 public void testOAuthConfigWithKeyAndSecretAndCallback() {
 OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret", "testCallback");
 Assert.assertEquals("testKey", oAuthConfig.getApiKey());
 Assert.assertEquals("testSecret", oAuthConfig.getApiSecret());
 Assert.assertEquals("testCallback", oAuthConfig.getCallback());
 }

 @Test
 public void testOAuthConfigWithKeyAndSecretAndCallbackAndSignatureType() {
 OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.QueryParameter, "testScope");
 Assert.assertEquals("testKey", oAuthConfig.getApiKey());
 Assert.assertEquals("testSecret", oAuthConfig.getApiSecret());
 Assert.assertEquals("testCallback", oAuthConfig.getCallback());
 Assert.assertEquals(SignatureType.QueryParameter, oAuthConfig.getSignatureType());
 Assert.assertEquals("testScope", oAuthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithNullSignatureType() {
 OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret", "testCallback", null, "testScope");
 Assert.assertEquals("testKey", oAuthConfig.getApiKey());
 Assert.assertEquals("testSecret", oAuthConfig.getApiSecret());
 Assert.assertEquals("oob", oAuthConfig.getCallback());
 Assert.assertEquals(SignatureType.Header, oAuthConfig.getSignatureType()); // default value
 Assert.assertEquals("testScope", oAuthConfig.getScope());
 }

 @Test(expected = NullPointerException.class)
 public void testNullApiKey() {
 OAuthConfig oAuthConfig = new OAuthConfig(null, "testSecret");
 }

 @Test(expected = NullPointerException.class)
 public void testNullApiSecret() {
 OAuthConfig oAuthConfig = new OAuthConfig("testKey", null);
 }

 @Test
 public void testHasScopeReturnsTrueWithScope() {
 OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret", null, null, "testScope");
 Assert.assertTrue(oAuthConfig.hasScope());
 }

 @Test
 public void testHasScopeReturnsFalseWithoutScope() {
 OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret");
 Assert.assertFalse(oAuthConfig.hasScope());
 }

}