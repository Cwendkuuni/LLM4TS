import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignaturesType;

public class OAuthConfigTest {

 private String apiKey;
 private String apiSecret;
 private String callback;
 private SignaturesType signatureType;
 private String scope;

 @Test
 public void testOAuthConfigWithNullKeyAndSecret() {
 OAuthConfig oAuthConfig = new OAuthConfig(null, null);
 assertNull(oAuthConfig.getApiKey());
 assertNull(oAuthConfig.getApiSecret());
 }

 @Test
 public void testOAuthConfigWithValidArguments() {
 apiKey = "testApiKey";
 apiSecret = "testApiSecret";
 callback = "testCallback";
 signatureType = SignaturesType.Header;
 scope = "testScope";
 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
 Assert.assertEquals(apiKey, oAuthConfig.getApiKey());
 Assert.assertEquals(apiSecret, oAuthConfig.getApiSecret());
 Assert.assertEquals(callback, oAuthConfig.getCallback());
 Assert.assertEquals(signatureType, oAuthConfig.getSignatureType());
 Assert.assertEquals(scope, oAuthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithNullCallback() {
 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, null, signatureType, scope);
 Assert.assertEquals("oob", oAuthConfig.getCallback());
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullSignatureType() {
 apiKey = "testApiKey";
 apiSecret = "testApiSecret";
 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, null, scope);
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullScope() {
 apiKey = "testApiKey";
 apiSecret = "testApiSecret";
 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, null);
 }

 @Test
 public void testOAuthConfigHasScope() {
 apiKey = "testApiKey";
 apiSecret = "testApiSecret";
 scope = "testScope";
 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
 assertTrue(oAuthConfig.hasScope());
 }

 @Test
 public void testOAuthConfigNoScope() {
 apiKey = "testApiKey";
 apiSecret = "testApiSecret";
 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, null);
 assertFalse(oAuthConfig.hasScope());
 }

}