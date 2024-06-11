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
 public void testOAuthConfigConstructorWithKeyAndSecret() {
 OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret");
 Assert.assertNotNull(oAuthConfig.getApiKey());
 Assert.assertNotNull(oAuthConfig.getApiSecret());
 }

 @Test
 public void testOAuthConfigConstructorWithKeySecretCallbackScopeAndSignatureType() {
 OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.Header, "testScope");
 Assert.assertNotNull(oAuthConfig.getApiKey());
 Assert.assertNotNull(oAuthConfig.getApiSecret());
 Assert.assertNotNull(oAuthConfig.getCallback());
 Assert.assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
 Assert.assertEquals("testScope", oAuthConfig.getScope());
 }

 @Test
 public void testOAuthConfigDefaultValues() {
 OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret");
 Assert.assertEquals("oob", oAuthConfig.getCallback());
 Assert.assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
 Assert.assertNull(oAuthConfig.getScope());
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigNullApiKey() {
 OAuthConfig oAuthConfig = new OAuthConfig(null, "testSecret");
 }

 @Test(expected = NullPointerException.class)
 public void testOAuthConfigNullApiSecret() {
 OAuthConfig oAuthConfig = new OAuthConfig("testKey", null);
 }

 @Test
 public void testOAuthConfigHasScope() {
 OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret", null, null, "testScope");
 Assert.assertTrue(oAuthConfig.hasScope());
 }

 @Test
 public void testOAuthConfigNoScope() {
 OAuthConfig oAuthConfig = new OAuthConfig("testKey", "testSecret", null, null, null);
 Assert.assertFalse(oAuthConfig.hasScope());
 }

}