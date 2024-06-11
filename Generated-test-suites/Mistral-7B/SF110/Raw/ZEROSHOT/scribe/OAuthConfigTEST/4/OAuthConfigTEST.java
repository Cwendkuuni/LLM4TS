import org.junit.Test;
import org.junit.Assert;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

 @Test
 public void testOAuthConfigWithKeyAndSecret() {
 OAuthConfig oAuthConfig = new OAuthConfig("testApiKey", "testApiSecret");
 Assert.assertEquals("testApiKey", oAuthConfig.getApiKey());
 Assert.assertEquals("testApiSecret", oAuthConfig.getApiSecret());
 Assert.assertNull(oAuthConfig.getCallback());
 Assert.assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
 Assert.assertNull(oAuthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithKeySecretCallbackAndSignatureType() {
 OAuthConfig oAuthConfig = new OAuthConfig("testApiKey", "testApiSecret", "testCallback", SignatureType.QueryString, "testScope");
 Assert.assertEquals("testApiKey", oAuthConfig.getApiKey());
 Assert.assertEquals("testApiSecret", oAuthConfig.getApiSecret());
 Assert.assertEquals("testCallback", oAuthConfig.getCallback());
 Assert.assertEquals(SignatureType.QueryString, oAuthConfig.getSignatureType());
 Assert.assertEquals("testScope", oAuthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithOnlyKeyAndSecret() {
 OAuthConfig oAuthConfig = new OAuthConfig("testApiKey", "testApiSecret");
 Assert.assertTrue(oAuthConfig.hasScope());
 }

 @Test(expected = NullPointerException.class)
 public void testNullApiKey() {
 OAuthConfig oAuthConfig = new OAuthConfig(null, "testApiSecret");
 }

 @Test(expected = NullPointerException.class)
 public void testNullApiSecret() {
 OAuthConfig oAuthConfig = new OAuthConfig("testApiKey", null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullCallback() {
 OAuthConfig oAuthConfig = new OAuthConfig("testApiKey", "testApiSecret", null);
 }

 @Test
 public void testHasScopeWhenHasScope() {
 OAuthConfig oAuthConfig = new OAuthConfig("testApiKey", "testApiSecret", null, null, "testScope");
 Assert.assertTrue(oAuthConfig.hasScope());
 }

 @Test
 public void testHasScopeWhenNoScope() {
 OAuthConfig oAuthConfig = new OAuthConfig("testApiKey", "testApiSecret");
 Assert.assertFalse(oAuthConfig.hasScope());
 }
}