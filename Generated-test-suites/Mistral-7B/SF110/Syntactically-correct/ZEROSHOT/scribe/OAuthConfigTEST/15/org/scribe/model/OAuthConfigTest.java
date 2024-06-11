import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithValidParameters() {
 String apiKey = "testApiKey";
 String apiSecret = "testApiSecret";
 String callback = "testCallback";
 SignatureType signatureType = SignatureType.Header;
 String scope = "testScope";

 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);

 Assert.assertEquals(apiKey, oAuthConfig.getApiKey());
 Assert.assertEquals(apiSecret, oAuthConfig.getApiSecret());
 Assert.assertEquals(callback, oAuthConfig.getCallback());
 Assert.assertEquals(signatureType, oAuthConfig.getSignatureType());
 Assert.assertEquals(scope, oAuthConfig.getScope());
 Assert.assertTrue(oAuthConfig.hasScope());
 }

 @Test
 public void testOAuthConfigWithNullParameters() {
 String apiKey = "testApiKey";
 String apiSecret = "testApiSecret";
 String callback = null;
 SignatureType signatureType = null;
 String scope = null;

 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);

 Assert.assertEquals(apiKey, oAuthConfig.getApiKey());
 Assert.assertEquals(apiSecret, oAuthConfig.getApiSecret());
 Assert.assertEquals("oob", oAuthConfig.getCallback());
 Assert.assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
 Assert.assertNull(oAuthConfig.getScope());
 Assert.assertFalse(oAuthConfig.hasScope());
 }

 @Test
 public void testOAuthConfigWithValidDefaultParameters() {
 String apiKey = "testApiKey";
 String apiSecret = "testApiSecret";

 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret);

 Assert.assertEquals(apiKey, oAuthConfig.getApiKey());
 Assert.assertEquals(apiSecret, oAuthConfig.getApiSecret());
 Assert.assertEquals("oob", oAuthConfig.getCallback());
 Assert.assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
 Assert.assertNull(oAuthConfig.getScope());
 Assert.assertFalse(oAuthConfig.hasScope());
 }

 @Test
 public void testOAuthConfigWithValidSignatureType() {
 String apiKey = "testApiKey";
 String apiSecret = "testApiSecret";
 String callback = "testCallback";
 SignatureType signatureType = SignatureType.QueryString;
 String scope = "testScope";

 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);

 Assert.assertEquals(apiKey, oAuthConfig.getApiKey());
 Assert.assertEquals(apiSecret, oAuthConfig.getApiSecret());
 Assert.assertEquals(callback, oAuthConfig.getCallback());
 Assert.assertEquals(signatureType, oAuthConfig.getSignatureType());
 Assert.assertEquals(scope, oAuthConfig.getScope());
 Assert.assertTrue(oAuthConfig.hasScope());
 }
}