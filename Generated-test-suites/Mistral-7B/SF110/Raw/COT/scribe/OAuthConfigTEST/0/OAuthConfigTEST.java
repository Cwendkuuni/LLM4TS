import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignaturaType;

public class OAuthConfigTest {

 private String apiKey;
 private String apiSecret;
 private String callback;
 private SignaturaType signatureType;
 private String scope;

 @Test
 public void testOAuthConfigWithNullKeyAndSecret() {
 OAuthConfig oAuthConfig = new OAuthConfig(null, null);
 Assert.assertNull(oAuthConfig.getApiKey());
 Assert.assertNull(oAuthConfig.getApiSecret());
 }

 @Test
 public void testOAuthConfigWithValidKeyAndSecret() {
 apiKey = "testKey";
 apiSecret = "testSecret";
 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret);
 Assert.assertEquals(apiKey, oAuthConfig.getApiKey());
 Assert.assertEquals(apiSecret, oAuthConfig.getApiSecret());
 }

 @Test
 public void testOAuthConfigWithValidKeyAndSecretAndCallback() {
 apiKey = "testKey";
 apiSecret = "testSecret";
 callback = "http://test.com/callback";
 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback);
 Assert.assertEquals(apiKey, oAuthConfig.getApiKey());
 Assert.assertEquals(apiSecret, oAuthConfig.getApiSecret());
 Assert.assertEquals(callback, oAuthConfig.getCallback());
 }

 @Test
 public void testOAuthConfigWithValidKeyAndSecretAndSignatureType() {
 apiKey = "testKey";
 apiSecret = "testSecret";
 signatureType = SignaturaType.QueryString;
 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, null, signatureType, null);
 Assert.assertEquals(apiKey, oAuthConfig.getApiKey());
 Assert.assertEquals(apiSecret, oAuthConfig.getApiSecret());
 Assert.assertEquals(signatureType, oAuthConfig.getSignatureType());
 }

 @Test
 public void testOAuthConfigWithValidKeyAndSecretAndScope() {
 apiKey = "testKey";
 apiSecret = "testSecret";
 scope = "testScope";
 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, null, null, scope);
 Assert.assertEquals(apiKey, oAuthConfig.getApiKey());
 Assert.assertEquals(apiSecret, oAuthConfig.getApiSecret());
 Assert.assertEquals(scope, oAuthConfig.getScope());
 Assert.assertTrue(oAuthConfig.hasScope());
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullSignatureType() {
 apiKey = "testKey";
 apiSecret = "testSecret";
 signatureType = null;
 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, null, signatureType, null);
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullScope() {
 apiKey = "testKey";
 apiSecret = "testSecret";
 scope = null;
 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, null, null, scope);
 }

 @Test
 public void testOAuthConfigWithEmptyCallback() {
 callback = "";
 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
 Assert.assertEquals(callback, oAuthConfig.getCallback());
 }
}