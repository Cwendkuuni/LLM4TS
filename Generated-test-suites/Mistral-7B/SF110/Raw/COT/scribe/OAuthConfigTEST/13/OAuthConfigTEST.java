import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class OAuthConfigTest {

 private String apiKey;
 private String apiSecret;
 private String callback;
 private SignatureType signatureType;
 private String scope;

 @Test
 public void testOAuthConfigWithKeyAndSecret() {
 OAuthConfig oauthConfig = new OAuthConfig("testKey", "testSecret");
 assertEquals("testKey", oauthConfig.getApiKey());
 assertEquals("testSecret", oauthConfig.getApiSecret());
 assertNull(oauthConfig.getCallback());
 assertSame(SignatureType.Header, oauthConfig.getSignatureType());
 assertNull(oauthConfig.getScope());
 }

 @Test
 public void testOAuthConfigWithAllParams() {
 OAuthConfig oauthConfig = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.QueryParam, "testScope");
 assertEquals("testKey", oauthConfig.getApiKey());
 assertEquals("testSecret", oauthConfig.getApiSecret());
 assertEquals("testCallback", oauthConfig.getCallback());
 assertSame(SignatureType.QueryParam, oauthConfig.getSignatureType());
 assertEquals("testScope", oauthConfig.getScope());
 }

 @Test(expected = NullPointerException.class)
 public void testNullApiKey() {
 assertNull(new OAuthConfig(null, "testSecret"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullApiSecret() {
 assertNull(new OAuthConfig("testKey", null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullSignaturType() {
 assertNull(new OAuthConfig("testKey", "testSecret", null, null, null));
 }

 @Test
 public void testEmptyScope() {
 OAuthConfig oauthConfig = new OAuthConfig("testKey", "testSecret", null, SignatureType.Header, "");
 assertFalse(oauthConfig.hasScope());
 assertNull(oauthConfig.getScope());
 }

 @Test
 public void testNonEmptyScope() {
 OAuthConfig oauthConfig = new OAuthConfig("testKey", "testSecret", null, SignatureType.Header, "testScope");
 assertTrue(oauthConfig.hasScope());
 assertEquals("testScope", oauthConfig.getScope());
 }

 @Test
 public void testDefaultCallback() {
 OAuthConfig oauthConfig = new OAuthConfig("testKey", "testSecret");
 assertEquals("oob", oauthConfig.getCallback());
 }

 @Test
 public void testSpecifiedCallback() {
 OAuthConfig oauthConfig = new OAuthConfig("testKey", "testSecret", "testCallback");
 assertEquals("testCallback", oauthConfig.getCallback());
 }

}