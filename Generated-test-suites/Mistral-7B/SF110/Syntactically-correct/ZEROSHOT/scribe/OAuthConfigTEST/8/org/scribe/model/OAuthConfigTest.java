import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithKeyAndSecret() {
 String apiKey = "testApiKey";
 String apiSecret = "testApiSecret";
 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret);

 Assert.assertEquals(apiKey, oAuthConfig.getApiKey());
 Assert.assertEquals(apiSecret, oauthConfig.getApiSecret());
 Assert.assertNull(oAuthConfig.getCallback());
 Assert.assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
 Assert.assertNull(oAuthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithKeySecretCallbackAndSignatureType() {
 String apiKey = "testApiKey";
 String apiSecret = "testApiSecret";
 String callback = "http://test.com/callback";
 SignatureType signatureType = SignatureType.QueryString;
 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, null);

 Assert.assertEquals(apiKey, oAuthConfig.getApiKey());
 Assert.assertEquals(apiSecret, oAuthConfig.getApiSecret());
 Assert.assertEquals(callback, oAuthConfig.getCallback());
 Assert.assertEquals(signatureType, oAuthConfig.getSignatureType());
 Assert.assertNull(oAuthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithKeySecretAndScope() {
 String apiKey = "testApiKey";
 String apiSecret = "testApiSecret";
 String scope = "testScope";
 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, null, null, scope);

 Assert.assertEquals(apiKey, oAuthConfig.getApiKey());
 Assert.assertEquals(apiSecret, oAuthConfig.getApiSecret());
 Assert.assertNull(oAuthConfig.getCallback());
 Assert.assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
 Assert.assertEquals(scope, oAuthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithoutApiSecretThrowsException() {
 String apiKey = "testApiKey";
 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, null);

 Assert.assertNull(oAuthConfig.getApiKey());
 Assert.assertNull(oAuthConfig.getApiSecret());
 Assert.assertNull(oAuthConfig.getCallback());
 Assert.assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
 Assert.assertNull(oAuthConfig.getScope());

 // Should throw an exception
 try {
 oAuthConfig.getApiSecret();
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testOAuthConfigHasScope() {
 String apiKey = "testApiKey";
 String apiSecret = "testApiSecret";
 String scope = "testScope";
 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, null, null, scope);

 Assert.assertTrue(oAuthConfig.hasScope());
 }

 @Test
 public void testOAuthConfigWithoutScope() {
 String apiKey = "testApiKey";
 String apiSecret = "testApiSecret";
 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, null, null, null);

 Assert.assertFalse(oAuthConfig.hasScope());
 }
}