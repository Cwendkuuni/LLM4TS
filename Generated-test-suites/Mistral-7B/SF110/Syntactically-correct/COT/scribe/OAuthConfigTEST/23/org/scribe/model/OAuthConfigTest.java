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
 public void testOAuthConfigWithNullKeyAndSecret() {
 OAuthConfig config = new OAuthConfig(null, null);
 Assert.assertNull(config.getApiKey());
 Assert.assertNull(config.getApiSecret());
 }

 @Test
 public void testOAuthConfigWithValidKeyAndSecret() {
 apiKey = "testKey";
 apiSecret = "testSecret";
 OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
 Assert.assertEquals(apiKey, config.getApiKey());
 Assert.assertEquals(apiSecret, config.getApiSecret());
 }

 @Test
 public void testOAuthConfigWithValidKeySecretAndOptionalParameters() {
 callback = "http://test.com";
 signatureType = SignatureType.Query;
 scope = "testScope";
 OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
 Assert.assertEquals(apiKey, config.getApiKey());
 Assert.assertEquals(apiSecret, config.getApiSecret());
 Assert.assertEquals(callback, config.getCallback());
 Assert.assertEquals(signatureType, config.getSignatureType());
 Assert.assertEquals(scope, config.getScope());
 Assert.assertTrue(config.hasScope());
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullSignatureType() {
 SignatureType nullSignatureType = null;
 OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, nullSignatureType, scope);
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullScope() {
 OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, null);
 }

}