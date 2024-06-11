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
 public void testOAuthConfigWithInvalidApiKey() {
 OAuthConfig oAuthConfig = new OAuthConfig(null, "secret");
 Assert.assertNull(oAuthConfig.getApiKey());
 }

 @Test
 public void testOAuthConfigWithNullApiSecret() {
 OAuthConfig oAuthConfig = new OAuthConfig("key", null);
 Assert.assertNull(oAuthConfig.getApiSecret());
 }

 @Test
 public void testOAuthConfigWithEmptyCallback() {
 OAuthConfig oAuthConfig = new OAuthConfig("key", "secret", "");
 Assert.assertEquals("oob", oAuthConfig.getCallback());
 }

 @Test
 public void testOAuthConfigWithDefaultSignatureType() {
 OAuthConfig oAuthConfig = new OAuthConfig("key", "secret");
 Assert.assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
 }

 @Test
 public void testOAuthConfigWithNullSignatureType() {
 OAuthConfig oAuthConfig = new OAuthConfig("key", "secret", null, null, null);
 Assert.assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
 }

 @Test
 public void testOAuthConfigWithValidValues() {
 apiKey = "testApiKey";
 apiSecret = "testApiSecret";
 callback = "http://test.com";
 signatureType = SignatureType.QueryString;
 scope = "testScope";
 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
 Assert.assertEquals(apiKey, oAuthConfig.getApiKey());
 Assert.assertEquals(apiSecret, oAuthConfig.getApiSecret());
 Assert.assertEquals(callback, oAuthConfig.getCallback());
 Assert.assertEquals(SignatureType.QueryString, oAuthConfig.getSignatureType());
 Assert.assertEquals(scope, oAuthConfig.getScope());
 Assert.assertTrue(oAuthConfig.hasScope());
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullApiKey() {
 OAuthConfig oAuthConfig = new OAuthConfig(null, apiSecret, callback, signatureType, scope);
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullApiSecret() {
 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, null, callback, signatureType, scope);
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullCallback() {
 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, null, signatureType, scope);
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullSignatureType() {
 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, null, scope);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOAuthConfigWithInvalidSignatureType() {
 SignatureType invalidSignatureType = new SignatureType("InvalidType");
 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, invalidSignatureType, scope);
 }

 @Test
 public void testOAuthConfigWithEmptyScope() {
 OAuthConfig oAuthConfig = new OAuthConfig(apiKey, apiSecret, callback, signatureType, "");
 Assert.assertNull(oAuthConfig.getScope());
 Assert.assertFalse(oAuthConfig.hasScope());
 }
}