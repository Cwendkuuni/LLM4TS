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
 public void testOAuthConfigWithValidArguments() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret");
 Assert.assertEquals("testKey", config.getApiKey());
 Assert.assertEquals("testSecret", config.getApiSecret());
 Assert.assertNull(config.getCallback());
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 Assert.assertNull(config.getScope());
 }

 @Test
 public void testOAuthConfigWithNullApiKey() {
 try {
 new OAuthConfig(null, "testSecret");
 Assert.fail();
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testOAuthConfigWithNullApiSecret() {
 try {
 new OAuthConfig("testKey", null);
 Assert.fail();
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testOAuthConfigWithNullCallback() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, null);
 Assert.assertEquals("oob", config.getCallback());
 }

 @Test
 public void testOAuthConfigWithNullSignatureType() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, null);
 Assert.assertEquals(SignatureType.Header, config.getSignatureType());
 }

 @Test
 public void testOAuthConfigWithValidSignatureType() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, SignatureType.QueryParam, null);
 Assert.assertEquals(SignatureType.QueryParam, config.getSignatureType());
 }

 @Test
 public void testOAuthConfigWithValidScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, "testScope");
 Assert.assertEquals("testScope", config.getScope());
 }

 @Test
 public void testOAuthConfigWithEmptyScope() {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, "");
 Assert.assertNull(config.getScope());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOAuthConfigWithInvalidSignatureType() {
 try {
 OAuthConfig config = new OAuthConfig("testKey", "testSecret", null, null, null);
 config.setSignatureType(new SignatureType("InvalidType"));
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

}