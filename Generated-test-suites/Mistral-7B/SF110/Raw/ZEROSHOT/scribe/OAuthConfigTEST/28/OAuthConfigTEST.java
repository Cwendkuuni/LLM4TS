import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigDefaultConstructor() {
 OAuthConfig oauthConfig = new OAuthConfig("");
 Assert.assertNull(oauthConfig.getApiKey());
 Assert.assertNull(oauthConfig.getApiSecret());
 Assert.assertEquals("oob", oauthConfig.getCallback());
 Assert.assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
 Assert.assertNull(oauthConfig.getScope());
 }

 @Test
 public void testOAuthConfigConstructorWithApiKeyAndApiSecret() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret");
 Assert.assertEquals("apiKey", oauthConfig.getApiKey());
 Assert.assertEquals("apiSecret", oauthConfig.getApiSecret());
 Assert.assertEquals("oob", oauthConfig.getCallback());
 Assert.assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
 Assert.assertNull(oauthConfig.getScope());
 }

 @Test
 public void testOAuthConfigConstructorWithApiKeySecretCallback() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", "callbackUrl");
 Assert.assertEquals("apiKey", oauthConfig.getApiKey());
 Assert.assertEquals("apiSecret", oauthConfig.getApiSecret());
 Assert.assertEquals("callbackUrl", oauthConfig.getCallback());
 Assert.assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
 Assert.assertNull(oauthConfig.getScope());
 }

 @Test
 public void testOAuthConfigConstructorWithApiKeySecretCallbackSignatureType() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", "callbackUrl", SignatureType.QueryParam, "scope1 scope2");
 Assert.assertEquals("apiKey", oauthConfig.getApiKey());
 Assert.assertEquals("apiSecret", oauthConfig.getApiSecret());
 Assert.assertEquals("callbackUrl", oauthConfig.getCallback());
 Assert.assertEquals(SignatureType.QueryParam, oauthConfig.getSignatureType());
 Assert.assertEquals("scope1 scope2", oauthConfig.getScope());
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullApiKey() {
 OAuthConfig oauthConfig = new OAuthConfig(null, "apiSecret");
 // A NullPointerException should be thrown
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullApiSecret() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", null);
 // A NullPointerException should be thrown
 }

 @Test
 public void testOAuthConfigHasScope() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", null, null, "scope1 scope2");
 Assert.assertTrue(oauthConfig.hasScope());
 }

 @Test
 public void testOAuthConfigNoScope() {
 OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", null, null, null);
 Assert.assertFalse(oauthConfig.hasScope());
 }
}