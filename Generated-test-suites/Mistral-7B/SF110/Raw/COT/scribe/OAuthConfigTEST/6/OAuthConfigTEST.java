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
 public void testOAuthConfigWithValidAPIKeyAndSecret() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret");
 Assert.assertEquals("testKey", config.getApiKey());
 Assert.assertEquals("testSecret", config.getApiSecret());
 }

 @Test
 public void testOAuthConfigWithNullAPIKeyAndSecret() {
 OAuthConfig config = new OAuthConfig(null, null);
 Assert.assertNull(config.getApiKey());
 Assert.assertNull(config.getApiSecret());
 }

 @Test
 public void testOAuthConfigWithValidAPIKeyAndSecretAndCallback() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback");
 Assert.assertEquals("testKey", config.getApiKey());
 Assert.assertEquals("testSecret", config.getApiSecret());
 Assert.assertEquals("testCallback", config.getCallback());
 }

 @Test
 public void testOAuthConfigWithValidAPIKeyAndSecretAndSignatureType() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, SignatureType.QueryString, "testScope");
 Assert.assertEquals("testKey", config.getApiKey());
 Assert.assertEquals("testSecret", config.getApiSecret());
 Assert.assertEquals(SignatureType.QueryString, config.getSignatureType());
 Assert.assertEquals("testScope", config.getScope());
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullAPIKey() {
 OAuthConfig config = new OAuthConfig(null, "testSecret");
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigWithNullAPISecret() {
 OAuthConfig config = new OAuthConfig("testKey", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOAuthConfigWithNullSignatureType() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, "testScope");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOAuthConfigWithInvalidSignatureType() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, SignatureType.values()[-1], "testScope");
 }

 @Test
 public void testOAuthConfigWithDefaults() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret");
 Assert.assertEquals("oob", config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigHasScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, "testScope");
 Assert.assertTrue(config.hasScope());
 }

 @Test
 public void testOAuthConfigDoesNotHaveScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, null);
 Assert.assertFalse(config.hasScope());
 }

}